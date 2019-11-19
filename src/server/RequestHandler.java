package server;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
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

        httpPath = httpPath.substring(1);
        switch (httpMethod){
            case "GET":
                doGet(httpPath);
                break;
            case "PUT":
            case "POST":
                doPut(httpPath);
                break;
            default:
                writeToSocket(getHttpResponse("Unknown http method", 404));
                return;
        }


//        try {
//            File file = new File(filesPath + httpPath);
//            BufferedReader fileReader = new BufferedReader(new FileReader(file));
//            String fileContent = ReadLineFromSocket(fileReader);
//
//            WriteHttpResponseToSocket(outputStream, fileContent);
//
//        } catch (FileNotFoundException e) {
//            WriteHttpResponseToSocket(outputStream, "File not found", 404);
//        }
    }

    private void doGet(String httpPath) throws InvocationTargetException, IllegalAccessException, IOException {
        String[] methodToCall = httpPath.split("[?]");
        for (Method controllerMethod : controller.getClass().getMethods()){
            if (controllerMethod.getName().equals(methodToCall[0])){
                writeToSocket((String) controllerMethod.invoke(controller,
                        methodToCall.length > 1 ? methodToCall[1] : null));
                return;
            }
        }
    }

    private void doPut(String httpPath) throws IllegalAccessException, IOException, InvocationTargetException {
        doGet(httpPath);
        String clientAnswer = readFromSocket();

        for (Method method : controller.getClass().getMethods()){
            if (method.getName().equals(httpPath)){
                writeToSocket(getHttpResponse("gut"));
                writeToSocket(
                        (String) method.invoke(readFromSocket()));
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
            line = input.readLine();
            assert line != null;
            result.append(line).append('\n');
        }
        String r = result.toString();
        assert !r.equals("");
        return result.toString();
    }
    private void writeToSocket(String message) throws IOException {
        output.write
                (message.getBytes());
        output.flush();
    }
}
