import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println();
        MailClient mail = new MailClient("nop@nop.dk", "nop");
        mail.sendMail("yep@yep.dk","yep", "gruppe 25", "detter er gruppe 25", "WIN_20220315_17_10_02_Pro.jpg"); // sender en mail til dist.bhsi.xys
        SenderMail.mail("dtutester2022@gmail.com", "TEST2022", "dtutester2022@gmail.com", 465,"WIN_20220315_17_10_02_Pro.jpg", "her skal der være et emne", "her et brødteksten"); // sender en mail fra en gmail til en anden gmail?


    }
}
