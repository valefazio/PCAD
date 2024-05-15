import java.net.*;
import java.io.*;

public class ClientProducer {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 4242);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                    socket.getOutputStream()));
			String strToInsert;

            pw.println("producer\n");
            pw.flush();
            String mess;
			do {
				mess = br.readLine();
			} while (mess == null || mess.equals(""));
            System.out.println("S: " + mess);

			System.out.print("Inserire stringa: ");
            strToInsert = System.console().readLine();
            pw.println(strToInsert);
            pw.flush();

            pw.close();
            br.close();
            socket.close();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}