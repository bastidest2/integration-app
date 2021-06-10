package edu.hm.cs.infra.dzi2;

import org.eclipse.paho.client.mqttv3.MqttException;

public class IntegratorMain {

    public static void main(String[] args) {
        System.out.println("Init");
        new DbConnection().initDb();

        System.out.println("MQTT Test");
        try {
            final MqttConnection connection = new MqttConnection("integrator");
            connection.connect();
            connection.subscribe("office/#");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
