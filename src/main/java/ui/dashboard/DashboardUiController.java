/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ui.dashboard;

import com.jfoenix.controls.JFXComboBox;
import dataclass.Courses;
import dataclass.EachCoursesForStudents;
import dataclass.EachRoomsForStudents;
import dataclass.Rooms;
import dataclass.Students;
import dataclass.Teachers;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Aung Ko Ko Phyo
 */
public class DashboardUiController implements Initializable {

    private Label lId;
    private Label lName;
    private Label lDob;
    private Label lStatus;
    @FXML
    private Label lblTotalActive;
    @FXML
    private Label lblCourses;
    @FXML
    private Label lblRoom;
    @FXML
    private Label lblYesterday;
    @FXML
    private Label lblToday;
    @FXML
    private Label lblTomorrow;
    @FXML
    private BarChart<String, Integer> chartStudents;
    @FXML
    private Label lbldaybefore;
    @FXML
    private Label lbldaynow;
    @FXML
    private Label lbldayafter;

    @FXML
    private JFXComboBox<String> cbTime;
    @FXML
    private NumberAxis AxisY;
    @FXML
    private CategoryAxis AxisX;
    @FXML
    private PieChart chartPieStudent;
    private ObservableList<PieChart.Data> list;
    @FXML
    private TableColumn<EachRoomsForStudents, Integer> cRoomId;
    @FXML
    private TableColumn<EachRoomsForStudents, Integer> cRStudents;
    @FXML
    private TableView<EachCoursesForStudents> tblCourses;
    @FXML
    private TableColumn<EachCoursesForStudents, String> cCourseName;
    @FXML
    private TableColumn<EachCoursesForStudents, Integer> cCStudents;
    @FXML
    private TableView<EachRoomsForStudents> tblRooms;
    @FXML
    private TableColumn<EachRoomsForStudents, String> cRoomName;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbTime.getSelectionModel().select("Month");
        setDate();
        setComboBox();
        loadBarChart(YearMonth.now().atDay(1), YearMonth.now().atEndOfMonth());
        loadPieChart();
        chartPieStudent.setData(list);
        setText();
        loadRoomsTbl();
        loadCoursesTbl();
    }

    public void setDate() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/mm/yyyy");

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate yesterday = LocalDate.now().minusDays(1);

        String now = today.getDayOfMonth() + "".trim();
        String after = tomorrow.getDayOfMonth() + "".trim();
        String before = yesterday.getDayOfMonth() + "".trim();

        String dayNow = today.getDayOfWeek() + "".trim();
        String dayAfter = tomorrow.getDayOfWeek() + "".trim();
        String dayBefore = yesterday.getDayOfWeek() + "".trim();

        lblToday.setText(now);
        lblTomorrow.setText(after);
        lblYesterday.setText(before);

        lbldaynow.setText(dayNow);
        lbldayafter.setText(dayAfter);
        lbldaybefore.setText(dayBefore);

    }

    public void setComboBox() {
        cbTime.getItems().addAll("Month", "Year");
    }

    @FXML
    private void chooseDate(ActionEvent event) {
        if (cbTime.getValue() == "Month") {
            LocalDate date1 = YearMonth.now().atDay(1);
            LocalDate date2 = YearMonth.now().atEndOfMonth();
            chartStudents.getData().clear();
            loadBarChart(date1, date2);
        } else if (cbTime.getValue() == "Year") {
            LocalDate date1 = YearMonth.now().atDay(1);
            LocalDate date2 = YearMonth.now().atEndOfMonth();
            chartStudents.getData().clear();
            loadBarChart(date1, date2);
        }

    }

    public void loadBarChart(LocalDate date1, LocalDate date2) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/YY");
        int count = 0;
        String sql = "SELECT COUNT(DISTINCT sid),sregisterdate FROM studentstbl WHERE sstatus='active' and sregisterdate BETWEEN ? AND ? GROUP BY sregisterdate";
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        try (Connection con = database.databaseController.getConnection(); PreparedStatement pstm = con.prepareStatement(sql);) {
            pstm.setDate(1, Date.valueOf(date1));
            pstm.setDate(2, Date.valueOf(date2));
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
                LocalDate date = LocalDate.parse(rs.getString(2));
                series.getData().add(new XYChart.Data<>(dtf.format(date), count));

            }

            chartStudents.getData().addAll(series);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        chartStudents.lookupAll(".default-color0.chart-bar").forEach(n -> n.setStyle("-fx-bar-fill:green;"));
        AxisX.setAnimated(false);
        AxisY.setAnimated(true);
        chartStudents.setAnimated(true);
        chartStudents.setLegendVisible(false);
    }

    public void loadPieChart() {
        list = FXCollections.observableArrayList();
        String sql = "SELECT coursestbl.cname,COUNT(studentstbl.sid) FROM coursestbl LEFT JOIN studentstbl ON coursestbl.cid =studentstbl.cid WHERE coursestbl.cstatus='active' AND studentstbl.sstatus='active' GROUP BY coursestbl.cid;";
        try (Connection con = database.databaseController.getConnection(); PreparedStatement pstm = con.prepareStatement(sql);) {
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                list.addAll(new PieChart.Data(rs.getString(1), rs.getInt(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void loadRoomsTbl() {
        cRoomId.setCellValueFactory(new PropertyValueFactory("rid"));
        cRoomName.setCellValueFactory(new PropertyValueFactory("rname"));
        cRStudents.setCellValueFactory(new PropertyValueFactory("sid"));
        getRooms();
    }

    public void getRooms() {
        ObservableList<EachRoomsForStudents> list = EachRoomsForStudents.getAllCountStudentsEachRoom();
        System.out.println(list);
        tblRooms.getItems().clear();
        tblRooms.setItems(list);
    }

    public void loadCoursesTbl() {
        cCourseName.setCellValueFactory(new PropertyValueFactory("cname"));
        cCStudents.setCellValueFactory(new PropertyValueFactory("sid"));
        getCourses();
    }

    public void getCourses() {
        ObservableList<EachCoursesForStudents> list = EachCoursesForStudents.getAllCountStudentsEachCourses();
        tblCourses.getItems().clear();
        tblCourses.setItems(list);
    }

    private void setText() {
        lblTotalActive.setText(Students.getAllStudentsCountByStatus("active") + "");
        lblRoom.setText(Rooms.getAllRoomsCount() + "");
        lblCourses.setText(Courses.getAllCoursesCount() + "");
    }

}
