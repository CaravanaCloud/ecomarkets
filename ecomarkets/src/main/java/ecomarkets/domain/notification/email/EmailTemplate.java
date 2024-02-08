package ecomarkets.domain.notification.email;

public interface EmailTemplate {
    String getBody(String partnerName, Long basketId, Double paymentValue, String status);
}
