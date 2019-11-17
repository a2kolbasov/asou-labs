import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            List<Class> controllerList = new ArrayList<>();
            controllerList.add(TestController.class);

            ServerSocket socket = new ServerSocket(80);

            System.out.println("Wait clinet...");

            while (true) {
                Socket clientSocket = socket.accept();
                new Thread(new RequestHandler(clientSocket, controllerList)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
