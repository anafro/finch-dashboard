package ru.anafro.finch.finchrobotproject;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import static ru.anafro.finch.finchrobotproject.FinchStats.*;

public class WindowController implements Initializable {

    private static final Finch finch = WindowApplication.finch;

    public ColorController colorController = new ColorController();
    @FXML
    public Line distanceLine;
    @FXML
    public Arc arc;
    @FXML
    public ProgressBar progressBar;
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

    @FXML
    Circle LeftCircleColor = new Circle();
    @FXML
    Circle RightCircleColor = new Circle();
    @FXML
    Circle TopCircleColor1 = new Circle();
    @FXML
    ColorPicker LeftColorPicker = new ColorPicker();
    @FXML
    ColorPicker RightColorPicker = new ColorPicker();
    @FXML
    ColorPicker TopColorPicker = new ColorPicker();
    @FXML
    ColorPicker AllColorPicker = new ColorPicker();
    @FXML
    Button TurnOffButton = new Button();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Timer timer = new Timer();
        timer.schedule(UpdateStatsUI, 0, 400);
        HookLightUI();
    }

    private final TimerTask UpdateStatsUI = new TimerTask() {
        @Override
        public void run() {
            double startLineY = 141;
            double distanceY = startLineY * (1 - ((double) finchDistance / 125));
            double fSpeed = Math.sqrt(Math.pow(acceleration[0], 2) + Math.pow(acceleration[1], 2) + Math.pow(acceleration[2], 2));

            Platform.runLater(() -> {
                labelLightLeft.setText("Light Left: " + finchLightLeft + "%");
                labelLightRight.setText("Light Right: " + finchLightRight + "%");

                labelCompass.setText("Compass: " + finchAngle + " degrees");
                arc.setRotate(finchAngle);

                labelDistance.setText("Distance to obstacle: " + finchDistance + " cm");
                distanceLine.setStartY(distanceY);
                distanceLine.setEndY(distanceY);

                labelSpeed.setText("Speed: " + fSpeed + " value");

                progressBar.setProgress(FinchController.speed / 100.0); //[0-1]
            });
        }
    };

    public void HookLightUI() {
        LeftColorPicker.setOnAction(actionEvent -> {
            Color LeftCurrentColor = LeftColorPicker.getValue();
            LeftCircleColor.setFill(LeftCurrentColor);
            colorController.SetFrontLight(LeftCurrentColor);
        });

        RightColorPicker.setOnAction(actionEvent -> {
            Color RightCurrentColor = RightColorPicker.getValue();
            RightCircleColor.setFill(RightCurrentColor);
            colorController.SetBackLight(RightCurrentColor);
        });

        TopColorPicker.setOnAction(actionEvent -> {
            Color TopCurrentColor = TopColorPicker.getValue();
            TopCircleColor1.setFill(TopCurrentColor);
        });

        AllColorPicker.setOnAction(actionEvent -> {
            Color CurrentColor = AllColorPicker.getValue();
            RightCircleColor.setFill(CurrentColor);
            LeftCircleColor.setFill(CurrentColor);
            TopCircleColor1.setFill(CurrentColor);
            RightColorPicker.setValue(CurrentColor);
            LeftColorPicker.setValue(CurrentColor);
            colorController.SetBackLight(CurrentColor);
            colorController.SetFrontLight(CurrentColor);
        });

        TurnOffButton.setOnAction(actionEvent -> colorController.TurnOffLight());
    }
}