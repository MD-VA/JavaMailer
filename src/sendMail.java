import groovy.swing.factory.TRFactory;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class sendMail {
    public static void main(String[] args) {
        final String username = "javamail132@gmail.com";
        final String password = "eepzgpxwkzhrimef";

        //properties gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session= Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("javamail132@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("dvrvxm@gmail.com"));
            message.setSubject("this is the subject");
            message.setText("This is the body of the email");

            Transport.send(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public sendMail(String to, String subject, String body) {

    }

}
