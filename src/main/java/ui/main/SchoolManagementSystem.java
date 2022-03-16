/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.main;

import database.databaseController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Aung Ko Ko Phyo
 */
public class SchoolManagementSystem extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/ui/main/main.fxml"));

        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("School Management System");
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        databaseController db = new databaseController();
        if (!db.checkDbExist()) {
            db.createRoomsTbl();
            db.createCoursesTbl();
            db.createTeachersTbl();
            db.createStudentsTbl();
        }
        launch(args);
    }
}
