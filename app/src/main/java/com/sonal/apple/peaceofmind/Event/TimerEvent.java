package com.sonal.apple.peaceofmind.Event;

public class TimerEvent {
    public static int TimeSelected = 1;
    private String timer;

    public TimerEvent(int i, String time) {
        this.TimeSelected = i;
        this.timer = time;
    }

    public int getActionAdd() {
        return TimeSelected;
    }

    public String gettimer() {
        return timer;
    }
}
