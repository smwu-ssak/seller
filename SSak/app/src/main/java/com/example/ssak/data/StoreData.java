package com.example.ssak.data;

// Customized by SY

public class StoreData {

    private String name;
    private String address;
    private float lat;
    private float log;
    private String tel;
    private StoreOperatingTimeData time[];

    public String getName() { return name; }

    public String getAddress() { return address; }

    public float getLat() { return lat; }

    public float getLog() { return log; }

    public String getTel() { return tel; }

    public StoreOperatingTimeData[] getStoreOperatingTimeData() { return time; }

    public void setName(String name) { this.name = name; }

    public void setAddress(String address) { this.address = address; }

    public void setLat(float lat) { this.lat = lat; }

    public void setLog(float log) { this.log = log; }

    public void setTel(String tel) { this.tel = tel; }

    public void setTime(StoreOperatingTimeData[] time) { this.time = time; }

}
