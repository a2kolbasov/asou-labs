public class MyController implements server.Controller {
    public String test(Parameters p) {
        return "{}";
    }

    public String Time(Parameters p) {
        return "" + System.currentTimeMillis();
    }

    public String ask(Parameters p){
        return String.format("Query: %s\r\nBody: %s", p.getQuery(), p.getBody());
    }
}
