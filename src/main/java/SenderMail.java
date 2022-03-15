import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class SenderMail {
    public static void mail(String from, String password, String to, int port, String filepath, String subjekt, String text) {

        //String filepath = "C:/Users/juliu/Pictures/Camera Roll/WIN_20220308_15_19_16_Pro.jpg";

        // Recipient's email ID needs to be mentioned.
        //String from = "struwingjulius3@gmail.com";

        // Sender's email ID needs to be mentioned
        //String to = "anlcmail1992@gmail.com";

        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(from, password);

            }

        });

        try {
            MimeMessage besked = new MimeMessage(session);
            besked.setFrom(new InternetAddress(from));
            besked.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            besked.setSubject(subjekt);
            besked.setText(text);

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filepath);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filepath);
            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            besked.setContent(multipart);
            System.out.println("sending...");
            Transport.send(besked);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
