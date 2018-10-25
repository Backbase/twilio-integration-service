package com.backbase.dbs.capabilities.extended.messaging.util;

import com.backbase.buildingblocks.backend.internalrequest.DefaultInternalRequestContext;
import com.backbase.buildingblocks.backend.internalrequest.InternalRequest;
import com.backbase.buildingblocks.backend.internalrequest.InternalRequestContext;

import javax.servlet.http.HttpServletRequest;

public final class ContextUtil {

    public static final <T> InternalRequest<T> createInternalRequest(T data, HttpServletRequest httpServletRequest, String requestID) {
        InternalRequestContext internalRequestContext = DefaultInternalRequestContext.contextFrom(httpServletRequest, requestID);
        InternalRequest<T> internalRequest = new InternalRequest<>();
        internalRequest.setInternalRequestContext(internalRequestContext);
        internalRequest.setData(data);
        return internalRequest;
    }

    public static final <T> InternalRequest<T> copyInternalRequest(InternalRequest original, T data) {
        InternalRequest result = new InternalRequest();
        result.setInternalRequestContext(original.getInternalRequestContext());
        result.setData(data);
        return result;
    }

}
