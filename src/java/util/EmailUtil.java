package util;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailUtil {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_USERNAME = "your-email@gmail.com";
    private static final String EMAIL_PASSWORD = "your-app-password";
    
    public static boolean sendEmail(String to, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);
            
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            System.err.println("Error sending email: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean sendOrderConfirmation(String to, String username, int orderId, String totalAmount) {
        String subject = "Order Confirmation - FruitStore";
        String body = "Dear " + username + ",\n\n" +
                     "Thank you for your order!\n\n" +
                     "Order ID: " + orderId + "\n" +
                     "Total Amount: $" + totalAmount + "\n\n" +
                     "Your order is being processed and will be delivered soon.\n\n" +
                     "Best regards,\n" +
                     "FruitStore Team";
        
        return sendEmail(to, subject, body);
    }
    
    public static boolean sendWelcomeEmail(String to, String username) {
        String subject = "Welcome to FruitStore!";
        String body = "Dear " + username + ",\n\n" +
                     "Welcome to FruitStore! Your account has been created successfully.\n\n" +
                     "You can now browse our fresh fruits and place orders.\n\n" +
                     "Happy shopping!\n\n" +
                     "Best regards,\n" +
                     "FruitStore Team";
        
        return sendEmail(to, subject, body);
    }
}