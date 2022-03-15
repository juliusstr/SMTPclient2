import java.io.*;
import java.net.Socket;
import java.util.Base64;

public class MailClient {
    private final String usermail;
    private final String name;

    public MailClient(String usermail, String name) {
        this.name = name;
        this.usermail = usermail;
    }

    public boolean sendMail(String mailTo, String nameTo, String subject, String content, String filename){
        try {
            Socket socket = new Socket("smtp2.bhsi.xyz", 2525);

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()));

            BufferedWriter writer=
                    new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String serverResponse;
            int clientCode = 0;
            
            while((serverResponse = reader.readLine()) != null){
                System.out.println("Server: " + serverResponse);
                try {
                    clientCode = Integer.parseInt(serverResponse.substring(0, serverResponse.indexOf(" ")));
                } catch(NumberFormatException e){
                    clientCode = 0;
                }
                //System.out.println("Client code: " + clientCode); bug fixing.
                
                switch (clientCode){
                    case 220:
                        writer.write("HELO localHost" + "\r\n");
                        writer.flush();
                        clientCode = 0;
                        break;
                    case 250:
                        if (serverResponse.contains("Nice to meet you")){
                            writer.write("MAIL FROM:<" + usermail + ">\r\n");
                        } else if (serverResponse.contains("New message started")){
                            writer.write("RCPT TO:<" + mailTo + ">\r\n");
                        } else if (serverResponse.contains("Recipient accepted")){
                            writer.write("DATA\r\n");
                        }else if (serverResponse.contains("Mail accepted")) {
                            writer.write("QUIT\r\n");
                        }else{
                            System.out.println("in case 250 recived wrong serverResponse not matching if statements");
                            writer.write("QUIT\r\n");
                        }
                        writer.flush();
                        clientCode = 0;
                        break;
                    case 354:
                        File file = new File(filename);
                        int fileSize = (int) file.length();
                        byte[] data = new byte[fileSize];

                        try (FileInputStream inputStream = new FileInputStream(filename)) {
                            inputStream.read(data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String encodedString = Base64.getEncoder().encodeToString(data);

                        writer.write("From: \"" + name + "\" <" + usermail + ">\r\n");
                        writer.write("To: " + mailTo + " <" + mailTo + ">\r\n");
                        writer.write("Subject: " + subject + "\r\n");
                        writer.write("MIME-Version: 1.0\r\n" +
                                "Content-Type: multipart/mixed; boundary=frontier\r\n" +
                                "\r\n" +
                                "This is a message with multiple parts in MIME format.\r\n" +
                                "--frontier\r\n" +
                                "Content-Type: text/plain\r\n" +
                                "\r\n");
                        writer.write(content + "\r\n");
                        writer.write("--frontier\r\n" +
                                "Content-Type: image/jpeg\r\n" +
                                "Content-Transfer-Encoding: base64\r\n" +
                                "Content-Disposition: attachment;filename=\""+file.getName()+"\"\r\n" +
                                "\r\n" +
                                encodedString +
                                "--frontier--\r\n");
                        writer.write(".\r\n");
                        writer.flush();
                        clientCode = 0;
                        //System.out.println("yep"); debugging
                        break;
                    case 0:
                        break;
                    default:
                        writer.write("QUIT\r\n");
                        writer.flush();
                        clientCode = 0;
                        break;
                }
            }

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
