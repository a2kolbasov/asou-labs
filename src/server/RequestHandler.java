package server;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class RequestHandler implements Runnable {

    private final String filesPath;
    private final Socket clientSocket;
    private final Controller controller;

    BufferedReader input;
    OutputStream output;

    RequestHandler(Socket clientSocket, String filesPath, Controller controller) {
        this.clientSocket = clientSocket;
        this.controller = controller;
        this.filesPath = filesPath;
    }

    @Override
    public void run() {
        try {
            init();
            doIt();
        } catch (IOException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void init() throws IOException {
        System.out.printf("Client accepted (:%d :%d at %d)\n", clientSocket.getLocalPort(), clientSocket.getPort(), System.nanoTime());
        output = clientSocket.getOutputStream();
        InputStream inputStream = clientSocket.getInputStream();
        input = new BufferedReader(new InputStreamReader(inputStream));
    }

    private void doIt() throws IOException, InvocationTargetException, IllegalAccessException {
        final String request =
                readFromSocket();
        System.out.println(request);

        String[] requestHeader = request.split("\n")[0].split(" ");

        String httpMethod = requestHeader[0];
        String httpPath = requestHeader[1];
        String httpVersion = requestHeader[2];

//        System.out.println("httpMethod: " + httpMethod);
//        System.out.println("httpPath: " + httpPath);
//        System.out.println("httpVersion: " + httpVersion);

        if (httpPath.equals("/")) {
            httpPath = "/index.html";
        }
        if (httpPath.contains("..")) {
            writeToSocket(getHttpResponse("File not found", 404));
//            WriteHttpResponseToSocket(outputStream, "File not found", 404);
            return;
        }

        String[] tmp = httpPath.substring(1).split("[?]");
        httpPath = tmp[0];
        String ask = tmp.length > 1 ? tmp[1] : "";
        String body = request.substring(request.indexOf("\n\n") + 2);

        String result =
                getControllerAnswer(httpPath, ask, body);

        if (result == null)
            result = getFile(httpPath);

        if (result == null)
            writeToSocket(getHttpResponse("Not found", 404));
        else
            writeToSocket(getHttpResponse(result));
    }

    private String getControllerAnswer(String httpPath, String ask, String body) throws InvocationTargetException, IllegalAccessException, IOException {
        for (Method controllerMethod : controller.getClass().getMethods()){
            if (controllerMethod.getName().equals(httpPath)){
                return (String) controllerMethod.invoke(controller, ask, body);
            }
        }
        return null;
    }

    private String getFile(String httpPath) throws IOException {
        try {
            System.out.println(
                    Paths.get(filesPath, httpPath).getParent().toString()
            );
            StringBuilder sb = new StringBuilder();
            for (String string : Files.readAllLines(Paths.get(
                    filesPath, httpPath)))
                sb.append(string);
            return sb.toString();
        } catch (IOException e) {
            return null;
        }
    }

    private void doPut(String httpPath, String body) throws IllegalAccessException, IOException, InvocationTargetException {
//        doGet(httpPath);
        String clientAnswer = readFromSocket();

        for (Method method : controller.getClass().getMethods()){
            if (method.getName().equals(httpPath)){
                writeToSocket(getHttpResponse("gut"));
                String answer = readFromSocket();
                System.out.println("answer" + answer);
                writeToSocket(
                        (String) method.invoke(answer));
                return;
            }
        }
        writeToSocket(getHttpResponse("error", 404));
    }

    private String getHttpResponse(String content){return getHttpResponse(content, 200);}
    private String getHttpResponse(String content, int statusCode) {
        return
                "HTTP/1.1 " + statusCode + " OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: " + content.length() + "\r\n" +
                        "Connection: close\r\n\r\n" +
                        content;
    }

//    private String ReadLineFromSocket(BufferedReader reader) throws IOException {
//        String line = null;
//        StringBuilder result = new StringBuilder();
//
//        do {
//            line = reader.readLine();
//
//            if (line != null)
//                result.append(line).append('\n');
//
//        } while (line != null && line.length() > 0);
//
//        return result.toString();
//    }

    private String readFromSocket() throws IOException {
        String line;
        StringBuilder result = new StringBuilder();
        while (input.ready()) {
            char[] tmp = new char[10];
            int len = input.read(tmp);
            result.append(String.valueOf(tmp, 0, len));
            //line = input.readLine();
//            result.append((char) input.read());
            //line = input.read();
            //assert line != null;
            //result.append(line);//.append('\n');
        }
        String r = result.toString();
        //assert !r.equals("");
        return result.toString().replaceAll("\r\n", "\n");
    }
    private void writeToSocket(String message) throws IOException {
        output.write
                (message.getBytes());
        output.flush();
    }
}
