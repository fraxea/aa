package org.example.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.example.controller.MainController;
import org.example.model.UserDatabase;

import java.io.IOException;

public class ScoreboardMenuController {
    private final MainController controller = MainController.getInstance();
    @FXML
    private BorderPane root;
    @FXML
    private VBox topTen;
    @FXML
    private ChoiceBox<String> choiceBox;
    
    @FXML
    public void initialize() {
        MainMenuController.blackAndWhite(root);
        controller.initVbox(topTen);
        controller.showTopTen(topTen);
        addListeners();
    }

    private void addListeners() {
        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                if (choiceBox.getItems().get((Integer) number2).equals("highScore"))
                    controller.showTopTen(topTen);
                else controller.showTopTenDegree(topTen);
            }
        });
    }
    
    public void back() throws IOException {
        Main.goToMenu("mainMenu");
    }
}
