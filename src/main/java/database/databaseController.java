/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Aung Ko Ko Phyo
 */
public class databaseController {

    static String dbserverip = "127.0.0.1";

    public static Connection getConnection() {
        Connection con = null;
        if (con == null) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + dbserverip.trim() + ":3306/schooldb?serverTimezone=UTC&useSSL=false", "akkp", "1234");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return con;
    }

    public static boolean checkDbExist() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://" + dbserverip.trim() + ":3306/?serverTimezone=UTC&useSSL=false", "akkp", "1234");) {
            ResultSet rs = con.getMetaData().getCatalogs();
            while (rs.next()) {
                if (rs.getString(1).equals("schooldb")) {
                    return true;
                }
            }

            String sql = "CREATE DATABASE schooldb";
            PreparedStatement psmt = con.prepareStatement(sql);
            psmt.executeUpdate();
            System.out.println("Databse Created");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean createStudentsTbl() {
        String sql = "CREATE TABLE studentstbl"
                + "("
                + "sid int not null auto_increment,"
                + "sname varchar(255),"
                + "saddress varchar(255),"
                + "sfathername varchar(255),"
                + "sdob date,"
                + "sregisterdate date,"
                + "sexitdate date,"
                + "sphone int,"
                + "sstatus varchar(255),"
                + "rid int not null,"
                + "cid int not null,"
                + "PRIMARY KEY(sid),"
                + "FOREIGN KEY(rid) REFERENCES roomstbl(rid),"
                + "FOREIGN KEY(cid) REFERENCES coursestbl(cid)"
                + ")";
        try (Connection con = getConnection(); PreparedStatement pstm = con.prepareStatement(sql);) {
            pstm.executeUpdate();
//            System.out.println("Create Student Table");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean createRoomsTbl() {
        String sql = "CREATE TABLE roomstbl"
                + "("
                + "rid int not null auto_increment,"
                + "rname varchar(255),"
                + "rstatus varchar(255),"
                + "PRIMARY KEY(rId)"
                + ")";
        try (Connection con = getConnection(); PreparedStatement pstm = con.prepareStatement(sql);) {
            pstm.executeUpdate();
//            System.out.println("Create Room Table");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean createTeachersTbl() {
        String sql = "CREATE TABLE teacherstbl"
                + "("
                + "tid int not null auto_increment,"
                + "tname varchar(255),"
                + "tnrc varchar(255),"
                + "tdob date,"
                + "tphone int,"
                + "tstatus varchar(255),"
                + "PRIMARY KEY(tId)"
                + ")";
        try (Connection con = getConnection(); PreparedStatement pstm = con.prepareStatement(sql);) {
            pstm.executeUpdate();
//            System.out.println("Create Teacher Table");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean createCoursesTbl() {
        String sql = "CREATE TABLE coursestbl"
                + "("
                + "cid int not null auto_increment,"
                + "cname varchar(255),"
                + "cfee double,"
                + "cstatus varchar(255),"
                + "PRIMARY KEY(cid)"
                + ")";
        try (Connection con = getConnection(); PreparedStatement pstm = con.prepareStatement(sql);) {
            pstm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
