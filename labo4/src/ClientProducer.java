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
            String mess = br.readLine();
            System.out.println("S: " + mess);

			do {
				System.out.println("Inserire stringa o '0' per uscire");
				strToInsert = System.console().readLine();
				pw.println(strToInsert);
                pw.flush();
			} while(strToInsert!=null && !strToInsert.equals("0"));

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