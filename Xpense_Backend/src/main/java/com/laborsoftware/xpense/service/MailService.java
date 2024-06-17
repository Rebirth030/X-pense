package com.laborsoftware.xpense.service;

import com.laborsoftware.xpense.domain.Expense;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;


@Service
public class MailService {

    private final JavaMailSender mailSender;

    @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void sendEmail(String to, String subject, String body) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void createExpenseOverTenHourEmail(Expense expense) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        String recipientEmail = expense.getUser().getSuperior().geteMail();
        String subject = "Mitarbeiter %s %s - 10 Stunden Warnung"
                .formatted(expense.getUser().getPrename(), expense.getUser().getLastname());
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<p>")
                .append(expense.getUser().getPrename())
                .append(" hat eine offene Expense die 10 Stunden überschreitet. </p><p>Aktuelle Expense:</p>")
                .append("<ul>")
                .append("<li>Start Datum: ").append(expense.getStartDateTime().format(formatter)).append("</li>")
                .append("<li>End Datum: ");
        if (expense.getEndDateTime() != null) {
            htmlBuilder.append("<li>End Datum: ").append(expense.getEndDateTime().format(formatter)).append("</li>");
        } else {
            htmlBuilder.append("<li>End Datum: ").append(ZonedDateTime.now().format(formatter)).append("</li>");
        }
        htmlBuilder.append("<li>Beschreibung: ").append(expense.getDescription()).append("</li>")
                .append("<li>Projekt Name: ").append(expense.getProject().getName()).append("</li>")
                .append("<li>Status: ").append(expense.getState()).append("</li>")
                .append("</ul>");
        System.out.println(htmlBuilder);
        this.sendEmail(recipientEmail, subject, htmlBuilder.toString());
        System.out.println("Email sent successfully.");
    }

    public void sendEmail1(String to, String subject, String body) {
        // sets SMTP server properties
        Properties props = this.setProperties();

        // creates a new session, no Authenticator (will connect() later)
        Session session = Session.getDefaultInstance(props);

        // creates a new e-mail message
        Message msg = new MimeMessage(session);

        try {
            msg.setFrom(new InternetAddress("luca.doll.study@outlook.com"));
            InternetAddress[] toAddresses = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, toAddresses);
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            // set plain text message
            msg.setText(body);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


        // sends the e-mail
        try (Transport t = session.getTransport("smtp")) {
            t.connect("luca.doll.study@outlook.com", "Lucido98!");
            t.sendMessage(msg, msg.getAllRecipients());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private Properties setProperties() {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.user", "luca.doll.study@outlook.com");
        return props;
    }
}

