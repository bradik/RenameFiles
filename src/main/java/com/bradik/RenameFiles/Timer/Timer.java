package com.bradik.RenameFiles.Timer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Brad on 06.04.2017.
 */
public class Timer {

    //TODO: Длительности выполнения операций, Простой таймер
    private static long startTime;
    private static long duration;
    private static boolean isStarted;

    public static long start() {
        startTime = System.currentTimeMillis();
        isStarted = true;
        return startTime;
    }

    public static long stop() {
        if (isStarted) {
            isStarted = !isStarted;
            duration = System.currentTimeMillis() - startTime;
        }
        return duration;
    }

    public static String formatTimer(long duration) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss:S");
        String[] strs = dateFormat.format(new Date(duration)).split(":");
        String outStr = "";
        int i = 0;
        outStr += strs[i].equals("00") ? "" : strs[i] + "m:";
        i++;
        outStr += strs[i].equals("00") ? "" : strs[i] + "s:";
        i++;
        outStr += strs[i] + "ms";

        return outStr;
    }

    public static void stopAndPrint(String prefix) {

        System.out.println(prefix+formatTimer(stop()));

    }

    public static void sleep(long millis) {
        //TODO: sleep пауза
        try {
            Thread.currentThread().sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
