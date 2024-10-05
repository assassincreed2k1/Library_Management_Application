package com.lma;

// import javax.swing.*;
// /**
//  * App Launcher
//  *
//  */

// public class App {
//     public static void main(String args[]) {
//         SwingUtilities.invokeLater(new Runnable() {
//             @Override
//             public void run() {
//                 new Main().setVisible(true);
//             }
//         });
//     }
// }

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Hello, JavaFX!");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 400, 300);

        primaryStage.setTitle("My JavaFX App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
