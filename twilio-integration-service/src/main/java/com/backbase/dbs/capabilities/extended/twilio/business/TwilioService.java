package com.backbase.dbs.capabilities.extended.twilio.business;

import com.backbase.buildingblocks.logging.api.Logger;
import com.backbase.buildingblocks.logging.api.LoggerFactory;
import com.backbase.dbs.capabilities.extended.twilio.config.TwilioConfigurationProperties;
import com.twilio.sdk.Twilio;
import com.twilio.sdk.resource.api.v2010.account.Message;
import com.twilio.sdk.type.PhoneNumber;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class TwilioService implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwilioService.class);

    private TwilioConfigurationProperties twilioConfigurationProperties;

    public TwilioService(TwilioConfigurationProperties twilioConfigurationProperties) {
        this.twilioConfigurationProperties = twilioConfigurationProperties;
    }

    public Message sendSMS(String content, String phoneNumber) {
        LOGGER.info("Sending SMS to: {}", phoneNumber);
        return Message.create(twilioConfigurationProperties.getAccountSid(),
                new PhoneNumber(phoneNumber),
                new PhoneNumber(twilioConfigurationProperties.getFromNumber()), content).execute();
    }

    @Override
    public void afterPropertiesSet() {
        LOGGER.info("Initializing Twilio");
        Twilio.init(twilioConfigurationProperties.getAccountSid(), twilioConfigurationProperties.getAuthToken());
    }
}
