/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.main;

import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

/**
 *
 * @author Aung Ko Ko Phyo
 */
public class fxmlLoader {

    private Pane view;

    public Pane getPane(String pageName) {
        if (pageName == "dashboardUi") {
            try {
                view = new FXMLLoader().load(getClass().getResource("/ui/dashboard/dashboardUi.fxml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (pageName == "teachersUi") {
            try {
                view = new FXMLLoader().load(getClass().getResource("/ui/teachers/teachersUi.fxml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (pageName == "studentsUi") {
            try {
                view = new FXMLLoader().load(getClass().getResource("/ui/students/studentsUi.fxml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (pageName == "coursesUi") {
            try {
                view = new FXMLLoader().load(getClass().getResource("/ui/courses/coursesUi.fxml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (pageName == "roomsUi") {
            try {
                view = new FXMLLoader().load(getClass().getResource("/ui/rooms/roomsUi.fxml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return view;
    }
}
