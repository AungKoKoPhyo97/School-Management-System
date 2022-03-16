/*
 * The MIT License
 * Copyright 2020 myoaung.
 * This software is protected by MIT License.
 * For more info please visit https://mit-license.org/
 */
package alert;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author myoaung
 */
public class AlertController {
    
    public static void  showInfoAlert (String title, String msg){
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setTitle(title);
        a.setContentText(msg);
        a.showAndWait();
    }
    public static void  showErrorAlert (String title, String msg){
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setTitle(title);
        a.setContentText(msg);
        a.showAndWait();
    }
    public static boolean showConfirmAlert (String title, String msg){
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText(null);
        a.setTitle(title);
        a.setContentText(msg);
        Optional <ButtonType> answer = a.showAndWait();
        if (answer.equals(ButtonType.OK)){
            return true;            
        }else {
            return false;
        }
        
    }
    public static void showWarningAlert (String title, String msg){
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);
        a.setTitle(title);
        a.setContentText(msg);
        a.showAndWait();
      
        }
    
    
    
    
}
