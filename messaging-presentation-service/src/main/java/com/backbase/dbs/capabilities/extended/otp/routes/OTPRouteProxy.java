package com.backbase.dbs.capabilities.extended.otp.routes;

import com.backbase.buildingblocks.backend.internalrequest.InternalRequest;
import com.backbase.presentation.otp.rest.spec.v1.one_time_password.OneTimePasswordPostRequestBody;
import com.backbase.presentation.otp.rest.spec.v1.one_time_password.OneTimePasswordPostResponseBody;
import com.backbase.presentation.otp.rest.spec.v1.one_time_password.OneTimePasswordVerifyPostRequestBody;
import com.backbase.presentation.otp.rest.spec.v1.one_time_password.OneTimePasswordVerifyPostResponseBody;

public interface OTPRouteProxy {

    OneTimePasswordPostResponseBody requestOneTimePassword(InternalRequest<OneTimePasswordPostRequestBody> requestBodyInternalRequest);

    OneTimePasswordVerifyPostResponseBody verifyOneTimePassword(InternalRequest<OneTimePasswordVerifyPostRequestBody> internalRequest);

}
