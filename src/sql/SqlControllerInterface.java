/*
 * Copyright © 2019 Alexander Kolbasov
 */

package sql;

import server.Controller.Parameters;
import server.Response;

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
        return Response.BAD_REQUEST.getHttpResponse();
    }
}
