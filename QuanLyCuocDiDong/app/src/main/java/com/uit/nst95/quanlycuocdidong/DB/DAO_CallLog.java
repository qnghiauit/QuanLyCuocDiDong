package com.uit.nst95.quanlycuocdidong.DB;

/**
 * Created by JT on 01/10/2016.
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.uit.nst95.quanlycuocdidong.Manager.DateTimeManager;

public class DAO_CallLog {
    private SQLiteDatabase _database;
    private DbHelper _dbHelper;
    private DateTimeManager _dateTimeManager;
    private String[] _listColumn = {_dbHelper.CALL_ID, _dbHelper.CALL_DATE,
            _dbHelper.CALL_NUMBER,_dbHelper.DURATION, _dbHelper.CALL_FEE, _dbHelper.CALL_TYPE};

    public DAO_CallLog(Context context)
    {
        _dbHelper = _dbHelper.getInstance(context);
        _dateTimeManager = DateTimeManager.get_instance();
    }
    public void Open() throws  SQLException
    {
        _database = _dbHelper.getWritableDatabase();
        _database = _dbHelper.getReadableDatabase();
    }
    public void Close()
    {
        _dbHelper.close();
    }

    public CallLog CursortoCallLog(Cursor c)
    {
        CallLog row = new CallLog();
        row.set_callId(c.getInt(0));
        Date temp = new Date(c.getLong(1));
        row.set_callDate(temp.toString());
        row.set_callNumber(c.getString(2));
        row.set_callDuration(c.getInt(3));
        row.set_callFee(c.getInt(4));
        row.set_callType(c.getInt(5));
        return row;
    }
    public CallLog GetLastedCallFromDB()
    {
        CallLog row = new CallLog();
        String orderBy = _dbHelper.CALL_DATE + " DESC";
        Cursor cursor = _database.query(_dbHelper.CALL_TABLE,_listColumn,null,null,null,null,orderBy,null);
        if(cursor.moveToFirst())
        {
            row = CursortoCallLog(cursor);
        }
        cursor.close();
        return row;
    }
    public CallLog CreateCallLogRow(String callDate, String callNumber, int duration, int callFee, int callType)
    {

        ContentValues values = new ContentValues();

        long milisecs = _dateTimeManager.convertToMilisec(callDate);
        values.put(_dbHelper.CALL_DATE, milisecs);
        values.put(_dbHelper.CALL_NUMBER, callNumber);
        values.put(_dbHelper.DURATION, duration);
        values.put(_dbHelper.CALL_FEE, callFee);
        values.put(_dbHelper.CALL_TYPE, callType);
        long insertId = _database.insert(_dbHelper.CALL_TABLE,null, values);
        Cursor cursor = _database.query(_dbHelper.CALL_TABLE, _listColumn,_dbHelper.CALL_ID + " = " + insertId,null,null,null,null);
        cursor.moveToFirst();
        CallLog newRow = CursortoCallLog(cursor);
        cursor.close();
        return  newRow;

    }
    public void CreateCallLogRow(CallLog callLog)
    {
        ContentValues values = new ContentValues();

        values.put(_dbHelper.CALL_DATE, _dateTimeManager.convertToMilisec(callLog.get_callDate()));
        values.put(_dbHelper.CALL_NUMBER, callLog.get_callNumber());
        values.put(_dbHelper.DURATION, callLog.get_callDuration());
        values.put(_dbHelper.CALL_FEE, callLog.get_callFee());
        values.put(_dbHelper.CALL_TYPE, callLog.get_callType());
        long insertId = _database.insert(_dbHelper.CALL_TABLE, null, values);

    }
    public void DeleteCallLogRow(int callId)
    {
        _database.delete(_dbHelper.CALL_TABLE, _dbHelper.CALL_ID + " = " + callId, null);
    }
    public void DeleteAllData()
    {
        _database.execSQL("delete from " + _dbHelper.CALL_TABLE);
    }
    public List<CallLog> GetAllCallLog()
    {

        List<CallLog> _listCall = new ArrayList<CallLog>();
        Cursor cursor =_database.query(_dbHelper.CALL_TABLE,_listColumn,null,null,null,null,_dbHelper.CALL_DATE + " DESC",null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {

            CallLog callLog = CursortoCallLog(cursor);
            _listCall.add(callLog);
            cursor.moveToNext();
        }
        cursor.close();
        return _listCall;
    }
    public List<CallLog> GetCallLogInDay(long day)
    {
        List<CallLog> _listCall = new ArrayList<CallLog>();
        long lasttime = day + (86400-1)*1000;
        String whereClause = _dbHelper.CALL_DATE + ">=?" + " AND " + _dbHelper.CALL_DATE + " <=?";
        String[] selectionArgs = {Long.toString(day), Long.toString(lasttime)};
        Cursor cursor = _database.query(_dbHelper.CALL_TABLE,_listColumn,whereClause,selectionArgs,null,null,_dbHelper.CALL_DATE + " DESC");
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {

            CallLog callLog = CursortoCallLog(cursor);
            _listCall.add(callLog);
            cursor.moveToNext();
        }
        cursor.close();
        return  _listCall;
    }
    public CallLog FindCallLogById(int _id)
    {
        CallLog temp;
        Cursor cursor = _database.query(_dbHelper.CALL_TABLE,_listColumn,_dbHelper.CALL_ID + " = " + _id,null,null,null,null);
        if(!cursor.moveToFirst())
            return null;
        else
        {

            temp = CursortoCallLog(cursor);
        }
        cursor.close();
        return temp;
    }
    public long getLastedCallTime()
    {
        Cursor cursor = _database.query(_dbHelper.CALL_TABLE,_listColumn,null,null,null,null,_dbHelper.CALL_DATE + " DESC","1");

        if(cursor.moveToFirst())
        {
            CallLog temp;
            temp = CursortoCallLog(cursor);
            return _dateTimeManager.convertToMilisec(temp.get_callDate());

        }
        cursor.close();
        return 0;
    }

    public long getOldestCallTime()
    {
        Cursor cursor = _database.query(_dbHelper.CALL_TABLE,_listColumn,null,null,null,null,_dbHelper.CALL_DATE + " ASC","1");

        if(cursor.moveToFirst())
        {
            CallLog temp;
            temp = CursortoCallLog(cursor);
            return _dateTimeManager.convertToMilisec(temp.get_callDate());

        }
        cursor.close();
        return 0;
    }
}
