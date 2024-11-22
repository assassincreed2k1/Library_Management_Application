package com.library.controller.Document;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;

import com.library.model.doc.Magazine;
import com.library.service.BackgroundService;
import com.library.service.MagazineManagement;
import com.library.service.ServiceManager;
import com.library.service.LibraryService;

public class MagazineController {

    private LibraryService libraryService;

    private MagazineManagement magazineManagement;

    private BackgroundService executor;

    @FXML
    private AnchorPane taskBar;

    @FXML
    Button exitButton;

    @FXML
    private TableView<Magazine> magazineTable;

    @FXML
    private TableColumn<Magazine, String> titleColumn;

    @FXML
    private TableColumn<Magazine, String> genreColumn;

    @FXML
    private TableColumn<Magazine, String> publisherColumn;

    @FXML
    private ImageView prevImage;

    @FXML
    private AnchorPane moreInfoPane;

    // This method is called by the FXMLLoader when initialization is complete
    @FXML
    private void initialize() {
        this.magazineManagement = ServiceManager.getMagazineManagement();
        this.executor = ServiceManager.getBackgroundService();

        // Set up the columns to use properties from the Magazine class
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));

        magazineTable.setOnMouseClicked(event -> showSelectedMagazineDetails());
        magazineTable.setOnKeyPressed(event -> showSelectedMagazineDetails());

        prevImage.setOnMouseClicked(event -> showPreview());

        exitButton.setOnAction(event -> {
            try {
                libraryService.switchTo("/fxml/Library/LibraryHome.fxml",
                        (Stage) exitButton.getScene().getWindow());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        loadMagazineListAsync();
    }

    private void loadMagazineListAsync() {
        Task<ObservableList<Magazine>> task = new Task<>() {
            @Override
            protected ObservableList<Magazine> call() {
                System.out.println("Running loadMagazineListAsync()...");
                return getMagazineList();
            }
        };

        task.setOnSucceeded(event -> {
            System.out.println("Succeeded: loadMagazineListAsync()");
            magazineTable.setItems(task.getValue());
        });

        task.setOnFailed(event -> {
            System.out.println("Failed to load magazine list.");
        });

        executor.startNewThread(task);
    }

    private Task<Void> showMagazineTask;
    private Task<Void> showPrevTask;

    private void showSelectedMagazineDetails() {
        if (showMagazineTask != null && showMagazineTask.isRunning()) {
            System.out.println("Task Cancelled");
            showMagazineTask.cancel();
        }

        showMagazineTask = new Task<>() {
            @Override
            protected Void call() {
                System.out.println("Running new updateMagazineDetails()...");

                Magazine selectedMagazine = magazineTable.getSelectionModel().getSelectedItem();
                if (selectedMagazine != null) {
                    Platform.runLater(() -> updateMagazineDetails(selectedMagazine));
                }
                return null;
            }

            @Override
            protected void succeeded() {
                System.out.println("updateMagazineDetails(): Succeeded!");
            }

            @Override
            protected void cancelled() {
                System.out.println("updateMagazineDetails(): Task Cancelled");
            }

            @Override
            protected void failed() {
                System.out.println("updateMagazineDetails(): Task Error");
            }
        };

        showPrevTask = new Task<>() {
            @Override
            protected Void call() {
                System.out.println("Running new showPrevTask()...");
                Magazine selectedMagazine = magazineTable.getSelectionModel().getSelectedItem();
                if (selectedMagazine != null) {
                    Platform.runLater(() -> prevImage.setImage(new Image(selectedMagazine.getImagePreview())));
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

        executor.startNewThread(showMagazineTask);
    }

    private void showPreview() {
        showPrevTask = new Task<>() {
            @Override
            protected Void call() {
                System.out.println("Running new showPrevTask()...");
                Magazine selectedMagazine = magazineTable.getSelectionModel().getSelectedItem();
                if (selectedMagazine != null) {
                    if (selectedMagazine.getImagePreview() != null || !selectedMagazine.getImagePreview().isEmpty()) {
                        Platform.runLater(() -> prevImage.setImage(new Image(selectedMagazine.getImagePreview())));
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

    private void updateMagazineDetails(Magazine selectedMagazine) {
        moreInfoPane.getChildren().clear();

        Label idLabel = new Label("ID: " + selectedMagazine.getID());
        Label titleLabel = new Label("Title: " + selectedMagazine.getName());
        Label genreLabel = new Label("Genre: " + selectedMagazine.getGroup());
        Label publisherLabel = new Label("Publisher: " + selectedMagazine.getPublisher());
        Label availabilityLabel = new Label("Available: " + (selectedMagazine.getIsAvailable() ? "Yes" : "No"));
        Label imagePreviewLabel = new Label("Image Preview: "
                + (selectedMagazine.getImagePreview().isEmpty() ? "No preview" : selectedMagazine.getImagePreview()));

        idLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        titleLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        genreLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        publisherLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        availabilityLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        imagePreviewLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");

        moreInfoPane.getChildren().addAll(idLabel, titleLabel, genreLabel, publisherLabel, availabilityLabel,
                imagePreviewLabel);

        idLabel.setLayoutX(5);
        idLabel.setLayoutY(0);

        titleLabel.setLayoutX(5);
        titleLabel.setLayoutY(20);

        genreLabel.setLayoutX(5);
        genreLabel.setLayoutY(40);

        publisherLabel.setLayoutX(5);
        publisherLabel.setLayoutY(60);

        availabilityLabel.setLayoutX(5);
        availabilityLabel.setLayoutY(80);

        imagePreviewLabel.setLayoutX(5);
        imagePreviewLabel.setLayoutY(100);

        prevImage.setImage(new Image(getClass().getResource("/img/prv.png").toExternalForm()));
    }

    private ObservableList<Magazine> getMagazineList() {
        return magazineManagement.getAllMagazines();
    }
}
