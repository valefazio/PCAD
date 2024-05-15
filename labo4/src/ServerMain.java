import java.net.*;

public class ServerMain {
    public static void main(String[] args) {
        try {
            try (ServerSocket server = new ServerSocket(4242)) {
                while (true) {
                    Socket socket = server.accept();
                    //Server serv = new Server(socket);
                    ServerLimited serv = new ServerLimited(socket);
                    Thread t = new Thread(serv);
                    t.start();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}