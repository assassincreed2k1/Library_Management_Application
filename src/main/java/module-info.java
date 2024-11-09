module com.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.json;
    requires javafx.graphics;
    
    opens com.library to javafx.fxml;
    opens com.library.controller to javafx.fxml;
    opens com.library.controller.personController to javafx.fxml;
    
    exports com.library;
    exports com.library.service to com.library;
}
