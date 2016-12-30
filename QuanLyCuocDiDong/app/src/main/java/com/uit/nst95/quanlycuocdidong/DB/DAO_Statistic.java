package com.uit.nst95.quanlycuocdidong.DB;

/**
 * Created by JT on 01/10/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
public class DAO_Statistic {
    private SQLiteDatabase _database;
    private DbHelper _dbHelper;
    private String[] _listColumn = {_dbHelper.MONTH, _dbHelper.YEAR, _dbHelper.INNER_CALL_FEE, _dbHelper.OUTER_CALL_FEE,
            _dbHelper.INNER_MESSAGE_FEE, _dbHelper.OUTER_MESSAGE_FEE, _dbHelper.INNER_CALL_DURATION, _dbHelper.OUTER_CALL_DURATION,
            _dbHelper.TOTAL_INNER_MESSAGE, _dbHelper.TOTAL_OUTER_MESSAGE};

    public DAO_Statistic(Context context)
    {
        _dbHelper = _dbHelper.getInstance(context);
    }
    public void Open() throws SQLException
    {
        _database = _dbHelper.getWritableDatabase();
        _database = _dbHelper.getReadableDatabase();
    }
    public void Close()
    {
        _dbHelper.close();
    }
    public Statistic CursortoStatistic(Cursor cursor)
    {
        Statistic row = new Statistic();

        row.set_month(cursor.getInt(cursor.getColumnIndex(_dbHelper.MONTH)));
        row.set_year(cursor.getInt(cursor.getColumnIndex(_dbHelper.YEAR)));
        row.set_innerCallFee(cursor.getInt(cursor.getColumnIndex(_dbHelper.INNER_CALL_FEE)));
        row.set_outerCallFee(cursor.getInt(cursor.getColumnIndex(_dbHelper.OUTER_CALL_FEE)));
        row.set_innerMessageFee(cursor.getInt(cursor.getColumnIndex(_dbHelper.INNER_MESSAGE_FEE)));
        row.set_outerMessageFee(cursor.getInt(cursor.getColumnIndex(_dbHelper.OUTER_MESSAGE_FEE)));
        row.set_innerCallDuration(cursor.getLong(cursor.getColumnIndex(_dbHelper.INNER_CALL_DURATION)));
        row.set_outerCallDuration(cursor.getLong(cursor.getColumnIndex(_dbHelper.OUTER_CALL_DURATION)));
        row.set_innerMessageCount(cursor.getInt(cursor.getColumnIndex(_dbHelper.TOTAL_INNER_MESSAGE)));
        row.set_outerMessageCount(cursor.getInt(cursor.getColumnIndex(_dbHelper.TOTAL_OUTER_MESSAGE)));


        return row;
    }
    public Statistic CreateStatisticRow(int month,
                                        int year,
                                        int innerCallFee,
                                        int outerCallFee,
                                        int innerMessageFee,
                                        int outerMessageFee,
                                        int innerDuration,
                                        int outerDuration,
                                        int totalInnerMessage,
                                        int totalOuterMessage)
    {
        ContentValues values = new ContentValues();
        values.put(_dbHelper.MONTH, month);
        values.put(_dbHelper.YEAR, year);
        values.put(_dbHelper.INNER_CALL_FEE, innerCallFee);
        values.put(_dbHelper.OUTER_CALL_FEE, outerCallFee);
        values.put(_dbHelper.INNER_MESSAGE_FEE, innerMessageFee);
        values.put(_dbHelper.OUTER_MESSAGE_FEE, outerMessageFee);
        values.put(_dbHelper.INNER_CALL_DURATION, innerDuration);
        values.put(_dbHelper.OUTER_CALL_DURATION, outerDuration);
        values.put(_dbHelper.TOTAL_INNER_MESSAGE, totalInnerMessage);
        values.put(_dbHelper.TOTAL_OUTER_MESSAGE, totalOuterMessage);
        _database.insert(_dbHelper.STATISTIC_TABLE, null, values);
        Cursor cursor = _database.query(_dbHelper.STATISTIC_TABLE,_listColumn, _dbHelper.MONTH + "=" + month + " AND "
                + _dbHelper.YEAR + "=" + year,null,null,null,null);
        cursor.moveToFirst();
        Statistic row = CursortoStatistic(cursor);
        cursor.close();
        return row;
    }
    public Statistic CreateStatisticRow(int month, int year)
    {
        ContentValues values = new ContentValues();
        values.put(_dbHelper.MONTH, month);
        values.put(_dbHelper.YEAR, year);
        _database.insert(_dbHelper.STATISTIC_TABLE, null, values);
        Cursor cursor = _database.query(_dbHelper.STATISTIC_TABLE,_listColumn, _dbHelper.MONTH + "=" + month + " AND "
                + _dbHelper.YEAR + "=" + year,null,null,null,null);
        cursor.moveToFirst();
        Statistic row = CursortoStatistic(cursor);
        cursor.close();
        return row;
    }
    public void DeleteStatisticRow(int month, int year)
    {
        _database.delete(_dbHelper.STATISTIC_TABLE, _dbHelper.MONTH + " = " + month + " AND " +
                _dbHelper.YEAR + " = " + year, null);
    }
    public void DeleteAllData()
    {
        _database.execSQL("delete from " + _dbHelper.STATISTIC_TABLE);
    }
    public List<Statistic> GetAllStatistic()
    {
        List<Statistic> _listStatistic = new ArrayList<Statistic>();
        Cursor cursor = _database.query(_dbHelper.STATISTIC_TABLE, _listColumn, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            Statistic temp;
            temp = CursortoStatistic(cursor);
            _listStatistic.add(temp);
            cursor.moveToNext();
        }
        cursor.close();
        return _listStatistic;
    }
    public Statistic FindStatisticByMonthYear(int month, int year)
    {
        Statistic result;

        //Cursor cursor = _database.query(_dbHelper.STATISTIC_TABLE, _listColumn,  _dbHelper.MONTH + " = " + month + " AND "
        //  + _dbHelper.YEAR + " = " + year,null,null,null,null);

        //String rawQuery = "SELECT * FROM " + _dbHelper.STATISTIC_TABLE + " WHERE " + _dbHelper.MONTH + " = " + month + " AND " + _dbHelper.YEAR + " = " + year;
        _database = _dbHelper.getReadableDatabase();
        String[] selectionArgs= {Integer.toString(month), Integer.toString(year)};
        Cursor cur = _database.query(_dbHelper.STATISTIC_TABLE, _listColumn,
                _dbHelper.MONTH + " =?" + " AND " + _dbHelper.YEAR + " =?",
                selectionArgs,
                null,
                null,
                _dbHelper.MONTH + " DESC, " + _dbHelper.YEAR + " DESC", null);
        //Cursor cursor = _database.rawQuery(rawQuery,null);
        if(!cur.moveToFirst())
            return null;
        else
        {
            result = CursortoStatistic(cur);
        }
        cur.close();
        return result;
    }
    public void UpdateStatisticRow(int month, int year, int value, String columnName)
    {
        String whereClause = _dbHelper.MONTH + " = " + month + " AND " + _dbHelper.YEAR + " = " +year;
        Cursor cursor = _database.query(_dbHelper.STATISTIC_TABLE,_listColumn,whereClause,null,null,null,null);
        int currentValue = 0;
        if(cursor.moveToFirst())
        {
            currentValue = cursor.getInt(cursor.getColumnIndex(columnName));
        }
        currentValue += value;
        ContentValues newValue = new ContentValues();
        newValue.put(columnName,currentValue);

        int _rowAffect;
        _rowAffect = _database.update(_dbHelper.STATISTIC_TABLE,newValue,whereClause, null);
        cursor.close();
    }
    public void ResetMessageData()
    {
        ContentValues newValue = new ContentValues();
        newValue.put(_dbHelper.INNER_MESSAGE_FEE,0);
        newValue.put(_dbHelper.OUTER_MESSAGE_FEE,0);
        newValue.put(_dbHelper.TOTAL_INNER_MESSAGE,0);
        newValue.put(_dbHelper.TOTAL_OUTER_MESSAGE,0);
        _database.update(_dbHelper.STATISTIC_TABLE, newValue, null, null);
    }
    public void ResetCallData()
    {
        ContentValues newValue = new ContentValues();
        newValue.put(_dbHelper.INNER_CALL_FEE,0);
        newValue.put(_dbHelper.OUTER_CALL_FEE,0);
        newValue.put(_dbHelper.INNER_CALL_DURATION,0);
        newValue.put(_dbHelper.OUTER_CALL_DURATION,0);
        _database.update(_dbHelper.STATISTIC_TABLE, newValue, null, null);
    }
    public int UpdateInnerCallInfo(int month, int year, int callFee, long callDuration)
    {
        String whereClause = _dbHelper.MONTH + " = ?"  + " AND " + _dbHelper.YEAR + " = ?";
        String[] selectionArgs = {Integer.toString(month), Integer.toString(year)};
        Cursor cursor = _database.query(_dbHelper.STATISTIC_TABLE, _listColumn, whereClause , selectionArgs, null, null, null);

        int currentCallFee = 0;
        int currentCallDuration = 0;
        if(cursor.moveToFirst())
        {

            currentCallFee = cursor.getInt(cursor.getColumnIndex(_dbHelper.INNER_CALL_FEE));
            currentCallDuration = cursor.getInt(cursor.getColumnIndex(_dbHelper.INNER_CALL_DURATION));
        }
        currentCallFee += callFee;
        currentCallDuration += callDuration;
        ContentValues values = new ContentValues();
        values.put(_dbHelper.INNER_CALL_FEE, currentCallFee);
        values.put(_dbHelper.INNER_CALL_DURATION, currentCallDuration);
        int rowAffect = _database.update(_dbHelper.STATISTIC_TABLE, values, whereClause, selectionArgs);

        cursor.close();
        return rowAffect;
    }

    public int UpdateOuterCallInfo(int month, int year, int callFee, long callDuration)
    {
       /* String whereClause = _dbHelper.MONTH + " = " + month + " AND " + _dbHelper.YEAR + " = " +year;
        Cursor cursor = _database.query(_dbHelper.STATISTIC_TABLE,_listColumn,whereClause,null,null,null,null);
        int currentCallFee = 0;
        int currentCallDuration = 0;
        if(cursor.moveToFirst())
        {
            currentCallFee = cursor.getInt(cursor.getColumnIndex(_dbHelper.OUTER_CALL_FEE));
            currentCallDuration = cursor.getInt(cursor.getColumnIndex(_dbHelper.OUTER_CALL_DURATION));
        }
        currentCallFee += callFee;
        currentCallDuration += callDuration;
        ContentValues values = new ContentValues();
        values.put(_dbHelper.OUTER_CALL_FEE, currentCallFee);
        values.put(_dbHelper.OUTER_CALL_DURATION, currentCallDuration);
        int rowAffect = _database.update(_dbHelper.STATISTIC_TABLE,values,whereClause,null);
        cursor.close();
        return rowAffect;*/
        String whereClause = _dbHelper.MONTH + " = ?"  + " AND " + _dbHelper.YEAR + " = ?";
        String[] selectionArgs = {Integer.toString(month), Integer.toString(year)};
        Cursor cursor = _database.query(_dbHelper.STATISTIC_TABLE, _listColumn, whereClause , selectionArgs, null, null, null);

        int currentCallFee = 0;
        int currentCallDuration = 0;
        if(cursor.moveToFirst())
        {

            currentCallFee = cursor.getInt(cursor.getColumnIndex(_dbHelper.OUTER_CALL_FEE));
            currentCallDuration = cursor.getInt(cursor.getColumnIndex(_dbHelper.OUTER_CALL_DURATION));
        }
        currentCallFee += callFee;
        currentCallDuration += callDuration;
        ContentValues values = new ContentValues();
        values.put(_dbHelper.OUTER_CALL_FEE, currentCallFee);
        values.put(_dbHelper.OUTER_CALL_DURATION, currentCallDuration);
        int rowAffect = _database.update(_dbHelper.STATISTIC_TABLE, values, whereClause, selectionArgs);

        cursor.close();
        return rowAffect;



        //String whereClause = _dbHelper.MONTH + " = ?"  + " AND " + _dbHelper.YEAR + " = ?" ;
        // Cursor cursor = _database.query(_dbHelper.STATISTIC_TABLE, _listColumn, whereClause, null, null, null, null);
        /*String[] selectionArgs = {Integer.toString(month), Integer.toString(year)};
        Cursor cursor = _database.query(_dbHelper.STATISTIC_TABLE, _listColumn, whereClause , selectionArgs, null, null, null);
        int currentCallFee = 0;
        int currentCallDuration = 0;
        if(cursor.moveToFirst())
        {

            currentCallFee = cursor.getInt(cursor.getColumnIndex(_dbHelper.OUTER_CALL_FEE));
            currentCallDuration = cursor.getInt(cursor.getColumnIndex(_dbHelper.OUTER_CALL_DURATION));
        }
        currentCallFee += callFee;
        currentCallDuration += callDuration;
        ContentValues values = new ContentValues();
        values.put(_dbHelper.OUTER_CALL_FEE, currentCallFee);
        values.put(_dbHelper.OUTER_CALL_DURATION, currentCallDuration);
        int rowAffect = _database.update(_dbHelper.STATISTIC_TABLE, values, whereClause,selectionArgs);

        cursor.close();
        return rowAffect;*/
        /*String whereClause = _dbHelper.MONTH + " = ?"  + " AND " + _dbHelper.YEAR + " = ?";
        String[] selectionArgs = {Integer.toString(month), Integer.toString(year)};
        Cursor cursor = _database.query(_dbHelper.STATISTIC_TABLE, _listColumn, whereClause , selectionArgs, null, null, null);

        int currentCallFee = 0;
        int currentCallDuration = 0;
        if(cursor.moveToFirst())
        {

            currentCallFee = cursor.getInt(cursor.getColumnIndex(_dbHelper.OUTER_CALL_FEE));
            currentCallDuration = cursor.getInt(cursor.getColumnIndex(_dbHelper.OUTER_CALL_DURATION));
        }
        currentCallFee += callFee;
        currentCallDuration += callDuration;
        ContentValues values = new ContentValues();
        values.put(_dbHelper.OUTER_CALL_FEE, currentCallFee);
        values.put(_dbHelper.OUTER_CALL_DURATION, currentCallDuration);
        int rowAffect = _database.update(_dbHelper.STATISTIC_TABLE, values, whereClause, selectionArgs);

        cursor.close();
        return rowAffect;*/
    }
    public void UpdateInnerMessageInfo(int month, int year, int messageFee)
    {
        String[] selectionArgs = {Integer.toString(month), Integer.toString(year)};
        String whereClause = _dbHelper.MONTH + " = ?"  + " AND " + _dbHelper.YEAR + " = ?" ;
        Cursor cursor = _database.query(_dbHelper.STATISTIC_TABLE,_listColumn,whereClause,selectionArgs,null,null,null);
        int currentMessageFee = 0;
        int currentMessageQuantity = 0;
        if(cursor.moveToFirst())
        {
            currentMessageFee = cursor.getInt(cursor.getColumnIndex(_dbHelper.INNER_MESSAGE_FEE));
            currentMessageQuantity = cursor.getInt(cursor.getColumnIndex(_dbHelper.TOTAL_INNER_MESSAGE));
        }
        currentMessageFee += messageFee;
        currentMessageQuantity++;
        ContentValues values = new ContentValues();
        values.put(_dbHelper.INNER_MESSAGE_FEE, currentMessageFee);
        values.put(_dbHelper.TOTAL_INNER_MESSAGE, currentMessageQuantity);
        int rowAffect = _database.update(_dbHelper.STATISTIC_TABLE,values,whereClause,selectionArgs);
        cursor.close();
    }

    public void UpdateOuterMessageInfo(int month, int year, int messageFee)
    {
        /*String[] selectionArgs = {Integer.toString(month), Integer.toString(year)};
        String whereClause = _dbHelper.MONTH + " = ?"  + " AND " + _dbHelper.YEAR + " = ?" ;
        Cursor cursor = _database.query(_dbHelper.STATISTIC_TABLE,_listColumn,whereClause,selectionArgs,null,null,null);
        int currentMessageFee = 0;
        int currentMessageQuantity = 0;
        if(cursor.moveToFirst())
        {
            currentMessageFee = cursor.getInt(cursor.getColumnIndex(_dbHelper.OUTER_MESSAGE_FEE));
            currentMessageQuantity = cursor.getInt(cursor.getColumnIndex(_dbHelper.TOTAL_OUTER_MESSAGE));
        }
        currentMessageFee += messageFee;
        currentMessageQuantity++;
        ContentValues values = new ContentValues();
        values.put(_dbHelper.OUTER_CALL_FEE, currentMessageFee);
        values.put(_dbHelper.TOTAL_OUTER_MESSAGE, currentMessageQuantity);
        int rowAffect = _database.update(_dbHelper.STATISTIC_TABLE,values,whereClause,selectionArgs);
        cursor.close();*/
        String[] selectionArgs = {Integer.toString(month), Integer.toString(year)};
        String whereClause = _dbHelper.MONTH + " = ?"  + " AND " + _dbHelper.YEAR + " = ?" ;
        Cursor cursor = _database.query(_dbHelper.STATISTIC_TABLE,_listColumn,whereClause,selectionArgs,null,null,null);
        int currentMessageFee = 0;
        int currentMessageQuantity = 0;
        if(cursor.moveToFirst())
        {
            currentMessageFee = cursor.getInt(cursor.getColumnIndex(_dbHelper.OUTER_MESSAGE_FEE));
            currentMessageQuantity = cursor.getInt(cursor.getColumnIndex(_dbHelper.TOTAL_OUTER_MESSAGE));
        }
        currentMessageFee += messageFee;
        currentMessageQuantity++;
        ContentValues values = new ContentValues();
        values.put(_dbHelper.OUTER_MESSAGE_FEE, currentMessageFee);
        values.put(_dbHelper.TOTAL_OUTER_MESSAGE, currentMessageQuantity);
        int rowAffect = _database.update(_dbHelper.STATISTIC_TABLE,values,whereClause,selectionArgs);
        cursor.close();
    }
    public boolean IsTableEmpty()
    {
        Cursor cursor = _database.rawQuery("SELECT COUNT(*) FROM STATISTIC",null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        if(count >0)
            return true;
        else
            return false;
    }
}
