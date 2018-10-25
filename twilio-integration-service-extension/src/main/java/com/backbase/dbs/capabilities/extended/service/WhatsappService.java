package com.backbase.dbs.capabilities.extended.service;


import com.backbase.messaging.integration.rest.spec.v1.sms.SendSMSPostRequestBody;
import com.backbase.messaging.integration.rest.spec.v1.sms.SendSMSPostResponseBody;

public interface WhatsappService {

    SendSMSPostResponseBody send(SendSMSPostRequestBody data);
}
