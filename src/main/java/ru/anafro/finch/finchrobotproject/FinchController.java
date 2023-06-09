package ru.anafro.finch.finchrobotproject;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Timer;
import java.util.TimerTask;

import static javafx.scene.input.KeyCode.*;
import static ru.anafro.finch.finchrobotproject.FinchStats.finchDistance;
import static ru.anafro.finch.finchrobotproject.WindowApplication.currentScene;
import static ru.anafro.finch.finchrobotproject.WindowApplication.finch;

public class FinchController {

    public static boolean safeMode = false;
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

    private static boolean isShooting = false;
    private static boolean isCheckDistancing = false;
    public static void CheckDistance() {
        if(finchDistance > 0 && finchDistance < 50 && finchDistance < speed) {
            if(isCheckDistancing)
                return;

            finch.setBeak(0,0, 255);
            finch.playNote(64, 1);
            isCheckDistancing = true;
        } else {
            ColorController.SetBackLight(null);
            ColorController.SetFrontLight(null);
            isCheckDistancing = false;
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
            case E: {
                if(speed == Math.min(100, speed + 15))
                    return;

                speed = Math.min(100, speed + 15);
                break;
            } // Увеличение скорости на 10%
            case Q: {
                if(speed == Math.max(10, speed - 15))
                    return;
                speed = Math.max(10, speed - 15);
                break;
            } // Уменьшение скорости на 10%
            case TAB: {
                if(!isShooting) {
                    System.out.println("here");
                    isShooting = true;
                    finch.playNote(90, 1);
                    finch.setBeak(255, 0, 0);
                    final TimerTask StableTail = new TimerTask() {
                        @Override
                        public void run() {
                            finch.setBeak(0, 255, 0);
                        }
                    };
                    timer.schedule(StableTail, 1000);
                }

                e.consume();
                return;
            }
            default: return;
        }
        UpdateSpeed();
        e.consume();
    }

    private static final Timer timer = new Timer();

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
            sLeft -= speed;
            sRight -= speed;
            count++;
        }

        if(activeLeft) { // Поворот влево
            sLeft -= Math.min(20, speed);
            sRight += Math.min(20, speed);
            count++;
        }

        if(activeRight) { // Поворот вправо
            sLeft += Math.min(20, speed);
            sRight -= Math.min(20, speed);
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
        if(code == W || code == A || code == S || code == D || code == TAB) {
            switch (code) {
                case W -> activeUp = false;
                case S -> activeDown = false;
                case A -> activeLeft = false;
                case D -> activeRight = false;
                case TAB -> {
                    isShooting = false;
                    e.consume();
                    return;
                }
            }
            UpdateSpeed();
            e.consume();
        }
    }
}