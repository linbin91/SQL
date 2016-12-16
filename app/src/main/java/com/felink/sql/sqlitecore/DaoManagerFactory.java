package com.felink.sql.sqlitecore;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016/12/16.
 */

public class DaoManagerFactory {

    private String path;
    private SQLiteDatabase sqLiteDatabase;
    private static DaoManagerFactory instance = new DaoManagerFactory(
            new File(Environment.getExternalStorageDirectory(), "login.db"));


    public static  DaoManagerFactory getInstance(){
        return instance;
    }
    private DaoManagerFactory(File file){
        this.path = file.getAbsolutePath();
        openDatabase();
    }

    private void openDatabase() {
        this.sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(path,null);

    }

    /**
     *
     * @param clazz  具体的操作类，比如插入用户userdao
     * @param entityClass  操作哪个表， 比如user
     * @param <T>
     * @param <M>
     * @return
     */
    public  synchronized <T extends BaseDao<M>,M> T getDataHelper(Class<T> clazz, Class<M> entityClass){
        BaseDao baseDao = null;
        try {
            baseDao = clazz.newInstance();
            baseDao.init(entityClass,sqLiteDatabase);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) baseDao;
    }
}
