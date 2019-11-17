import java.util.Date;

public class TestController implements IRequestController {
    public String foo() {
        return "it work!";
    }

    public long getTime() {
        return new Date().getTime();
    }
}
