package server;

public class TestController implements Controller {
    public String foo(String ask, String body) {
        return "it work!";
    }

    public String getTime(String ask, String body) {
        return "" + System.currentTimeMillis();
    }

    public String put(String ask, String body){
        System.out.println("ask:" + ask);
        if (body.equals(""))
            return "error";
        else
            return body;
    }
}
