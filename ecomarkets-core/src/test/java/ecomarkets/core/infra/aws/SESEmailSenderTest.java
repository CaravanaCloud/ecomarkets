package ecomarkets.infra.aws;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import ecomarkets.core.domain.notification.email.Email;
import ecomarkets.core.domain.register.EmailAddress;
import ecomarkets.core.infra.aws.SESEmailSender;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.VerifyEmailIdentityRequest;
import software.amazon.awssdk.services.ses.model.VerifyEmailIdentityResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class SESEmailSenderTest {
    @ConfigProperty(name = "email.from.notification")
    private String emailFrom;
    @Inject
    SESEmailSender sesEmailSender;
    @Inject
    SesClient ses;
    @TestTransaction
    @Test
    public void testSendEmail(){

        VerifyEmailIdentityRequest request = VerifyEmailIdentityRequest.builder()
                .emailAddress(emailFrom)
                .build();

        VerifyEmailIdentityResponse resp = ses.verifyEmailIdentity(request);

        Email email = new Email(EmailAddress.of(emailFrom),
                EmailAddress.of("test@example.com"),
                "Test email notification",
                " This is a email body!"
        );

        sesEmailSender.send(email);
    }



}

