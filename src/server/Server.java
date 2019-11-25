/*
 * Copyright © 2019 Alexander Kolbasov
 */

package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final int port;
    private final String path;
    private final Controller controller;

    public Server(int port, String filesPath, Controller controller){
        this.port = port;
        this.path = filesPath;
        this.controller = controller;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.printf("Wait client on :%d\n\n", port);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(
                    new Handler(clientSocket, path, controller)
            ).start();
        }
    }
}
