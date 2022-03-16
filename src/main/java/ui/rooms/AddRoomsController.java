/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ui.rooms;

import alert.AlertController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import dataclass.Rooms;
import java.net.URL;
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
public class AddRoomsController implements Initializable {

    @FXML
    private JFXTextField txfName;
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
    private void onCancel(ActionEvent event) {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void onSave(ActionEvent event) {
        Rooms r = new Rooms(1, txfName.getText(), "active");
        Rooms.addRooms(r);
        AlertController.showInfoAlert("Database Info!", "New Room added");
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();

    }

}
