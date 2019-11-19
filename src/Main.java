import server.Controller;
import server.Server;
import server.TestController;

import java.io.IOException;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) throws IOException {
        Server server = new Server(8080, "www", new TestController());
        server.start();
    }
}
