module com.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires transitive org.json;
    requires transitive javafx.graphics;
    
    opens com.library to javafx.fxml;
    opens com.library.controller to javafx.fxml;
    opens com.library.controller.personController to javafx.fxml;
    opens com.library.controller.tools to javafx.fxml;
    opens com.library.controller.Document to javafx.fxml;
    opens com.library.model.doc to javafx.base;

    exports com.library;
    exports com.library.model.doc to com.library.service;
    exports com.library.model.Person to com.library.service;
    exports com.library.service to com.library;

}
