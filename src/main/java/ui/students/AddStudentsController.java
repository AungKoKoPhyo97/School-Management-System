/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ui.students;

import alert.AlertController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import static database.databaseController.getConnection;
import dataclass.Rooms;
import dataclass.Students;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Aung Ko Ko Phyo
 */
public class AddStudentsController implements Initializable {

    @FXML
    private JFXTextField txfName;
    @FXML
    private JFXTextField txfAddress;
    @FXML
    private JFXTextField txfFatherName;
    @FXML
    private JFXDatePicker dpDob;
    @FXML
    private JFXDatePicker dpRegisterDate;
    @FXML
    private JFXTextField txfPhone;
    ObservableList optionRoom = FXCollections.observableArrayList();
    ObservableList optionCourse = FXCollections.observableArrayList();
    @FXML
    private JFXComboBox cbRoom = new JFXComboBox(optionRoom);
    @FXML
    private JFXComboBox cbCourse = new JFXComboBox(optionCourse);
    @FXML
    private JFXButton btnSave;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addCBRooms();
        addCBCourse();
        dpRegisterDate.setValue(LocalDate.now());
    }

    @FXML
    private void onCancel(ActionEvent event) {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void onSave(ActionEvent event) {
        Students s = new Students(1, txfName.getText(), txfAddress.getText(), txfFatherName.getText(), dpDob.getValue(), dpRegisterDate.getValue(), dpRegisterDate.getValue().plusMonths(3),
                Integer.parseInt(txfPhone.getText()), "active", Integer.parseInt(cbCourse.getValue().toString().trim()), Integer.parseInt(cbRoom.getValue().toString().trim()));
        Students.addStudents(s);
        AlertController.showInfoAlert("Database Info!", "New teacher added");
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }

    public void addCBRooms() {
        try (Connection con = getConnection()) {
            String sql = "SELECT rid FROM roomstbl where rstatus='active'";
            PreparedStatement pstm = con.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                optionRoom.add(rs.getInt(1));
            }
            cbRoom.setItems(optionRoom);
            pstm.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addCBCourse() {
        try (Connection con = getConnection()) {
            String sql = "SELECT cid FROM coursestbl WHERE cstatus='active'";
            PreparedStatement pstm = con.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                optionCourse.add(rs.getInt(1));
            }
            cbCourse.setItems(optionCourse);
            pstm.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
