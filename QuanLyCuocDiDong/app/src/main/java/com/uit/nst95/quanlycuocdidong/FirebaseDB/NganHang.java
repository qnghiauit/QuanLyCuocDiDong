package com.uit.nst95.quanlycuocdidong.FirebaseDB;

/**
 * Created by JT on 20/11/2016.
 */

public class NganHang {
    private Integer ID;
    private String NAME;
    private String NUMBER;
    public  NganHang()
    {

    }
    public void setID(Integer ID) {
        this.ID = ID;
    }

    public void setNUMBER(String NUMBER) {
        this.NUMBER = NUMBER;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public Integer getID() {
        return ID;
    }

    public String getNAME() {
        return NAME;
    }

    public String getNUMBER() {
        return NUMBER;
    }

    public NganHang(Integer ID, String NAME, String NUMBER) {
        this.ID = ID;
        this.NAME = NAME;
        this.NUMBER = NUMBER;
    }
}
