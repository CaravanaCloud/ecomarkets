package ecomarkets.infra;

import ecomarkets.domain.notification.email.EmailTemplate;
import io.quarkus.qute.Template;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class EmailTemplateImp implements EmailTemplate {

    @Inject
    Template basket;

    @Override
    public String getBody(String partnerName, Long basketId, Double paymentValue, String status){
        return basket
                .data("partnerName", partnerName)
                .data("basketId", basketId)
                .data("paymentValue", paymentValue)
                .data("status", status).render();
    }
}
