package com.backbase.dbs.capabilities.extended.messaging.service.impl;

import com.backbase.buildingblocks.backend.communication.event.proxy.RequestProxyWrapper;
import com.backbase.buildingblocks.backend.internalrequest.InternalRequest;
import com.backbase.buildingblocks.logging.api.Logger;
import com.backbase.buildingblocks.logging.api.LoggerFactory;
import com.backbase.com.backbase.dbs.capabilities.extended.messaging.presentation.rest.spec.v1.one_time_password.OneTimePasswordPostRequestBody;
import com.backbase.com.backbase.dbs.capabilities.extended.messaging.presentation.rest.spec.v1.one_time_password.OneTimePasswordPostResponseBody;
import com.backbase.dbs.capabilities.extended.messaging.routes.MessaginConstants;
import com.backbase.dbs.capabilities.extended.messaging.service.MessagingService;
import com.backbase.dbs.capabilities.extended.messaging.service.OneTimePasswordService;
import com.backbase.dbs.capabilities.extended.messaging.util.ContextUtil;
import com.backbase.integration.messaging.listener.client.v1.otp.IntegrationMessagingOTPRequestClient;
import com.backbase.integration.messaging.rest.spec.v1.otp.OTPRequestPostRequestBody;
import com.backbase.integration.messaging.rest.spec.v1.otp.OTPRequestPostResponseBody;
import com.backbase.persistence.messaging.listener.client.v1.transactions.PersistenceMessagingOTPTransactionsClient;
import com.backbase.persistence.messaging.rest.spec.v1.transactions.OTPTransactionsPostRequestBody;
import com.backbase.persistence.messaging.rest.spec.v1.transactions.OTPTransactionsPostResponseBody;
import org.apache.camel.Consume;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;


/**
 * Messaging Controller provider the main resources for Messaging Integrations
 * @author Paulo Cardoso
 * @since 1.0.0
 */
@Service
public class MessagingServiceImpl implements MessagingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagingServiceImpl.class);

    private PersistenceMessagingOTPTransactionsClient persistenceOtpOTPTransactionsClient;
    private IntegrationMessagingOTPRequestClient integrationOtpOTPRequestClient;
    private OneTimePasswordService oneTimePasswordService;

    public MessagingServiceImpl(PersistenceMessagingOTPTransactionsClient persistenceOtpOTPTransactionsClient, IntegrationMessagingOTPRequestClient integrationOtpOTPRequestClient, OneTimePasswordService oneTimePasswordService){
        this.persistenceOtpOTPTransactionsClient = persistenceOtpOTPTransactionsClient;
        this.integrationOtpOTPRequestClient = integrationOtpOTPRequestClient;
        this.oneTimePasswordService = oneTimePasswordService;
    }

    /**
     * @param internalRequest
     * @return OneTimePasswordPostResponseBody
     * @consume direct:request.messaging.otp
     */
    @Override
    @Consume(uri = MessaginConstants.DIRECT_REQUEST_OTP)
    public OneTimePasswordPostResponseBody requestOneTimePassword(InternalRequest<OneTimePasswordPostRequestBody> internalRequest) {
            LOGGER.info("Requesting OTP for Internal Request: {}", internalRequest);
            int otp = oneTimePasswordService.generateOpt();
            String transactionID = persistNewRequest(internalRequest, otp);

            RequestProxyWrapper<OTPRequestPostResponseBody> otpRequestPostResponseBody = callIntegrationServiceToSendSms(internalRequest, transactionID, otp);
            Boolean smsSent = otpRequestPostResponseBody.getRequest().getData().getSmsSent();
            LOGGER.info("Successfully requested new OTP {} with Transaction ID: {}", otp, transactionID);
            return new OneTimePasswordPostResponseBody().withTransactionID(transactionID).withOtp(otpRequestPostResponseBody.getRequest().getData().getOtp())
                    .withSmsSent(smsSent);
    }

    private RequestProxyWrapper<OTPRequestPostResponseBody> callIntegrationServiceToSendSms(
            InternalRequest<OneTimePasswordPostRequestBody> internalRequest,
            String transactionID, Integer otp) {

        OTPRequestPostRequestBody requestPostRequestBody = new OTPRequestPostRequestBody()
                .withPhoneNumber(internalRequest.getData().getPhoneNumber())
                .withTransactionID(transactionID)
                .withOtp(otp);

        return integrationOtpOTPRequestClient.postOTPRequest(ContextUtil.copyInternalRequest(internalRequest, requestPostRequestBody));
    }

    private String persistNewRequest(InternalRequest<OneTimePasswordPostRequestBody> internalRequest, int otp) {
        OTPTransactionsPostRequestBody transactionPostRequestBody
                = new ModelMapper().map(internalRequest.getData(), OTPTransactionsPostRequestBody.class).withOtp(otp);

        RequestProxyWrapper<OTPTransactionsPostResponseBody> transactionPostResponseBody = persistenceOtpOTPTransactionsClient
                        .postOTPTransactions(ContextUtil.copyInternalRequest(internalRequest, transactionPostRequestBody));
        return transactionPostResponseBody.getRequest().getData().getTransactionID();
    }



}
