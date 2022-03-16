/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ui.teachers;

import alert.AlertController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import dataclass.Teachers;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Aung Ko Ko Phyo
 */
public class EditTeachersController implements Initializable {

    @FXML
    private JFXTextField txfName;
    @FXML
    private JFXTextField txfNrc;
    @FXML
    private JFXDatePicker datePicker;
    @FXML
    private JFXTextField txfPhone;
    @FXML
    private JFXButton btnSave;
    int tid;
    String tstatus;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void onCancel(ActionEvent event) {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void onSave(ActionEvent event) {
        Teachers ts = new Teachers(tid, txfName.getText(), txfNrc.getText(), datePicker.getValue(), Integer.parseInt(txfPhone.getText().trim()), "active");
        Teachers.updateTeachersData(ts);
        AlertController.showInfoAlert("Database Info", "Successfully update the Teacher");
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }

    public void tId(int id) {
        tid = id;
    }

    public void tName(String name) {
        txfName.setText(name);
    }

    public void tNrc(String nrc) {
        txfNrc.setText(nrc);
    }

    public void tPhone(int phone) {
        txfPhone.setText(phone + "");
    }

    public void tDob(LocalDate date) {
        datePicker.setValue(date);
    }

    public void tStatus(String status) {
        tstatus = status;
    }
}
