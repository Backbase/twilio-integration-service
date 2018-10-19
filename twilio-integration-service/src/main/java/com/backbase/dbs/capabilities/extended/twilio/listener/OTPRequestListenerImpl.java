package com.backbase.dbs.capabilities.extended.twilio.listener;

import com.backbase.buildingblocks.backend.communication.event.annotations.RequestListener;
import com.backbase.buildingblocks.backend.communication.event.proxy.RequestProxyWrapper;
import com.backbase.buildingblocks.backend.internalrequest.InternalRequest;
import com.backbase.buildingblocks.logging.api.Logger;
import com.backbase.buildingblocks.logging.api.LoggerFactory;
import com.backbase.buildingblocks.presentation.errors.BadRequestException;
import com.backbase.buildingblocks.presentation.errors.InternalServerErrorException;
import com.backbase.dbs.capabilities.extended.twilio.routes.OTPRoute;
import com.backbase.dbs.capabilities.extended.twilio.routes.OTPRouteProxy;
import com.backbase.integration.otp.listener.spec.v1.otp.OTPRequestListener;
import com.backbase.integration.otp.rest.spec.v1.otp.OTPRequestPostRequestBody;
import com.backbase.integration.otp.rest.spec.v1.otp.OTPRequestPostResponseBody;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.springframework.stereotype.Service;

@Service
@RequestListener
public class OTPRequestListenerImpl implements OTPRequestListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(OTPRequestListenerImpl.class);

    @Produce(uri = OTPRoute.DIRECT_OTP_REQUEST)
    private OTPRouteProxy otpRouteProxy;

    @Override
    public RequestProxyWrapper<OTPRequestPostResponseBody> postOTPRequest(RequestProxyWrapper<OTPRequestPostRequestBody> oTPRequestPostRequestBody, Exchange exchange) throws BadRequestException, InternalServerErrorException {
        OTPRequestPostResponseBody otpRequestPostResponseBodyRequestProxyWrapper = otpRouteProxy.sendOTP(oTPRequestPostRequestBody.getRequest().getData());
        return buildPostRequestProxyWrapper(otpRequestPostResponseBodyRequestProxyWrapper);
    }

    private RequestProxyWrapper<OTPRequestPostResponseBody> buildPostRequestProxyWrapper(OTPRequestPostResponseBody data) {
        InternalRequest internalRequest = new InternalRequest();
        internalRequest.setData(data);
        RequestProxyWrapper result = new RequestProxyWrapper();
        result.setRequest(internalRequest);
        result.setHttpMethod("post");
        return result;
    }
}
