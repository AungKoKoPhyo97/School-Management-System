package ui.students;

import alert.AlertController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import dataclass.Courses;
import dataclass.Rooms;
import dataclass.Students;
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
public class StudentsUiController implements Initializable {

    @FXML
    private JFXTextField txfSearch;
    @FXML
    private JFXRadioButton rAll;
    @FXML
    private JFXRadioButton rActive;
    @FXML
    private JFXRadioButton rGraduated;
    @FXML
    private JFXButton btnGraduated;
    @FXML
    private JFXButton btnRefresh;
    @FXML
    private VBox vTable;
    private Label lblActive;
    @FXML
    private HBox hTbl1;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private JFXButton btnEdit;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private TableView<Students> StudentsTable;
    @FXML
    private TableColumn<Students, Integer> cId;
    @FXML
    private TableColumn<Students, String> cName;
    @FXML
    private TableColumn<Students, String> cAddress;
    @FXML
    private TableColumn<Students, String> cFatherName;
    @FXML
    private TableColumn<Students, LocalDate> cRegisterDate;
    @FXML
    private TableColumn<Students, LocalDate> cExitDate;
    @FXML
    private TableColumn<Students, LocalDate> cDoB;
    @FXML
    private TableColumn<Students, Integer> cPhone;
    @FXML
    private TableColumn<Students, Integer> cCourse;
    @FXML
    private TableColumn<Students, Integer> cRoom;
    @FXML
    private TableColumn<Students, String> cStatus;
    @FXML
    private JFXButton btnDismissed;
    @FXML
    private JFXRadioButton rDismissed;
    @FXML
    private Label lblTotalActive;
    @FXML
    private Label lblCourses;
    @FXML
    private Label lblRoom;

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
    private void btnGraduated(ActionEvent event) {
        Students s = StudentsTable.getSelectionModel().getSelectedItem();
        if (s == null) {
            AlertController.showWarningAlert("Warning", "Please Selcect a Record First!");

        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to change graduated?", ButtonType.OK, ButtonType.CANCEL);
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                Students.granduatedStudents(s.getSid());
                AlertController.showInfoAlert("Database Info", "Successfully graduated");
                getStudents();
                setText();
            } else {
                alert.close();
            }
        }
    }

    @FXML
    private void btnDismissed(ActionEvent event) {
        Students s = StudentsTable.getSelectionModel().getSelectedItem();
        if (s == null) {
            AlertController.showWarningAlert("Warning", "Please Selcect a Record First!");

        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to change dismissed?", ButtonType.OK, ButtonType.CANCEL);
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                Students.deleteStudents(s.getSid());
                AlertController.showInfoAlert("Database Info", "Successfully dismissed");
                getStudents();
                setText();
            } else {
                alert.close();
            }
        }
    }

    @FXML
    private void btnRefresh(ActionEvent event) {
        getStudents();
        rAll.setSelected(true);
        unselectRow();
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
    private void btnEditAction(ActionEvent event) {
        loadEditFxml();
    }

    @FXML
    private void btnAddAction(ActionEvent event) {
        loadAddFxml();
    }

    public void loadTbl() {
        cId.setCellValueFactory(new PropertyValueFactory("sid"));
        cName.setCellValueFactory(new PropertyValueFactory<>("sname"));
        cAddress.setCellValueFactory(new PropertyValueFactory<>("saddress"));
        cFatherName.setCellValueFactory(new PropertyValueFactory<>("sfathername"));
        cDoB.setCellValueFactory(new PropertyValueFactory<>("sdob"));
        cRegisterDate.setCellValueFactory(new PropertyValueFactory<>("sregisterdate"));
        cExitDate.setCellValueFactory(new PropertyValueFactory<>("sexitdate"));
        cPhone.setCellValueFactory(new PropertyValueFactory<>("sphone"));
        cStatus.setCellValueFactory(new PropertyValueFactory<>("sstatus"));
        cRoom.setCellValueFactory(new PropertyValueFactory<>("rid"));
        cCourse.setCellValueFactory(new PropertyValueFactory<>("cid"));

//        StudentsTable.getColumns().addAll(cId, cName, cAddress, cFatherName, cDoB, cRegisterDate, cExitDate, cPhone, cStatus, cRoom, cCourse);
        getStudents();
    }

    public void getStudents() {
        ObservableList<Students> list = Students.getAllStudents();
        StudentsTable.getItems().clear();
        StudentsTable.setItems(list);
    }

    public void unselectRow() {
        StudentsTable.getSelectionModel().clearSelection();
    }

    public void loadAddFxml() {
        FXMLLoader secView = new FXMLLoader(getClass().getResource("/ui/students/AddStudents.fxml"));
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
        Students s = StudentsTable.getSelectionModel().getSelectedItem();

        if (s == null) {
            AlertController.showWarningAlert("Warning", "Please Selcect a Record First!");
        } else {

            if (s.getSstatus().equals("active")) {
                FXMLLoader secView = new FXMLLoader(getClass().getResource("/ui/students/EditStudents.fxml"));
                try {
                    Parent root = (Parent) secView.load();
                    EditStudentsController esc = secView.getController();
                    esc.sId(s.getSid());
                    esc.sName(s.getSname());
                    esc.sAddress(s.getSaddress());
                    esc.sFatherName(s.getSfathername());
                    esc.sDob(s.getSdob());
                    esc.sRegisterDate(s.getSregisterdate());
                    esc.sPhone(s.getSphone());
                    esc.rId(s.getRid());
                    esc.cId(s.getCid());
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
        ObservableList<Students> list = Students.getAllStudents();
        StudentsTable.getItems().setAll(list);

        FilteredList<Students> filter = new FilteredList<>(list, e -> true);
        txfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate((students) -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String s = txfSearch.getText().toLowerCase();
                rAll.setSelected(true);
                if (students.getSname().contains(s)) {
                    return true;
                }
                return false;

            });
            SortedList<Students> sl = new SortedList<>(filter);
            sl.comparatorProperty().bind(StudentsTable.comparatorProperty());
            StudentsTable.getItems().clear();
            StudentsTable.getItems().addAll(sl);
        });
    }

    public void searchActionByStatus() {
//        System.out.println(Teachers.getAllTeachersByStatus("dismissed"));
        ToggleGroup statusGp = new ToggleGroup();
        rActive.setToggleGroup(statusGp);
        rAll.setToggleGroup(statusGp);
        rGraduated.setToggleGroup(statusGp);
        rDismissed.setToggleGroup(statusGp);
        rAll.setSelected(true);

        statusGp.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (rAll.isSelected()) {
                    getStudents();
                } else if (rActive.isSelected()) {
                    searchByStatus("active");
                } else if (rGraduated.isSelected()) {
                    searchByStatus("graduated");
                } else if (rDismissed.isSelected()) {
                    searchByStatus("dismissed");
                }
            }

        });
    }

    public void searchByStatus(String status) {
        ObservableList<Students> list = Students.getAllStudentsByStatus(status);
        StudentsTable.getItems().clear();
        StudentsTable.setItems(list);

    }

    private void setText() {
        lblTotalActive.setText(Students.getAllStudentsCountByStatus("active") + "");
        lblRoom.setText(Rooms.getAllRoomsCount() + "");
        lblCourses.setText(Courses.getAllCoursesCount() + "");
    }
}
