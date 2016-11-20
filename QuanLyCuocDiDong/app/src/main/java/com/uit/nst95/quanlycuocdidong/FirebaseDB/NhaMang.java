package com.uit.nst95.quanlycuocdidong.FirebaseDB;

/**
 * Created by JT on 20/11/2016.
 */

public class NhaMang {
    private Integer ID;
    private String NAME;
    private String NUMBER;
    public  NhaMang(){}
    public NhaMang(Integer ID, String NAME, String NUMBER) {
        this.ID = ID;
        this.NAME = NAME;
        this.NUMBER = NUMBER;
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
}
