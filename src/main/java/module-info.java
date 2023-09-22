module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires javafx.media;
    
    
    opens org.example.view to javafx.fxml;
    opens org.example.model to com.google.gson;
    exports org.example.view;
}