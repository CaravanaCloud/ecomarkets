package ecomarkets.core.infra;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import ecomarkets.core.domain.notification.email.EmailTemplate;


@ApplicationScoped
public class EmailTemplateImp implements EmailTemplate {
    // TODO: Fix template resolution
    // @Location("email/basket-notification.html")
    Template basketTemplate;

    @Override
    public String getBody(String partnerName, Long basketId, BigDecimal paymentValue, String status){
        return basketTemplate
                .data("partnerName", partnerName)
                .data("basketId", basketId)
                .data("paymentValue", formatPayment(paymentValue))
                .data("status", status).render();
    }

    //TODO add internationalization
    public String formatPayment(BigDecimal value){
        DecimalFormat nf =  new DecimalFormat("#,###,##0.00");;
        nf.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.getDefault()));
        return nf.format(value);
    }
}
