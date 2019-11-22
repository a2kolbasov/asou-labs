import server.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        String filePath = "./www";
        Server server = new Server(port, filePath, new MyController());
        server.start();
    }
}
