package ecomarkets.core.domain.notification.email;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import ecomarkets.core.domain.core.basket.Basket;
import ecomarkets.core.domain.core.basket.event.*;
import ecomarkets.core.domain.core.partner.Partner;
import ecomarkets.core.domain.register.EmailAddress;

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
        List<EmailPendingToSend> emailPendingList = EmailPendingToSend.findAll().list();

        emailPendingList.stream().forEach(e ->{
            emailNotificationService.send(parse(e.getBasketEventId()));
            e.delete();
        });
    }

    private Email parse(BasketEventId eventId) {
        BasketEvent basketEvent = BasketEvent.findById(eventId.id());
        Basket basket = Basket.findById(basketEvent.getBasketId().id());
        Partner partner = Partner.findById(basket.getPartnerId().id());

        /*TODO check 
        final String status = switch (basketEvent){
            case BasketReservedEvent r -> "Separada";
            case BasketDeliveredEvent d -> "Entregue";
            case BasketPayedEvent p -> "Paga";
            default -> throw new IllegalStateException("event not supported!");
        };*/
        final String status;
        if (basketEvent instanceof BasketReservedEvent) {
            status = "Separada";
        } else if (basketEvent instanceof BasketDeliveredEvent) {
            status = "Entregue";
        } else if (basketEvent instanceof BasketPayedEvent) {
            status = "Paga";
        } else {
            throw new IllegalStateException("event not supported!");
        }


        Email email = new Email(EmailAddress.of(emailFrom),
                partner.getEmailAddress(),
                getSubject(basketEvent),
                emailTemplate.getBody(partner.getName(), basket.id, basket.totalPayment(), status)
        );

        return email;
    }

    public String getSubject(BasketEvent event){
        /* TODO
        return switch (event){
            case BasketReservedEvent r -> "REDE BEM VIVER-ES - Sua Cesta Montada e Conferida!";
            case BasketDeliveredEvent d -> "REDE BEM VIVER-ES - Sua Cesta Chegou!";
            case BasketPayedEvent p -> "REDE BEM VIVER-ES - Pagamento confirmado!";
            default -> throw new IllegalStateException("event not supported!");
        };
         */
        if (event instanceof BasketReservedEvent) {
            return "REDE BEM VIVER-ES - Sua Cesta Montada e Conferida!";
        } else if (event instanceof BasketDeliveredEvent) {
            return "REDE BEM VIVER-ES - Sua Cesta Chegou!";
        } else if (event instanceof BasketPayedEvent) {
            return "REDE BEM VIVER-ES - Pagamento confirmado!";
        } else {
            throw new IllegalStateException("event not supported!");
        }
        
    }

}
