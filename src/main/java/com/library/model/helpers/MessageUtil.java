package com.library.model.helpers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Utility class for displaying messages and alerts in the application.
 */
public class MessageUtil {

    /**
     * Displays a message on the screen for a limited duration.
     *
     * @param messageText the {@link Text} node where the message will be displayed
     * @param message     the message content to be displayed
     * @param color       the color of the message text, specified as a CSS color string
     */
    public static void showMessage(Text messageText, String message, String color) {
        messageText.setText(message);
        messageText.setStyle("-fx-fill: " + color + ";");

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> messageText.setText("")));
        timeline.play();
    }

    /**
     * Displays an alert dialog with the specified type, title, and message.
     *
     * @param alertTypeString the type of alert (e.g., "information", "warning", "error", "confirmation")
     * @param title           the title of the alert dialog
     * @param message         the content of the alert dialog
     */
    public static void showAlert(String alertTypeString, String title, String message) {
        Alert.AlertType alertType;

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

        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
