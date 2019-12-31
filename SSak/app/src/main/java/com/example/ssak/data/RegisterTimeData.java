package com.example.ssak.data;

// Customized by SY

public class RegisterTimeData {

    public int image;
    public boolean isOpen;
    public int day;
    public String startTime;
    public String endTime;

    public RegisterTimeData(int image, boolean isOpen, int day, String startTime, String endTime) {
        this.image = image;
        this.isOpen = isOpen;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

}
