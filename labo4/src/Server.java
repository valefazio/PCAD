import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class Server implements Runnable {
	public Socket socket;
	static ArrayList<String> buffer = new ArrayList<String>();

	public Server(Socket s) {
		this.socket = s;
	}

	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			String mess = br.readLine();

			System.out.println("C: " + mess);
			if (mess.equals("producer")) {
				try {
					pw.println("okprod\n");
					pw.flush();
					do {
						mess = br.readLine();
					} while (mess == null || mess.equals(""));
					synchronized (buffer) {
						buffer.add(mess);
						buffer.notifyAll();
					}
				} catch (Exception e) {
					System.out.println(e);
					e.printStackTrace();
				}
			} else {
				try {
					pw.println("okcons\n");
					pw.flush();

					synchronized (buffer) {
						while (buffer.isEmpty()) {
							buffer.wait();
						}
						pw.println(buffer.get(0));
						pw.flush();
						buffer.remove(0);
					}
				} catch (Exception e) {
					System.out.println(e);
					e.printStackTrace();
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
