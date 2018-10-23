package com.backbase.dbs.capabilities.extended.twilio.routes;

import com.backbase.buildingblocks.backend.communication.extension.SimpleExtensibleRouteBuilder;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Service extensions may wish to extend this class or provide another
 * {@link com.backbase.buildingblocks.backend.communication.extension.ExtensibleRouteBuilder} implementation using
 * the same {@link #ROUTE_ID}, and annotated with the @Primary annotation to customise this route somehow.
 * </p>
 */
@Component
public class SMSRoute extends SimpleExtensibleRouteBuilder {

    public static final String ROUTE_ID = "messaging-route";
    public static final String DIRECT_SMS_REQUEST = "direct:messaging-request";
    public static final String DIRECT_SEND_SMS = "direct:send-messaging-sms";

    public SMSRoute() {
        super(ROUTE_ID, DIRECT_SMS_REQUEST, DIRECT_SEND_SMS);
    }

}
