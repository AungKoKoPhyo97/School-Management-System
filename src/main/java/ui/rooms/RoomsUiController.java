/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ui.rooms;

import alert.AlertController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import dataclass.Courses;
import dataclass.Rooms;
import dataclass.Students;
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
import ui.courses.EditCoursesController;

/**
 * FXML Controller class
 *
 * @author Aung Ko Ko Phyo
 */
public class RoomsUiController implements Initializable {

    @FXML
    private JFXTextField txfSearch;
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
    @FXML
    private VBox vTable;
    @FXML
    private TableView<Rooms> RoomsTable;
    @FXML
    private TableColumn<Rooms, Integer> cId;
    @FXML
    private TableColumn<Rooms, String> cName;
    @FXML
    private TableColumn<Rooms, String> cStatus;
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
        getRooms();
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
        Rooms r = RoomsTable.getSelectionModel().getSelectedItem();
        if (r == null) {
            AlertController.showWarningAlert("Warning", "Please Selcect a Record First!");

        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to change dismissed?", ButtonType.OK, ButtonType.CANCEL);
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                Rooms.deleteRooms(r.getRid());
                AlertController.showInfoAlert("Database Info", "Successfully dismissed");
                getRooms();
                setText();
            } else {
                alert.close();
            }
        }
    }

    private void loadTbl() {
        cId.setCellValueFactory(new PropertyValueFactory("rid"));
        cName.setCellValueFactory(new PropertyValueFactory<>("rname"));
        cStatus.setCellValueFactory(new PropertyValueFactory<>("rstatus"));

        getRooms();
    }

    public void getRooms() {
        ObservableList<Rooms> list = Rooms.getAllRooms();
        RoomsTable.getItems().clear();
        RoomsTable.setItems(list);
    }

    public void unselectRow() {
        RoomsTable.getSelectionModel().clearSelection();
    }

    public void SearchActionEvent() {
        ObservableList<Rooms> list = Rooms.getAllRooms();
        RoomsTable.getItems().setAll(list);

        FilteredList<Rooms> filter = new FilteredList<>(list, e -> true);
        txfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate((rooms) -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String s = txfSearch.getText().toLowerCase();
                rAll.setSelected(true);
                if (rooms.getRname().contains(s)) {
                    return true;
                }
                return false;

            });
            SortedList<Rooms> sl = new SortedList<>(filter);
            sl.comparatorProperty().bind(RoomsTable.comparatorProperty());
            RoomsTable.getItems().clear();
            RoomsTable.getItems().addAll(sl);
        });
    }

    public void searchActionByStatus() {
        ToggleGroup statusGp = new ToggleGroup();
        rActive.setToggleGroup(statusGp);
        rAll.setToggleGroup(statusGp);
        rDismissed.setToggleGroup(statusGp);
        rAll.setSelected(true);

        statusGp.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (rAll.isSelected()) {
                    getRooms();
                } else if (rActive.isSelected()) {
                    searchByStatus("active");
                } else if (rDismissed.isSelected()) {
                    searchByStatus("dismissed");
                }
            }
        });
    }

    public void searchByStatus(String status) {
        ObservableList<Rooms> list = Rooms.getAllRoomsByStatus(status);
        RoomsTable.getItems().clear();
        RoomsTable.setItems(list);

    }

    private void setText() {
        lblTotalActive.setText(Students.getAllStudentsCountByStatus("active") + "");
        lblRoom.setText(Rooms.getAllRoomsCount() + "");
        lblCourses.setText(Courses.getAllCoursesCount() + "");
    }

    public void loadAddFxml() {
        FXMLLoader secView = new FXMLLoader(getClass().getResource("/ui/rooms/AddRooms.fxml"));
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
        Rooms r = RoomsTable.getSelectionModel().getSelectedItem();

        if (r == null) {
            AlertController.showWarningAlert("Warning", "Please Selcect a Record First!");
        } else {

            if (r.getRstatus().equals("active")) {
                FXMLLoader secView = new FXMLLoader(getClass().getResource("/ui/rooms/EditRooms.fxml"));
                try {
                    Parent root = (Parent) secView.load();
                    EditRoomsController erc = secView.getController();
                    erc.rId(r.getRid());
                    erc.rName(r.getRname());

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
