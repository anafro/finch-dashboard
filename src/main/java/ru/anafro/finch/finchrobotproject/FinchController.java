package ru.anafro.finch.finchrobotproject;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

import static javafx.scene.input.KeyCode.*;

public class FinchController {
    private static final Finch finch = HelloApplication.finch;
    private int speed = 10; // Начальная скорость

    public FinchController(Scene scene) {
        finch.setMotors(0, 0); // Начальное состояние: робот стоит на месте

        scene.setOnKeyPressed(this::keyPressed);
        scene.setOnKeyReleased(this::keyReleased);

        finch.setBeak(0, 255, 0); // Зеленый светодиод для обозначения работы робота
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case UP -> finch.setMotors(speed, speed); // Движение вперед
            case DOWN -> finch.setMotors(-speed, -speed); // Движение назад
            case LEFT -> finch.setMotors(-speed, speed); // Поворот влево
            case RIGHT -> finch.setMotors(speed, -speed); // Поворот вправо
            case Q -> speed = Math.min(100, speed + 10); // Увеличение скорости на 10%
            case E -> speed = Math.max(0, speed - 10); // Уменьшение скорости на 10%
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getCode() == UP ||
                e.getCode() == DOWN ||
                e.getCode() == LEFT ||
                e.getCode() == RIGHT) {
            finch.setMotors(0, 0); // Остановка движения при отпускании стрелок
        }
    }
}