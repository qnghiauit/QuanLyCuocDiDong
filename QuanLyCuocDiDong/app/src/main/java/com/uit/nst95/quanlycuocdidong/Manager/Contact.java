package com.uit.nst95.quanlycuocdidong.Manager;

/**
 * Created by QNghia on 17/10/2016.
 */

public class Contact {
    private String _name;
    private String _phoneNumber;

    public Contact(){}
    public Contact(String name, String phoneNumber) {
        _name = name;
        _phoneNumber = phoneNumber;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_phoneNumber() {
        return _phoneNumber;
    }

    public void set_phoneNumber(String _phoneNumber) {
        this._phoneNumber = _phoneNumber;
    }
}