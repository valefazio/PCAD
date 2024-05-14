import java.net.*;
import java.util.ArrayList;
import java.io.*;
import java.lang.*;

public class ServiceEcho implements Runnable {
	public Socket socket;

	public ServiceEcho(Socket s) {
		this.socket = s;
	}

	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			ArrayList<String> buffer = new ArrayList<String>();
			String mess = br.readLine();

			System.out.println("C: " + mess);
			if (mess.equals("producer")) {
				pw.println("okprod\n");
				pw.flush();
				do {
					mess = br.readLine();
					if (mess != null && !mess.equals("0") && !mess.equals("") && !mess.equals("\n")) {
						buffer.add(mess);
						System.out.println(buffer.size() + ": " + mess);
					}
				} while (mess != null && !mess.equals("0"));
			} else {
				pw.println("okcons\n");
				pw.flush();
				System.out.println(buffer.size());
				if(buffer.size() > 0) {	//PROBLEM: buffer.size() is always 0 for Consumer
					pw.println(buffer.get(0));
					pw.flush();
					buffer.remove(0);
				}
			}

			pw.flush();
			br.close();
			pw.close();
			socket.close();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
}