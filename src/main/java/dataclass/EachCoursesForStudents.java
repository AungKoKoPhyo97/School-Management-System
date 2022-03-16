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
public class EachCoursesForStudents {

    private String cname;
    private int sid;

    public EachCoursesForStudents(String cname, int sid) {
        this.cname = cname;
        this.sid = sid;
    }

    public static ObservableList<EachCoursesForStudents> getAllCountStudentsEachCourses() {
        ObservableList<EachCoursesForStudents> list = FXCollections.observableArrayList();
        int count;
        try (Connection con = getConnection()) {
            String sql = "SELECT coursestbl.cname,COUNT(studentstbl.sid) FROM coursestbl LEFT JOIN studentstbl ON"
                    + " coursestbl.cid =studentstbl.cid"
                    + " WHERE coursestbl.cstatus='active' AND studentstbl.sstatus='active' GROUP BY coursestbl.cid";
            PreparedStatement pstm = con.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                count = rs.getInt(2);
                EachCoursesForStudents e = new EachCoursesForStudents(rs.getString(1), count);
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
