package com.backbase.dbs.capabilities.extended.twilio.service;


import com.backbase.integration.otp.rest.spec.v1.otp.OTPRequestPostRequestBody;
import com.backbase.integration.otp.rest.spec.v1.otp.OTPRequestPostResponseBody;

public interface OTPService {

    OTPRequestPostResponseBody requestOneTimePassword(OTPRequestPostRequestBody smsRequestPostRequestBody);
}
