package ru.anafro.finch.finchrobotproject;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Timer;
import java.util.TimerTask;

import static javafx.scene.input.KeyCode.*;
import static ru.anafro.finch.finchrobotproject.ColorController.SwitchLight;
import static ru.anafro.finch.finchrobotproject.FinchStats.finchDistance;
import static ru.anafro.finch.finchrobotproject.WindowApplication.currentScene;
import static ru.anafro.finch.finchrobotproject.WindowApplication.finch;

public class FinchController {

    public static boolean safeMode = false;

    private static final boolean[] btnStatus = new boolean[] {false, false, false, false}; //W, A, S, D
    public static int speed = 10;
    private static final int MAX_SPEED = 100; //[MIN_SPEED - 100]
    private static final int MIN_SPEED = 10; //[0 - MAX_SPEED]
    private static final int STEP_SPEED = 15; //[0 - 100]

    private static boolean isShooting = false;
    private static boolean isAlertDistance = false;
    private static boolean isSwitchSafeMode = false;
    private static boolean isSwitchLights = false;

    private static final Timer timer = new Timer();

    public static void Start() {
        if (finch != null) {
            finch.setMotors(0, 0);
            finch.setBeak(0, 255, 0);
        }

        //Collect finch stats
        FinchStats.StartDataCollect();

        currentScene.setOnKeyPressed(FinchController::keyPressed);
        currentScene.setOnKeyReleased(FinchController::keyReleased);
    }

    public static void CheckDistance() {
        if(!safeMode) return;

        if (finchDistance > 0 && finchDistance < 75) {
            if (isAlertDistance)
                return;

            finch.setBeak(0, 0, 255);
            finch.playNote(64, 1);
            isAlertDistance = true;
        } else {
            ColorController.SetBackLight(null);
            ColorController.SetFrontLight(null);
            isAlertDistance = false;
        }
    }

    public static void keyPressed(KeyEvent e) {
        if (finch == null)
            return;

        KeyCode code = e.getCode();
        switch (code) {
            case W -> {
                e.consume();
                if (btnStatus[0]) return;
                btnStatus[0] = true;
                UpdateSpeed();
            }
            case S -> {
                e.consume();
                if (btnStatus[1]) return;
                btnStatus[1] = true;
                UpdateSpeed();
            }
            case A -> {
                e.consume();
                if (btnStatus[2]) return;
                btnStatus[2] = true;
                UpdateSpeed();
            }
            case D -> {
                e.consume();
                if (btnStatus[3]) return;
                btnStatus[3] = true;
                UpdateSpeed();
            }
            case E -> {
                e.consume();
                if (speed == MAX_SPEED) return;
                WindowApplication.windowController.UpdateGearbox();
                speed = Math.min(MAX_SPEED, speed + STEP_SPEED);
                UpdateSpeed();
            }
            case Q -> {
                e.consume();
                if (speed == MIN_SPEED) return;
                WindowApplication.windowController.UpdateGearbox();
                speed = Math.max(MIN_SPEED, speed - STEP_SPEED);
                UpdateSpeed();
            }
            case F -> {
                e.consume();
                if(isSwitchLights) return;
                isSwitchLights = true;
                SwitchLight();
            }
            case X -> {
                e.consume();
                if(isSwitchSafeMode) return;
                isSwitchSafeMode = true;
                safeMode ^= true;
                if(!safeMode) {
                    ColorController.SetBackLight(null);
                    ColorController.SetFrontLight(null);
                    isAlertDistance = false;
                }
            }
            case TAB -> {
                e.consume();
                if (isShooting) return;

                isShooting = true;
                finch.playNote(90, 1);
                finch.setBeak(255, 0, 0);
                finch.setTail("All",255, 0, 0);
                final TimerTask StableTail = new TimerTask() {
                    @Override
                    public void run() {
                        ColorController.SetFrontLight(null);
                        ColorController.SetBackLight(null);
                    }
                };
                timer.schedule(StableTail, 1000);
            }
        }
    }

    public static void UpdateSpeed() {
        if (btnStatus[0] == btnStatus[1]) {
            finch.setMotors(0, 0);
            return;
        }

        var moveDirection = (btnStatus[0] ? 1 : -1);
        int moveSpeed = moveDirection * speed;
        if (btnStatus[2] == btnStatus[3]) {
            finch.setMotors(moveSpeed, moveSpeed);
            return;
        }

        var rotateSpeed = moveDirection * Math.min(20, speed) * (btnStatus[2] ? -1 : 1);
        finch.setMotors(Math.ceilDiv(moveSpeed + rotateSpeed, 2), Math.ceilDiv(moveSpeed - rotateSpeed, 2));
    }

    public static void keyReleased(KeyEvent e) {
        if (finch == null)
            return;

        KeyCode code = e.getCode();
        switch (code) {
            case W -> btnStatus[0] = false;
            case S -> btnStatus[1] = false;
            case A -> btnStatus[2] = false;
            case D -> btnStatus[3] = false;
            case TAB -> isShooting = false;
            case F -> isSwitchLights = false;
            case X -> isSwitchSafeMode = false;
            default -> {
                return;
            }
        }
        e.consume();

        if (code == W || code == A || code == S || code == D)
            UpdateSpeed();
    }
}