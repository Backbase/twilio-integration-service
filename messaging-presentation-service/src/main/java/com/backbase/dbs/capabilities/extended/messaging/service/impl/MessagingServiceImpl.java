package com.backbase.dbs.capabilities.extended.messaging.service.impl;

import com.backbase.buildingblocks.backend.communication.event.proxy.RequestProxyWrapper;
import com.backbase.buildingblocks.backend.internalrequest.InternalRequest;
import com.backbase.buildingblocks.logging.api.Logger;
import com.backbase.buildingblocks.logging.api.LoggerFactory;
import com.backbase.buildingblocks.presentation.errors.InternalServerErrorException;
import com.backbase.com.backbase.dbs.capabilities.extended.messaging.presentation.rest.spec.v1.otp.OneTimePasswordPostRequestBody;
import com.backbase.com.backbase.dbs.capabilities.extended.messaging.presentation.rest.spec.v1.otp.OneTimePasswordPostResponseBody;
import com.backbase.com.backbase.dbs.capabilities.extended.messaging.presentation.rest.spec.v1.otp.OneTimePasswordVerifyPostRequestBody;
import com.backbase.com.backbase.dbs.capabilities.extended.messaging.presentation.rest.spec.v1.otp.OneTimePasswordVerifyPostResponseBody;
import com.backbase.dbs.capabilities.extended.messaging.routes.MessaginConstants;
import com.backbase.dbs.capabilities.extended.messaging.service.MessagingService;
import com.backbase.dbs.capabilities.extended.messaging.service.OneTimePasswordStrategyService;
import com.backbase.dbs.capabilities.extended.messaging.util.ContextUtil;
import com.backbase.messaging.integration.listener.client.v1.sms.MessagingIntegrationSendSMSClient;
import com.backbase.messaging.integration.rest.spec.v1.sms.SendSMSPostRequestBody;
import com.backbase.messaging.integration.rest.spec.v1.sms.SendSMSPostResponseBody;
import com.backbase.persistence.messaging.listener.client.v1.transactions.PersistenceMessagingOTPTransactionsClient;
import com.backbase.persistence.messaging.rest.spec.v1.transactions.*;
import org.apache.camel.Consume;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


/**
 * Messaging Controller provider the main resources for Messaging Integrations
 *
 * @author Paulo Cardoso
 * @since 1.0.0
 */
@Service
public class MessagingServiceImpl implements MessagingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagingServiceImpl.class);

    private PersistenceMessagingOTPTransactionsClient persistenceOtpOTPTransactionsClient;
    private MessagingIntegrationSendSMSClient integrationOtpOTPRequestClient;
    private OneTimePasswordStrategyService oneTimePasswordStrategyService;

    public MessagingServiceImpl(PersistenceMessagingOTPTransactionsClient persistenceOtpOTPTransactionsClient, MessagingIntegrationSendSMSClient integrationOtpOTPRequestClient, OneTimePasswordStrategyService oneTimePasswordStrategyService) {
        this.persistenceOtpOTPTransactionsClient = persistenceOtpOTPTransactionsClient;
        this.integrationOtpOTPRequestClient = integrationOtpOTPRequestClient;
        this.oneTimePasswordStrategyService = oneTimePasswordStrategyService;
    }

    /**
     * @param internalRequest
     * @return OneTimePasswordPostResponseBody
     * @consume direct:request.messaging.otp
     */
    @Override
    @Consume(uri = MessaginConstants.DIRECT_BUSINESS_REQUEST_OTP)
    public OneTimePasswordPostResponseBody requestOneTimePassword(InternalRequest<OneTimePasswordPostRequestBody> internalRequest) {
        LOGGER.info("Requesting OTP for Internal Request: {}", internalRequest);
        int otp = oneTimePasswordStrategyService.generateOpt();
        String transactionID = persistNewRequest(internalRequest, otp);

        RequestProxyWrapper<SendSMSPostResponseBody> otpRequestPostResponseBody = callIntegrationServiceToSendSms(internalRequest, String.valueOf(otp));
        Boolean smsSent = otpRequestPostResponseBody.getRequest().getData().getSmsSent();
        LOGGER.info("Successfully requested new OTP {} with Transaction ID: {}", otp, transactionID);
        return new OneTimePasswordPostResponseBody().withTransactionID(transactionID).withOtp(otp)
                .withSmsSent(smsSent);
    }

    @Override
    @Consume(uri = MessaginConstants.DIRECT_BUSINESS_VERIFY_OTP)
    public OneTimePasswordVerifyPostResponseBody verifyOneTimePassword(InternalRequest<OneTimePasswordVerifyPostRequestBody> internalRequest) {
        LOGGER.info("Verifying OTP : {}", internalRequest);

        final OneTimePasswordVerifyPostRequestBody otpRequest = internalRequest.getData();

        final RequestProxyWrapper<TransactionGetResponseBody> response = persistenceOtpOTPTransactionsClient
                .getTransaction(new InternalRequest<>(null, internalRequest.getInternalRequestContext()), otpRequest.getTransactionID());

        final TransactionGetResponseBody otpDataResponse = response.getRequest().getData();

        if(otpDataResponse.getVerified()){
            LOGGER.info("OTP for this transaction was already verified");
            throw new InternalServerErrorException().withMessage("OTP is not valid");
        }

        if(!otpDataResponse.getOtp().equals(otpRequest.getOtp())) {
            LOGGER.info("OTP is not matching");
            throw new InternalServerErrorException().withMessage("OTP does not Match");
        }

        VerifyTransactionPostRequestBody verifyRequest = new VerifyTransactionPostRequestBody();
        verifyRequest.setTransactionID(otpRequest.getTransactionID());
        verifyRequest.setVerified(true);

        final RequestProxyWrapper<VerifyTransactionPostResponseBody> UpdateResponse = persistenceOtpOTPTransactionsClient.postVerifyTransaction(new InternalRequest<>(verifyRequest, internalRequest.getInternalRequestContext()));


        return new OneTimePasswordVerifyPostResponseBody()
                .withTransactionID(UpdateResponse.getRequest().getData().getTransactionID())
                .withVerified(true);
    }

    private RequestProxyWrapper<SendSMSPostResponseBody> callIntegrationServiceToSendSms(
            InternalRequest<OneTimePasswordPostRequestBody> internalRequest, String message) {

        SendSMSPostRequestBody requestPostRequestBody = new SendSMSPostRequestBody()
                .withPhoneNumber(internalRequest.getData().getPhoneNumber())
                .withMessage(message);

        return integrationOtpOTPRequestClient.postSendSMS(ContextUtil.copyInternalRequest(internalRequest, requestPostRequestBody));
    }

    private String persistNewRequest(InternalRequest<OneTimePasswordPostRequestBody> internalRequest, int otp) {
        OTPTransactionsPostRequestBody transactionPostRequestBody
                = new ModelMapper().map(internalRequest.getData(), OTPTransactionsPostRequestBody.class).withOtp(otp);

        RequestProxyWrapper<OTPTransactionsPostResponseBody> transactionPostResponseBody = persistenceOtpOTPTransactionsClient
                .postOTPTransactions(ContextUtil.copyInternalRequest(internalRequest, transactionPostRequestBody));
        return transactionPostResponseBody.getRequest().getData().getTransactionID();
    }



}
