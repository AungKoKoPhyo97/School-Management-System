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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Aung Ko Ko Phyo
 */
public class EditStudentsController implements Initializable {

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

    private int sid;
    private int rid, cid;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addCBCourse();
        addCBRooms();
    }

    @FXML
    private void onCancel(ActionEvent event) {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void onSave(ActionEvent event) {
        Students s = new Students(sid, txfName.getText(), txfAddress.getText(), txfFatherName.getText(),
                dpDob.getValue(), dpRegisterDate.getValue(), dpRegisterDate.getValue().plusMonths(3),
                Integer.parseInt(txfPhone.getText().trim()), "active",
                Integer.parseInt(cbCourse.getValue().toString().trim()),
                Integer.parseInt(cbRoom.getValue().toString().trim()));
        Students.updateStudentsData(s);
        AlertController.showInfoAlert("Database Info", "Successfully update the Student");
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }

    public void sId(int id) {
        sid = id;
    }

    public void sName(String name) {
        txfName.setText(name);
    }

    public void sAddress(String address) {
        txfAddress.setText(address);
    }

    public void sFatherName(String fathername) {
        txfFatherName.setText(fathername);
    }

    public void sRegisterDate(LocalDate date) {
        dpRegisterDate.setValue(date);
    }

    public void sPhone(int phone) {
        txfPhone.setText(phone + "");
    }

    public void rId(int id) {
        rid = id;
    }

    public void cId(int id) {
        cid = id;
    }

    public void sDob(LocalDate date) {
        dpDob.setValue(date);
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
            cbRoom.getSelectionModel().select(rid);
            pstm.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addCBCourse() {
        try (Connection con = getConnection()) {
            String sql = "SELECT cid FROM coursestbl where cstatus='active'";
            PreparedStatement pstm = con.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                optionCourse.add(rs.getInt(1));
            }
            cbCourse.setItems(optionCourse);
            cbCourse.getSelectionModel().select(cid);
            pstm.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
