package ecomarkets.core.domain.notification.email;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import ecomarkets.core.domain.notification.email.EmailTemplate;

import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class EmailTemplateTest {
    @Inject
    EmailTemplate emailTemplate;

    @Test
    public void testTemplateEmailBodyPtBrFormatPrice(){
        emailTemplate.setLocale(Locale.of("pt", "BR"));

        String emailBody = emailTemplate.getBody("Fulano", 1234l,  new BigDecimal(10.5), "Conferido");

        assertTrue(emailBody.contains("Prezado(a) PARCEIRO(A) Fulano"));
        assertTrue(emailBody.contains("Sua Cesta 1234 foi Conferido com SUCESSO."));
        assertTrue(emailBody.contains("Valor Final: R$ 10,50."));
    }

    //TODO use an english template
    @Test
    public void testTemplateEmailBodyUsFormatPrice(){
        emailTemplate.setLocale(Locale.US);

        String emailBody = emailTemplate.getBody("Fulano", 1234l,  new BigDecimal(10.5), "Conferido");

        assertTrue(emailBody.contains("Prezado(a) PARCEIRO(A) Fulano"));
        assertTrue(emailBody.contains("Sua Cesta 1234 foi Conferido com SUCESSO."));
        assertTrue(emailBody.contains("Valor Final: R$ 10.50."));
    }
}
