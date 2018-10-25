package com.backbase.dbs.capabilities.extended.listener;

import com.backbase.buildingblocks.backend.communication.event.annotations.RequestListener;
import com.backbase.buildingblocks.backend.communication.event.proxy.RequestProxyWrapper;
import com.backbase.buildingblocks.backend.internalrequest.InternalRequest;
import com.backbase.buildingblocks.logging.api.Logger;
import com.backbase.buildingblocks.logging.api.LoggerFactory;
import com.backbase.dbs.capabilities.extended.domain.OTP;
import com.backbase.dbs.capabilities.extended.service.impl.MessagingPersistenceServiceImpl;
import com.backbase.persistence.messaging.listener.spec.v1.transactions.OTPTransactionsListener;
import com.backbase.persistence.messaging.rest.spec.v1.transactions.*;
import org.apache.camel.Exchange;
import org.apache.camel.builder.script.Groovy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RequestListener
public class OTPTransactionsListenerImpl implements OTPTransactionsListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(OTPTransactionsListenerImpl.class);

    private MessagingPersistenceServiceImpl messagingPersistenceServiceImpl;

    @Autowired
    public OTPTransactionsListenerImpl(MessagingPersistenceServiceImpl messagingPersistenceServiceImpl) {
        this.messagingPersistenceServiceImpl = messagingPersistenceServiceImpl;
    }

    @Override
    public RequestProxyWrapper<OTPTransactionsPostResponseBody> postOTPTransactions(
            RequestProxyWrapper<OTPTransactionsPostRequestBody> transactionsPostRequestBody,
            Exchange exchange) {
        LOGGER.info("Call to persist new OPT Request: {}", transactionsPostRequestBody);
        String transactionID = messagingPersistenceServiceImpl.saveNewOneTimePasswordRequest(transactionsPostRequestBody.getRequest().getData());
        OTPTransactionsPostResponseBody transactionPostResponseBody = new OTPTransactionsPostResponseBody().withTransactionID(transactionID);
        return buildRequestProxyWrapper(transactionPostResponseBody, "POST");
    }

    @Override
    public RequestProxyWrapper<TransactionGetResponseBody> getTransaction(RequestProxyWrapper<Void> request, Exchange exchange, @Groovy("assert request.body.pathParams[\"transactionId\"]||request.body.pathParams[\"transactionId\"]==0; request.body.pathParams[\"transactionId\"]") String transactionId) {
        OTP oneTimePasswordRequest = messagingPersistenceServiceImpl.fetchOneTimePasswordRequest(transactionId);
        TransactionGetResponseBody transactionGetResponseBody = new TransactionGetResponseBody()
                .withTransactionId(oneTimePasswordRequest.getId())
                .withOtp(oneTimePasswordRequest.getOtp())
                .withPhoneNumber(oneTimePasswordRequest.getPhoneNumber())
                .withUserId(oneTimePasswordRequest.getUserId())
                .withVerified(oneTimePasswordRequest.getVerified());
        return buildRequestProxyWrapper(transactionGetResponseBody, "GET");
    }

    @Override
    public RequestProxyWrapper<VerifyTransactionPostResponseBody> postVerifyTransaction(
            RequestProxyWrapper<VerifyTransactionPostRequestBody> verifyTransactionPostRequestBody,
            Exchange exchange) {
        LOGGER.info("Call to update OPT Request verification status: {}", verifyTransactionPostRequestBody);
        String transactionID = messagingPersistenceServiceImpl.updateOneTimePasswordRequestVerificationStatus(
                verifyTransactionPostRequestBody.getRequest().getData());
        VerifyTransactionPostResponseBody verifyTransactionPostResponseBody = new VerifyTransactionPostResponseBody().withTransactionID(transactionID);
        return buildRequestProxyWrapper(verifyTransactionPostResponseBody, "POST");
    }

    private <T> RequestProxyWrapper<T> buildRequestProxyWrapper(T data, String method) {
        InternalRequest internalRequest = new InternalRequest();
        internalRequest.setData(data);
        RequestProxyWrapper result = new RequestProxyWrapper();
        result.setRequest(internalRequest);
        result.setHttpMethod(method);
        return result;
    }
}
