/*
package server;

enum StatusCode {
    // OK
    OK(202, ""),
    ACCEPTED(202, "Accepted"),

    // Client errors
    BAD_REQUEST(400, "Bad request"),
    NOT_FOUND(404, "Not found"),

    // Server errors
    NOT_IMPLEMENTED(501, "Unsupported request"),

    ;

    static String getHttpResponse(int statusCode, String content) {
        return
                "HTTP/1.1 " + statusCode + " OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: " + content.length() + "\r\n" +
                        "Connection: close\r\n\r\n" +
                        content;
    }

    private int code;
    private String message;

    StatusCode(int code, String message) {}

    String getHttpResponse() {
        return getHttpResponse(this.code, this.message);
    }
    String getHttpResponse(String message) {
        return getHttpResponse(this.code, message);
    }

    int getCode() {
        return code;
    }
    String getMessage() {
        return message;
    }
}
*/
