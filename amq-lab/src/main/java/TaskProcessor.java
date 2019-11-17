import javax.jms.*;

public class TaskProcessor implements Runnable {
    public void run() {
        try {
            Session session = TaskQueryConnectionManager.createSession();
            MessageConsumer consumer = TaskQueryConnectionManager.createConsumer(session);

            while (true)
            {
                TextMessage receivedMessage = (TextMessage) consumer.receive();
                if (receivedMessage != null) {
                    processMessage(receivedMessage);
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void processMessage(TextMessage msg) throws JMSException, InterruptedException {
        System.out.println("[TaskProcessor] Receive message: " + msg.getText());

        Thread.sleep(1000);

        System.out.println("[TaskProcessor] Process message " + msg.getText() + " done.");
    }
}