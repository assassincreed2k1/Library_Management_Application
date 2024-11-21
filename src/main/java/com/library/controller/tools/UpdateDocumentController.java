package com.library.controller.tools;

import com.library.model.doc.Book;
import com.library.model.doc.Magazine;
import com.library.model.doc.Newspaper;

import com.library.controller.Document.BookController;
import com.library.controller.Document.MagazineController;
import com.library.controller.Document.NewspaperController;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UpdateDocumentController {

    private Object callerController; 
    private Object selectedDocument; 

    @FXML
    private VBox fieldContainer;

    public void setCallerController(Object callerController) {
        this.callerController = callerController;
    }

    public void setSelectedDocument(Object selectedDocument) {
        this.selectedDocument = selectedDocument;
        setupFields();
    }

    private void setupFields() {
        fieldContainer.getChildren().clear(); 

        if (selectedDocument instanceof Book) {
            Book book = (Book) selectedDocument;
            addField("ID:", book.getID(), false);
            addField("Title:", book.getName(), true);
            addField("Author:", book.getAuthor(), true);
            addField("Genre:", book.getGroup(), true);
            addField("Publish Date:", book.getPublishDate(), true);
            addField("ISBN:", book.getISBN(), true);
        } else if (selectedDocument instanceof Magazine) {
            Magazine magazine = (Magazine) selectedDocument;
            addField("ID:", magazine.getID(), false);
            addField("Name:", magazine.getName(), true);
            addField("Genre:", magazine.getGroup(), true);
            addField("Publisher:", magazine.getPublisher(), true);
            addCheckbox("Available:", magazine.getIsAvailable());
        } else if (selectedDocument instanceof Newspaper) {
            Newspaper newspaper = (Newspaper) selectedDocument;
            addField("ID:", newspaper.getID(), false);
            addField("Name:", newspaper.getName(), true);
            addField("Genre:", newspaper.getGroup(), true);
            addField("Source:", newspaper.getSource(), true);
            addField("Region:", newspaper.getRegion(), true);
            addCheckbox("Available:", newspaper.getIsAvailable());
        }
    }

    private void addField(String label, String value, boolean editable) {
        Label fieldLabel = new Label(label);
        TextField textField = new TextField(value);
        textField.setEditable(editable);
        fieldContainer.getChildren().addAll(fieldLabel, textField);
    }

    private void addCheckbox(String label, boolean value) {
        Label fieldLabel = new Label(label);
        CheckBox checkBox = new CheckBox();
        checkBox.setSelected(value);
        fieldContainer.getChildren().addAll(fieldLabel, checkBox);
    }

    @FXML
    private void updateDocument() {
        if (selectedDocument instanceof Book) {
            Book book = (Book) selectedDocument;
            updateBookFields(book);
        } else if (selectedDocument instanceof Magazine) {
            Magazine magazine = (Magazine) selectedDocument;
            updateMagazineFields(magazine);
        } else if (selectedDocument instanceof Newspaper) {
            Newspaper newspaper = (Newspaper) selectedDocument;
            updateNewspaperFields(newspaper);
        }

        if (callerController instanceof BookController) {
            //((BookController) callerController).updateSelectedBook((Book) selectedDocument);
            

        } else if (callerController instanceof MagazineController) {
            //((MagazineController) callerController).updateSelectedMagazine((Magazine) selectedDocument);
        } else if (callerController instanceof NewspaperController) {
            //((NewspaperController) callerController).updateSelectedNewspaper((Newspaper) selectedDocument);
        }

        closeWindow();
    }

    private void updateBookFields(Book book) {
        book.setName(((TextField) fieldContainer.getChildren().get(1)).getText());
        book.setAuthor(((TextField) fieldContainer.getChildren().get(3)).getText());
        book.setGroup(((TextField) fieldContainer.getChildren().get(5)).getText());
        book.setPublishDate(((TextField) fieldContainer.getChildren().get(7)).getText());
        book.setISBN(((TextField) fieldContainer.getChildren().get(9)).getText());
    }

    private void updateMagazineFields(Magazine magazine) {
        magazine.setName(((TextField) fieldContainer.getChildren().get(1)).getText());
        magazine.setGroup(((TextField) fieldContainer.getChildren().get(3)).getText());
        magazine.setPublisher(((TextField) fieldContainer.getChildren().get(5)).getText());
        magazine.setIsAvailable(((CheckBox) fieldContainer.getChildren().get(7)).isSelected());
    }

    private void updateNewspaperFields(Newspaper newspaper) {
        newspaper.setName(((TextField) fieldContainer.getChildren().get(1)).getText());
        newspaper.setGroup(((TextField) fieldContainer.getChildren().get(3)).getText());
        newspaper.setSource(((TextField) fieldContainer.getChildren().get(5)).getText());
        newspaper.setRegion(((TextField) fieldContainer.getChildren().get(7)).getText());
        newspaper.setIsAvailable(((CheckBox) fieldContainer.getChildren().get(9)).isSelected());
    }

    @FXML
    private void closeWindow() {
        ((Stage) fieldContainer.getScene().getWindow()).close();
    }
}
