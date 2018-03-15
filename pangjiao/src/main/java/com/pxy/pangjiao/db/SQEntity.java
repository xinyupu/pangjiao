package com.pxy.pangjiao.db;

import android.database.Cursor;

import com.pxy.pangjiao.db.annotation.Entity;
import com.pxy.pangjiao.db.sql.DBHelper;
import com.pxy.pangjiao.db.sql.PJDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pxy on 2018/2/2.
 */


public class SQEntity {

    public static <T> List<T> search(Class<T> cls) {
        checkTable(cls);
        Cursor cursor = PJDB.getDB().search("select * from " + DBHelper.getTableNameClass(cls));
        return DBHelper.cursor(cursor, cls);
    }

    public static <T> List<T> search(Class<T> cls, Object o) {
        checkTable(cls);
        try {
            Cursor cursor = PJDB.getDB().search("select * from " + DBHelper.getTableNameClass(cls) + "  where  " + DBHelper.getWhere(o));
            return DBHelper.cursor(cursor, cls);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    public static <T> List<T> search(Class<T> cls, int id) {
        checkTable(cls);
        Cursor cursor = PJDB.getDB().search("select * from " + DBHelper.getTableNameClass(cls) + " where id=" + id);
        return DBHelper.cursor(cursor, cls);
    }

    public static <T> List<T> search(Class<T> cls, String where) {
        checkTable(cls);
        Cursor cursor = PJDB.getDB().search("select * from " + DBHelper.getTableNameClass(cls) + " where " + where);
        return DBHelper.cursor(cursor, cls);
    }

    public static void save(Object o) {
        checkTable(o.getClass());
        try {
            int tableId = DBHelper.getTableId(o);
            List<?> search = search(o.getClass());
            if (search.size() == 0) {
                String insert = DBHelper.insert(o);
                PJDB.getDB().save(insert);
            } else {
                String update = DBHelper.getUpdate(o);
                PJDB.getDB().update(update);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void delete(Object o) {
        checkTable(o.getClass());
        try {
            String delete = DBHelper.getDelete(o);
            PJDB.getDB().delete(delete);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void delete(Class csl, int id) {
        checkTable(csl);
        String delete = "delete from  " + DBHelper.getTableNameClass(csl) + " where id=" + id;
        PJDB.getDB().delete(delete);
    }

    public static void deleteWhere(Class cls, Object o) {
        checkTable(cls);
        String delete = null;
        try {
            delete = "delete from  " + DBHelper.getTableNameClass(cls) + " where " + DBHelper.getWhere(o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        PJDB.getDB().delete(delete);
    }

    public static void deleteWhere(Class cls, String where) {
        checkTable(cls);
        String delete = "delete from  " + DBHelper.getTableNameClass(cls) + " where " + where;
        PJDB.getDB().delete(delete);
    }


    public static void update(Class cls, String where, Object o) {
        checkTable(cls);
        try {
            String sql = "update " + DBHelper.getTableNameClass(cls) + "  " + DBHelper.getUpdateValues(o) + " where " + where;
            PJDB.getDB().update(sql);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void checkTable(Class cls) {
        if (!cls.isAnnotationPresent(Entity.class))
            throw new RuntimeException(cls.getName() + " is not add @Entity");
        try {
            String sql = DBHelper.createSql(cls.newInstance());
            PJDB.getDB().createTable(sql);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
