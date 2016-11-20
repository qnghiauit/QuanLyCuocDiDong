package com.uit.nst95.quanlycuocdidong.FirebaseDB;

/**
 * Created by JT on 20/11/2016.
 */

public class Taxi {
    private Integer ID;
    private String NAME;
    private String NUMBER;
    private Integer REGION;
    public Taxi(){}
    public Taxi(Integer ID, String NAME, String NUMBER, Integer REGION) {
        this.ID = ID;
        this.NAME = NAME;
        this.NUMBER = NUMBER;
        this.REGION = REGION;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getNUMBER() {
        return NUMBER;
    }

    public void setNUMBER(String NUMBER) {
        this.NUMBER = NUMBER;
    }

    public Integer getREGION() {
        return REGION;
    }

    public void setREGION(Integer REGION) {
        this.REGION = REGION;
    }
}
