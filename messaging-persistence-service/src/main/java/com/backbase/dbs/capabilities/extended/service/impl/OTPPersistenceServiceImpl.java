package com.backbase.dbs.capabilities.extended.service.impl;

import com.backbase.dbs.capabilities.extended.domain.OTP;
import com.backbase.dbs.capabilities.extended.repository.OTPRepository;
import com.backbase.dbs.capabilities.extended.service.OTPPersistenceService;
import com.backbase.persistence.otp.rest.spec.v1.transactions.OTPTransactionsPostRequestBody;
import com.backbase.persistence.otp.rest.spec.v1.transactions.VerifyTransactionPostRequestBody;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OTPPersistenceServiceImpl implements OTPPersistenceService {

    private final OTPRepository otpRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public OTPPersistenceServiceImpl(OTPRepository otpRepository) {
        this.otpRepository = otpRepository;
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
    public String updateOneTimePasswordRequestVerificationStatus(VerifyTransactionPostRequestBody verifyTransactionPostRequestBody) {
        String transactionID = verifyTransactionPostRequestBody.getTransactionID();
        OTP oneTimePasswordRequest = otpRepository.findOne(transactionID);
        oneTimePasswordRequest.setVerified(verifyTransactionPostRequestBody.getVerified());
        otpRepository.save(oneTimePasswordRequest);
        return transactionID;
    }

    private OTP mapRequestToModel(OTPTransactionsPostRequestBody transactionPostRequestBody) {
        OTP oneTimePasswordRequest = new OTP();
        modelMapper.map(transactionPostRequestBody, oneTimePasswordRequest);
        oneTimePasswordRequest.setVerified(false);
        return oneTimePasswordRequest;
    }
}
