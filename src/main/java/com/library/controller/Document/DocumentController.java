// package com.library.controller.Document;

// import javafx.application.Platform;

// import javafx.collections.ObservableList;

// import javafx.concurrent.Task;

// import javafx.event.ActionEvent;

// import javafx.fxml.FXML;
// import javafx.fxml.FXMLLoader;

// import javafx.scene.control.Label;
// import javafx.scene.control.TableColumn;
// import javafx.scene.control.TableView;
// import javafx.scene.control.cell.PropertyValueFactory;
// import javafx.scene.control.Button;
// import javafx.scene.control.TableCell;

// import javafx.scene.image.Image;
// import javafx.scene.image.ImageView;
// import javafx.scene.layout.AnchorPane;
// import javafx.stage.Stage;
// import javafx.scene.Parent;
// import javafx.scene.Scene;

// import javafx.event.EventHandler;

// import java.io.IOException;

// import com.library.controller.tools.RemoveDocumentController;
// import com.library.controller.tools.UpdateDocumentController;
// import com.library.model.doc.Book;
// import com.library.service.BackgroundService;
// import com.library.service.BookManagement;
// import com.library.service.ServiceManager;
// import com.library.service.LibraryService;

// public class DocumentController {

//     @FXML
//     private AnchorPane taskBar;

//     @FXML
//     private Button exitButton;

//     @FXML
//     private TableView<Document> documentTable;

//     @FXML
//     private TableColumn<Document, String> idColumn;

//     @FXML
//     private TableColumn<Document, String> titleColumn;

//     @FXML
//     private TableColumn<Document, String> genreColumn;

//     @FXML
//     private TableColumn<Document, Boolean> isAvailableColumn;

//     @FXML
//     private ImageView prevImage;

//     @FXML
//     private AnchorPane moreInfoPane;

//     private final Image defaultImage = new Image(getClass().getResource("/img/icon.png").toExternalForm());

//     @FXML
//     private void initialize() {
//         // Kết nối cột dữ liệu với Document model
//         idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
//         titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
//         genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
//         isAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("isAvailable"));

//         // Định nghĩa cell factory cho cột IsAvailable
//         isAvailableColumn.setCellFactory(column -> new TableCell<>() {
//             @Override
//             protected void updateItem(Boolean item, boolean empty) {
//                 super.updateItem(item, empty);
//                 if (empty || item == null) {
//                     setText(null);
//                 } else {
//                     setText(item ? "Yes" : "No");
//                 }
//             }
//         });

//         // Thiết lập hành động khi click vào dòng trong bảng
//         documentTable.setOnMouseClicked(event -> showDocumentDetails());

//         exitButton.setOnAction(event -> goBack());
//     }

//     private void goBack() {
//         Stage stage = (Stage) exitButton.getScene().getWindow();
//         stage.close(); 
//     }

//     private void showDocumentDetails() {
//         Document selectedDocument = documentTable.getSelectionModel().getSelectedItem();
//         if (selectedDocument != null) {
//             moreInfoPane.getChildren().clear();
//             Label titleLabel = createLabel("Title: " + selectedDocument.getTitle(), 10, 10);
//             Label genreLabel = createLabel("Genre: " + selectedDocument.getGenre(), 10, 30);
//             Label availableLabel = createLabel("Available: " + (selectedDocument.getIsAvailable() ? "Yes" : "No"), 10, 50);

//             moreInfoPane.getChildren().addAll(titleLabel, genreLabel, availableLabel);

//             prevImage.setImage(defaultImage); // Đặt ảnh mặc định
//         }
//     }

//     private Label createLabel(String text, double x, double y) {
//         Label label = new Label(text);
//         label.setLayoutX(x);
//         label.setLayoutY(y);
//         return label;
//     }
// }
