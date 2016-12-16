package com.felink.sql;

import com.felink.sql.sqlitecore.DbFiled;
import com.felink.sql.sqlitecore.DbTable;

/**
 * Created by Administrator on 2016/12/16.
 */

@DbTable("tb_user")
public class User {
    @DbFiled("name")
    public String name;
    public String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
