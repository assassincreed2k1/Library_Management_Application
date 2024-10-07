module com.libmgr {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.libmgr to javafx.fxml;
    exports com.libmgr;
}
