package ru.anafro.finch.finchrobotproject;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    private static final Finch finch = HelloApplication.finch;

    @FXML
    private Label labelLightLeft;
    @FXML
    private Label labelLightRight;
    @FXML
    private Label labelCompass;
    @FXML
    private Label labelDistance;
    @FXML
    private Label labelSpeed;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Thread thread = new Thread(this::UpdateStats);
        thread.start();
    }

    public void UpdateStats() {
        Platform.runLater(() -> {
            if (finch == null || !finch.isConnectionValid()) {
                Sleep(1000);
                UpdateStats();
                return;
            }

            labelLightLeft.setText("Light Left: " + finch.getLight("Left") + "%");
            labelLightRight.setText("Light Right: " + finch.getLight("Right") + "%");
            labelCompass.setText("Compass: " + finch.getCompass() + " degrees");
            labelDistance.setText("Distance to obstacle: " + finch.getDistance() + " cm");

            double[] acceleration = finch.getAcceleration();
            double fSpeed = Math.sqrt(Math.pow(acceleration[0], 2) + Math.pow(acceleration[1], 2) + Math.pow(acceleration[2], 2));
            labelSpeed.setText("Speed: " + fSpeed + " value");

            Sleep(200);
            UpdateStats();
        });
    }

    /**
     * @param time time for sleep in milliseconds
     */
    static void Sleep(int time)
    {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.out.println("Thread has been interrupted");
        }
    }
}