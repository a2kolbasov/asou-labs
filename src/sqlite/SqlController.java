package sqlite;

import server.StatusCode;

public class SqlController implements server.Controller, SqlControllerInterface {
    private Sql sqlite = Sql.init("db.db");

    @Override
    public String create(Parameters p) {
        if (notEqualMethod(p, "post"))
            return badRequest();

        Cart cart = Json.fromJson(p.getBody());
        return StatusCode.getHttpResponse(
                sqlite.create(cart) ?
                        StatusCode.ACCEPTED : StatusCode.NOT_ACCEPTABLE
        );
    }

    @Override
    public String update(Parameters p) {
        if (notEqualMethod(p, "put"))
            return badRequest();

        Cart cart = Json.fromJson(p.getBody());
        return StatusCode.getHttpResponse(
                sqlite.update(cart) ?
                        StatusCode.ACCEPTED : StatusCode.NOT_ACCEPTABLE
        );
    }

    @Override
    public String list(Parameters p) {
        Cart[] carts = sqlite.list();
        StringBuilder sb = new StringBuilder();
//        if (carts == null)
//            return StatusCode.NOT_ACCEPTABLE.getHttpResponse();
        for (Cart cart : carts)
            sb.append(Json.toJson(cart)).append('\n');

        return StatusCode.OK.getHttpResponse(
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

            String tmp = p.getQuery().substring(index + param.length()).split(",")[0];
            int id = Integer.parseInt(
                    tmp
            );
            System.out.println("id=" + id);

            Cart cart = sqlite.get(id);
            return StatusCode.getHttpResponse(
                    StatusCode.OK,
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
        return StatusCode.getHttpResponse(
                sqlite.delete(id) ?
                        StatusCode.ACCEPTED : StatusCode.NOT_ACCEPTABLE
        );
    }
}
