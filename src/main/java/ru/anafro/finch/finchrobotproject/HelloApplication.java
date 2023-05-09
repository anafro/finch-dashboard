package ru.anafro.finch.finchrobotproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setTitle("Finch Robot - Control panel");
        stage.setScene(scene);

        // Disabling the split pane division changing:
        SplitPane splitPane = (SplitPane) scene.lookup("#split-pane");
        AnchorPane topPane = (AnchorPane) splitPane.getItems().get(0);
        topPane.maxHeightProperty().bind(splitPane.heightProperty().multiply(0.085));
        topPane.minHeightProperty().bind(splitPane.heightProperty().multiply(0.085));

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}