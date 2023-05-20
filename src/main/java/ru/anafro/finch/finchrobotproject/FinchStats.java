package ru.anafro.finch.finchrobotproject;

import java.util.Timer;
import java.util.TimerTask;

public class FinchStats {
    private static final int timeForTick = 200; //ms
    private static final Finch finch = WindowApplication.finch;
    public static int finchLightLeft = 0;
    public static int finchLightRight = 0;
    public static int finchAngle = 0;
    public static int finchDistance = 0;
    public static double[] acceleration = new double[]{0, 0, 0};

    public static void StartDataCollect() {
        Timer timer = new Timer();
        timer.schedule(UpdateStats, 0, timeForTick); //call the run() method at 1 second intervals
    }

    private static final TimerTask UpdateStats = new TimerTask() {
        @Override
        public void run() {
            if (finch == null || !finch.isConnectionValid())
                return;

            finchLightLeft = finch.getLight("Left");
            finchLightRight = finch.getLight("Right");
            finchAngle = finch.getCompass();
            finchDistance = finch.getDistance();
            acceleration = finch.getAcceleration();
        }
    };
}
