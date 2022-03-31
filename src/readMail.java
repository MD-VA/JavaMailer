import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
import java.io.IOException;

public class readMail {
    /**************************************************************
     * Méthode statique à utiliser pour récupérer le corps d'un email passé en paramètre
     * @param message
     * @return le corps du mail, dans une String
     * @throws MessagingException
     * @throws IOException
     **************************************************************/
    public static String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    /*****************************************************
     * Méthode private, utilisée par getTextFromMessage
     * @param mimeMultipart
     * @return
     * @throws MessagingException
     * @throws IOException
     *****************************************************/
    private static String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart)  throws MessagingException, IOException {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
            }
        }
        return result;
    }

    public static void main(String[] args) {
        final String username = "javamail132@gmail.com";
        final String password = "eepzgpxwkzhrimef";
        getEmails(username, password);
    }
    public static Message[] getEmails(String username, String password) {
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
            Properties properties = new Properties();
            properties.put("mail.imap.host", "imap.gmail.com");
            properties.put("mail.imap.port", "993");
            properties.put("mail.imap.starttls.enable", "true");
            //Another way to init session
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("imaps");
            store.connect("imap.gmail.com", username, password);

            //Create the IMAP folder object and open it in read-only mode
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            //retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            System.out.println("Nombre de message dans la box" + messages.length);

            //loop through the messages in INBOX
            for (int i = 0, n = messages.length; i < n; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + ((InternetAddress)((Address)(message.getFrom()[0]))).getAddress());
                System.out.println("Date: " + message.getSentDate());
                System.out.println("Content: " + getTextFromMessage(message));
            }

            emailFolder.close(false);
            store.close();
            return messages;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
