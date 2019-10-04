package com.dcinspirations.medassistant.models;

public class med {
    public String ailment, drugtype,drugname,dosage,startd,nottime;
    public int id, completed,endd;

    public med(int id, String ailment, String drugtype, String drugname, String dosage,String startd, int endd,String nottime, int completed) {
        this.id = id;
        this.ailment = ailment;
        this.drugtype = drugtype;
        this.drugname = drugname;
        this.dosage = dosage;
        this.startd = startd;
        this.endd = endd;
        this.nottime = nottime;
        this.completed = completed;
    }
}
