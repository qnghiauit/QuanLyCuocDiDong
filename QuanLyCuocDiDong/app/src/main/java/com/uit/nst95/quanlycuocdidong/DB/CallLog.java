package com.uit.nst95.quanlycuocdidong.DB;

/**
 * Created by JT on 01/10/2016.
 */

public class CallLog {
    private int _callId;
    private String _callDate;
    private String _callNumber;
    private int _callDuration;
    private int _callFee;
    private int _callType;
    public CallLog()
    {
        _callId = -1;
        _callDate = "";
        _callNumber = "";
        _callDuration = 0;
        _callFee = 0;
        _callType = -1;
    }
    public CallLog(int id, String date, String number, int duration, int fee, int callType)
    {
        _callId = id;
        _callDate = date;
        _callNumber = number;
        _callDuration = duration;
        _callFee = fee;
        _callType = callType;
    }
    public int get_callId()
    {
        return _callId;
    }
    public String get_callDate()
    {
        return _callDate;
    }
    public String get_callNumber()
    {
        return _callNumber;
    }
    public int get_callDuration()
    {
        return _callDuration;
    }
    public int get_callFee()
    {
        return _callFee;
    }
    public int get_callType(){ return _callType;}

    public void set_callId(int callId)
    {
        _callId = callId;
    }
    public void set_callDate(String callDate)
    {
        _callDate = callDate;
    }
    public void set_callNumber(String callNumber)
    {
        _callNumber = callNumber;
    }
    public void set_callDuration(int callDuration)
    {
        _callDuration = callDuration;
    }
    public void set_callFee(int callFee)
    {
        _callFee = callFee;
    }
    public void set_callType(int callType) {_callType = callType;}
}
