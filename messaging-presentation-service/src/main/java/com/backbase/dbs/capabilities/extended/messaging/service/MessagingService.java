package com.backbase.dbs.capabilities.extended.messaging.service;

import com.backbase.buildingblocks.backend.internalrequest.InternalRequest;
import com.backbase.com.backbase.dbs.capabilities.extended.messaging.presentation.rest.spec.v1.otp.OneTimePasswordPostRequestBody;
import com.backbase.com.backbase.dbs.capabilities.extended.messaging.presentation.rest.spec.v1.otp.OneTimePasswordPostResponseBody;
import com.backbase.com.backbase.dbs.capabilities.extended.messaging.presentation.rest.spec.v1.otp.OneTimePasswordVerifyPostRequestBody;
import com.backbase.com.backbase.dbs.capabilities.extended.messaging.presentation.rest.spec.v1.otp.OneTimePasswordVerifyPostResponseBody;

public interface MessagingService {

    OneTimePasswordPostResponseBody requestOneTimePassword(InternalRequest<OneTimePasswordPostRequestBody> internalRequest);

    OneTimePasswordVerifyPostResponseBody verifyOneTimePassword(InternalRequest<OneTimePasswordVerifyPostRequestBody> internalRequest);


}
