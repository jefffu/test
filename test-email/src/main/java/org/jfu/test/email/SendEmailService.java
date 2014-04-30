package org.jfu.test.email;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public interface SendEmailService {

    void sendEmail(EmailVO email) throws AddressException, MessagingException, IOException;
}
