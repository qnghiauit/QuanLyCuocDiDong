package com.uit.nst95.quanlycuocdidong.Manager;

/**
 * Created by QNghia on 07/11/2016.
 */

public class DayFee extends MonthFee{
    private String day;
    public DayFee()
    {

    }
    public DayFee(int day, int month, int year)
    {
        this.day = String.valueOf(day);
        this.month = String.valueOf(month);
        this.year = String.valueOf(year);
        fee_innerCall = 0;
        fee_outerCall = 0;
        number_innerMess = 0;
        fee_innerMess = 0;
        number_outerMess = 0;
        fee_outerMess = 0;
    }
    public DayFee(String day, String month, String year,
                  String minutes_innerCall , int fee_innerCall,
                  String minutes_outerCall, int fee_outerCall,
                  int number_innerMess, int fee_innerMess,
                  int number_outerMess, int fee_outerMess) {
        super(month,year,minutes_innerCall,fee_innerCall,minutes_outerCall,fee_outerCall,
                number_innerMess,fee_innerMess,number_outerMess,fee_outerMess);
        this.day = day;
    }
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void addFee_innerCall(int fee)
    {
        fee_innerCall+= fee;
    }
    public void addFee_outerCall(int fee)
    {
        fee_outerCall += fee;
    }
    public void countUpNumber_innerMess()
    {
        number_innerMess++;
    }
    public void countUpNumber_outerMess()
    {
        number_outerMess++;
    }
    public void addFee_innerMess(int fee)
    {
        fee_innerMess += fee;
    }
    public void addFee_outerMess(int fee)
    {
        fee_outerMess += fee;
    }
}
