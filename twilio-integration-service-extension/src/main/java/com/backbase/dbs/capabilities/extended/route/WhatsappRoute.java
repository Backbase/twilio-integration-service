package com.backbase.dbs.capabilities.extended.route;

import static com.backbase.dbs.capabilities.extended.route.CustomEndpoints.DIRECT_SEND_WHATSAPP;

import com.backbase.messaging.integration.rest.spec.v1.sms.SendSMSPostRequestBody;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.model.RouteDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Extend the twilio-integration-service to allow configuring pre/post hook.
 */
@Component
@Primary
public class WhatsappRoute extends com.backbase.dbs.capabilities.extended.twilio.routes.SMSRoute
{
    private final Predicate isWhatsApp = new Predicate() {
        public boolean matches(Exchange exchange) {
            SendSMSPostRequestBody smsPostRequestBody = exchange.getIn().getBody(SendSMSPostRequestBody.class);
            return smsPostRequestBody.getPhoneNumber().toLowerCase().contains("whatsapp");
        }
    };

    @Override
    protected void configurePreHook(RouteDefinition rd) throws Exception {
        rd.filter(isWhatsApp)
          .to(DIRECT_SEND_WHATSAPP)
          .setProperty(Exchange.ROUTE_STOP).constant(true)
          .end();
    }

}
