package com.uit.nst95.quanlycuocdidong.DB;

/**
 * Created by JT on 01/10/2016.
 */

public class Statistic {
    private int _month;
    private int _year;
    private int _innerCallFee;
    private int _outerCallFee;
    private int _innerMessageFee;
    private int _outerMessageFee;

    private long _innerCallDuration;
    private long _outerCallDuration;
    private int _innerMessageCount;
    private int _outerMessageCount;
    public Statistic() {
        _month = 0;
        _year = 0;
        _innerCallFee = 0;
        _outerCallFee = 0;
        _innerMessageFee = 0;
        _outerMessageFee = 0;
        _innerCallDuration = 0;
        _outerCallDuration = 0;
        _innerMessageCount = 0;
        _outerMessageCount = 0;
    }

    public int get_month() {
        return _month;
    }

    public int get_year() {
        return _year;
    }

    public long get_innerCallDuration(){return _innerCallDuration;}

    public long get_outerCallDuration(){return _outerCallDuration;}

    public int get_innerMessageCount() {return _innerMessageCount;}

    public int get_outerMessageCount() {return _outerMessageCount;
    }
    public int get_innerCallFee() {
        return _innerCallFee;
    }

    public int get_outerCallFee() {
        return _outerCallFee;
    }

    public int get_innerMessageFee() {
        return _innerMessageFee;
    }

    public int get_outerMessageFee() {
        return _outerMessageFee;
    }

    public void set_month(int month) {
        _month = month;
    }

    public void set_year(int year) {
        _year = year;
    }

    public void set_innerCallFee(int innerCallFee) {
        _innerCallFee = innerCallFee;
    }

    public void set_outerCallFee(int outerCallFee)
    {
        _outerCallFee = outerCallFee;
    }
    public void set_innerMessageFee(int innerMessageFee)
    {
        _innerMessageFee = innerMessageFee;
    }
    public void set_outerMessageFee(int outerMessageFee)
    {
        _outerMessageFee = outerMessageFee;
    }

    public void set_innerCallDuration(long innerCallDuration){_innerCallDuration = innerCallDuration;}
    public void set_outerCallDuration(long outerCallDuration) {_outerCallDuration = outerCallDuration;}

    public void set_innerMessageCount(int innerMessageCount) {_innerMessageCount = innerMessageCount;}
    public void set_outerMessageCount (int outerMessageCount) {_outerMessageCount = outerMessageCount;}
}
