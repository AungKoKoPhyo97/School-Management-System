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
public class Students {

    private int sid;
    private String sname, saddress, sfathername;
    private LocalDate sdob, sregisterdate, sexitdate;
    private int sphone;
    private String sstatus;
    private int rid, cid;

    public Students(int sid, String sname, String saddress, String sfathername, LocalDate sdob, LocalDate sregisterdate, LocalDate sexitdate, int sphone, String sstatus, int rid, int cid) {
        this.sid = sid;
        this.sname = sname;
        this.saddress = saddress;
        this.sfathername = sfathername;
        this.sdob = sdob;
        this.sregisterdate = sregisterdate;
        this.sexitdate = sexitdate;
        this.sphone = sphone;
        this.sstatus = sstatus;
        this.rid = rid;
        this.cid = cid;
    }

    
    
    public static boolean addStudents(Students s) {
        try (Connection con = databaseController.getConnection()) {

            PreparedStatement stmt = con.prepareStatement("INSERT INTO studentstbl(sname,saddress,sfathername,sdob,sregisterdate,sexitdate,sphone,sstatus,rid,cid) VALUES(?,?,?,?,?,?,?,?,?,?)");
            stmt.setString(1, s.getSname());
            stmt.setString(2, s.getSaddress());
            stmt.setString(3, s.getSfathername());
            stmt.setDate(4, Date.valueOf(s.getSdob().plusDays(1)));
            stmt.setDate(5, Date.valueOf(s.getSregisterdate().plusDays(1)));
            stmt.setDate(6, Date.valueOf(s.getSexitdate().plusDays(1)));
            stmt.setInt(7, s.getSphone());
            stmt.setString(8, "active");
            stmt.setInt(9, s.getRid());
            stmt.setInt(10, s.getCid());

            stmt.executeUpdate();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static ObservableList<Students> getAllStudents() {
        ObservableList<Students> list = FXCollections.observableArrayList();
        try (Connection con = getConnection()) {
            String sql = "SELECT * FROM studentstbl ORDER BY sstatus";
            PreparedStatement pstm = con.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                LocalDate date = LocalDate.parse(rs.getString(5));
                LocalDate date1 = LocalDate.parse(rs.getString(6));
                LocalDate date2 = LocalDate.parse(rs.getString(7));

                Students s = new Students(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), date, date1, date2, rs.getInt(8),
                        rs.getString(9), rs.getInt(10), rs.getInt(11));
                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list.isEmpty()) {
            System.out.println("list empty");
        }
        return list;
    }

    public static boolean updateStudentsData(Students s) {
        String sql = "UPDATE studentstbl SET sname=?,saddress=?,sfathername=?,sdob=?,sregisterdate=?,sexitdate=?,sphone=?,rid=?,cid=? where sid=?";
        try (Connection con = databaseController.getConnection()) {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, s.getSname());
            stmt.setString(2, s.getSaddress());
            stmt.setString(3, s.getSfathername());
            stmt.setDate(4, Date.valueOf(s.getSdob().plusDays(1)));
            stmt.setDate(5, Date.valueOf(s.getSregisterdate().plusDays(1)));
            stmt.setDate(6, Date.valueOf(s.getSexitdate().plusDays(1)));
            stmt.setInt(7, s.getSphone());
            stmt.setInt(8, s.getRid());
            stmt.setInt(9, s.getCid());
            stmt.setInt(10, s.getSid());

            stmt.executeUpdate();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean granduatedStudents(int id) {
        try (Connection con = getConnection()) {
            String sql = "UPDATE studentstbl set sStatus=? where sId=?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, "graduated");
            pstm.setInt(2, id);
            pstm.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteStudents(int id) {
        try (Connection con = getConnection()) {
            String sql = "UPDATE studentstbl set sexitdate=?,sstatus=? where sid=?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setDate(1, Date.valueOf(LocalDate.now()));
            pstm.setString(2, "dismissed");
            pstm.setInt(3, id);
            pstm.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ObservableList<Students> getAllStudentsByStatus(String status) {
        ObservableList<Students> list = FXCollections.observableArrayList();
        try (Connection con = getConnection()) {
            String sql = "SELECT * FROM studentstbl WHERE sstatus=?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, status);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                LocalDate date = LocalDate.parse(rs.getString(5));
                LocalDate date1 = LocalDate.parse(rs.getString(6));
                LocalDate date2 = LocalDate.parse(rs.getString(7));

                Students s = new Students(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), date, date1, date2, rs.getInt(8),
                        rs.getString(9), rs.getInt(10), rs.getInt(11));
                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list.isEmpty()) {
            System.out.println("list empty");
        }
        return list;
    }
    
    public static int getAllStudentsCountByStatus(String status) {
        int count = 0;
        try (Connection con = getConnection()) {
            String sql = "SELECT COUNT(sid) FROM studentstbl WHERE sstatus=?";
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

    
    
}
