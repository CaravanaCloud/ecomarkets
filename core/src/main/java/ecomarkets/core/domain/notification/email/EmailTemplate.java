package ecomarkets.core.domain.notification.email;

import java.math.BigDecimal;
import java.util.Locale;

public interface EmailTemplate {
    String getBody(String partnerName, Long basketId, BigDecimal paymentValue, String status);

    void setLocale(Locale locale);
}
