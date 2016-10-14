package com.uit.nst95.quanlycuocdidong.DB;

/**
 * Created by JT on 01/10/2016.
 */

public class MessageLog {
    private int _messageId;
    private String _messageDate;
    private String _receiverNumber;
    private int _messageFee;
    private int _messageType;

    public MessageLog()
    {
        _messageId = -1;
        _messageDate = "";
        _receiverNumber = "";
        _messageFee = 0;
        _messageType = -1;
    }

    public MessageLog(String date, String number, int fee, int type)
    {
        _messageId = -1;
        _messageDate = date;
        _receiverNumber = number;
        _messageFee = fee;
        _messageType = type;
    }

    public int get_messageId()
    {
        return _messageId;
    }
    public int get_messageFee()
    {
        return _messageFee;
    }
    public String get_messageDate()
    {
        return _messageDate;
    }
    public String get_receiverNumber()
    {
        return _receiverNumber;
    }
    public int get_messageType(){return _messageType;}

    public void set_messageId(int messageId)
    {
        _messageId = messageId;
    }
    public void set_messageDate(String messageDate)
    {
        _messageDate = messageDate;
    }
    public void set_messageFee(int messageFee)
    {
        _messageFee= messageFee;
    }
    public void set_recieverNumber(String recieverNumber)
    {
        _receiverNumber = recieverNumber;
    }
    public void set_messageType(int type){_messageType = type;}
}
