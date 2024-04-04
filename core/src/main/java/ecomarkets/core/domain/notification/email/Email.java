package ecomarkets.core.domain.notification.email;

import ecomarkets.core.domain.register.EmailAddress;

public class Email {
    private EmailAddress from;
    private EmailAddress to;
    private String subject;
    private String body;

    private Email(){}

    public Email(EmailAddress from, EmailAddress to, String subject, String body) {
        this();
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public EmailAddress getFrom() {
        return from;
    }

    public EmailAddress getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

}
