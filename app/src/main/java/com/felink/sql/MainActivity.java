package com.felink.sql;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.felink.sql.sqlitecore.BaseDao;
import com.felink.sql.sqlitecore.DaoManagerFactory;

public class MainActivity extends AppCompatActivity {

    private TextView tvAdd;
    private TextView tvUpdate;
    private TextView tvDelete;
    private BaseDao baseDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvAdd = (TextView) findViewById(R.id.add);
        tvUpdate = (TextView) findViewById(R.id.update);
        tvDelete = (TextView) findViewById(R.id.delete);

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAdd();
            }
        });


        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setName("linbin");
                user.setPassword("123456");

                User where = new User();
                where.setName("linbin");
                baseDao.update(user,where);
            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User where = new User();
                where.setName("linbin");
                baseDao.delete(where);
            }
        });
        baseDao = DaoManagerFactory.getInstance().getDataHelper(UserDao.class,User.class);

    }

    private void onAdd() {
        User user = new User();
        user.setName("linbin");
        user.setPassword("11111");

        baseDao.insert(user);
    }
}
