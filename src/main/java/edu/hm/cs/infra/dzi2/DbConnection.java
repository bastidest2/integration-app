package edu.hm.cs.infra.dzi2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnection {
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/digital_twin";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    public void initDb() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "create table if not exists CTFIFC_NMS_NE (" +
                    "CREATE_DATE date, " +
                    "MODIFY_DATE date, " +
                    "NMS_ID varchar(200) not null, " +
                    "NMS_TYPE varchar(300), " +
                    "SOURCE_SYSTEM varchar(60), " +
                    "COMMAND_TYPE varchar(200), " +
                    "REMARK varchar(200), " +
                    "NMS_INTERNAL_ID varchar(300), " +
                    "NMS_ZONE_ID varchar(200), " +
                    "COMMAND_ZONE_ID varchar(300), " +
                    "primary key ( NMS_ID )" +
                    ")";
            stmt.executeUpdate(sql);

            sql = "create table if not exists CTFIFC_NMS_CHASSIS (" +
                    "CREATE_DATE date, " +
                    "MODIFY_DATE date, " +
                    "NMS_ID varchar(200) not null, " +
                    "NMS_TYPE varchar(300), " +
                    "SOURCE_SYSTEM varchar(60), " +
                    "COMMAND_TYPE varchar(500), " +
                    "HARDWARE_VERSION varchar(60), " +
                    "IP varchar(15), " +
                    "NE_ID varchar(200), " +
                    "SERIAL_NUMBER varchar(100), " +
                    "SOFTWARE_VERSION varchar(60), " +
                    "NMS_INTERNAL_ID varchar(300), " +
                    "IS_MASTER_DEVICE varchar(1), " +
                    "CABINET_ID varchar(200), " +
                    "CABINET_POSITION varchar(200), " +
                    "NMS_CABINET_POSITION varchar(200), " +
                    "WARE_HOUSE varchar(200), " +
                    "primary key ( NMS_ID ), " +
                    "foreign key ( NE_ID ) references CTFIFC_NMS_NE ( NMS_ID )" +
                    ")";
            stmt.executeUpdate(sql);

            sql = "create table if not exists CTFIFC_NMS_ADDITIONAL_DATA (" +
                    "NMS_ID varchar(200) not null, " +
                    "`KEY` varchar(200) not null, " +
                    "VALUE varchar(200), " +
                    "foreign key ( NMS_ID ) references CTFIFC_NMS_CHASSIS ( NMS_ID )" +
                    ")";
            stmt.executeUpdate(sql);
            System.out.println("DB tables created successfully...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
