package com.uit.nst95.quanlycuocdidong.FirebaseDB;

/**
 * Created by JT on 20/11/2016.
 */

public class Data3GPackage {
    private Integer id;
    private Integer id_provider;
    private Integer number_receive;
    private String id_3gpackage;
    private String data_highspeed;
    private String charges_arise;
    private String expiry_date;
    private String fee;
    private String syntax_reg;
    public Data3GPackage(){}
    public Data3GPackage(Integer id, Integer id_provider, Integer number_receive, String id_3gpackage, String data_highspeed, String charges_arise, String expiry_date, String fee, String syntax_reg) {
        this.id = id;
        this.id_provider = id_provider;
        this.number_receive = number_receive;
        this.id_3gpackage = id_3gpackage;
        this.data_highspeed = data_highspeed;
        this.charges_arise = charges_arise;
        this.expiry_date = expiry_date;
        this.fee = fee;
        this.syntax_reg = syntax_reg;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_provider() {
        return id_provider;
    }

    public void setId_provider(Integer id_provider) {
        this.id_provider = id_provider;
    }

    public Integer getNumber_receive() {
        return number_receive;
    }

    public void setNumber_receive(Integer number_receive) {
        this.number_receive = number_receive;
    }

    public String getId_3gpackage() {
        return id_3gpackage;
    }

    public void setId_3gpackage(String id_3gpackage) {
        this.id_3gpackage = id_3gpackage;
    }

    public String getData_highspeed() {
        return data_highspeed;
    }

    public void setData_highspeed(String data_highspeed) {
        this.data_highspeed = data_highspeed;
    }

    public String getCharges_arise() {
        return charges_arise;
    }

    public void setCharges_arise(String charges_arise) {
        this.charges_arise = charges_arise;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expery_date) {
        this.expiry_date = expery_date;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getSyntax_reg() {
        return syntax_reg;
    }

    public void setSyntax_reg(String syntax_reg) {
        this.syntax_reg = syntax_reg;
    }
}
