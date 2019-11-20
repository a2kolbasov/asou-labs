package server;

import java.util.Date;

public class TestController implements Controller {
    public String foo(String param) {
        return "it work!";
    }

    public long getTime(String param) {
        return new Date().getTime();
    }

    public String put(String param){
        if (param == null)
            return "error";
        else
            return param;
    }
}
