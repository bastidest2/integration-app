package edu.hm.cs.infra.dzi2;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MockSenderMain {
    private final Random random = new Random();
    private final MqttConnection connection;

    private MockSenderMain() throws MqttException {
        connection = new MqttConnection("mock-sender");
    }

    private void run() {
        try (connection) {
            connection.connect();

            final List<String> locations = Arrays.asList("warehouse", "office", "server-room");
            final List<String> direction = Arrays.asList("north", "east", "south", "west");
            final List<String> sensorPrefix = Arrays.asList("a", "b", "c");
            final List<String> metric = Arrays.asList("temperature", "battery-percentage");

            for (; ; ) {
                final String topic = String.format(
                        "%s/%s/%s%d/%s",
                        randomElement(locations),
                        randomElement(direction),
                        randomElement(sensorPrefix),
                        random.nextInt(1),
                        randomElement(metric)
                );

                connection.publishMessage(topic, Integer.toString(random.nextInt(100)));
                Thread.sleep(200);
            }
        } catch (IOException | MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String randomElement(List<String> list) {
        return list.get(random.nextInt(list.size()));
    }

    public static void main(String[] args) throws MqttException {
        final MockSenderMain mockSender = new MockSenderMain();
        mockSender.run();
    }
}
