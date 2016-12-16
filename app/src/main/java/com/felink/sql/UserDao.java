package com.felink.sql;

import com.felink.sql.sqlitecore.BaseDao;

/**
 * Created by Administrator on 2016/12/16.
 */

public class UserDao extends BaseDao {

    @Override
    public String createDataBase() {
        return "create table if not exists tb_user(name varchar(20),password varchar(10))";
    }
}
