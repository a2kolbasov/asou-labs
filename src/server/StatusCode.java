package server;

import static server.Handler.NEW_LINE;

public enum StatusCode {
    // OK
    OK(200, "OK"),
    ACCEPTED(202, "Accepted"),

    // Client errors
    BAD_REQUEST(400, "Bad request"),
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    NOT_FOUND(404, "Not found"),

    // Server errors
    NOT_IMPLEMENTED(501, "Unsupported request"),

    ;

    public final int code;
    public final String content;

    StatusCode(int code, String content) {
        this.code = code;
        this.content = content;
    }

    public static String getHttpResponse(int statusCode, String content) {
        return
                "HTTP/1.1 " + statusCode + NEW_LINE +
                        "Content-Type: text/html" + NEW_LINE +
                        "Content-Length: " + content.length() + NEW_LINE +
                        "Connection: close" + NEW_LINE + NEW_LINE +
                        content;
    }
    public static String getHttpResponse(StatusCode statusCode, String content) {
        return StatusCode.getHttpResponse(statusCode.code, content);
    }
    public static String getHttpResponse(StatusCode statusCode) {
        return StatusCode.getHttpResponse(statusCode.code, statusCode.content);
    }
    public String getHttpResponse() {
        return getHttpResponse(this);
    }
    public String getHttpResponse(String content) {
        return getHttpResponse(this, content);
    }
}
