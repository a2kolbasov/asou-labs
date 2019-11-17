import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class TaskQueryConnectionManager {
    public static final String QUERY_ADDRESS = "vm://query1";
    public static final String QUERY_NAME = "requests";

    public static Session createSession() throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(QUERY_ADDRESS);

        Connection connection = connectionFactory.createConnection();
        connection.start();

        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    public static MessageProducer createProducer(Session session) throws JMSException {
        Destination requestQuery = session.createQueue(QUERY_NAME);

        return session.createProducer(requestQuery);
    }

    public static MessageConsumer createConsumer(Session session) throws JMSException {
        Destination requestQuery = session.createQueue(QUERY_NAME);

        return session.createConsumer(requestQuery);
    }
}
