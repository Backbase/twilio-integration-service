package com.backbase.dbs.capabilities.extended.service;

import com.backbase.dbs.capabilities.extended.domain.OTP;
import com.backbase.persistence.messaging.rest.spec.v1.transactions.OTPTransactionsPostRequestBody;
import com.backbase.persistence.messaging.rest.spec.v1.transactions.VerifyTransactionPostRequestBody;

public interface MessagingPersistenceService {

    String saveNewOneTimePasswordRequest(OTPTransactionsPostRequestBody transactionPostRequestBody);

    OTP fetchOneTimePasswordRequest(String transactionId);

    String updateOneTimePasswordRequestVerificationStatus(VerifyTransactionPostRequestBody verifyTransactionPostRequestBody);
}
