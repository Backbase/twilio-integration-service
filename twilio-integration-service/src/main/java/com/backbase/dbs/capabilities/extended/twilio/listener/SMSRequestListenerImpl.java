package com.backbase.dbs.capabilities.extended.twilio.listener;

import com.backbase.buildingblocks.backend.communication.event.annotations.RequestListener;
import com.backbase.buildingblocks.backend.communication.event.proxy.RequestProxyWrapper;
import com.backbase.buildingblocks.backend.internalrequest.InternalRequest;
import com.backbase.buildingblocks.logging.api.Logger;
import com.backbase.buildingblocks.logging.api.LoggerFactory;
import com.backbase.buildingblocks.presentation.errors.BadRequestException;
import com.backbase.buildingblocks.presentation.errors.InternalServerErrorException;
import com.backbase.dbs.capabilities.extended.twilio.routes.SMSRoute;
import com.backbase.dbs.capabilities.extended.twilio.routes.SMSRouteProxy;
import com.backbase.messaging.integration.listener.spec.v1.sms.SendSMSListener;
import com.backbase.messaging.integration.rest.spec.v1.sms.SendSMSPostRequestBody;
import com.backbase.messaging.integration.rest.spec.v1.sms.SendSMSPostResponseBody;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.springframework.stereotype.Service;


/**
 * SMS Send Listener provide. List the message queue:
 * <b>com.backbase.com.backbase.dbs.capabilities.extended.messaging.integration.listener.v1.sms.post"</b>
 *
 * @author Paulo Cardoso
 * @since 1.0.0
 */
@Service
@RequestListener
public class SMSRequestListenerImpl implements SendSMSListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SMSRequestListenerImpl.class);

    @Produce(uri = SMSRoute.DIRECT_SMS_REQUEST)
    private SMSRouteProxy smsRouteProxy;


    /**
     * @param sendSMSPostRequestBody
     * @param exchange
     * @return
     * @throws BadRequestException
     * @throws InternalServerErrorException
     */
    @Override
    public RequestProxyWrapper<SendSMSPostResponseBody> postSendSMS(RequestProxyWrapper<SendSMSPostRequestBody> sendSMSPostRequestBody, Exchange exchange) throws BadRequestException, InternalServerErrorException {
        LOGGER.info("Request received. Sending SMS");
        SendSMSPostResponseBody otpRequestPostResponseBodyRequestProxyWrapper = smsRouteProxy.sendSMS(sendSMSPostRequestBody.getRequest().getData());
        return buildPostRequestProxyWrapper(otpRequestPostResponseBodyRequestProxyWrapper);
    }

    /**
     * @param data
     * @return
     */
    private RequestProxyWrapper<SendSMSPostResponseBody> buildPostRequestProxyWrapper(SendSMSPostResponseBody data) {
        InternalRequest internalRequest = new InternalRequest();
        internalRequest.setData(data);
        RequestProxyWrapper result = new RequestProxyWrapper();
        result.setRequest(internalRequest);
        result.setHttpMethod("post");
        return result;
    }

}
