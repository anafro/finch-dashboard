package ru.anafro.finch.finchrobotproject;

import java.util.Timer;
import java.util.TimerTask;

public class FinchStats {
    private static final int timeForTick = 500; //ms
    private static Finch finch;
    public static int finchLightLeft = 0;
    public static int finchLightRight = 0;
    public static int finchAngle = 0;
    public static int finchDistance = 0;
    public static double[] acceleration = new double[]{0, 0, 0};

    public static void StartDataCollect() {
        finch = WindowApplication.finch;
        Timer timer = new Timer();
        timer.schedule(UpdateStats, 400, timeForTick);
    }

    private static final TimerTask UpdateStats = new TimerTask() {
        @Override
        public void run() {
            if (finch != null) {
                finchLightLeft = finch.getLight("Left");
                finchLightRight = finch.getLight("Right");
                finchAngle = finch.getCompass();
                finchDistance = finch.getDistance();
                acceleration = finch.getAcceleration();
            }
        }
    };
}
