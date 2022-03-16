/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataclass;

import database.databaseController;
import static database.databaseController.getConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;

/**
 *
 * @author Aung Ko Ko Phyo
 */
@Data
public class Courses {

    private int cid;
    private String cname;
    private double cfee;
    private String cstatus;

    public Courses(int cid, String cname, double cfee, String cstatus) {
        this.cid = cid;
        this.cname = cname;
        this.cfee = cfee;
        this.cstatus = cstatus;
    }

    public static boolean addCourses(Courses c) {
        try (Connection con = databaseController.getConnection()) {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO coursestbl(cname,cfee,cstatus) VALUES (?,?,?)");
            stmt.setString(1, c.getCname());
            stmt.setDouble(2, c.getCfee());
            stmt.setString(3, "active");
            stmt.executeUpdate();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static ObservableList<Courses> getAllCourses() {
        ObservableList<Courses> list = FXCollections.observableArrayList();
        try (Connection con = getConnection()) {
            String sql = "SELECT * FROM coursestbl ORDER BY cstatus";
            PreparedStatement pstm = con.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Courses c = new Courses(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4));
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list.isEmpty()) {
            System.out.println("list empty");
        }
        return list;
    }

    public static boolean updateCoursesData(Courses c) {
        String sql = "UPDATE coursestbl SET cname=?,cfee=? where cid=?";
        try (Connection con = databaseController.getConnection()) {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, c.getCname());
            stmt.setDouble(2, c.getCfee());
            stmt.setInt(3, c.getCid());
            stmt.executeUpdate();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean deleteCourses(int id) {
        try (Connection con = getConnection()) {
            String sql = "UPDATE coursestbl set cstatus=? where cid=?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, "dismissed");
            pstm.setInt(2, id);
            pstm.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ObservableList<Courses> getAllCoursesByStatus(String status) {
        ObservableList<Courses> list = FXCollections.observableArrayList();
        try (Connection con = getConnection()) {
            String sql = "SELECT * FROM coursestbl WHERE cstatus=?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, status);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Courses c = new Courses(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4));
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list.isEmpty()) {
            System.out.println("list empty");
        }
        return list;
    }

    public static int getAllCoursesCount() {
        int count = 0;
        try (Connection con = getConnection()) {
            String sql = "SELECT COUNT(cid) FROM coursestbl where cstatus='active'";
            PreparedStatement pstm = con.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
}
