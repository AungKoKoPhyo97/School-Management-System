/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ui.courses;

import alert.AlertController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import dataclass.Courses;
import dataclass.Rooms;
import dataclass.Students;
import dataclass.Teachers;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ui.teachers.EditTeachersController;

/**
 * FXML Controller class
 *
 * @author Aung Ko Ko Phyo
 */
public class CoursesUiController implements Initializable {

    @FXML
    private JFXTextField txfSearch;
    @FXML
    private VBox vTable;
    @FXML
    private TableView<Courses> CoursesTable;
    @FXML
    private TableColumn<Courses, Integer> cId;
    @FXML
    private TableColumn<Courses, String> cName;
    @FXML
    private TableColumn<Courses, Double> cFee;
    @FXML
    private TableColumn<Courses, String> cStatus;
    @FXML
    private Label lblTotalActive;
    @FXML
    private Label lblCourses;
    @FXML
    private Label lblRoom;
    @FXML
    private HBox hTbl1;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private JFXButton btnEdit;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private JFXRadioButton rAll;
    @FXML
    private JFXRadioButton rActive;
    @FXML
    private JFXRadioButton rDismissed;
    @FXML
    private JFXButton btnDismissed;
    @FXML
    private JFXButton btnRefresh;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadTbl();
        SearchActionEvent();
        unselectRow();
        searchActionByStatus();
        setText();
    }

    @FXML
    private void btnCancelAction(ActionEvent event) {
        txfSearch.setText("");
        unselectRow();
        rAll.setSelected(true);
        setText();
    }

    @FXML
    private void btnRefresh(ActionEvent event) {
        getCourses();
        rAll.setSelected(true);
        unselectRow();
        setText();
    }

    @FXML
    private void btnEditAction(ActionEvent event) {
        loadEditFxml();
    }

    @FXML
    private void btnAddAction(ActionEvent event) {
        loadAddFxml();
    }

    @FXML
    private void btnDismissed(ActionEvent event) {
        Courses c = CoursesTable.getSelectionModel().getSelectedItem();
        if (c == null) {
            AlertController.showWarningAlert("Warning", "Please Selcect a Record First!");

        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to change dismissed?", ButtonType.OK, ButtonType.CANCEL);
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                Courses.deleteCourses(c.getCid());
                AlertController.showInfoAlert("Database Info", "Successfully dismissed");
                getCourses();
                setText();
            } else {
                alert.close();
            }
        }
    }

    private void loadTbl() {
        cId.setCellValueFactory(new PropertyValueFactory("cid"));
        cName.setCellValueFactory(new PropertyValueFactory<>("cname"));
        cFee.setCellValueFactory(new PropertyValueFactory<>("cfee"));
        cStatus.setCellValueFactory(new PropertyValueFactory<>("cstatus"));

        getCourses();
    }

    public void getCourses() {
        ObservableList<Courses> list = Courses.getAllCourses();
        CoursesTable.getItems().clear();
        CoursesTable.setItems(list);
    }

    public void unselectRow() {
        CoursesTable.getSelectionModel().clearSelection();
    }

    public void SearchActionEvent() {
        ObservableList<Courses> list = Courses.getAllCourses();
        CoursesTable.getItems().setAll(list);

        FilteredList<Courses> filter = new FilteredList<>(list, e -> true);
        txfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate((courses) -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String s = txfSearch.getText().toLowerCase();
                rAll.setSelected(true);
                if (courses.getCname().contains(s)) {
                    return true;
                }
                return false;

            });
            SortedList<Courses> sl = new SortedList<>(filter);
            sl.comparatorProperty().bind(CoursesTable.comparatorProperty());
            CoursesTable.getItems().clear();
            CoursesTable.getItems().addAll(sl);
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
                    getCourses();
                } else if (rActive.isSelected()) {
                    searchByStatus("active");
                } else if (rDismissed.isSelected()) {
                    searchByStatus("dismissed");
                }
            }
        });
    }

    public void searchByStatus(String status) {
        ObservableList<Courses> list = Courses.getAllCoursesByStatus(status);
        CoursesTable.getItems().clear();
        CoursesTable.setItems(list);

    }

    private void setText() {
        lblTotalActive.setText(Students.getAllStudentsCountByStatus("active") + "");
        lblRoom.setText(Rooms.getAllRoomsCount() + "");
        lblCourses.setText(Courses.getAllCoursesCount() + "");
    }
    
    public void loadAddFxml() {
        FXMLLoader secView = new FXMLLoader(getClass().getResource("/ui/courses/AddCourses.fxml"));
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
        Courses c = CoursesTable.getSelectionModel().getSelectedItem();

        if (c == null) {
            AlertController.showWarningAlert("Warning", "Please Selcect a Record First!");
        } else {

            if (c.getCstatus().equals("active")) {
                FXMLLoader secView = new FXMLLoader(getClass().getResource("/ui/courses/EditCourses.fxml"));
                try {
                    Parent root = (Parent) secView.load();
                    EditCoursesController ecc = secView.getController();
                    ecc.cId(c.getCid());
                    ecc.cName(c.getCname());
                    ecc.cFee(c.getCfee());
                    
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
}
