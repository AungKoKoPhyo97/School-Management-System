/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ui.courses;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import dataclass.Courses;
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
public class EditCoursesController implements Initializable {

    @FXML
    private JFXTextField txfName;
    @FXML
    private JFXTextField txfFee;
    @FXML
    private JFXButton btnSave;

    int cid;

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
        Courses c = new Courses(cid, txfName.getText(), Double.parseDouble(txfFee.getText()), "active");
        Courses.updateCoursesData(c);
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }

    public void cId(int id) {
        cid = id;
    }

    public void cName(String name) {
        txfName.setText(name);
    }

    public void cFee(double fee) {
        txfFee.setText(fee + "");
    }

}
