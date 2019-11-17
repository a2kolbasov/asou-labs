import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class RequestHandler implements Runnable {

    private Socket clientSocket;
    private List<Class> controllerList;

    public RequestHandler(Socket clientSocket, List<Class> controllerList) {
        this.clientSocket = clientSocket;
        this.controllerList = controllerList;
    }

    @Override
    public void run() {
        try {
            System.out.println("Clinet acepted");

            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String request = null;
            request = ReadLineFromSocket(reader);
            String[] requestHeader = request.split("\n")[0].split(" ");
            String httpMethod = requestHeader[0];
            String httpPath = requestHeader[1];
            String httpVersion = requestHeader[2];

            System.out.println("httpMethod: " + httpMethod);
            System.out.println("httpPath: " + httpPath);
            System.out.println("httpVersion: " + httpVersion);

            if (httpPath.equals("/")) {
                httpPath = "/index.html";
            }
            if (httpPath.contains("..")) {
                WriteHttpResponseToSocket(outputStream, "File not found", 404);
                return;
            }

            String methodName = httpPath.substring(1);
            for (Class controllerClass : controllerList) {
                for (Method method : controllerClass.getMethods()) {
                    if (method.getName().equalsIgnoreCase(methodName)) {

                        IRequestController controller = (IRequestController) controllerClass.getConstructors()[0].newInstance(new Object[]{});
                        Object resp = method.invoke(controller);

                        WriteHttpResponseToSocket(outputStream, resp.toString());
                        return;
                    }
                }
            }

            try {
                String contentBasePath = "www";
                File file = new File(contentBasePath + httpPath);
                BufferedReader fileReader = new BufferedReader(new FileReader(file));
                String fileContent = ReadLineFromSocket(fileReader);

                WriteHttpResponseToSocket(outputStream, fileContent);

            } catch (FileNotFoundException e) {
                WriteHttpResponseToSocket(outputStream, "File not found", 404);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private static void WriteHttpResponseToSocket(OutputStream outputStream, String responseContent) throws IOException {
        WriteHttpResponseToSocket(outputStream, responseContent, 200);
    }

    private static void WriteHttpResponseToSocket(OutputStream outputStream, String responseContent, int statusCode) throws IOException {
        String responseHeader =
                "HTTP/1.1 " + statusCode + " OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: " + responseContent.length() + "\r\n" +
                        "Connection: close\r\n\r\n" +
                        responseContent;

        outputStream.write(responseHeader.getBytes());
        outputStream.flush();
    }

    private static String ReadLineFromSocket(BufferedReader reader) throws IOException {
        String line = null;
        StringBuilder result = new StringBuilder();

        do {
            line = reader.readLine();

            if (line != null)
                result.append(line).append('\n');

        } while (line != null && line.length() > 0);

        return result.toString();
    }
}
