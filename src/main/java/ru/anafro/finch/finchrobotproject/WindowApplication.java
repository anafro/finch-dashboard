package ru.anafro.finch.finchrobotproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowApplication extends Application {
    public static Finch finch = null;
    public static Scene currentScene = null;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WindowApplication.class.getResource("hello-view.fxml"));
        currentScene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setTitle("Finch Robot - Control panel");
        stage.setScene(currentScene);

        // Disabling the split pane division changing:
        SplitPane splitPane = (SplitPane) currentScene.lookup("#split-pane");
        AnchorPane topPane = (AnchorPane) splitPane.getItems().get(0);
        topPane.maxHeightProperty().bind(splitPane.heightProperty().multiply(0.085));
        topPane.minHeightProperty().bind(splitPane.heightProperty().multiply(0.085));

        stage.show();

        InitFinch();
    }

    private static void InitFinch() {
        try {
            finch = new Finch("A"); //TODO Cycle while for await finch
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //Collect finch stats
        FinchStats.StartDataCollect();

        //Control from keyboard
        FinchController.Start();

        //On program end stop finch motors
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(finch != null && finch.isConnectionValid())
                finch.setMotors(0,0);
        }));
    }

    public static void main(String[] args) {
        launch();
    }
}