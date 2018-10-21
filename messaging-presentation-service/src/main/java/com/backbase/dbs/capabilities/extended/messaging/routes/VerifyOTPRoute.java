package com.backbase.dbs.capabilities.extended.messaging.routes;

import com.backbase.buildingblocks.backend.communication.extension.SimpleExtensibleRouteBuilder;
import org.springframework.stereotype.Component;


/**
 * Send OTP Route responsible for provide channels for sending OTP
 * @author Paulo Cardoso
 * @since 1.0.0
 * @see com.backbase.buildingblocks.backend.communication.extension.SimpleExtensibleRouteBuilder
 */
@Component
public class VerifyOTPRoute extends SimpleExtensibleRouteBuilder {

    public static final String ROUTE_ID = "VerifyOTPRoute";

    public VerifyOTPRoute() {
        super(ROUTE_ID, MessaginConstants.DIRECT_BUSINESS_REQUEST_OTP, MessaginConstants.DIRECT_REQUEST_OTP);
    }

}
