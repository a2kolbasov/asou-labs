package sqlite;

import server.Controller.Parameters;
import server.StatusCode;

interface SqlControllerInterface {
    public String create(Parameters p);
    public String update(Parameters p);
    public String list(Parameters p);
    public String get(Parameters p);
    public String delete(Parameters p);

    default boolean notEqualMethod(Parameters p, String method) {
        return ! p.getMethod().equals(method);
    }

    default String badRequest() {
        return StatusCode.BAD_REQUEST.getHttpResponse();
    }
}
