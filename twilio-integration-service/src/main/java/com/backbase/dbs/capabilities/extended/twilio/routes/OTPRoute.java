package com.backbase.dbs.capabilities.extended.twilio.routes;

import com.backbase.buildingblocks.backend.communication.extension.SimpleExtensibleRouteBuilder;
import org.springframework.stereotype.Component;

/**
 * An example camel route for a service.
 * <p>
 *     Service extensions may wish to extend this class or provide another
 *     {@link com.backbase.buildingblocks.backend.communication.extension.ExtensibleRouteBuilder} implementation using
 *     the same {@link #ROUTE_ID}, and annotated with the @Primary annotation to customise this route somehow.
 * </p>
 */
@Component
public class OTPRoute extends SimpleExtensibleRouteBuilder {

    public static final String ROUTE_ID = "messaging-route";
    public static final String DIRECT_OTP_REQUEST = "direct:messaging-request";
    public static final String DIRECT_OTP_SEND_OTP_SMS = "direct:send-messaging-sms";

    public OTPRoute() {
        super(ROUTE_ID, DIRECT_OTP_REQUEST, DIRECT_OTP_SEND_OTP_SMS);
    }

}
