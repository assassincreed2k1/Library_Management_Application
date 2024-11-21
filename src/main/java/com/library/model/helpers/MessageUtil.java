package com.library.model.helpers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
}
