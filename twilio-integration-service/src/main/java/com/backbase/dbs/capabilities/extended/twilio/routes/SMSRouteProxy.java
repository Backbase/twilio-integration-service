package com.backbase.dbs.capabilities.extended.twilio.routes;

import com.backbase.messaging.integration.rest.spec.v1.sms.SendSMSPostRequestBody;
import com.backbase.messaging.integration.rest.spec.v1.sms.SendSMSPostResponseBody;

public interface SMSRouteProxy {
    SendSMSPostResponseBody sendSMS(SendSMSPostRequestBody data);
}
