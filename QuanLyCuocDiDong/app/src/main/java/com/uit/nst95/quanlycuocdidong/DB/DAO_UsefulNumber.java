package com.uit.nst95.quanlycuocdidong.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.uit.nst95.quanlycuocdidong.Manager.Contact;
import com.uit.nst95.quanlycuocdidong.Manager.Data3GPackage;

import java.util.ArrayList;

import static android.R.attr.type;

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
    public Data3GPackage CursorToData3GPackage(Cursor cursor){
        Data3GPackage data3GPackage = new Data3GPackage();
        data3GPackage.setId_3gpackage(cursor.getString(2));
        data3GPackage.setSyntax_reg(cursor.getString(3));
        data3GPackage.setNumber_receive(cursor.getString(4));
        data3GPackage.setFee(cursor.getString(5));
        data3GPackage.setData_highspeed(cursor.getString(6));
        data3GPackage.setExpiry_date(cursor.getString(7));
        data3GPackage.setCharges_arise(cursor.getString(8));
        return data3GPackage;
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
    public ArrayList<Data3GPackage> GetAll3GPackage(int type)
    {
        ArrayList<Data3GPackage> _list3Gpackage = new ArrayList<Data3GPackage>();
        String selectQuery = "SELECT * FROM Data3GPackage WHERE id_provider=?";
        Cursor cursor = _database.rawQuery(selectQuery,new String[] { String.valueOf(type) });
        if(cursor.moveToFirst()) {
            do {
                Data3GPackage temp  = CursorToData3GPackage(cursor);
                _list3Gpackage.add(temp);
            }
            while(cursor.moveToNext());
        }
        return _list3Gpackage;
    }
}
