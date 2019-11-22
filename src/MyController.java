import server.Controller;

public class MyController implements Controller {
    public String test(Parameters p) {
        return "it work!";
    }

    public String Time(Parameters p) {
        return "" + System.currentTimeMillis();
    }

    public String ask(Parameters p){
        return String.format("Query: %s\r\nBody: %s", p.getQuery(), p.getBody());
    }
}
