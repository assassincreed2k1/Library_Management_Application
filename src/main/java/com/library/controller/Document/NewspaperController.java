package com.library.controller.Document;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

import com.library.controller.tools.RemoveDocumentController;
import com.library.controller.tools.UpdateDocumentController;
import com.library.model.Person.User;
import com.library.model.Person.User;
import com.library.model.doc.Newspaper;
import com.library.service.BackgroundService;
import com.library.service.NewsPaperManagement;
import com.library.service.ServiceManager;
import com.library.service.LibraryService;

public class NewspaperController {

    private LibraryService libraryService = new LibraryService();
    private NewsPaperManagement newspaperManagement;
    private BackgroundService executor;

    @FXML
    private AnchorPane taskBar;

    @FXML
    private Button exitButton;

    @FXML
    private TableView<Newspaper> newspaperTable;

    @FXML
    private TableColumn<Newspaper, String> idColumn;

    @FXML
    private TableColumn<Newspaper, String> titleColumn;

    @FXML
    private TableColumn<Newspaper, String> genreColumn;

    @FXML
    private TableColumn<Newspaper, String> sourceColumn;

    @FXML
    private TableColumn<Newspaper, String> regionColumn;

    @FXML
    private ImageView prevImage;

    @FXML
    private AnchorPane moreInfoPane;

    private final Image defaultImagePrv = new Image(getClass().getResource("/img/prv.png").toExternalForm());
    private final Image defaultNoImagePrv = new Image(getClass().getResource("/img/Noprev.png").toExternalForm());

    @FXML
    private void initialize() {
        this.newspaperManagement = ServiceManager.getNewsPaperManagement();
        this.executor = ServiceManager.getBackgroundService();

        // Set up the columns to use properties from the Newspaper class
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        sourceColumn.setCellValueFactory(new PropertyValueFactory<>("source"));
        regionColumn.setCellValueFactory(new PropertyValueFactory<>("region"));

        newspaperTable.setOnMouseClicked(event -> runShowNewspaperTask());
        newspaperTable.setOnKeyPressed(event -> runShowNewspaperTask());

        prevImage.setOnMouseClicked(event -> showPreview());

        exitButton.setOnAction(event -> {
            try {
                if (User.isAdmin() || User.isLibrarian()) {
                    libraryService.switchTo("/fxml/Library/LibraryHome.fxml",
                    (Stage) exitButton.getScene().getWindow());
                } else {
                    libraryService.switchTo("/fxml/Library/LibraryForBorrower.fxml",
                    (Stage) exitButton.getScene().getWindow());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        loadNewspaperListAsync();
    }

    private void loadNewspaperListAsync() {
        Task<ObservableList<Newspaper>> loadTask = new Task<>() {
            @Override
            protected ObservableList<Newspaper> call() {
                System.out.println("Running loadNewspaperListAsync()...");
                return newspaperManagement.getAllNewspapers();
            }
        };

        loadTask.setOnSucceeded(event -> {
            System.out.println("Succeeded: loadNewspaperListAsync()");
            newspaperTable.setItems(loadTask.getValue());
        });

        loadTask.setOnFailed(event -> {
            System.out.println("Failed to load newspaper list.");
        });

        executor.startNewThread(loadTask);
    }

    private void runShowNewspaperTask() {
        Task<Void> showTask = createShowNewspaperTask();
        executor.startNewThread(showTask);
    }

    private Task<Void> createShowNewspaperTask() {
        return new Task<>() {
            @Override
            protected Void call() {
                System.out.println("Running showNewspaperTask...");
                Newspaper selectedNewspaper = newspaperTable.getSelectionModel().getSelectedItem();
                if (selectedNewspaper != null) {
                    Platform.runLater(() -> updateNewspaperDetails(selectedNewspaper));
                }
                return null;
            }
        };
    }

    private void showPreview() {
        Task<Void> previewTask = createPreviewTask();
        executor.startNewThread(previewTask);
    }

    private Task<Void> createPreviewTask() {
        return new Task<>() {
            @Override
            protected Void call() {
                System.out.println("Running showPreviewTask...");
                Newspaper selectedNewspaper = newspaperTable.getSelectionModel().getSelectedItem();
                if (selectedNewspaper != null) {
                    Image img = new Image(selectedNewspaper.getImagePreview(), true);
                    Platform.runLater(() -> {
                        if (!img.isError()) {
                            prevImage.setImage(img);
                        } else {
                            prevImage.setImage(defaultNoImagePrv);
                        }
                    });
                }
                return null;
            }
        };
    }

    private void updateNewspaperDetails(Newspaper selectedNewspaper) {
        moreInfoPane.getChildren().clear();

        Label idLabel = createStyledLabel("ID: " + selectedNewspaper.getID(), 5, 0);
        Label titleLabel = createStyledLabel("Title: " + selectedNewspaper.getName(), 5, 20);
        Label genreLabel = createStyledLabel("Genre: " + selectedNewspaper.getGroup(), 5, 40);
        Label sourceLabel = createStyledLabel("Source: " + selectedNewspaper.getSource(), 5, 60);
        Label regionLabel = createStyledLabel("Region: " + selectedNewspaper.getRegion(), 5, 80);

        Button editButton = new Button();
        Button deleteButton = new Button();
        if (User.isAdmin() || User.isLibrarian()) {
            editButton = createStyledButton("Edit", 5, 120, event -> openEditPage(selectedNewspaper));
            deleteButton = createStyledButton("Delete", 200, 120, event -> openDeletePage(selectedNewspaper));
        }
    
        moreInfoPane.getChildren().addAll(idLabel, titleLabel, genreLabel, sourceLabel, regionLabel, editButton,
                deleteButton);

        moreInfoPane.getChildren().addAll(idLabel, titleLabel, genreLabel, sourceLabel, regionLabel);
        prevImage.setImage(defaultImagePrv);
    }

    private Label createStyledLabel(String text, double x, double y) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        label.setLayoutX(x);
        label.setLayoutY(y);
        return label;
    }

    private Button createStyledButton(String text, double x, double y, EventHandler<ActionEvent> eventHandler) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setOnAction(eventHandler);
        return button;
    }

    private void openEditPage(Newspaper selectedNewspaper) {
        try {
            FXMLLoader editPage = new FXMLLoader(getClass().getResource("/fxml/Library/Tools/updateDocunent.fxml"));
            Parent root = editPage.load();

            UpdateDocumentController upController = editPage.getController();
            upController.setId(selectedNewspaper.getID());

            Stage stage = new Stage();
            stage.setTitle("Edit Newspaper");
            stage.setScene(new Scene(root));

            stage.setOnCloseRequest(event -> newspaperTable.setItems(newspaperManagement.getAllNewspapers()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openDeletePage(Newspaper selectedNewspaper) {
        try {
            FXMLLoader delPage = new FXMLLoader(getClass().getResource("/fxml/Library/Tools/removeDocument.fxml"));
            Parent root = delPage.load();

            RemoveDocumentController rmController = delPage.getController();
            rmController.setId(selectedNewspaper.getID());

            Stage stage = new Stage();
            stage.setTitle("Delete Newspaper");
            stage.setScene(new Scene(root));

            stage.setOnCloseRequest(event -> newspaperTable.setItems(newspaperManagement.getAllNewspapers()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
