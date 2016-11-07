package com.uit.nst95.quanlycuocdidong.Manager;

/**
 * Created by QNghia on 07/11/2016.
 */

public class MonthFee {
    public static final String TAG = MonthFee.class.getSimpleName();

    protected String month;
    protected String year;
    //Call variable
    protected String minutes_outerCall;
    protected String minutes_innerCall;
    protected int fee_outerCall;
    protected int fee_innerCall;
    //Message variaable
    protected int number_outerMess;
    protected int number_innerMess;
    protected int fee_outerMess;
    protected int fee_innerMess;

    public MonthFee()
    {

    }
    public MonthFee(String month, String year) {
        this.month = month;
        this.year = year;
    }

    public MonthFee(String month, String year,
                    String minutes_innerCall , int fee_innerCall,
                    String minutes_outerCall, int fee_outerCall,
                    int number_innerMess, int fee_innerMess,
                    int number_outerMess, int fee_outerMess) {
        this.month = month;
        this.year = year;
        this.minutes_innerCall = minutes_innerCall;
        this.fee_innerCall = fee_innerCall;
        this.minutes_outerCall = minutes_outerCall;
        this.fee_outerCall = fee_outerCall;
        this.number_innerMess = number_innerMess;
        this.fee_innerMess = fee_innerMess;
        this.number_outerMess = number_outerMess;
        this.fee_outerMess = fee_outerMess;
    }

    public String getMinutes_outerCall() {
        return minutes_outerCall;
    }

    public void setMinutes_outerCall(String minutes_outerCall) {
        this.minutes_outerCall = minutes_outerCall;
    }

    public String getMinutes_innerCall() {
        return minutes_innerCall;
    }

    public void setMinutes_innerCall(String minutes_innerCall) {
        this.minutes_innerCall = minutes_innerCall;
    }

    public int getFee_outerCall() {
        return fee_outerCall;
    }

    public void setFee_outerCall(int fee_outerCall) {
        this.fee_outerCall = fee_outerCall;
    }

    public int getFee_innerCall() {
        return fee_innerCall;
    }

    public void setFee_innerCall(int fee_innerCall) {
        this.fee_innerCall = fee_innerCall;
    }

    public int getNumber_outerMess() {
        return number_outerMess;
    }

    public void setNumber_outerMess(int number_outerMess) {
        this.number_outerMess = number_outerMess;
    }

    public int getNumber_innerMess() {
        return number_innerMess;
    }

    public void setNumber_innerMess(int number_innerMess) {
        this.number_innerMess = number_innerMess;
    }

    public int getFee_outerMess() {
        return fee_outerMess;
    }

    public void setFee_outerMess(int fee_outerMess) {
        this.fee_outerMess = fee_outerMess;
    }

    public int getFee_innerMess() {
        return fee_innerMess;
    }

    public void setFee_innerMess(int fee_innerMess) {
        this.fee_innerMess = fee_innerMess;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}

