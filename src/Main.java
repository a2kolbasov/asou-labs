/*
 * Copyright © 2019 Aleksandr Kolbasov
 */

import server.Controller;
import server.Server;
import sql.SqlController;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        String filePath = "./www";
//        Controller controller = new MyController();
        Controller controller = new SqlController();
        Server server = new Server(port, filePath, controller);
        server.start();
    }
}
