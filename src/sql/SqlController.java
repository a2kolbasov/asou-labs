/*
 * Copyright © 2019 Aleksandr Kolbasov
 */

package sql;

import server.Response;

public class SqlController implements server.Controller, SqlControllerInterface {
    private Sql sqlite = Sql.init("db.db");

    @Override
    public String create(Parameters p) {
        if (notEqualMethod(p, "post"))
            return badRequest();

        Cart cart = Json.fromJson(p.getBody());
        return Response.getHttpResponse(
                sqlite.create(cart) ?
                        Response.ACCEPTED : Response.NOT_ACCEPTABLE
        );
    }

    @Override
    public String update(Parameters p) {
        if (notEqualMethod(p, "put"))
            return badRequest();

        Cart cart = Json.fromJson(p.getBody());
        return Response.getHttpResponse(
                sqlite.update(cart) ?
                        Response.ACCEPTED : Response.NOT_ACCEPTABLE
        );
    }

    @Override
    public String list(Parameters p) {
        Cart[] carts = sqlite.list();
        StringBuilder sb = new StringBuilder();
        for (Cart cart : carts)
            sb.append(Json.toJson(cart)).append('\n');

        return Response.OK.getHttpResponse(
                sb.length() > 0 ? sb.toString() : "{}"
        );
    }

    @Override
    public String get(Parameters p) {
        try {
            String param = "id=";
            int index = p.getQuery().indexOf(param);

            if (notEqualMethod(p, "get") || index == -1)
                return badRequest();

            int id = Integer.parseInt(
                    p.getQuery().substring(index + param.length()).split(",")[0]
            );

            Cart cart = sqlite.get(id);
            return Response.getHttpResponse(
                    Response.OK,
                    cart != null ?
                            Json.toJson(cart) : "{}"
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

    @Override
    public String delete(Parameters p) {
        if (notEqualMethod(p, "delete"))
            return badRequest();
        int id = Json.fromJson(p.getBody()).getId();
        return Response.getHttpResponse(
                sqlite.delete(id) ?
                        Response.ACCEPTED : Response.NOT_ACCEPTABLE
        );
    }
}
