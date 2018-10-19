package com.backbase.dbs.capabilities.extended.otp.api;

import com.backbase.buildingblocks.backend.api.IDUtils;
import com.backbase.buildingblocks.backend.communication.event.util.LoggingUtils;
import com.backbase.buildingblocks.backend.internalrequest.DefaultInternalRequestContext;
import com.backbase.buildingblocks.backend.internalrequest.InternalRequest;
import com.backbase.buildingblocks.backend.internalrequest.InternalRequestContext;
import com.backbase.buildingblocks.logging.api.Logger;
import com.backbase.buildingblocks.logging.api.LoggerFactory;
import com.backbase.buildingblocks.presentation.errors.InternalServerErrorException;
import com.backbase.dbs.capabilities.extended.otp.routes.OTPConstants;
import com.backbase.dbs.capabilities.extended.otp.routes.OTPRouteProxy;
import com.backbase.presentation.otp.rest.spec.v1.one_time_password.*;
import org.apache.camel.Produce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Rest controller for the service, defines all methods listed in the RAML
 */
@RestController
public class OTPController implements OneTimePasswordApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(OTPController.class);

    @Autowired
    private IDUtils idUtils;

    @Produce(uri = OTPConstants.DIRECT_BUSINESS_REQUEST_OTP)
    private OTPRouteProxy otpRouteProxy;

    @Override
    public OneTimePasswordPostResponseBody postOneTimePassword(@RequestBody @Valid OneTimePasswordPostRequestBody oneTimePasswordPostRequestBody,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) throws InternalServerErrorException {
        try {
            InternalRequest<OneTimePasswordPostRequestBody> internalRequest = createInternalRequest(oneTimePasswordPostRequestBody, httpServletRequest);
            return otpRouteProxy.requestOneTimePassword(internalRequest);
        } catch (Exception e) {
            LOGGER.error(LoggingUtils.ERROR_CAUGHT_REQUEST, e.getMessage(), e);
            throw new InternalServerErrorException("An unexpected error occurred");
        }
    }

    @Override
    public OneTimePasswordVerifyPostResponseBody postOneTimePasswordVerify(@RequestBody @Valid OneTimePasswordVerifyPostRequestBody oneTimePasswordVerifyPostRequestBody,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) throws InternalServerErrorException {
        try {
            InternalRequest<OneTimePasswordVerifyPostRequestBody> internalRequest = createInternalRequest(oneTimePasswordVerifyPostRequestBody, httpServletRequest);
            return otpRouteProxy.verifyOneTimePassword(internalRequest);
        } catch (Exception e) {
            LOGGER.error(LoggingUtils.ERROR_CAUGHT_REQUEST, e.getMessage(), e);
            throw new InternalServerErrorException("An unexpected error occurred");
        }
    }

    private <T> InternalRequest<T> createInternalRequest(T data, HttpServletRequest httpServletRequest) {
        InternalRequestContext internalRequestContext = DefaultInternalRequestContext.contextFrom(httpServletRequest, idUtils.generateRandomID());
        InternalRequest<T> internalRequest = new InternalRequest<>();
        internalRequest.setInternalRequestContext(internalRequestContext);
        internalRequest.setData(data);
        return internalRequest;
    }

}
