package ru.anafro.finch.finchrobotproject;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.NumberAxis;
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
    ColorPicker AllColorPicker = new ColorPicker();
    @FXML
    Button TurnOffButton = new Button();

    @FXML
    NumberAxis yAxis;

    @FXML
    CheckBox safeModeCheckBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Timer timer = new Timer();
        timer.schedule(UpdateStatsUI, 0, 400);
        HookLightUI();
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(250);
        yAxis.setTickUnit(25);
        WindowApplication.windowController = this;
    }

    private final TimerTask UpdateStatsUI = new TimerTask() {
        @Override
        public void run() {
            double startLineY = 15;
            double distanceY = startLineY + 210 * (1 - ((double) finchDistance / 255));
            if(distanceY < startLineY)
                distanceY = startLineY;

            double finalDistanceY = distanceY;
            Platform.runLater(() -> {
                labelLightLeft.setText("Light Left: " + finchLightLeft + "%");
                labelLightRight.setText("Light Right: " + finchLightRight + "%");
                if(finchAngle >= 0)
                    arc.setRotate(finchAngle);
                if(finchDistance >= 0) {
                    distanceLine.setStartY(finalDistanceY);
                    distanceLine.setEndY(finalDistanceY);
                }
            });
        }
    };

    public void UpdateGearbox() {
        Platform.runLater(() -> {
            progressBar.setProgress(FinchController.speed / 100.0); //[0-1]
        });
    }

    public void HookLightUI() {
        safeModeCheckBox.selectedProperty().addListener(
        (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> FinchController.safeMode = new_val);

        LeftColorPicker.setOnAction(actionEvent -> {
            Color LeftCurrentColor = LeftColorPicker.getValue();
            LeftCircleColor.setFill(LeftCurrentColor);
            ColorController.SetFrontLight(LeftCurrentColor);
        });

        RightColorPicker.setOnAction(actionEvent -> {
            Color RightCurrentColor = RightColorPicker.getValue();
            RightCircleColor.setFill(RightCurrentColor);
            ColorController.SetBackLight(RightCurrentColor);
        });

        AllColorPicker.setOnAction(actionEvent -> {
            Color CurrentColor = AllColorPicker.getValue();
            RightCircleColor.setFill(CurrentColor);
            LeftCircleColor.setFill(CurrentColor);
            TopCircleColor1.setFill(CurrentColor);
            RightColorPicker.setValue(CurrentColor);
            LeftColorPicker.setValue(CurrentColor);
            ColorController.SetBackLight(CurrentColor);
            ColorController.SetFrontLight(CurrentColor);
        });

        TurnOffButton.setOnAction(actionEvent -> ColorController.TurnOffLight());
    }
}