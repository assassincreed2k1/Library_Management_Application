package com.library;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class App extends Application {

    private static Scene scene;
    private static Connection connection; // Declare the connection variable

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        //test dabase work
        /*myDatabase myDB = new myDatabase();
        myDB.createDB();
        System.out.println("db created successfull");
        myDB.createTable();
        System.out.println("table is created");
        myDB.insertData();
        System.out.println("inserted data");
        myDB.updateData();
        System.out.println("updated data");
        myDB.selectData();
        System.out.println("selected data done");
        myDB.deleteData();
        System.out.println("delete done");.*/
        launch();
    }
}
