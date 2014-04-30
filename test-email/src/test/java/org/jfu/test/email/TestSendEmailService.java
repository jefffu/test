package org.jfu.test.email;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class TestSendEmailService {

    private SendEmailService sendEmailService = new SendEmailServiceSmtp(
            "smtpHost", 25, false, true, "smtpUser", "smtpPassword",
            "fromEmail", "fromName");

    @Test
    public void test() {
        EmailVO email = new EmailVO.Builder(
                "toEmail",
                "subject",
                "textContent",
                "htmlContent")
                .addCc("ccEmail")
                .attachFile("filePath")
                .attach("attachmentName", "attachmentContent".getBytes())
                .build();
        try {
            sendEmailService.sendEmail(email);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
