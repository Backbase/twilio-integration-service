package com.backbase.dbs.capabilities.extended.otp.service;

import com.backbase.buildingblocks.backend.internalrequest.InternalRequest;
import com.backbase.presentation.otp.rest.spec.v1.one_time_password.OneTimePasswordPostRequestBody;
import com.backbase.presentation.otp.rest.spec.v1.one_time_password.OneTimePasswordPostResponseBody;

public interface OneTimePasswordService {

    OneTimePasswordPostResponseBody requestOneTimePassword(InternalRequest<OneTimePasswordPostRequestBody> internalRequest);
}
