package com.pxy.pangjiao.db.sql;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pxy.pangjiao.PangJiao;


/**
 * Created by pxy on 2018/2/2.
 */

public class PJDB {

    private static PJDB _instance;

    private Application context;
    private SQLiteDatabase db;

    public static PJDB init(Application context) {
        if (_instance == null) {
            synchronized (PJDB.class) {
                if (_instance == null) {
                    _instance = new PJDB(context);
                }
            }
        }
        return _instance;
    }

    private PJDB() {
    }

    public static PJDB getDB() {
        return _instance;
    }

    @SuppressLint("WrongConstant")
    private PJDB(Application context) {
        this.context = context;
        db = context.openOrCreateDatabase("pj.db", Context.MODE_PRIVATE, null);
    }

    public void update(String sql) {
        PangJiao.info(sql);
        db.execSQL(sql);
    }

    public void save(String sql) {
        PangJiao.info(sql);
        db.execSQL(sql);
    }

    public Cursor search(String sql) {
        PangJiao.info(sql);
        return db.rawQuery(sql, null);
    }

    public void createTable(String sql) {
        PangJiao.info(sql);
        db.execSQL(sql);
    }

    public void delete(String sql) {
        db.execSQL(sql);
    }
}
