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
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Aung Ko Ko Phyo
 */
public class AddTeachersController implements Initializable {

    @FXML
    private JFXTextField txfName;
    @FXML
    private JFXTextField txfNrc;
    @FXML
    private JFXTextField txfPhone;
    @FXML
    private JFXDatePicker datePicker;
    @FXML
    private JFXButton btnSave;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void onSave(ActionEvent event) {
        Teachers t = new Teachers(1, txfName.getText(), txfNrc.getText(), datePicker.getValue(), Integer.parseInt(txfPhone.getText()), "active");
        Teachers.addTeachersData(t);
        AlertController.showInfoAlert("Database Info!", "New teacher added");
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void onCancel(ActionEvent event) {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }

}
