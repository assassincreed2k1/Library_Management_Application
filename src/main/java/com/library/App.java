package com.library;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

//import com.library.model.Person.Librarian;
import com.library.service.BookManagement;
import com.library.service.LibrarianManagement;
import com.library.service.LoanManagement;
import com.library.service.MagazineManagement;
import com.library.service.MemberManagement;
import com.library.service.NewsPaperManagament;

public class App extends Application {

    public static BookManagement bookManagement = new BookManagement();
    public static MagazineManagement magazineManagement = new MagazineManagement();
    public static NewsPaperManagament newsPaperManagament = new NewsPaperManagament();

    public static LibrarianManagement librarianManagement = new LibrarianManagement();
    public static MemberManagement memberManagement = new MemberManagement();
    public static LoanManagement loanManagement = new LoanManagement();

    //API Service: Static

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login/Login.fxml"));
        
        Scene scene = new Scene(root);
        
        setDragEvent(root, stage);

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
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
        launch(args);
    }
}
