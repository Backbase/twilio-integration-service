package com.backbase.dbs.capabilities.extended.otp.service.impl;

import com.backbase.buildingblocks.backend.communication.event.proxy.RequestProxyWrapper;
import com.backbase.buildingblocks.backend.internalrequest.InternalRequest;
import com.backbase.buildingblocks.logging.api.Logger;
import com.backbase.buildingblocks.logging.api.LoggerFactory;
import com.backbase.dbs.capabilities.extended.otp.routes.OTPConstants;
import com.backbase.dbs.capabilities.extended.otp.service.OneTimePasswordService;
import com.backbase.integration.otp.listener.client.v1.otp.IntegrationOtpOTPRequestClient;
import com.backbase.integration.otp.rest.spec.v1.otp.OTPRequestPostRequestBody;
import com.backbase.integration.otp.rest.spec.v1.otp.OTPRequestPostResponseBody;
import com.backbase.persistence.otp.listener.client.v1.transactions.PersistenceOtpOTPTransactionsClient;
import com.backbase.persistence.otp.rest.spec.v1.transactions.OTPTransactionsPostRequestBody;
import com.backbase.persistence.otp.rest.spec.v1.transactions.OTPTransactionsPostResponseBody;
import com.backbase.presentation.otp.rest.spec.v1.one_time_password.OneTimePasswordPostRequestBody;
import com.backbase.presentation.otp.rest.spec.v1.one_time_password.OneTimePasswordPostResponseBody;
import org.apache.camel.Consume;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class OneTimePasswordServiceImpl implements OneTimePasswordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OneTimePasswordServiceImpl.class);

    private PersistenceOtpOTPTransactionsClient persistenceOtpOTPTransactionsClient;
    private IntegrationOtpOTPRequestClient integrationOtpOTPRequestClient;

    public OneTimePasswordServiceImpl(PersistenceOtpOTPTransactionsClient persistenceOtpOTPTransactionsClient, IntegrationOtpOTPRequestClient integrationOtpOTPRequestClient){
        this.persistenceOtpOTPTransactionsClient = persistenceOtpOTPTransactionsClient;
        this.integrationOtpOTPRequestClient = integrationOtpOTPRequestClient;
    }

    @Override
    @Consume(uri = OTPConstants.DIRECT_REQUEST_OTP)
    public OneTimePasswordPostResponseBody requestOneTimePassword(InternalRequest<OneTimePasswordPostRequestBody> internalRequest) {
            LOGGER.info("Requesting OTP for Internal Request: {}", internalRequest);
            int otp = ThreadLocalRandom.current().nextInt(1000, 10000);
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

        return integrationOtpOTPRequestClient.postOTPRequest(copyInternalRequest(internalRequest, requestPostRequestBody));
    }

    private String persistNewRequest(InternalRequest<OneTimePasswordPostRequestBody> internalRequest, int otp) {
        OTPTransactionsPostRequestBody transactionPostRequestBody
                = new ModelMapper().map(internalRequest.getData(), OTPTransactionsPostRequestBody.class).withOtp(otp);

        RequestProxyWrapper<OTPTransactionsPostResponseBody> transactionPostResponseBody = persistenceOtpOTPTransactionsClient
                        .postOTPTransactions(copyInternalRequest(internalRequest, transactionPostRequestBody));
        return transactionPostResponseBody.getRequest().getData().getTransactionID();
    }


    private <T> InternalRequest<T> copyInternalRequest(InternalRequest original, T data) {
        InternalRequest result = new InternalRequest();
        result.setInternalRequestContext(original.getInternalRequestContext());
        result.setData(data);
        return result;
    }

}
