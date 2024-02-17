package ecomarkets.domain.notification.email;

import java.math.BigDecimal;

public interface EmailTemplate {
    String getBody(String partnerName, Long basketId, BigDecimal paymentValue, String status);
}
