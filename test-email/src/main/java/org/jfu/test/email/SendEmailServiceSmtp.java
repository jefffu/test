package org.jfu.test.email;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmailServiceSmtp implements SendEmailService {

    private String host;
    private Integer port;
    private boolean auth;
    private String user;
    private String password;
    private String fromEmail;
    private String fromName;

    private Session session;

    public SendEmailServiceSmtp(String host, Integer port, boolean auth,
            String user, String password, String fromEmail, String fromName) {
        this.host = host;
        this.port = port;
        this.auth = auth;
        this.user = user;
        this.password = password;
        this.fromEmail = fromEmail;
        this.fromName = fromName;

        this.session = createSession();
    }

    private Session createSession() {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", host);
        if (port != null) {
            props.put("mail.smtp.port", port);
        }
        if (auth) {
            props.put("mail.smtp.auth", true);
        }
        Session session = Session.getInstance(props, null);
        return session;
    }

    private Transport connect() throws MessagingException {
        Transport t = session.getTransport("smtp");
        if (auth) {
            t.connect(user, password);
        } else {
            t.connect();
        }
        return t;
    }

    @Override
    public void sendEmail(EmailVO email) throws AddressException, MessagingException, IOException {
        MimeMessage msg = new MimeMessage(session);
        if (fromEmail != null) {
            if (email.getFromName() != null) {
                msg.setFrom(new InternetAddress(fromEmail, email.getFromName()));
            } else {
                msg.setFrom(new InternetAddress(fromEmail, fromName));
            }
        } else {
            msg.setFrom();
        }

        for (String to : email.getTo()) {
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        }

        if (email.getCc() != null) {
            for (String cc : email.getCc()) {
                msg.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
            }
        }
        if (email.getBcc() != null) {
            for (String bcc : email.getBcc()) {
                msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
            }
        }

        msg.setSubject(email.getSubject());

        msg.setContent(email.getContent());

        msg.setHeader("X-Mailer", fromEmail);
        msg.setSentDate(new Date());

        Transport t = connect();
        try {
            t.sendMessage(msg, msg.getAllRecipients());
        } finally {
            t.close();
        }
    }

}
