/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ui.main;

import ui.main.fxmlLoader;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Aung Ko Ko Phyo
 */
public class MainController implements Initializable {

    @FXML
    private BorderPane mainBorder;
    @FXML
    private JFXButton btnDashboard;
    @FXML
    private JFXButton btnTeacher;
    @FXML
    private JFXButton btnCourses;
    @FXML
    private JFXButton btnStudents;
    @FXML
    private JFXButton btnRooms;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getFxmlPage("dashboardUi");
//        getFxmlPage("teachersUi");

    }

    @FXML
    private void DashboardFxml(ActionEvent event) {
        getFxmlPage("dashboardUi");
    }

    @FXML
    private void TeacherFxml(ActionEvent event) {
        getFxmlPage("teachersUi");
    }

    @FXML
    private void CoursesFxml(ActionEvent event) {
        getFxmlPage("coursesUi");

    }

    @FXML
    private void StudentsFxml(ActionEvent event) {
        getFxmlPage("studentsUi");

    }

    public void getFxmlPage(String pageName) {
        fxmlLoader object = new fxmlLoader();
        Pane view = object.getPane(pageName);
//        Scene s = btnDashboard.getScene();
        mainBorder.setCenter(view);
    }

    @FXML
    private void RoomsFxml(ActionEvent event) {
        getFxmlPage("roomsUi");
   }
}
