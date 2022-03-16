/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ui.rooms;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import dataclass.Rooms;

/**
 * FXML Controller class
 *
 * @author Aung Ko Ko Phyo
 */
public class EditRoomsController implements Initializable {
    
    @FXML
    private JFXTextField txfName;
    @FXML
    private JFXButton btnSave;
    private int rid;

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
        Rooms r = new Rooms(rid, txfName.getText(), "active");
        Rooms.updateRoomsData(r);
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
        
    }
    
    public void rId(int id) {
        rid = id;
    }
    
    public void rName(String name) {
        txfName.setText(name);
    }
    
}
