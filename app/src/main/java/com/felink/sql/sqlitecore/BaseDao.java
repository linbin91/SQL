package com.felink.sql.sqlitecore;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/12/16.
 */

public abstract class BaseDao<T> implements IBaseDao<T> {

    private SQLiteDatabase sqLiteDatabase;
    private boolean isInit = false;
    private String tableName;
    private Class<T> entityClass;
    private Map<String,Field> cacheMap;

    public synchronized  void init(Class<T> entity, SQLiteDatabase sqLiteDatabase){
        if (!isInit){
            this.sqLiteDatabase = sqLiteDatabase;
            this.tableName = entity.getAnnotation(DbTable.class).value();
            this.entityClass = entity;
            sqLiteDatabase.execSQL(createDataBase());

            cacheMap = new HashMap<>();
            initCacheMap();
        }
    }


    protected void initCacheMap() {
        //即取出第一条记录
        String sqlite = "select * from " + this.tableName + " limit 1,0";
        Cursor cursor = null;
        cursor = this.sqLiteDatabase.rawQuery(sqlite, null);
        try {
            String[] columnNames = cursor.getColumnNames();
            Field[] comumnFileds = entityClass.getFields();
            for (Field field : comumnFileds) {
                field.setAccessible(true);
            }

            for (String columnName : columnNames) {
                for (Field field : comumnFileds) {
                    String filedName;
                    //设置注解
                    if (field.getAnnotation(DbFiled.class) != null) {
                        filedName = field.getAnnotation(DbFiled.class).value();
                    } else {
                        filedName = field.getName();
                    }
                    if (columnName.equals(filedName)) {
                        cacheMap.put(filedName, field);
                    }
                }
            }
        } catch (Exception e) {

        } finally {
            cursor.close();
        }
    }


    public abstract String createDataBase();


    public long insert(T entity){
        Map<String,String> map = getValues(entity);
        ContentValues contentValues = getContentValues(map);
        long result = sqLiteDatabase.insert(tableName,null,contentValues);
        return result;
    }


    @Override
    public int delete(T where) {

        Condition condition = new Condition(getValues(where));
        int result= sqLiteDatabase.delete(tableName,condition.whereClause,condition.whereArgs);
        return result;
    }

    private ContentValues getContentValues(Map<String,String> map){
        ContentValues contentValues = new ContentValues();
        Set keys = map.keySet();
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            String value = map.get(key);
            if (value != null){
                contentValues.put(key,value);
            }
        }
        return contentValues;
    }

    private Map<String,String> getValues(T entity) {
        Map<String,String> result = new HashMap<>();
        Iterator fieldsIterator = cacheMap.values().iterator();
        while (fieldsIterator.hasNext()){
            Field colmunToField = (Field) fieldsIterator.next();
            String cacheKey = null;
            String cacheValue = null;
            if (colmunToField.getAnnotation(DbFiled.class) != null){
                cacheKey = colmunToField.getAnnotation(DbFiled.class).value();
            }else{
                cacheKey = colmunToField.getName();
            }
            try {
                if (null == colmunToField.get(entity)){
                    continue;
                }
                cacheValue = colmunToField.get(entity).toString();
            }catch (Exception e){

            }

            result.put(cacheKey,cacheValue);
        }
        return result;
    }

    public int update(T entity,T where){
        int result = -1;
        //name -> 李四
        Map valuse = getValues(entity);
        Condition condition = new Condition(getValues(where));
        result= sqLiteDatabase.update(tableName,getContentValues(valuse),condition.whereClause,condition.whereArgs);
        return result;
    }

    class Condition{
        private String whereClause; //name=?
        private String[] whereArgs;

        public Condition(Map<String,String> map){
            ArrayList list = new ArrayList();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(" 1=1 ");
            Set keys = map.keySet();
            Iterator iterator = keys.iterator();
            while (iterator.hasNext()){
                String key = (String) iterator.next();
                String value = map.get(key);
                if (value != null){
                    stringBuffer.append(" and " + key + " =?");
                    list.add(value);
                }
            }
            this.whereClause = stringBuffer.toString();
            this.whereArgs = (String[]) list.toArray(new String[list.size()]);
        }
    }

}
