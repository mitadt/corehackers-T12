package com.example.chintan.smartwardrobe;

import java.util.ArrayList;

public class UsedList
{
    public static ArrayList<String> clothid;
    public static ArrayList<String> clothtype;

    public static ArrayList<String> getClothid() {
        return clothid;
    }

    public static void setClothid(ArrayList<String> clothid) {
        UsedList.clothid = clothid;
    }

    public static ArrayList<String> getClothtype() {
        return clothtype;
    }

    public static void setClothtype(ArrayList<String> clothtype) {
        UsedList.clothtype = clothtype;
    }

}
