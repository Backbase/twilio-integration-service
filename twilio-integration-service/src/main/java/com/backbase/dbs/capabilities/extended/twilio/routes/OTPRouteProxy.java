package com.backbase.dbs.capabilities.extended.twilio.routes;

import com.backbase.integration.otp.rest.spec.v1.otp.OTPRequestPostRequestBody;
import com.backbase.integration.otp.rest.spec.v1.otp.OTPRequestPostResponseBody;

public interface OTPRouteProxy {

    OTPRequestPostResponseBody sendOTP(OTPRequestPostRequestBody data);
}
