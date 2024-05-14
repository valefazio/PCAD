import java.net.*;
import java.io.*;

public class ClientConsumer {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 4242);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                    socket.getOutputStream()));

            pw.println("consumer\n");
            pw.flush();
            String mess = br.readLine();
            System.out.println("S: " + mess);
            
            // Read from the server until the server sends a ******** message

            pw.close();
            br.close();
            socket.close();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}