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
public class Teachers {

    private int tid;
    private String tname, tnrc;
    private LocalDate tdob;
    private int tphone;
    private String tstatus;

    public Teachers(int tid, String tname, String tnrc, LocalDate tdob, int tphone, String tstatus) {
        this.tid = tid;
        this.tname = tname;
        this.tnrc = tnrc;
        this.tdob = tdob;
        this.tphone = tphone;
        this.tstatus = tstatus;
    }

    public static boolean addTeachersData(Teachers t) {

        try (Connection con = databaseController.getConnection()) {

            PreparedStatement stmt = con.prepareStatement("INSERT INTO teacherstbl(tname,tnrc,tdob,tphone,tstatus) VALUES(?,?,?,?,?)");
            stmt.setString(1, t.getTname());
            stmt.setString(2, t.getTnrc());
            stmt.setDate(3, Date.valueOf(t.getTdob().plusDays(1)));
            stmt.setInt(4, t.getTphone());
            stmt.setString(5, "active");

            stmt.executeUpdate();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static ObservableList<Teachers> getAllTeachers() {
        ObservableList<Teachers> list = FXCollections.observableArrayList();
        try (Connection con = getConnection()) {
            String sql = "SELECT * FROM teacherstbl ORDER BY tstatus";
            PreparedStatement pstm = con.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                LocalDate date = LocalDate.parse(rs.getString(4));
                Teachers t = new Teachers(rs.getInt(1), rs.getString(2), rs.getString(3), date, rs.getInt(5), rs.getString(6));
                list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list.isEmpty()) {
            System.out.println("list empty");
        }
        return list;
    }

    public static ObservableList<Teachers> getAllTeachersByStatus(String status) {
        ObservableList<Teachers> list = FXCollections.observableArrayList();
        try (Connection con = getConnection()) {
            String sql = "SELECT * FROM teacherstbl where tstatus=?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, status);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                LocalDate date = LocalDate.parse(rs.getString(4));
                Teachers t = new Teachers(rs.getInt(1), rs.getString(2), rs.getString(3), date, rs.getInt(5), rs.getString(6));
                list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list.isEmpty()) {
            System.out.println("list empty");
        }
        return list;
    }

    public static int getAllTeachersCountByStatus(String status) {
        int count = 0;
        try (Connection con = getConnection()) {
            String sql = "SELECT COUNT(tid) FROM teacherstbl WHERE tstatus=?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, status);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public static int getAllTeachersCount() {
        int count = 0;
        try (Connection con = getConnection()) {
            String sql = "SELECT COUNT(tid) FROM teacherstbl";
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

    public static boolean updateTeachersData(Teachers t) {
        String sql = "UPDATE teacherstbl SET tname=?,tnrc=?,tdob=?,tphone=? where tid=?";
        try (Connection con = getConnection()) {
            PreparedStatement psmt = con.prepareStatement(sql);
            psmt.setString(1, t.getTname());
            psmt.setString(2, t.getTnrc());
            psmt.setDate(3, java.sql.Date.valueOf(t.getTdob().plusDays(1)));
            psmt.setInt(4, t.getTphone());
            psmt.setInt(5, t.getTid());
            psmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean deleteTeachers(int tId) {
        try (Connection con = getConnection()) {
            String sql = "UPDATE teacherstbl set tStatus=? where tId=?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, "dismissed");
            pstm.setInt(2, tId);
            pstm.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
