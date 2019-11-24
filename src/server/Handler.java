package server;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

class Handler implements Runnable {
    final static String NEW_LINE = "\r\n";

    private final String filesPath;
    private final Socket clientSocket;
    private final Controller controller;

    private BufferedReader input;
    private OutputStream output;

    Handler(Socket clientSocket, String filesPath, Controller controller) {
        this.clientSocket = clientSocket;
        this.controller = controller;
        this.filesPath = filesPath;
    }

    @Override
    public void run() {
        try {
            init();
            doIt();
        } catch (IOException | IllegalAccessException | InvocationTargetException ignored) {
            ignored.printStackTrace();
        }
        finally {
            try {
                input.close();
                output.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void init() throws IOException {
        System.out.printf("Client accepted (:%d :%d at %d)\n",
                clientSocket.getLocalPort(), clientSocket.getPort(), System.nanoTime());
        output = clientSocket.getOutputStream();
        InputStream inputStream = clientSocket.getInputStream();
        input = new BufferedReader(new InputStreamReader(inputStream));
    }

    private void doIt() throws IOException, InvocationTargetException, IllegalAccessException {
        final String request =
                readFromSocket();
        System.out.println(request);

        String[] requestHeader = request.toLowerCase().split("\n")[0].split(" ");

        String httpMethod = requestHeader[0];
        String httpPath = requestHeader[1];
        String httpVersion = requestHeader[2];

        if (httpPath.equals("/")) {
            httpPath = "/index.html";
        }
        if (httpPath.contains("..")) {
            writeToSocket(StatusCode.BAD_REQUEST.getHttpResponse());
            // Stop Handler
            throw new IOException();
        }

        Controller.Parameters requestParameters;
        {
            String query, body;
            String[] parts = httpPath.substring(1).split("[?]");
            httpPath = parts[0];
            query = parts.length > 1 ? parts[1] : "";

            int indexOfBody = request.indexOf("\n\n");
            body = request.substring(
                    // Если найдено
                    indexOfBody != -1 ?
                            // Смещение на 2 '\n'
                            indexOfBody + 2 :
                            // Иниче вернёт пустую строку ""
                            request.length()
            );
            requestParameters = new Controller.Parameters(httpMethod, query, body);
        }

        String result = getControllerAnswer(httpPath, requestParameters);
        if (result != null)
            writeToSocket(result);
        else {
            result = getFile(httpPath);
            writeToSocket(
                    result != null ?
                            result : StatusCode.NOT_FOUND.getHttpResponse()
            );
        }
    }

    private String getControllerAnswer(String httpPath, Controller.Parameters requestParameters) throws InvocationTargetException, IllegalAccessException {
        for (Method controllerMethod : controller.getClass().getMethods()){
            if (controllerMethod.getName()
                    .toLowerCase().equals(httpPath)){
                return (String) controllerMethod.invoke(controller, requestParameters);
            }
        }
        // If method isn't found
        return null;
    }

    private String getFile(String httpPath) {
        try {
            StringBuilder sb = new StringBuilder();
            for (String string : Files.readAllLines(Paths.get(
                    filesPath, httpPath)))
                sb.append(string).append(NEW_LINE);
            return StatusCode.OK.getHttpResponse(sb.toString());
        } catch (IOException e) {
            return null;
        }
    }

    private String readFromSocket() throws IOException {
        StringBuilder result = new StringBuilder();
        while (input.ready()) {
            char[] buffer = new char[32];
            int length = input.read(buffer);
            result.append(String.valueOf(buffer, 0, length));
        }
        return result.toString().replaceAll("\r\n", "\n");
    }

    private void writeToSocket(String content) throws IOException {
        output.write(
                content.getBytes());
        output.flush();
    }

}
