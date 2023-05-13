package ru.anafro.finch.finchrobotproject;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static javafx.scene.input.KeyCode.*;

public class FinchController {
    private static final Finch finch = HelloApplication.finch;

    private static boolean activeUp = false;
    private static boolean activeDown = false;
    private static boolean activeLeft = false;
    private static boolean activeRight = false;
    public static int speed = 10; // Начальная скорость

    public FinchController(Scene scene) {
        finch.setMotors(0, 0); // Начальное состояние: робот стоит на месте

        scene.setOnKeyPressed(this::keyPressed);
        scene.setOnKeyReleased(this::keyReleased);

        finch.setBeak(0, 255, 0); // Зеленый светодиод для обозначения работы робота

    }

    public static void CheckDistance() {
        int distance =  finch.getDistance();
        if(distance < 50 && distance > 0) {
            finch.setBeak(0,0, 255);
            finch.playNote(64, 1);
        } else {
            finch.setBeak(0,255, 0);
        }
    }

    public void keyPressed(KeyEvent e) {
        KeyCode code = e.getCode();
        if(code == W || code == S || code == A || code == D) {
            if(code == W)
                activeUp = true;
            else if(code == S)
                activeDown = true;
            else if(code == A)
                activeLeft = true;
            else
                activeRight = true;
            UpdateSpeed();
            e.consume();
        } else if(code == Q || code == E) {
            if(code == Q) {
                speed = Math.min(100, speed + 10); // Увеличение скорости на 10%
            } else {
                speed = Math.max(0, speed - 10); // Уменьшение скорости на 10%
            }
            UpdateSpeed();
            e.consume();
        }
    }

    public void UpdateSpeed() {
        int sLeft = 0;
        int sRight = 0;
        int count = 0;
        if(activeUp) { // Движение вперед
            sLeft += speed;
            sRight += speed;
            count++;
        }

        if(activeDown) { // Движение назад
            sLeft += -speed;
            sRight += -speed;
            count++;
        }

        if(activeLeft) { // Поворот влево
            sLeft += -Math.min(20, speed);
            sRight += Math.min(20, speed);
            count++;
        }

        if(activeRight) { // Поворот вправо
            sLeft += Math.min(20, speed);
            sRight += -Math.min(20, speed);
            count++;
        }

        if(count > 0) {
            sLeft = Math.ceilDiv(sLeft, count);
            sRight = Math.ceilDiv(sRight, count);
        }

        finch.setMotors(sLeft, sRight);
    }

    public void keyReleased(KeyEvent e) {
        KeyCode code = e.getCode();
        if(code == W || code == A || code == S || code == D) {
            if(code == W)
                activeUp = false;
            else if(code == S)
                activeDown = false;
            else if(code == A)
                activeLeft = false;
            else
                activeRight = false;
            UpdateSpeed();
            e.consume();
        }
    }
}