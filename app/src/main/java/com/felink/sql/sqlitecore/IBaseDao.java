package com.felink.sql.sqlitecore;

/**
 * Created by Administrator on 2016/12/16.
 */

public interface IBaseDao<T> {
    long insert(T entity);

    int update(T entity,T where);

    int delete(T where);
}
