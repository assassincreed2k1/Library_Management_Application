package com.library.controller.Document;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;

import com.library.controller.tools.RemoveDocumentController;
import com.library.controller.tools.UpdateDocumentController;
import com.library.model.doc.Magazine;
import com.library.service.BackgroundService;
import com.library.service.MagazineManagement;
import com.library.service.ServiceManager;
import com.library.service.LibraryService;

public class MagazineController {

    private LibraryService libraryService = new LibraryService();

    private MagazineManagement magazineManagement;

    private BackgroundService executor;

    @FXML
    private AnchorPane taskBar;

    @FXML
    Button exitButton;

    @FXML
    private TableView<Magazine> magazineTable;

    @FXML
    private TableColumn<Magazine, String> idColumn;

    @FXML
    private TableColumn<Magazine, String> titleColumn;

    @FXML
    private TableColumn<Magazine, String> genreColumn;

    @FXML
    private TableColumn<Magazine, String> publisherColumn;

    @FXML
    private TableColumn<Magazine, Boolean> isAvailableColumn;

    @FXML
    private ImageView prevImage;

    @FXML
    private AnchorPane moreInfoPane;

    private Task<Void> showMagazineTask;
    private Task<Void> showPrevTask;
    private final Image defaultImagePrv = new Image(getClass().getResource("/img/prv.png").toExternalForm());
    private final Image defaultNoImagePrv = new Image(getClass().getResource("/img/Noprev.png").toExternalForm());

    @FXML
    private void initialize() {
        this.magazineManagement = ServiceManager.getMagazineManagement();
        this.executor = ServiceManager.getBackgroundService();

        // Set up the columns to use properties from the Magazine class
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        isAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("isAvailable"));

        isAvailableColumn.setCellFactory(column -> new TableCell<Magazine, Boolean>() {
            @Override
            protected void updateItem(Boolean isAvailable, boolean empty) {
                super.updateItem(isAvailable, empty);
                if (empty || isAvailable == null) {
                    setText(null);
                } else {
                    setText(isAvailable ? "Yes" : "No");
                }
            }
        });

        magazineTable.setOnMouseClicked(event -> runShowMagazineTask());
        magazineTable.setOnKeyPressed(event -> runShowMagazineTask());

        prevImage.setOnMouseClicked(event -> showPreview());

        exitButton.setOnAction(event -> {
            try {
                libraryService.switchTo("/fxml/Library/LibraryHome.fxml",
                        (Stage) exitButton.getScene().getWindow());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        showMagazineTask = reShowMagazineTask();
        showPrevTask = reShowPrevTask();

        loadMagazineListAsync();
    }

    private Task<Void> reShowMagazineTask() {
        return new Task<>() {
            @Override
            protected Void call() {
                System.out.println("Running new showMagazineTask()...");
                Magazine selectedMagazine = magazineTable.getSelectionModel().getSelectedItem();
                Platform.runLater(() -> updateMagazineDetails(selectedMagazine));
                return null;
            }

            @Override
            protected void succeeded() {
                System.out.println("showMagazineTask(): Succeeded!");
            }

            @Override
            protected void cancelled() {
                System.out.println("showMagazineTask(): Task Cancelled");
            }

            @Override
            protected void failed() {
                System.out.println("showMagazineTask(): Task Error");
            }
        };
    }

    private Task<Void> reShowPrevTask() {
        return new Task<>() {
            @Override
            protected Void call() {
                System.out.println("Running new showPrevTask()...");
                Magazine selectedMagazine = magazineTable.getSelectionModel().getSelectedItem();
                if (selectedMagazine != null) {
                    Image img = new Image(selectedMagazine.getImagePreview(), true);
                    if (!img.isError()) {
                        Platform.runLater(() -> prevImage.setImage(img));
                    } else {
                        Platform.runLater(() -> prevImage.setImage(defaultNoImagePrv));
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
                Platform.runLater(() -> prevImage.setImage(defaultNoImagePrv));
            }
        };
    }

    private void loadMagazineListAsync() {
        Task<ObservableList<Magazine>> loadMagazineListAsyncTask = new Task<>() {
            @Override
            protected ObservableList<Magazine> call() {
                System.out.println("Running loadMagazineListAsync()...");
                return magazineManagement.getAllMagazines();
            }
        };

        loadMagazineListAsyncTask.setOnSucceeded(event -> {
            System.out.println("Succeeded: loadMagazineListAsync()");
            magazineTable.setItems(loadMagazineListAsyncTask.getValue());
        });

        loadMagazineListAsyncTask.setOnFailed(event -> {
            System.out.println("Failed to load magazine list.");
        });

        executor.startNewThread(loadMagazineListAsyncTask);
    }

    private void runShowMagazineTask() {
        if (showMagazineTask != null && showMagazineTask.isRunning()) {
            System.out.println("Task Cancelled");
            showMagazineTask.cancel();
        }

        showMagazineTask = reShowMagazineTask();

        Magazine selectedMagazine = magazineTable.getSelectionModel().getSelectedItem();
        if (selectedMagazine == null) {
            System.out.println("No magazine selected.");
            return;
        }

        executor.startNewThread(showMagazineTask);
    }

    private void showPreview() {
        showPrevTask = reShowPrevTask();
        executor.startNewThread(showPrevTask);
    }

    private void updateMagazineDetails(Magazine selectedMagazine) {
        moreInfoPane.getChildren().clear();

        Label idLabel = createStyledLabel("ID: " + selectedMagazine.getID(), 5, 0);
        Label titleLabel = createStyledLabel("Title: " + selectedMagazine.getName(), 5, 20);
        Label genreLabel = createStyledLabel("Genre: " + selectedMagazine.getGroup(), 5, 40);
        Label publisherLabel = createStyledLabel("Publisher: " + selectedMagazine.getPublisher(), 5, 60);
        Label availabilityLabel = createStyledLabel("Available: " + (selectedMagazine.getIsAvailable() ? "Yes" : "No"),
                5, 80);

        Button editButton = createStyledButton("Edit", 5, 160, event -> openEditPage(selectedMagazine));
        Button deleteButton = createStyledButton("Delete", 200, 160, event -> openDeletePage(selectedMagazine));

        moreInfoPane.getChildren().addAll(idLabel, titleLabel, genreLabel, publisherLabel, availabilityLabel,
                editButton, deleteButton);
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

    private void openEditPage(Magazine selectedMagazine) {
        try {
            FXMLLoader editPage = new FXMLLoader(getClass().getResource("/fxml/Library/Tools/updateDocument.fxml"));
            Parent root = editPage.load();
    
            UpdateDocumentController upController = editPage.getController();
            upController.setId(selectedMagazine.getID());
    
            Stage stage = new Stage();
            stage.setTitle("Edit Magazine");
            stage.setScene(new Scene(root));
    
            stage.setOnCloseRequest(event -> magazineTable.setItems(magazineManagement.getAllMagazines()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openDeletePage(Magazine selectedMagazine) {
        try {
            FXMLLoader delPage = new FXMLLoader(getClass().getResource("/fxml/Library/Tools/removeDocument.fxml"));
            Parent root = delPage.load();
    
            RemoveDocumentController rmController = delPage.getController();
            rmController.setId(selectedMagazine.getID());
    
            Stage stage = new Stage();
            stage.setTitle("Delete Magazine");
            stage.setScene(new Scene(root));
    
            stage.setOnCloseRequest(event -> magazineTable.setItems(magazineManagement.getAllMagazines()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
