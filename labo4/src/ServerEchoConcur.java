import java.net.*;
import java.io.*;

public class ServerEchoConcur {
    public static void main(String[] args) {
        try {
            try (ServerSocket server = new ServerSocket(4242)) {
                while (true) {
                    Socket socket = server.accept();
                    ServiceEcho serv = new ServiceEcho(socket);
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