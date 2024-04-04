package ecomarkets.core.infra.aws;

import ecomarkets.core.domain.notification.email.Email;
import ecomarkets.core.domain.notification.email.EmailNotificationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import software.amazon.awssdk.services.ses.SesClient;

@ApplicationScoped
public class SESEmailSender implements EmailNotificationService {
    @Inject
    SesClient ses;

    @Transactional
    public void send(Email email) {
        final String messageId = ses.sendEmail(req -> req
                .source(email.getFrom().value())
                .destination(d -> d.toAddresses(email.getTo().value()))
                .message(msg -> msg
                        .subject(sub -> sub.data(email.getSubject()))
                        .body(b -> b.text(txt -> txt.data(email.getBody()))))).messageId();
        //TODO add observability for emails sent and error treatment
    }

}
