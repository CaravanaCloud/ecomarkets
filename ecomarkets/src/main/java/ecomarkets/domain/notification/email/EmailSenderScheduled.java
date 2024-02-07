package ecomarkets.domain.notification.email;

import ecomarkets.domain.core.basket.Basket;
import ecomarkets.domain.core.basket.BasketEvent;
import ecomarkets.domain.core.partner.Partner;
import ecomarkets.domain.register.EmailAddress;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.text.MessageFormat;
import java.util.List;

@ApplicationScoped
public class EmailSenderScheduled {

    @ConfigProperty(name = "email.from.notification")
    private String emailFrom;
    @Inject
    EmailNotificationService emailNotificationService;
    @Scheduled(every="2m")
    public void sendPendingEmails() {
        List<BasketEvent> basketEvents = BasketEvent.findAll().list();

        basketEvents.stream().forEach(e ->{
            emailNotificationService.send(parse(e));
            e.delete();
        });
    }

    private Email parse(BasketEvent event) {
        Basket basket = Basket.findById(event.getBasketId().id());
        Partner partner = Partner.findById(basket.getPartnerId().id());

        Email email = new Email(EmailAddress.of(emailFrom),
                partner.getEmailAddress(),
                getSubject(event.getType()),
                getBody(basket, partner, event.getType())
        );

        return email;
    }

    //TODO Change to use parameters and template from database
    private String getSubject(BasketEvent.EventType eventType){
        return switch (eventType){
            case RESERVED -> "REDE BEM VIVER-ES - Sua Cesta Montada e Conferida!";
            case DELIVERED -> "REDE BEM VIVER-ES - Sua Cesta Chegou!";
        };
    }

    //TODO Change to use parameters and template from database. It is for test...
    private String getBody(Basket basket, Partner partner, BasketEvent.EventType eventType){
        final String template = """
                    Prezado(a) PARCEIRO(A) {0}
                                        
                    Sua Cesta {1} foi {2} com SUCESSO.
                                        
                    Valor Final: R$ {3}.
                                        
                    Agradecemos a parceria!
                                        
                    *** EMAIL AUTOMÁTICO - FAVOR NÃO RESPONDER O MESMO. Dívidas use os canais de comunicação abaixo! *** Att.
                                        
                    REDE BEM VIVER-ES / MPA Serrana
                    Tel: (27) 12345-6789
                    email: mpaescestacamponesa@gmail.com
                    """;
        return switch (eventType){
            case RESERVED -> MessageFormat.format(template, partner.getName(), basket.basketId(), "Conferida/Separada", basket.totalPayment());
            case DELIVERED -> MessageFormat.format(template, partner.getName(), basket.basketId(), "Entregue", basket.totalPayment());
        };
    }

}
