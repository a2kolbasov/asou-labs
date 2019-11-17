public class Main {
    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 3; i++) {
            Thread generatorThread = new Thread(new TaskGenerator(i));
            generatorThread.start();
        }

        Thread processorThread = new Thread(new TaskProcessor());
        processorThread.start();

        processorThread.wait();
    }
}
