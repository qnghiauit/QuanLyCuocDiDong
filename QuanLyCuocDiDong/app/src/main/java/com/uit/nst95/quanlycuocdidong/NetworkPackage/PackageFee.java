package com.uit.nst95.quanlycuocdidong.NetworkPackage;

/**
 * Created by JT on 01/10/2016.
 */
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public abstract class PackageFee implements Serializable
{
    //Attributes
    protected NumberHeaderManager.networkName _myNetwork;
    protected String _outGoingPhoneNumber;
    protected int _callDuration;
    protected int _callFee;
    protected int _messageFee;
    protected  NumberHeaderManager _numberHeader;
    //
    protected String _callTime;
    protected String _sendMessageTime;
    protected int _callBlock;
    protected int _internalCallFee;
    protected int _outerCallFee;
    protected int _internalMessageFee;
    protected int _outerMessageFee;
    //

    protected  int _type;   // 0.Goi noi mang, 1. Goi ngoai mang, 2. Goi so khan cap
    //get methods
    public String get_sendMessageTime(){return this._sendMessageTime;}
    public String get_callTime()
    {
        return this._callTime;
    }

    public String get_outGoingPhoneNumber()
    {
        return this._outGoingPhoneNumber;
    }
    public int get_callDuration()
    {
        return this._callDuration;
    }
    public int get_callFee()
    {
        return this._callFee;
    }
    public int get_messageFee()
    {
        return this._messageFee;
    }
    public int get_type()
    {
        return this._type;
    }
    public NumberHeaderManager.networkName get_myNetwork()
    {
        return this._myNetwork;
    }
    public int get_internalCallFee(){
        return _internalCallFee;
    }
    public int get_outerCallFee() {
        return _outerCallFee;
    }
    public int get_internalMessageFee(){
        return _internalMessageFee;
    }
    public int get_outerMessageFee()
    {
        return _outerMessageFee;
    }



    //set methods
    public void set_sendMessageTime(String messageTime){this._sendMessageTime = messageTime;}
    public void set_callTime(String time)
    {
        this._callTime = time;
    }
    public void NormalizePhoneNumber(String number)
    {
        String result = "";
        if(number.contains("+"))
        {
            result = "0" + number.substring(3);
        }
        if(number.contains("-"))
            result = result.replaceAll("-", "");
        //return result;
    }
    public void set_outGoingPhoneNumber(String number)
    {
        this._outGoingPhoneNumber = number;
        NormalizePhoneNumber(this._outGoingPhoneNumber);
        //this._outGoingPhoneNumber = NormalizePhoneNumber(number);

        if (this._numberHeader.isEmergencyCall(this._outGoingPhoneNumber))
            this._type = 2;
        if (this._numberHeader.isInternalNetwork(this._myNetwork, this._outGoingPhoneNumber))
            this._type = 0;
        else
            this._type = 1;
    }
    public void set_callDuration(int callduration)
    {
        this._callDuration = callduration;
    }
    public void set_callFee(int callFee)
    {
        this._callFee = callFee;
    }
    public void set_messageFee(int messageFee)
    {
        this._messageFee = messageFee;
    }
    public void set_type(int type){this._type = type;}
    public void set_myNetwork(NumberHeaderManager.networkName network){this._myNetwork = network;}
    //Methods
    public PackageFee()
    {
        _callTime = "";
        _sendMessageTime = "";
        _numberHeader = new NumberHeaderManager();

        _outGoingPhoneNumber ="";
        _callDuration = 0;
        _callFee = 0;
        _messageFee = 0;
        _callBlock = 6;
        _type = -1;

    }
    public PackageFee(String outGoingPhoneNumber, int callDuration, int callFee, int messageFee)
    {

        _callTime = "";
        _sendMessageTime = "";
        _numberHeader = new NumberHeaderManager();
        _outGoingPhoneNumber = outGoingPhoneNumber;
        _callDuration = callDuration;
        _callFee = callFee;
        _messageFee = messageFee;
        _callBlock = 6;
        _type = -1;

    }

    public int CalculateCallFee()
    {
        // this.get_type();
        if(this._callDuration == 0)
            return 0;
        if(this._numberHeader.isMobifoneCareCostRequire(this._myNetwork,this._outGoingPhoneNumber))
        {
            if(this._callDuration <= this._callBlock)
            {
                this._callFee = 200/10;
            }
            else
            {
                int remainDuration = this._callDuration - this._callBlock;
                this._callFee = (200/10) + remainDuration*Math.round(((float)200/60));
            }
        }
        if(this._numberHeader.isEmergencyCall(this._outGoingPhoneNumber))
        {
            this._callFee = 0;
            return this._internalCallFee;
        }
        if(this._numberHeader.isInternalNetwork(this._myNetwork, this._outGoingPhoneNumber))
        {
            if(this._callDuration <= this._callBlock)
                this._callFee = this._internalCallFee/10;
            else
            {
                int remainDuration = this._callDuration- this._callBlock;
                this._callFee = (this._internalCallFee/10) + remainDuration*Math.round(((float)this._internalCallFee/60));
            }
        }
        else
        {
            if(this._callDuration <= this._callBlock)
                this._callFee  = this._outerCallFee/10;
            else
            {
                int remainDuration = this._callDuration - this._callBlock;
                this._callFee  = (this._outerCallFee/10) + remainDuration*Math.round(((float)this._outerCallFee/60));
            }
        }
        return this._callFee;
    }
    public boolean isSpecialTime()
    {
        try
        {
            String timeRange1 = "6:00";

            Date time1 = new SimpleDateFormat("HH:mm").parse(timeRange1);
            Calendar c1 = Calendar.getInstance();
            c1.setTime(time1);

            String timeRange2 = "8:00";
            Date time2 = new SimpleDateFormat("HH:mm").parse(timeRange2);
            Calendar c2 = Calendar.getInstance();
            c2.setTime(time2);
            c2.add(Calendar.DATE, 1);
            String timeRange3 = "12:00";
            Date time3 = new SimpleDateFormat("HH:mm").parse(timeRange3);
            Calendar c3= Calendar.getInstance();
            c3.setTime(time3);
            c3.add(Calendar.DATE, 1);

            String timeRange4 = "13:00";
            Date time4 = new SimpleDateFormat("HH:mm").parse(timeRange4);
            Calendar c4 = Calendar.getInstance();
            c4.setTime(time4);
            c4.add(Calendar.DATE, 1);

            Date timeSpan = new SimpleDateFormat("HH:mm").parse(_callTime);
            Calendar c5 = Calendar.getInstance();
            c5.setTime(timeSpan);
            c5.add(Calendar.DATE, 1);
            Date compareDate = c5.getTime();

            if(compareDate.compareTo(c1.getTime()) == 0)
                return true;
            if(compareDate.compareTo(c2.getTime()) == 0)
                return true;
            if(compareDate.compareTo(c3.getTime()) == 0)
                return true;
            if(compareDate.compareTo(c4.getTime()) == 0)
                return true;
            if(compareDate.after(c1.getTime()) && compareDate.before(c2.getTime()))
                return true;
            if(compareDate.after(c3.getTime()) && compareDate.before(c4.getTime()))
                return true;



        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }
        return false;

    }
    public int CalculateMessageFee()
    {
        //this.set_type(this.get_type());
        if(this._numberHeader.isInternalNetwork(this._myNetwork, this._outGoingPhoneNumber))
        {
            return this._internalMessageFee;
        }
        else
            return this._outerMessageFee;

    }

}