package com.example.chintan.smartwardrobe;

public class Clothesdata
{
    public static String clothtype,weather,occasion,rfid,color,picpath;
    private  byte[] image;

    public static String get_clothtype() { return clothtype; }

    public static void set_clothtype(String clothtype) { Clothesdata.clothtype = clothtype; }

    public static String get_weather() {
        return weather;
    }

    public static void set_weather(String weather)
    {
        Clothesdata.weather = weather;
    }

    public static String get_occasion() {
        return occasion;
    }

    public static void set_occasion(String occasion)
    {
        Clothesdata.occasion = occasion;
    }

    public static String get_rfid() {
        return rfid;
    }

    public static void set_rfid(String rfid)
    {
        Clothesdata.rfid = rfid;
    }

    public static String getColor() { return color; }

    public static void setColor(String color) { Clothesdata.color = color; }

    public static String getPicpath() { return picpath; }

    public static void setPicpath(String picpath) { Clothesdata.picpath = picpath; }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
