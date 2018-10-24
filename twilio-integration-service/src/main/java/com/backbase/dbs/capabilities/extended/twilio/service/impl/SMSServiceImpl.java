package com.backbase.dbs.capabilities.extended.twilio.service.impl;

import com.backbase.dbs.capabilities.extended.twilio.business.TwilioService;
import com.backbase.dbs.capabilities.extended.twilio.routes.SMSRoute;
import com.backbase.dbs.capabilities.extended.twilio.service.SMSService;
import com.backbase.messaging.integration.rest.spec.v1.sms.SendSMSPostRequestBody;
import com.backbase.messaging.integration.rest.spec.v1.sms.SendSMSPostResponseBody;
import com.twilio.sdk.resource.api.v2010.account.Message;
import org.apache.camel.Consume;
import org.springframework.stereotype.Component;


/**
 * @author Paulo Cardoso
 * @since 1.0.0
 */
@Component
public class SMSServiceImpl implements SMSService {

    private TwilioService twilioService;

    public SMSServiceImpl(TwilioService twilioService) {
        this.twilioService = twilioService;
    }

    /**
     * @param data that contains the phone number and message
     * @return Response Status from Twilio API
     */
    @Override
    @Consume(uri = SMSRoute.DIRECT_SEND_SMS)
    public SendSMSPostResponseBody sendSMS(SendSMSPostRequestBody data) {
        Message message = twilioService.sendSMS(data.getMessage(), data.getPhoneNumber());

        return new SendSMSPostResponseBody().withSmsSent(true);
    }
}
