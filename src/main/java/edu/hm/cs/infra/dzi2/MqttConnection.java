package edu.hm.cs.infra.dzi2;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttConnection {

    private static final String BROKER_ADDRESS = "tcp://localhost:1883";
    private static final String TOPIC_HELLO = "topic-hello";
    private static final String CLIENT_ID = "HelloClient";

    private final MqttClient sampleClient;

    public MqttConnection() throws MqttException {
        MemoryPersistence persistence = new MemoryPersistence();
        sampleClient = new MqttClient(BROKER_ADDRESS, CLIENT_ID, persistence);
    }

    public void testConnection() {
        String content = "Message from MqttPublishSample";

        try {
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + BROKER_ADDRESS);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("Publishing message: " + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(2);
            sampleClient.publish(TOPIC_HELLO, message);
            System.out.println("Message published");
            sampleClient.disconnect();
            System.out.println("Disconnected");
            System.exit(0);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
}
