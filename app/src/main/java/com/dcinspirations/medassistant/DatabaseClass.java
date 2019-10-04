package com.dcinspirations.medassistant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseClass extends SQLiteOpenHelper {

    public DatabaseClass(Context context) {
        super(context, "MedAssistant", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table medications  (id INTEGER PRIMARY KEY AUTOINCREMENT,ailment TEXT,drugtype TEXT,drugname TEXT, dosage TEXT,startdate TEXT,duration INTEGER, nottime TEXT,status INTEGER  )");
        db.execSQL("create table contacts  (id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT, status TEXT, email TEXT, number TEXT )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS  medications");
        db.execSQL("DROP TABLE IF EXISTS  contacts");
        onCreate(db);
    }
    public boolean insertIntoMeds(String ail,String dt,String dn,String dose, String sd, int ed, String nt,int status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ailment", ail);
        cv.put("drugtype", dt);
        cv.put("drugname", dn);
        cv.put("dosage", dose);
        cv.put("startdate", sd);
        cv.put("duration", ed);
        cv.put("nottime", nt);
        cv.put("status",status);
        long result = db.insert("medications",null,cv);
        if(result==-1){
            return true;
        }else{
            return false;
        }
   }
    public boolean updateDur(int id, int duration){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("duration", duration);
        int result = db.update("medications",cv,"id=?",new String[]{String.valueOf(id)});
        if(result==1){
            return true;
        }else{
            return false;
        }
    }
    public boolean updateStatus(int id, int status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("status", status);
        int result = db.update("medications",cv,"id=?",new String[]{String.valueOf(id)});
        if(result==1){
            return true;
        }else{
            return false;
        }
    }
    public Cursor getDur(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("Select duration from medications where id=?", new String[]{String.valueOf(id)} );
    }
    public Cursor getMedData(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("Select * from medications", null );
    }
    public int deleteData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("medications","id=?",new String[]{String.valueOf(id)});
    }


    public boolean insertIntoContacts(String name, String status, String email, String number){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("status", status);
        cv.put("email", email);
        cv.put("number", number);
        long result = db.insert("contacts",null,cv);
        if(result==-1){
            return true;
        }else{
            return false;
        }
    }
    public Cursor getContactData(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("Select * from contacts", null );
    }
    public int deleteContactData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts","id=?",new String[]{String.valueOf(id)});
    }

    public void initDelete(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("contacts",null,null);
        db.delete("medications",null,null);
    }



}
