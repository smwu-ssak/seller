package com.example.ssak.data;

// Customized by SY

public class RegisterTimeData {

    private int image;
    private boolean isOpen;
    private int day;
    private String startTime;
    private String endTime;

    public RegisterTimeData(int image, boolean isOpen, int day, String startTime, String endTime) {
        this.image = image;
        this.isOpen = isOpen;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getImage() { return image; }

    public boolean getIsOpen() { return isOpen; }

    public int getDay() { return day; }

    public String getStartTime() { return startTime; }

    public String getEndTime() { return endTime; }

    public void setImage(int image) { this.image = image; }

    public void setIsOpen(boolean isOpen) { this.isOpen = isOpen; }

    public void setDay(int day) { this.day = day; }

    public void setStartTime(String startTime) { this.startTime = startTime; }

    public void setEndTime(String endTime) { this.endTime = endTime; }

}
