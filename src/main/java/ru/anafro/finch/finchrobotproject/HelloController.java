package ru.anafro.finch.finchrobotproject;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    private static final Finch finch = HelloApplication.finch;

    public ColorController colorController = new ColorController();
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
    Circle TopCircleColor2 = new Circle();
    @FXML
    Circle TopCircleColor3 = new Circle();
    @FXML
    Circle TopCircleColor4 = new Circle();
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
        Thread thread = new Thread(this::UpdateStats);
        Thread thread2 = new Thread(this::ColorPickerStuff);
        Thread thread3 = new Thread(this::TurnOffLight);
        thread.start();
        thread2.start();
        thread3.start();
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
     * this is just for taking the value of the colorpicker
     * need to change the ui because basically one of the button
     * is useless.
     * theres only beak (front) light and tail (top) light
     */
    public void ColorPickerStuff () {
        LeftColorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Color LeftCurrentColor = LeftColorPicker.getValue();
                LeftCircleColor.setFill(LeftCurrentColor);
                colorController.SetFrontLight(LeftCurrentColor);
            }
        });
        RightColorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Color RightCurrentColor = RightColorPicker.getValue();
                RightCircleColor.setFill(RightCurrentColor);
                colorController.SetBackLight(RightCurrentColor);
            }
        });
        TopColorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Color TopCurrentColor = TopColorPicker.getValue();
                TopCircleColor1.setFill(TopCurrentColor);
                TopCircleColor2.setFill(TopCurrentColor);
                TopCircleColor3.setFill(TopCurrentColor);
                TopCircleColor4.setFill(TopCurrentColor);
            }
        });
        AllColorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Color CurrentColor = AllColorPicker.getValue();
                RightCircleColor.setFill(CurrentColor);
                LeftCircleColor.setFill(CurrentColor);
                TopCircleColor1.setFill(CurrentColor);
                TopCircleColor2.setFill(CurrentColor);
                TopCircleColor3.setFill(CurrentColor);
                TopCircleColor4.setFill(CurrentColor);
                RightColorPicker.setValue(CurrentColor);
                LeftColorPicker.setValue(CurrentColor);
                colorController.SetBackLight(CurrentColor);
                colorController.SetFrontLight(CurrentColor);
            }
        });
    }

    /**
     * turn off light button
     */
    public void TurnOffLight () {
        TurnOffButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                colorController.TurnOffLight();
            }
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