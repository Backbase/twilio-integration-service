package com.backbase.dbs.capabilities.extended.twilio.service;


import com.backbase.messaging.integration.rest.spec.v1.sms.SendSMSPostRequestBody;
import com.backbase.messaging.integration.rest.spec.v1.sms.SendSMSPostResponseBody;

public interface SMSService {

    SendSMSPostResponseBody sendSMS(SendSMSPostRequestBody data);
}
