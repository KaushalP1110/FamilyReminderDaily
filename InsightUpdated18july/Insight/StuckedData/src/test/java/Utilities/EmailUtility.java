package Utilities;

import Test.Insight;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

import static Test.Insight.date_range;

public class EmailUtility {

    // Map to store clients with reminder sent
    public static Set<String> clientsWithReminder = new HashSet<>();

    // Map to store client -> list of patient names
    public static Map<String, List<String>> patientReminderMap = new HashMap<>();

    public static void sendEmailWithAttachment(String host, String port, final String userName,
                                               final String password, String toAddress,
                                               String subject, String message, String attachFiles) throws MessagingException {

        // Set properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };

        // Create session
        Session session = Session.getInstance(properties, auth);

        // Create message
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(userName));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
        msg.setSubject(subject);
        msg.setSentDate(new Date());

        // Create email content (HTML body + attachment)
        MimeMultipart multipart = new MimeMultipart("mixed");

        // HTML body part
        MimeBodyPart htmlBodyPart = new MimeBodyPart();
        htmlBodyPart.setContent(buildHtmlContent(), "text/html");
        multipart.addBodyPart(htmlBodyPart);

        // Attachment part
        if (attachFiles != null) {
            MimeBodyPart attachPart = new MimeBodyPart();
            DataSource source = new FileDataSource(attachFiles);
            attachPart.setDataHandler(new DataHandler(source));
            attachPart.setFileName("InsightReport.html");
            multipart.addBodyPart(attachPart);
        }

        // Set full content
        msg.setContent(multipart);

        // Send message
        Transport.send(msg);
        System.out.println("Email sent successfully to: " + toAddress);
    }

    private static String buildHtmlContent() {
        StringBuilder html = new StringBuilder();

        html.append("<html><body>");
        html.append("<h2 style='color:#0073e6;'>🚀 Family Reminder Insight Report</h2>");
        html.append("<p>Date: <b>").append(date_range).append("</b></p>");

        if (clientsWithReminder.isEmpty()) {
            html.append("<p style='color:red;'>❌ No Family Reminders Sent</p>");
        } else {
            html.append("<p style='color:green;'>✅ Family Reminders Sent for the following Clients:</p>");
            html.append("<ul>");
            for (String client : clientsWithReminder) {
                html.append("<li><b>").append(client).append("</b>");
                List<String> patients = patientReminderMap.getOrDefault(client, new ArrayList<>());
                if (!patients.isEmpty()) {
                    html.append("<ul>");
                    for (String patient : patients) {
                        html.append("<li>").append(patient).append("</li>");
                    }
                    html.append("</ul>");
                }
                html.append("</li>");
            }
            html.append("</ul>");
        }

        html.append("<p>📎 Please find the attached detailed report.</p>");
        html.append("</body></html>");

        return html.toString();
    }
}
