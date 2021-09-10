package edu.hm.cs.infra.dzi2;

import java.sql.*;

public class DbConnection {
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/digital_twin";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    private static final String NE_ID = "THE_NE";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    public void initDb() {
        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement()
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

            sql = "insert ignore into CTFIFC_NMS_NE (NMS_ID,CREATE_DATE) values (?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, NE_ID);
            pstmt.setDate(2, new Date(new java.util.Date().getTime()));
            pstmt.executeUpdate();
            System.out.println("NE created successfully...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void writeMessage(String chassis, String key, String value) {
        try(Connection conn = this.getConnection()) {
            String sql = "insert ignore into CTFIFC_NMS_CHASSIS (NMS_ID,NE_ID,CREATE_DATE) values (?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, chassis);
            pstmt.setString(2, NE_ID);
            pstmt.setDate(3, new Date(new java.util.Date().getTime()));
            pstmt.executeUpdate();

            sql = "update CTFIFC_NMS_ADDITIONAL_DATA set VALUE=? where NMS_ID=? and `KEY`=?;";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, value);
            pstmt.setString(2, chassis);
            pstmt.setString(3, key);
            final int updateCount = pstmt.executeUpdate();
            if (updateCount == 0) {
                sql = "insert into CTFIFC_NMS_ADDITIONAL_DATA(NMS_ID,`KEY`,VALUE) values(?,?,?);";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, chassis);
                pstmt.setString(2, key);
                pstmt.setString(3, value);
                pstmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
