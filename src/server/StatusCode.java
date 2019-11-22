package server;

class StatusCode {
    static final int
    // OK
        OK = 200,
        ACCEPTED = 202,
    // Client errors
        BAD_REQUEST = 400,
        NOT_FOUND = 404,
    // Server errors
        NOT_IMPLEMENTED = 501;
}
