package ru.anafro.finch.finchrobotproject;


import javafx.scene.paint.Color;

import static ru.anafro.finch.finchrobotproject.WindowApplication.finch;

public class ColorController {

    private static int[] GetUIColor(Color colorPicker) {
        //the color is from the ui is between 0 and 1
        return new int[] {
                (int) Math.round(100 * colorPicker.getRed()),
                (int) Math.round(100 * colorPicker.getGreen()),
                (int) Math.round(100 * colorPicker.getBlue())
        };
    }

    private static int[] FrontLight = new int[] {0, 255, 0};
    public static void SetFrontLight (Color colorPicker) {
        if(colorPicker != null)
            FrontLight = GetUIColor(colorPicker);
        finch.setBeak(FrontLight[0], FrontLight[1], FrontLight[2]);
    }

    private static int[] BackLight = new int[] {0, 0, 0};
    public static void SetBackLight (Color colorPicker) {
        if(colorPicker != null)
            BackLight = GetUIColor(colorPicker);
        finch.setTail("all", BackLight[0], BackLight[1], BackLight[2]);
    }

    public static void TurnOffLight () {
        finch.setBeak(0, 0, 0);
        finch.setTail("all",0, 0, 0);
    }
}
