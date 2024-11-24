package com.library;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import com.library.model.Person.User;

import com.library.service.ServiceManager;

public class App extends Application {


    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws IOException {
        ServiceManager.initialize();

<<<<<<< HEAD
        // Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login/SignIn.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Library/LibraryHome.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/fxml/Person/DocBorrow.fxml"));
=======
        //Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login/SignIn.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/fxml/Library/Tools/DocumentBorrow.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Library/Tools/BorrowingHistoryForMember.fxml"));
>>>>>>> 52d46eb5478d92cd78f49ec4f3d246a3ad07ca17
        //Parent root = FXMLLoader.load(getClass().getResource("/fxml/Documents/Books.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/fxml/Person/DocBorrow.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/fxml/Library/LibraryForBorrower.fxml"));
        
        Scene scene = new Scene(root); 
        
        setDragEvent(root, stage);

        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            ServiceManager.getBackgroundService().stopAllThreads();
        });
        stage.show();
    }

    private void setDragEvent(Parent root, Stage stage) {
        root.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    public static void main(String[] args) {
        User.setUser("M000006");
        launch(args);
    }
}
