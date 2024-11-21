package com.library.controller.Document;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

import com.library.model.doc.Newspaper;
import com.library.service.BackgroundService;
import com.library.service.NewsPaperManagament;
import com.library.service.ServiceManager;

public class NewspaperController {

    private NewsPaperManagament newspaperManagement;
    private BackgroundService executor;

    @FXML
    private AnchorPane taskBar;

    @FXML
    private Button exitButton;

    @FXML
    private TableView<Newspaper> newspaperTable;

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

    // This method is called by the FXMLLoader when initialization is complete
    @FXML
    private void initialize() {
        this.newspaperManagement = ServiceManager.getNewsPaperManagament();
        this.executor = ServiceManager.getBackgroundService();

        // Set up the columns to use properties from the Newspaper class
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        sourceColumn.setCellValueFactory(new PropertyValueFactory<>("source"));
        regionColumn.setCellValueFactory(new PropertyValueFactory<>("region"));

        newspaperTable.setOnMouseClicked(event -> showSelectedNewspaperDetails());
        newspaperTable.setOnKeyPressed(event -> showSelectedNewspaperDetails());

        prevImage.setOnMouseClicked(event -> showPreview());

        exitButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Library/LibraryHome.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) exitButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        loadNewspaperListAsync();
    }

    private void loadNewspaperListAsync() {
        Task<ObservableList<Newspaper>> task = new Task<>() {
            @Override
            protected ObservableList<Newspaper> call() {
                System.out.println("Running loadNewspaperListAsync()...");
                return getNewspaperList();
            }
        };

        task.setOnSucceeded(event -> {
            System.out.println("Succeeded: loadNewspaperListAsync()");
            newspaperTable.setItems(task.getValue());
        });

        task.setOnFailed(event -> {
            System.out.println("Failed to load newspaper list.");
        });

        executor.startNewThread(task);
    }

    private Task<Void> showNewspaperTask;
    private Task<Void> showPrevTask;

    private void showSelectedNewspaperDetails() {
        if (showNewspaperTask != null && showNewspaperTask.isRunning()) {
            System.out.println("Task Cancelled");
            showNewspaperTask.cancel();
        }

        showNewspaperTask = new Task<>() {
            @Override
            protected Void call() {
                System.out.println("Running new updateNewspaperDetails()...");

                Newspaper selectedNewspaper = newspaperTable.getSelectionModel().getSelectedItem();
                if (selectedNewspaper != null) {
                    Platform.runLater(() -> updateNewspaperDetails(selectedNewspaper));
                }
                return null;
            }

            @Override
            protected void succeeded() {
                System.out.println("updateNewspaperDetails(): Succeeded!");
            }

            @Override
            protected void cancelled() {
                System.out.println("updateNewspaperDetails(): Task Cancelled");
            }

            @Override
            protected void failed() {
                System.out.println("updateNewspaperDetails(): Task Error");
            }
        };

        showPrevTask = new Task<>() {
            @Override
            protected Void call() {
                System.out.println("Running new showPrevTask()...");
                Newspaper selectedNewspaper = newspaperTable.getSelectionModel().getSelectedItem();
                if (selectedNewspaper != null) {
                    Platform.runLater(() -> prevImage.setImage(new Image(selectedNewspaper.getImagePreview())));
                }
                return null;
            }

            @Override
            protected void succeeded() {
                System.out.println("showPrevTask(): Succeeded!");
            }

            @Override
            protected void cancelled() {
                System.out.println("showPrevTask(): Task Cancelled");
            }

            @Override
            protected void failed() {
                System.out.println("showPrevTask(): Task Error");
            }
        };

        executor.startNewThread(showNewspaperTask);
    }

    private void showPreview() {
        showPrevTask = new Task<>() {
            @Override
            protected Void call() {
                System.out.println("Running new showPrevTask()...");
                Newspaper selectedNewspaper = newspaperTable.getSelectionModel().getSelectedItem();
                if (selectedNewspaper != null) {
                    if (selectedNewspaper.getImagePreview() != null || !selectedNewspaper.getImagePreview().isEmpty()) {
                        Platform.runLater(() -> prevImage.setImage(new Image(selectedNewspaper.getImagePreview())));
                    } else {
                        prevImage.setImage(new Image(getClass().getResource("/img/prve.png").toExternalForm()));
                    }
                }
                return null;
            }

            @Override
            protected void succeeded() {
                System.out.println("showPrevTask(): Succeeded!");
            }

            @Override
            protected void cancelled() {
                System.out.println("showPrevTask(): Task Cancelled");
            }

            @Override
            protected void failed() {
                System.out.println("showPrevTask(): Task Error");
            }
        };
        executor.startNewThread(showPrevTask);
    }

    private void updateNewspaperDetails(Newspaper selectedNewspaper) {
        moreInfoPane.getChildren().clear();

        Label idLabel = new Label("ID: " + selectedNewspaper.getID());
        Label titleLabel = new Label("Title: " + selectedNewspaper.getName());
        Label genreLabel = new Label("Genre: " + selectedNewspaper.getGroup());
        Label sourceLabel = new Label("Source: " + selectedNewspaper.getSource());
        Label regionLabel = new Label("Region: " + selectedNewspaper.getRegion());

        idLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        titleLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        genreLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        sourceLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        regionLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");

        moreInfoPane.getChildren().addAll(idLabel, titleLabel, genreLabel, sourceLabel, regionLabel);

        idLabel.setLayoutX(5);
        idLabel.setLayoutY(0);

        titleLabel.setLayoutX(5);
        titleLabel.setLayoutY(20);

        genreLabel.setLayoutX(5);
        genreLabel.setLayoutY(40);

        sourceLabel.setLayoutX(5);
        sourceLabel.setLayoutY(60);

        regionLabel.setLayoutX(5);
        regionLabel.setLayoutY(80);

        prevImage.setImage(new Image(getClass().getResource("/img/prv.png").toExternalForm()));
    }

    private ObservableList<Newspaper> getNewspaperList() {
        return newspaperManagement.getAllNewspapers();
    }
}
