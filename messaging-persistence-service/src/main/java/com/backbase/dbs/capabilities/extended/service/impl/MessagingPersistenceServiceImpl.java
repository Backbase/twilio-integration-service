package com.backbase.dbs.capabilities.extended.service.impl;

import com.backbase.dbs.capabilities.extended.domain.OTP;
import com.backbase.dbs.capabilities.extended.repository.OTPRepository;
import com.backbase.dbs.capabilities.extended.service.MessagingPersistenceService;
import com.backbase.persistence.messaging.rest.spec.v1.transactions.OTPTransactionsPostRequestBody;
import com.backbase.persistence.messaging.rest.spec.v1.transactions.VerifyTransactionPostRequestBody;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MessagingPersistenceServiceImpl implements MessagingPersistenceService {

    private final OTPRepository otpRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public MessagingPersistenceServiceImpl(OTPRepository otpRepository, ModelMapper modelMapper ){
        this.otpRepository =  otpRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public String saveNewOneTimePasswordRequest(OTPTransactionsPostRequestBody transactionPostRequestBody) {
        OTP oneTimePasswordRequest = mapRequestToModel(transactionPostRequestBody);
        otpRepository.save(oneTimePasswordRequest);
        return oneTimePasswordRequest.getId();
    }

    @Override
    public OTP fetchOneTimePasswordRequest(String transactionId) {
        return otpRepository.findOne(transactionId);
    }

    @Override
    @Transactional
    public String updateOneTimePasswordRequestVerificationStatus(VerifyTransactionPostRequestBody verifyTransactionPostRequestBody) {
        String transactionID = verifyTransactionPostRequestBody.getTransactionID();
        OTP oneTimePasswordRequest = otpRepository.findOne(transactionID);
        oneTimePasswordRequest.setVerified(verifyTransactionPostRequestBody.getVerified());
        return transactionID;
    }

    private OTP mapRequestToModel(OTPTransactionsPostRequestBody transactionPostRequestBody) {
        OTP oneTimePasswordRequest = new OTP();
        oneTimePasswordRequest.setOtp(transactionPostRequestBody.getOtp());
        oneTimePasswordRequest.setPhoneNumber(transactionPostRequestBody.getPhoneNumber());
        oneTimePasswordRequest.setUserId(transactionPostRequestBody.getUserID());
        oneTimePasswordRequest.setVerified(false);
        return oneTimePasswordRequest;
    }
}
