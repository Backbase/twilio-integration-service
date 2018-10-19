package com.backbase.dbs.capabilities.extended.otp.routes;

import com.backbase.buildingblocks.backend.communication.extension.SimpleExtensibleRouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class OTPRoute extends SimpleExtensibleRouteBuilder {

    public static final String ROUTE_ID = "OTPRoute";

    public OTPRoute() {
        super(ROUTE_ID, OTPConstants.DIRECT_BUSINESS_REQUEST_OTP, OTPConstants.DIRECT_REQUEST_OTP);
    }

}
