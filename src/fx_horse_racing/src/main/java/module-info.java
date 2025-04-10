module it.priori {
    requires javafx.controls;
    requires javafx.fxml;

    opens it.priori to javafx.fxml;
    exports it.priori;
}
