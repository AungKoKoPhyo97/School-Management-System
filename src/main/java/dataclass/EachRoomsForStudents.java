/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataclass;

import static database.databaseController.getConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;

/**
 *
 * @author Aung Ko Ko Phyo
 */
@Data
public class EachRoomsForStudents {

    private int rid;
    private String rname;
    private int sid;

    public EachRoomsForStudents(int rid, String rname, int sid) {
        this.rid = rid;
        this.rname = rname;
        this.sid = sid;
    }

    public static ObservableList<EachRoomsForStudents> getAllCountStudentsEachRoom() {
        ObservableList<EachRoomsForStudents> list = FXCollections.observableArrayList();
        int count;
        try (Connection con = getConnection()) {
            String sql = "SELECT roomstbl.rid,roomstbl.rname,COUNT(studentstbl.sid) FROM roomstbl "
                    + "LEFT JOIN studentstbl ON roomstbl.rid =studentstbl.rid WHERE"
                    + " studentstbl.sstatus='active' AND roomstbl.rstatus='active' GROUP BY roomstbl.rid";
            PreparedStatement pstm = con.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                count = rs.getInt(3);
                EachRoomsForStudents e = new EachRoomsForStudents(rs.getInt(1), rs.getString(2), count);
                list.add(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list.isEmpty()) {
            System.out.println("list empty");
        }
        return list;
    }

}
