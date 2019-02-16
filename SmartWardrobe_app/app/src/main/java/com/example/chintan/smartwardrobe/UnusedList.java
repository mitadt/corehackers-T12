package com.example.chintan.smartwardrobe;

import java.util.ArrayList;

public class UnusedList
{
    public static ArrayList<String> clothid;
    public static ArrayList<String> clothtype;

    public static ArrayList<String> getClothid() {
        return clothid;
    }

    public static void setClothid(ArrayList<String> clothid) {
        UnusedList.clothid = clothid;
    }

    public static ArrayList<String> getClothtype() {
        return clothtype;
    }

    public static void setClothtype(ArrayList<String> clothtype) {
        UnusedList.clothtype = clothtype;
    }
}
