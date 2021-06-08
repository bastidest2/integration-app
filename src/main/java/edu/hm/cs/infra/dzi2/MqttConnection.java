package edu.hm.cs.infra.dzi2;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.Closeable;
import java.io.IOException;

import static edu.hm.cs.infra.dzi2.GlobalConfig.BROKER_ADDRESS;

public class MqttConnection implements Closeable {

    private static final String TOPIC_HELLO = "topic-hello";

    private final MqttClient client;
    private final String clientId;

    public MqttConnection(String clientId) throws MqttException {
        this.clientId = clientId;
        final MemoryPersistence persistence = new MemoryPersistence();
        client = new MqttClient(BROKER_ADDRESS, clientId, persistence);
    }

    public void connect() throws MqttException {
        final MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        System.out.println(clientId + ": Connecting to broker: " + BROKER_ADDRESS);
        client.connect(connOpts);
    }

    @Override
    public void close() throws IOException {
        try {
            client.disconnect();
        } catch (MqttException e) {
            throw new IOException(e);
        }
    }

    public void publishMessage(String topic, String messageString) throws MqttException {
        final MqttMessage message = new MqttMessage(messageString.getBytes());
        message.setQos(0);
        client.publish(topic, message);
        System.out.format("[%s] publishing: %s=%s\n", clientId, topic, messageString);
    }
}
