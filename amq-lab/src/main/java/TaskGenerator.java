import javax.jms.*;

public class TaskGenerator implements Runnable {
    private int generatorId;

    public TaskGenerator(int i) {
        generatorId = i;
    }

    public void run() {
        try {
            System.out.println("[TaskGenerator-" + generatorId + "] Start sending");

            for (int i = 0; i < 5; i++) {
                sendMessage(i);
                Thread.sleep(500);
            }

            System.out.println("[TaskGenerator-" + generatorId + "] End");
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(int i) throws JMSException {
        Session session = TaskQueryConnectionManager.createSession();

        MessageProducer producer = TaskQueryConnectionManager.createProducer(session);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        String text = "<msg>id-" + i + "-" + generatorId + "</msg>";
        System.out.println("[TaskGenerator-" + generatorId + "] Send message " + text);

        TextMessage msg = session.createTextMessage();
        msg.setText(text);
        producer.send(msg);

        session.close();
    }
}