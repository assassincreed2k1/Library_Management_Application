package com.library;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import com.library.model.Person.Librarian;
import com.library.model.Person.Member;
import com.library.service.BookManagement;
import com.library.service.LibrarianManagement;
import com.library.service.LibraryService;
import com.library.service.LoanManagement;
import com.library.service.MagazineManagement;
import com.library.service.MemberManagement;
import com.library.service.NewsPaperManagament;

public class TestApp extends Application {
    public static BookManagement bookManagement = new BookManagement();
    public static MagazineManagement magazineManagement = new MagazineManagement();
    public static NewsPaperManagament newsPaperManagament = new NewsPaperManagament();

    public static LibrarianManagement librarianManagement = new LibrarianManagement();
    public static MemberManagement memberManagement = new MemberManagement();
    public static LoanManagement loanManagement = new LoanManagement();

    public static LibraryService libraryService = new LibraryService();

    @Override
    public void start(Stage stage) throws IOException {
        new MemberManagement();
        new LibrarianManagement();

        // Tải file FXML cần thử nghiệm
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/DemoPerson/Updatemem.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Test AddMem.fxml");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
