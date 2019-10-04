package com.dcinspirations.medassistant;

import android.content.Context;
import android.content.SharedPreferences;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Sp {
    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor spe;
    private Boolean isLoggedIn = false;

    public Sp() {
        sharedPreferences = BaseApp.ctx.getSharedPreferences("default", 0);
        spe = sharedPreferences.edit();
    }

//    public static void addToRecent(file fm) {
//
//        ArrayList<file> filelist = getRecent();
//        if(!filelist.contains(fm)) {
//            if(filelist.size()<3){
//                filelist.add(0, fm);
//            }else{
//                filelist.remove(filelist.size()-1);
//                filelist.add(0,fm);
//            }
//
//        }
//        Gson gson = new Gson();
//        String json = gson.toJson(filelist);
//        spe.putString("recent", json);
//        spe.commit();
//    }

//    public static ArrayList<file> getRecent() {
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString("recent", "");
//        if (json.isEmpty()) {
//            return new ArrayList<>();
//        } else {
//            Type type = new TypeToken<List<file>>() {
//            }.getType();
//            ArrayList<file> arrPackageData = gson.fromJson(json, type);
//            return arrPackageData;
//        }
//    }

    public static void setLoggedIn(boolean state,String name, String email,String disorder, int gender,String key){
        spe.putBoolean("loggedIn",state);
        spe.putString("name",name);
        spe.putString("email",email);
        spe.putString("disorder",disorder);
        spe.putInt("gender",gender);
        spe.putString("key",key);
        spe.commit();
    }
    public static boolean getLoggedIn(){
        boolean li = sharedPreferences.getBoolean("loggedIn",false);
        return li;
    }
    public static String[] getBasics(){
        String name = sharedPreferences.getString("name","");
        String email = sharedPreferences.getString("email","");
        String dis = sharedPreferences.getString("disorder","");
        int gend = sharedPreferences.getInt("gender",0);
        String key = sharedPreferences.getString("key","");
        String[] arr = {name,email,dis,String.valueOf(gend),key};
        return arr;
    }

    public static void addLast(){
        spe.putInt("last",getLast()+1);
        spe.commit();
    }
    public static int getLast(){
        int li = sharedPreferences.getInt("last",0);
        return li;
    }
//    public static void setUInfo(String key,String username,String status){
//        spe.putString("key",key);
//        spe.putString("username",username);
//        spe.putString("status",status);
//        spe.commit();
//    }
//    public static user getUInfo(){
//        String name = sharedPreferences.getString("name","");
//        String key = sharedPreferences.getString("key","");
//        String status = sharedPreferences.getString("status","");
//        user cu = new user(key,name,status);
//        return cu;
//    }

}
