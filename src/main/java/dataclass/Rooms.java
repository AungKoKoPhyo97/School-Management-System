/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataclass;

import database.databaseController;
import static database.databaseController.getConnection;
import java.sql.Connection;
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
public class Rooms {

    private int rid;
    private String rname;
    private String rstatus;

    public Rooms(int rid, String rname, String rstatus) {
        this.rid = rid;
        this.rname = rname;
        this.rstatus = rstatus;
    }

    public static boolean addRooms(Rooms r) {
        try (Connection con = databaseController.getConnection()) {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO roomstbl(rname,rstatus) VALUES (?,?)");
            stmt.setString(1, r.getRname());
            stmt.setString(2, "active");
            stmt.executeUpdate();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static ObservableList<Rooms> getAllRooms() {
        ObservableList<Rooms> list = FXCollections.observableArrayList();
        try (Connection con = getConnection()) {
            String sql = "SELECT * FROM roomstbl ORDER BY rstatus";
            PreparedStatement pstm = con.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Rooms r = new Rooms(rs.getInt(1), rs.getString(2), rs.getString(3));
                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list.isEmpty()) {
            System.out.println("list empty");
        }
        return list;
    }

    public static boolean updateRoomsData(Rooms r) {
        String sql = "UPDATE roomstbl SET rname=? where rid=?";
        try (Connection con = databaseController.getConnection()) {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, r.getRname());
            stmt.setInt(2, r.getRid());
            stmt.executeUpdate();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean deleteRooms(int id) {
        try (Connection con = getConnection()) {
            String sql = "UPDATE roomstbl set rstatus=? where rid=?";
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

    public static ObservableList<Rooms> getAllRoomsByStatus(String status) {
        ObservableList<Rooms> list = FXCollections.observableArrayList();
        try (Connection con = getConnection()) {
            String sql = "SELECT * FROM roomstbl WHERE rstatus=?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, status);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Rooms r = new Rooms(rs.getInt(1), rs.getString(2), rs.getString(3));
                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list.isEmpty()) {
            System.out.println("list empty");
        }
        return list;
    }

    public static ObservableList<Rooms> getRoomId() {
        ObservableList<Rooms> list = FXCollections.observableArrayList();
        try (Connection con = getConnection()) {
            String sql = "SELECT rid FROM roomstbl where rstatus='active'";
            PreparedStatement pstm = con.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Rooms r = new Rooms(rs.getInt(1), rs.getString(2), rs.getString(3));
                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list.isEmpty()) {
            System.out.println("list empty");
        }
        return list;
    }

    public static int getAllRoomsCount() {
        int count = 0;
        try (Connection con = getConnection()) {
            String sql = "SELECT COUNT(rid) FROM roomstbl where rstatus='active'";
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
