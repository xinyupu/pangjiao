package com.pxy.pangjiao.db.sql;

import android.database.Cursor;

import com.pxy.pangjiao.common.Utils;
import com.pxy.pangjiao.db.annotation.Column;
import com.pxy.pangjiao.db.annotation.Entity;
import com.pxy.pangjiao.db.annotation.Id;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pxy on 2018/2/2.
 */

public class DBHelper {

    public static String createSql(Object o) {
        StringBuilder sql = new StringBuilder("create table if not exists");
        StringBuilder columnSql = new StringBuilder();
        sql.append(" " + getTableName(o));
        Field[] fields = o.getClass().getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                if (field.getType() != Long.class && field.getType() != Integer.class && field.getType() != int.class && field.getType() != long.class) {
                    throw new RuntimeException("unknown @Id field type at" + o.getClass().getName());
                }
                Id annotation = field.getAnnotation(Id.class);
                String primary_key;
                if (Utils.isEmpty(annotation.name())) {
                    primary_key = field.getName() + " " + getSqlLiteDataType(field.getType()) + " PRIMARY KEY AUTOINCREMENT";
                } else {
                    primary_key = annotation.name() + " " + getSqlLiteDataType(field.getType()) + " PRIMARY KEY AUTOINCREMENT";
                }
                columnSql.append(primary_key + ",");
            } else if (field.isAnnotationPresent(Column.class)) {
                String columnName;
                Type columnType;
                Column column = field.getAnnotation(Column.class);
                if (Utils.isEmpty(column.name())) {
                    columnName = field.getName();
                } else {
                    columnName = column.name();
                }
                columnType = field.getType();
                columnSql.append(" " + columnName);
                columnSql.append(" " + getSqlLiteDataType(columnType) + ",");
            }
        }
        String substring = columnSql.toString().substring(0, columnSql.toString().length() - 1);
        sql.append("(");
        sql.append(substring);
        sql.append(")");
        return sql.toString();
    }


    public static String insert(Object o) throws IllegalAccessException {
        StringBuilder sql = new StringBuilder("insert into ");
        sql.append(getTableName(o));
        StringBuilder columnNames = new StringBuilder();
        StringBuilder values = new StringBuilder();
        Field[] fields = o.getClass().getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                String columnName;
                Type columnType;
                Column column = field.getAnnotation(Column.class);
                if (Utils.isEmpty(column.name())) {
                    columnName = field.getName();
                } else {
                    columnName = column.name();
                }
                columnNames.append(" " + columnName + ",");
                if (field.getType() == String.class) {
                    values.append(" '" + field.get(o) + "',");
                } else if (field.getType() == Boolean.class || field.getType() == boolean.class) {
                    Boolean aBoolean = (Boolean) field.get(o);
                    if (aBoolean) {
                        values.append(" " + 1 + ",");
                    } else {
                        values.append(" " + 0 + ",");
                    }
                } else {
                    values.append(" " + field.get(o) + ",");
                }
            }
        }

        sql.append("(");
        sql.append(columnNames.toString().substring(0, columnNames.toString().length() - 1));
        sql.append(")");
        sql.append(" values (");
        sql.append(values.toString().substring(0, values.toString().length() - 1));
        sql.append(")");
        return sql.toString();
    }


    public static String getUpdate(Object o) throws IllegalAccessException {
        StringBuilder sql = new StringBuilder("update " + getTableName(o) + " ");
        String id = "0";
        Field[] fields = o.getClass().getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                id = field.get(o) + "";
                break;
            }
        }

        String update = getUpdateValues(o);
        sql.append(update + " ");
        sql.append("where id=" + id);
        return sql.toString();
    }

    public static String getTableIdName(Class cls) {
        Field[] fields = cls.getFields();
        String id = "";
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                Id annotation = field.getAnnotation(Id.class);
                if (Utils.isEmpty(annotation.name())) {
                    id = field.getName();
                } else {
                    id = annotation.name();
                }
                break;
            }
        }
        return id;
    }

    public static int getTableId(Object o) throws IllegalAccessException {
        Field[] fields = o.getClass().getFields();
        int id = 0;
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                id = (int) field.get(o);
                break;
            }
        }
        return id;
    }

    public static String getTableName(Object o) {
        String tableName;
        Entity annotation = o.getClass().getAnnotation(Entity.class);
        if (Utils.isEmpty(annotation.name())) {
            tableName = o.getClass().getSimpleName();
        } else {
            tableName = annotation.name();
        }
        return tableName;
    }

    public static String getTableNameClass(Class cls) {
        String tableName;
        Entity annotation = (Entity) cls.getAnnotation(Entity.class);
        if (Utils.isEmpty(annotation.name())) {
            tableName = cls.getSimpleName();
        } else {
            tableName = annotation.name();
        }
        return tableName;
    }

    public static String getColumnName(Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            Column annotation = field.getAnnotation(Column.class);
            if (Utils.isEmpty(annotation.name())) {
                return field.getName();
            } else {
                return annotation.name();
            }
        } else {
            return field.getName();
        }
    }

    public static <T> List<T> cursor(Cursor cursor, Class<T> cls) {
        List<T> datas = new ArrayList();
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            if (cursor.getLong(cursor.getColumnIndex("id")) != 0) {
                try {
                    T t = cls.newInstance();
                    Field[] fields = cls.getFields();
                    for (Field field : fields) {
                        if (field.getType() == String.class) {
                            field.set(t, cursor.getString(cursor.getColumnIndex(DBHelper.getColumnName(field))));
                        } else if (field.getType() == int.class || field.getType() == Integer.class) {
                            field.set(t, cursor.getInt(cursor.getColumnIndex(DBHelper.getColumnName(field))));
                        } else if (field.getType() == long.class || field.getType() == Long.class) {
                            field.set(t, cursor.getLong(cursor.getColumnIndex(DBHelper.getColumnName(field))));
                        } else if (field.getType() == float.class || field.getType() == Float.class) {
                            field.set(t, cursor.getFloat(cursor.getColumnIndex(DBHelper.getColumnName(field))));
                        } else if (field.getType() == double.class || field.getType() == Double.class) {
                            field.set(t, cursor.getDouble(cursor.getColumnIndex(DBHelper.getColumnName(field))));
                        } else if (field.getType() == Boolean.class || field.getType() == boolean.class) {
                            field.set(t, cursor.getInt(cursor.getColumnIndex(DBHelper.getColumnName(field))) != 0);
                        }
                    }
                    datas.add(t);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
        return datas;
    }


    public static String getWhere(Object o) throws IllegalAccessException {
        StringBuilder where = new StringBuilder();
        Field[] fields = o.getClass().getFields();
        for (Field field : fields) {
            if (field.get(o) != null) {
                if (field.isAnnotationPresent(Id.class)) {
                    if (((int) field.get(o)) != 0) {
                        where.append(field.getName() + "=");
                        if (field.getType() == String.class) {
                            where.append("'" + field.get(o) + "' and ");
                        } else {
                            where.append(field.get(o) + " and ");
                        }
                    }
                } else {
                    where.append(field.getName() + "=");
                    if (field.getType() == String.class) {
                        where.append("'" + field.get(o) + "' and ");
                    } else {
                        where.append(field.get(o) + " and ");
                    }
                }


            }
        }
        String ws = where.toString();
        return ws.substring(0, ws.length() - 4);
    }


    public static String getUpdateValues(Object o) throws IllegalAccessException {
        StringBuilder sql = new StringBuilder(" set ");
        StringBuilder values = new StringBuilder();
        Field[] fields = o.getClass().getFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Id.class)) {
                values.append(getColumnName(field) + "=");
                if (field.getType() == String.class) {
                    values.append(" '" + field.get(o) + "',");
                } else if (field.getType() == Boolean.class || field.getType() == boolean.class) {
                    Boolean aBoolean = (Boolean) field.get(o);
                    if (aBoolean) {
                        values.append(" " + 1 + ",");
                    } else {
                        values.append(" " + 0 + ",");
                    }
                } else {
                    values.append(" " + field.get(o) + ",");
                }
            }
        }
        String vs = values.toString();
        String substring = vs.substring(0, vs.length() - 1);
        sql.append(substring);
        return sql.toString();
    }

    public static String getDelete(Object o) throws IllegalAccessException {
        Field[] fields = o.getClass().getFields();
        StringBuilder sql = new StringBuilder("delete from  " + getTableName(o));
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                sql.append(" where id=" + field.get(o));
                break;
            }
        }
        return sql.toString();
    }

    private static String getSqlLiteDataType(Type type) {
        if (type == Long.class || type == long.class || type == Integer.class || type == int.class) {
            return "INTEGER";
        } else if (type == float.class || type == Float.class || type == double.class || type == Double.class) {
            return "REAL";
        } else if (type == String.class) {
            return "TEXT";
        } else if (type == Boolean.class || type == boolean.class) {
            return "INTEGER";
        }
        return "";
    }
}
