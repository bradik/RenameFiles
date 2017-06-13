package com.bradik.RenameFiles.Timer;

/**
 * Created by Brad on 08.04.2017.
 *
 */
public class Progress {

    private int min_value;
    private int max_value;
    private int value;
    private int percent;
    private boolean needPassedTime = true;
    private boolean needLeftTime = true;
    private String format = "progress: {percent}% ({passedTime}|{leftTime})";
    private long startTime;

    public Progress(int max_value) {
        this.min_value = 1;
        this.max_value = max_value;
        value = min_value - 1;
    }

    public Progress(int min_value, int max_value) {
        this.min_value = min_value;
        this.max_value = max_value;
        value = min_value - 1;
    }

    public Progress(int min_value, int max_value, int value) {
        this.min_value = min_value;
        this.max_value = max_value;
        this.value = value;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format     = format;
        needLeftTime    = format.contains("{leftTime}");
        needPassedTime  = format.contains("{needPassedTime}");
    }

    public Progress add(int inc){
        value+=inc;
        if (value==min_value)
            startTime = System.currentTimeMillis();
        return this;
    }

    public Progress add(){
        return add(1);
    }

    public void show(){

        int percent = value*100/(max_value-min_value+1);

        if (this.percent!=percent){
            this.percent=percent;
            System.out.print("\r");

            long passedTime = System.currentTimeMillis() - startTime;
            long leftTime = max_value*passedTime/value;

            String str = this.format;
            if (needPassedTime)
                str = str.replace("{passedTime}", Timer.formatTimer(passedTime));

            if (needLeftTime)
                str = str.replace("{leftTime}",Timer.formatTimer(leftTime));

            if (str.contains("{percent}"))
                str = str.replace("{percent}",""+percent);

            System.out.print(str);
        }
    }
}
