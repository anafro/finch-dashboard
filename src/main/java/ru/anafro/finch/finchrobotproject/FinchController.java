package ru.anafro.finch.finchrobotproject;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import static javafx.scene.input.KeyCode.*;
import static ru.anafro.finch.finchrobotproject.FinchStats.finchDistance;
import static ru.anafro.finch.finchrobotproject.WindowApplication.currentScene;

public class FinchController {
    private static final Finch finch = WindowApplication.finch;

    private static boolean activeUp = false;
    private static boolean activeDown = false;
    private static boolean activeLeft = false;
    private static boolean activeRight = false;
    public static int speed = 10;

    public static boolean lateLoad = false;
    public static void Start() {
        if(finch != null) {
            finch.setMotors(0, 0); // Начальное состояние: робот стоит на месте
            finch.setBeak(0, 255, 0); // Зеленый светодиод для обозначения работы робота
            lateLoad = true;
        }

        //Collect finch stats
        FinchStats.StartDataCollect();

        currentScene.setOnKeyPressed(FinchController::keyPressed);
        currentScene.setOnKeyReleased(FinchController::keyReleased);
    }

    //TODO
    public static void CheckDistance() {
        if(finchDistance < 50 && finchDistance > 0) {
            finch.setBeak(0,0, 255);
            finch.playNote(64, 1);
        } else {
            finch.setBeak(0,255, 0);
        }
    }

    public static void keyPressed(KeyEvent e) {
        if(finch == null)
            return;

        if(lateLoad) {
            finch.setBeak(0, 255, 0); // Зеленый светодиод для обозначения работы робота
            lateLoad = false;
        }

        KeyCode code = e.getCode();
        switch (code) {
            case W: {
                if(activeUp) return;
                activeUp = true;
                break;
            }
            case S: {
                if(activeDown) return;
                activeDown = true;
                break;
            }
            case A: {
                if(activeLeft) return;
                activeLeft = true;
                break;
            }
            case D: {
                if(activeRight) return;
                activeRight = true;
                break;
            }
            case Q: {
                if(speed == Math.min(100, speed + 15))
                    return;

                speed = Math.min(100, speed + 15);
                break;
            } // Увеличение скорости на 10%
            case E: {
                if(speed == Math.max(10, speed - 15))
                    return;
                speed = Math.max(10, speed - 15);
                break;
            } // Уменьшение скорости на 10%
            default: return;
        }
        UpdateSpeed();
        e.consume();
    }

    public static void UpdateSpeed() {
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
        //System.out.println("" + activeUp + " " + activeDown + " " + activeLeft + " " + activeRight);
}

    public static void keyReleased(KeyEvent e) {
        if(finch == null)
            return;

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