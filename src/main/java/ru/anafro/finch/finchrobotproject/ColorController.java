package ru.anafro.finch.finchrobotproject;


import javafx.scene.paint.Color;

import static ru.anafro.finch.finchrobotproject.WindowApplication.finch;

public class ColorController {

    public void SetFrontLight (Color currentColor) {
        //the color is from the ui is between 0 and 1
        double Red = Math.round((100)*currentColor.getRed());
        double Green = Math.round((100)*currentColor.getGreen());
        double Blue = Math.round((100)*currentColor.getBlue());
        finch.setBeak((int)Red,(int)Green,(int)Blue);
        /*color test stuff
        System.out.println("R : " + LeftCurrentColor.getRed() + " G : " + LeftCurrentColor.getGreen() + " B : " + LeftCurrentColor.getBlue());
        System.out.println("R : " + (int)Red + " G : " + (int)Green + " B : " + (int)Blue + "\n");
        */
    }

    public void SetBackLight (Color currentColor) {
        //the color is from the ui is between 0 and 1
        double Red = Math.round((100)*currentColor.getRed());
        double Green = Math.round((100)*currentColor.getGreen());
        double Blue = Math.round((100)*currentColor.getBlue());
        finch.setTail("all", (int)Red,(int)Green,(int)Blue);
    }

    public void TurnOffLight () {
        finch.setBeak(0, 0, 0);
        finch.setTail("all",0, 0, 0);
    }
}
