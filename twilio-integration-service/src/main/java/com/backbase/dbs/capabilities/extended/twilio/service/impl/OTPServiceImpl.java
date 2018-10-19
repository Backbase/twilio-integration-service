package com.backbase.dbs.capabilities.extended.twilio.service.impl;

import com.backbase.dbs.capabilities.extended.twilio.business.TwilioService;
import com.backbase.dbs.capabilities.extended.twilio.routes.OTPRoute;
import com.backbase.dbs.capabilities.extended.twilio.service.OTPService;
import com.backbase.integration.otp.rest.spec.v1.otp.OTPRequestPostRequestBody;
import com.backbase.integration.otp.rest.spec.v1.otp.OTPRequestPostResponseBody;
import com.twilio.sdk.resource.api.v2010.account.Message;
import org.apache.camel.Consume;
import org.springframework.stereotype.Component;

@Component
public class OTPServiceImpl implements OTPService {

    private TwilioService twilioService;

    public OTPServiceImpl(TwilioService twilioService){
        this.twilioService = twilioService;
    }

    @Consume(uri = OTPRoute.DIRECT_OTP_SEND_OTP_SMS)
    @Override
    public OTPRequestPostResponseBody requestOneTimePassword(OTPRequestPostRequestBody otpRequestPostRequestBody) {
        Message message = twilioService.sendSMS(otpRequestPostRequestBody.getOtp().toString(), otpRequestPostRequestBody.getPhoneNumber());

        return new OTPRequestPostResponseBody().withOtp(Integer.valueOf(otpRequestPostRequestBody.getOtp())).withSmsSent(true);
    }
}
