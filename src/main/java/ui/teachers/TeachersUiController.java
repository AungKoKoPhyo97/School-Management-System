/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ui.teachers;

import alert.AlertController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import dataclass.Teachers;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Aung Ko Ko Phyo
 */
public class TeachersUiController implements Initializable {

    @FXML
    private JFXTextField txfSearch;
    @FXML
    private JFXRadioButton rAll = new JFXRadioButton("all");
    @FXML
    private JFXRadioButton rActive = new JFXRadioButton("active");
    @FXML
    private JFXRadioButton rDismissed = new JFXRadioButton("dismissed");
    @FXML
    private Label lblTotal;
    @FXML
    private Label lblActive;
    @FXML
    private Label lblInactive;
    private TableView<Teachers> view;

    @FXML
    private JFXButton btnCancel;
    @FXML
    private JFXButton btnEdit;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private VBox vTable;
    @FXML
    private JFXButton btnRefresh;
    @FXML
    private HBox hTbl1;
    @FXML
    private JFXButton btnDismiss;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setText();
        loadTbl();
        SearchActionEvent();
        unselectRow();
        searchActionByStatus();
    }

    @FXML
    private void btnEditAction(ActionEvent event) {
        loadEditFxml();
    }

    @FXML
    private void btnCancelAction(ActionEvent event) {
        txfSearch.setText("");
        unselectRow();
        setText();
        rAll.setSelected(true);
    }

    @FXML
    private void btnAddAction(ActionEvent event) {
        loadAddFxml();
    }

    public void setText() {
        lblActive.setText(Teachers.getAllTeachersCountByStatus("active") + "");
        lblTotal.setText(Teachers.getAllTeachersCount() + "");
        lblInactive.setText(Teachers.getAllTeachersCountByStatus("dismissed") + "");
    }

    public void loadTbl() {
        view = new TableView<>();
        TableColumn<Teachers, Integer> ctid;
        TableColumn<Teachers, String> ctname;
        TableColumn<Teachers, String> ctnrc;
        TableColumn<Teachers, LocalDate> ctdob;
        TableColumn<Teachers, Integer> ctphone;
        TableColumn<Teachers, String> ctstatus;

        ctid = new TableColumn<>("ID");
//        ctid.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

        ctid.setCellValueFactory(new PropertyValueFactory("tid"));
        ctname = new TableColumn<>("Name");
        ctname.setCellValueFactory(new PropertyValueFactory("tname"));
        ctnrc = new TableColumn<>("NRC");
        ctnrc.setCellValueFactory(new PropertyValueFactory("tnrc"));
        ctdob = new TableColumn<>("Phone");
        ctdob.setCellValueFactory(new PropertyValueFactory("tdob"));
        ctphone = new TableColumn<>("Date of Birth");
        ctphone.setCellValueFactory(new PropertyValueFactory("tphone"));
        ctstatus = new TableColumn<>("Status");
        ctstatus.setCellValueFactory(new PropertyValueFactory("tstatus"));

        view.getColumns().addAll(ctid, ctname, ctnrc, ctdob, ctphone, ctstatus);
        vTable.getChildren().clear();
        vTable.getChildren().add(view);
        getTeachers();
    }

    public void getTeachers() {
        ObservableList<Teachers> list = Teachers.getAllTeachers();
        view.getItems().clear();
        view.setItems(list);
    }

    @FXML
    private void btnDismiss(ActionEvent event) {
        Teachers t = view.getSelectionModel().getSelectedItem();

        if (t == null) {
            AlertController.showWarningAlert("Warning", "Please Selcect a Record First!");

        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to change dismissed?", ButtonType.OK, ButtonType.CANCEL);
//            AlertController.showConfirmAlert("Confirmation", "Do you want to change dismissed?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                Teachers.deleteTeachers(t.getTid());
                AlertController.showInfoAlert("Database Info", "Successfully dismissed");
                getTeachers();
                setText();
            } else {
                alert.close();
            }
        }
    }

    @FXML
    private void btnRefresh(ActionEvent event) {
        rAll.setSelected(true);
        getTeachers();
        unselectRow();
        setText();

    }

    public void unselectRow() {
        view.getSelectionModel().clearSelection();
    }

    public void loadAddFxml() {
        FXMLLoader secView = new FXMLLoader(getClass().getResource("/ui/teachers/AddTeachers.fxml"));
        try {
            Parent root = (Parent) secView.load();

            Stage st = new Stage();
            Scene sc = new Scene(root);
            st.setScene(sc);
            st.show();

        } catch (IOException ex) {
            System.out.println("error");
        }
    }

    public void loadEditFxml() {
        Teachers t = view.getSelectionModel().getSelectedItem();

        if (t == null) {
            AlertController.showWarningAlert("Warning", "Please Selcect a Record First!");
        } else {

            if (t.getTstatus().equals("active")) {
                FXMLLoader secView = new FXMLLoader(getClass().getResource("/ui/teachers/EditTeachers.fxml"));
                try {
                    Parent root = (Parent) secView.load();
                    EditTeachersController etc = secView.getController();
                    etc.tName(t.getTname());
                    etc.tNrc(t.getTnrc());
                    etc.tDob(t.getTdob());
                    etc.tPhone(t.getTphone());
                    etc.tId(t.getTid());
                    etc.tStatus(t.getTstatus());
                    Stage st = new Stage();
                    Scene sc = new Scene(root);
                    st.setScene(sc);
                    st.show();
                } catch (IOException ex) {
                    System.out.println("error");
                }
            } else {
                AlertController.showErrorAlert("Error", "Status must be active");
            }
        }
    }

    public void SearchActionEvent() {
        ObservableList<Teachers> list = Teachers.getAllTeachers();
        view.getItems().setAll(list);

        FilteredList<Teachers> filter = new FilteredList<>(list, e -> true);
        txfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate((teacher) -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String s = txfSearch.getText().toLowerCase();
                rAll.setSelected(true);
                if (teacher.getTname().contains(s)) {
                    return true;
                }
                return false;

            });
            SortedList<Teachers> sl = new SortedList<>(filter);
            sl.comparatorProperty().bind(view.comparatorProperty());
            view.getItems().clear();
            view.getItems().addAll(sl);
        });
    }

    public void searchActionByStatus() {
//        System.out.println(Teachers.getAllTeachersByStatus("dismissed"));
        ToggleGroup statusGp = new ToggleGroup();
        rActive.setToggleGroup(statusGp);
        rAll.setToggleGroup(statusGp);
        rDismissed.setToggleGroup(statusGp);
        rAll.setSelected(true);

        statusGp.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (rAll.isSelected()) {
                    getTeachers();
                } else if (rActive.isSelected()) {
                    searchByStatus("active");
                } else if (rDismissed.isSelected()) {
                    searchByStatus("dismissed");
                }
            }
        });
    }

    public void searchByStatus(String status) {
        ObservableList<Teachers> list = Teachers.getAllTeachersByStatus(status);
        view.getItems().clear();
        view.setItems(list);
    }

}
