package com.uit.nst95.quanlycuocdidong.FirebaseDB;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by JT on 20/11/2016.
 */

public  class CuuHo {
    private Integer ID;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getNUMBER() {
        return NUMBER;
    }

    public void setNUMBER(String NUMBER) {
        this.NUMBER = NUMBER;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    private String NAME;
    private String NUMBER;


    public CuuHo()
    {}
    public CuuHo(Integer ID, String NAME, String NUMBER)
    {
        this.ID = ID;
        this.NAME = NAME;
        this.NUMBER = NUMBER;
    }

}
