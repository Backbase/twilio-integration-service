package com.backbase.dbs.capabilities.extended.route;

import com.backbase.buildingblocks.backend.internalrequest.InternalRequest;
import com.backbase.buildingblocks.logging.api.Logger;
import com.backbase.buildingblocks.logging.api.LoggerFactory;

import org.apache.camel.Consume;
import org.springframework.stereotype.Component;

/**
 * Replace the camel route from the twilio-integration-service to allow configuring pre/post hook, as well as overriding the
 * complete route.
 */
@Component
public class CustomEndpoints 
{

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomEndpoints.class);

    public static final String DIRECT_SEND_WHATSAPP = "direct:send-messaging-whatsapp";

}
