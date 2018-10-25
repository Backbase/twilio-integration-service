package com.backbase.dbs.capabilities.extended.service.impl;

import static com.backbase.dbs.capabilities.extended.route.CustomEndpoints.DIRECT_SEND_WHATSAPP;

import com.backbase.buildingblocks.logging.api.Logger;
import com.backbase.buildingblocks.logging.api.LoggerFactory;
import com.backbase.dbs.capabilities.extended.service.WhatsappService;
import com.backbase.dbs.capabilities.extended.twilio.business.TwilioService;
import com.backbase.dbs.capabilities.extended.twilio.config.TwilioConfigurationProperties;
import com.backbase.dbs.capabilities.extended.twilio.routes.SMSRoute;
import com.backbase.dbs.capabilities.extended.twilio.service.SMSService;
import com.backbase.messaging.integration.rest.spec.v1.sms.SendSMSPostRequestBody;
import com.backbase.messaging.integration.rest.spec.v1.sms.SendSMSPostResponseBody;
import com.twilio.sdk.Twilio;
import com.twilio.sdk.resource.api.v2010.account.Message;
import com.twilio.sdk.type.PhoneNumber;
import org.apache.camel.Body;
import org.apache.camel.Consume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author Paulo Cardoso
 * @since 1.0.0
 */
@Component
public class WhatsappServiceImpl implements WhatsappService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WhatsappService.class);

    private final TwilioConfigurationProperties twilioConfigurationProperties;
    private String fromNumber;

    @Autowired
    public WhatsappServiceImpl(TwilioConfigurationProperties twilioConfigurationProperties,
      @Value("${integration.twilio.whatsapp}") String fromNumber) {
        this.twilioConfigurationProperties = twilioConfigurationProperties;
        this.fromNumber = fromNumber;
        Twilio.init(twilioConfigurationProperties.getAccountSid(), twilioConfigurationProperties.getAuthToken());
    }

    @Consume(uri = DIRECT_SEND_WHATSAPP)
    public SendSMSPostResponseBody send(@Body SendSMSPostRequestBody data) {
        String phoneNumber = data.getPhoneNumber();
        LOGGER.info("Sending WhatsApp to: {}, from: {}, {}", phoneNumber, fromNumber, data.getMessage());
        Message message = Message.create(twilioConfigurationProperties.getAccountSid(),
          new PhoneNumber(phoneNumber),
          new PhoneNumber(fromNumber),
          String.format("Welcome. Your OTP is : %d", Integer.parseInt(data.getMessage()))).execute();
        return new SendSMSPostResponseBody().withSmsSent(true);
    }
}
