package ecomarkets.domain.notification.email;

import ecomarkets.domain.core.basket.Basket;
import ecomarkets.domain.core.basket.BasketEvent;
import ecomarkets.domain.core.partner.Partner;
import ecomarkets.domain.register.EmailAddress;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;

@ApplicationScoped
public class EmailSenderScheduled {
    @Inject
    EmailNotificationService emailNotificationService;
    @Inject
    EmailTemplate emailTemplate;
    @ConfigProperty(name = "email.from.notification")
    private String emailFrom;

    @Scheduled(every="2m")
    public void sendPendingEmails() {
        List<BasketEvent> basketEvents = BasketEvent.findAll().list();

        basketEvents.stream().forEach(event ->{
            emailNotificationService.send(parse(event));
            event.delete();
        });
    }

    private Email parse(BasketEvent event) {
        Basket basket = Basket.findById(event.getBasketId().id());
        Partner partner = Partner.findById(basket.getPartnerId().id());

        final String status = switch (event.getType()){
            case RESERVED -> "Separada";
            case DELIVERED -> "Entregue";
        };

        Email email = new Email(EmailAddress.of(emailFrom),
                partner.getEmailAddress(),
                getSubject(event.getType()),
                emailTemplate.getBody(partner.getName(), basket.id, basket.totalPayment(), status)
        );

        return email;
    }

    public String getSubject(BasketEvent.EventType eventType){
        return switch (eventType){
            case RESERVED -> "REDE BEM VIVER-ES - Sua Cesta Montada e Conferida!";
            case DELIVERED -> "REDE BEM VIVER-ES - Sua Cesta Chegou!";
        };
    }

}
