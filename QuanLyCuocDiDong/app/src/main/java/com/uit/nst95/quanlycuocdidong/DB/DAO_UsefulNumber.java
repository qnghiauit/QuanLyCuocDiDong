package com.uit.nst95.quanlycuocdidong.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.uit.nst95.quanlycuocdidong.Manager.Contact;

import java.util.ArrayList;

/**
 * Created by QNghia on 17/10/2016.
 */

public class DAO_UsefulNumber {
    private SQLiteDatabase _database;
    private UsefulNumberDbHelper _dbHelper;
    public DAO_UsefulNumber(Context context) {
        _dbHelper = new UsefulNumberDbHelper(context);
    }
    public void Open() {
        _database = _dbHelper.getReadableDatabase();
    }
    public void Close() {
        _database.close();
    }
    public Contact CursorToContact(Cursor cursor){
        Contact contact = new Contact();
        contact.set_phoneNumber(cursor.getString(2));
        contact.set_name(cursor.getString(1));
        return contact;
    }
    public ArrayList<Contact> GetAllTuVan()
    {
        ArrayList<Contact> _listInfo = new ArrayList<Contact>();
        Cursor cursor = _database.query("TuVan",null,null,null,null,null,null);
        if(cursor.moveToFirst()) {
            do {
                Contact temp  = CursorToContact(cursor);
                _listInfo.add(temp);
            }
            while(cursor.moveToNext());
        }
        return _listInfo;
    }
    public ArrayList<Contact> GetAllNhaMang()
    {
        ArrayList<Contact> _listInfo = new ArrayList<Contact>();
        Cursor cursor = _database.query("NhaMang",null,null,null,null,null,null);
        if(cursor.moveToFirst()) {
            do {
                Contact temp  = CursorToContact(cursor);
                _listInfo.add(temp);
            }
            while(cursor.moveToNext());
        }
        return _listInfo;
    }
    public ArrayList<Contact> GetAllNganHang()
    {
        ArrayList<Contact> _listInfo = new ArrayList<Contact>();
        Cursor cursor = _database.query("NganHang",null,null,null,null,null,null);
        if(cursor.moveToFirst()) {
            do {
                Contact temp  = CursorToContact(cursor);
                _listInfo.add(temp);
            }
            while(cursor.moveToNext());
        }
        return _listInfo;
    }
    public ArrayList<Contact> GetAllCuuHo()
    {
        ArrayList<Contact> _listInfo = new ArrayList<Contact>();
        Cursor cursor = _database.query("CuuHo",null,null,null,null,null,null);
        if(cursor.moveToFirst()) {
            do {
                Contact temp  = CursorToContact(cursor);
                _listInfo.add(temp);
            }
            while(cursor.moveToNext());
        }
        return _listInfo;
    }
    public ArrayList<Contact> GetAllTaxi(int type)
    {
        ArrayList<Contact> _listInfo = new ArrayList<Contact>();
        String selectQuery = "SELECT * FROM Taxi WHERE REGION=?";
        Cursor cursor = _database.rawQuery(selectQuery,new String[] { String.valueOf(type) });
        if(cursor.moveToFirst()) {
            do {
                Contact temp  = CursorToContact(cursor);
                _listInfo.add(temp);
            }
            while(cursor.moveToNext());
        }
        return _listInfo;
    }
}
