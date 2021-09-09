package edu.hm.cs.infra.dzi2;

import org.eclipse.paho.client.mqttv3.MqttException;

public class IntegratorMain {

    public static void main(String[] args) {
        System.out.println("Init");
        DbConnection dbConnection = new DbConnection();
        dbConnection.initDb();

        final GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.readConfig();

        System.out.println("MQTT Test");
        try {
            final MqttConnection connection = new MqttConnection("integrator");
            connection.connect(new MqttStoreCallback(dbConnection));

            for(final String topic : globalConfig.getTopics()) {
                connection.subscribe(topic);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
