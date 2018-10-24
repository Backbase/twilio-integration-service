package com.backbase.dbs.capabilities.extended.messaging.routes;

/**
 * Util class for keep the Camel Channel references
 *
 * @author Paulo Cardoso
 * @since 1.0.0
 */
public final class MessaginConstants {

    public static final String DIRECT_BUSINESS_REQUEST_OTP = "direct:business.request.otp";
    public static final String DIRECT_REQUEST_OTP = "direct:request.messaging.otp";

    public static final String DIRECT_VERIFY_OTP = "direct:request.messaging.verify.otp";
    public static final String DIRECT_BUSINESS_VERIFY_OTP = "direct:business.verity.otp";

    private MessaginConstants() {
    }

}
