package org.example.view;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

import org.example.view.music.Music;
import org.example.view.music.Soundtrack;

public class Main extends Application {
    private static Scene scene;
    private static Stage stage;
    
    @Override
    public void start(Stage stage) throws Exception {
        Main.stage = stage;
        goToMenu("registerMenu");
        Music.LOGIN_MENU.getMediaPlayer().play();
        stage.setTitle("aa");
        stage.getIcons().add(new Image(getClass().getResource("/images/icon.png").toExternalForm()));
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }

    public static void goToMenu(String fxmlFileName) throws IOException {
        URL url = Main.class.getResource("/fxml/" + fxmlFileName + ".fxml");
        BorderPane borderPane = FXMLLoader.load(url);
        setScene(new Scene(borderPane));
    }

    public static void setScene(Scene scene) {
        scene.setOnMouseClicked((EventHandler<Event>) event -> Soundtrack.BUTTON_CLICK.getAudioClip().play());
        Main.scene = scene;
        stage.setScene(scene);
    }

    public static Scene getScene() {
        return scene;
    }

    public static Stage getStage() {
        return stage;
    }

    public static Alert getConfirmationAlert(String headerText, String contentText) {
        ButtonType yesButton = new ButtonType("Yes", ButtonData.YES);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonData.NO);
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setGraphic(null);
        alert.getDialogPane().getStylesheets().add(Main.class.getResource("/css/exit.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("dialog-pane");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(yesButton, cancelButton);
        return alert;
    }

}
