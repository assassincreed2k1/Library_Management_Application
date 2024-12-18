module com.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive java.sql;
    requires org.apache.pdfbox;
    requires com.google.zxing;
    requires com.google.zxing.javase;

    requires transitive org.json;
    requires transitive javafx.graphics;
    opens com.library.model.loanDoc to javafx.base;
    
    opens com.library to javafx.fxml;
    opens com.library.controller.Menu to javafx.fxml;
    opens com.library.controller.Library to javafx.fxml;
    opens com.library.controller.personController to javafx.fxml;
    opens com.library.controller.tools to javafx.fxml;
    opens com.library.controller.Document to javafx.fxml;
    opens com.library.model.doc to javafx.base;
    

    exports com.library;
    exports com.library.model.doc to com.library.service;
    exports com.library.model.Person to com.library.service;
    exports com.library.service to com.library;

}
