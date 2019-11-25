/*
 * Copyright © 2019 Alexander Kolbasov
 */

package server;

public enum Response {
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

    final static String NEW_LINE = "\r\n";

    public final int code;
    public final String content;

    Response(int statusCode, String content) {
        this.code = statusCode;
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

    public static String getHttpResponse(Response statusCode, String content) {
        return Response.getHttpResponse(statusCode.code, content);
    }
    public static String getHttpResponse(Response statusCode) {
        return Response.getHttpResponse(statusCode.code, statusCode.content);
    }

    public String getHttpResponse() {
        return getHttpResponse(this);
    }
    public String getHttpResponse(String content) {
        return getHttpResponse(this, content);
    }
}
