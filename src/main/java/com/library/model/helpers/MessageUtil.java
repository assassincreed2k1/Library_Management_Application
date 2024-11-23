package com.library.model.helpers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class MessageUtil {
    /**
     * show message to screen.
     * @param messageText Text messageText
     * @param message String message
     * @param color String color 
     */
    public static void showMessage(Text messageText, String message, String color) {
        messageText.setText(message);
        messageText.setStyle("-fx-fill: " + color + ";");

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> messageText.setText("")));
        timeline.play();
    }

    public static void showAlert(String alertTypeString, String title, String message) {
        Alert.AlertType alertType;
    
        // Convert the String to Alert.AlertType
        switch (alertTypeString.toLowerCase()) {
            case "information":
                alertType = Alert.AlertType.INFORMATION;
                break;
            case "warning":
                alertType = Alert.AlertType.WARNING;
                break;
            case "error":
                alertType = Alert.AlertType.ERROR;
                break;
            case "confirmation":
                alertType = Alert.AlertType.CONFIRMATION;
                break;
            default:
                alertType = Alert.AlertType.NONE;  // Default type if no valid string is passed
                break;
        }
    
        // Create and show the alert
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
}
