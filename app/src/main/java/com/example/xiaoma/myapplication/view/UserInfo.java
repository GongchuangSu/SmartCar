package com.example.xiaoma.myapplication.view;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.xiaoma.myapplication.R;
import com.example.xiaoma.myapplication.model.MyDBOpenHelper;

/**
 * Created by SGC on 2015/12/29.
 */

public class UserInfo extends Activity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{
    private Context mContext;
    private SQLiteDatabase db;
    private MyDBOpenHelper myDBHelper;
    private Handler mHandler;
    private TextView username;
    private TextView sex;
    private TextView age;
    private TextView phonenumber;
    private TextView address;
    private String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo);

        // 以下三条语句每次进行数据库操作时都必须先执行
        mContext = UserInfo.this;
        myDBHelper = new MyDBOpenHelper(mContext, "db_ebike", null, 1);
        db = myDBHelper.getWritableDatabase();

        account = this.getIntent().getStringExtra("username");

        // 接收子线程传来的消息
        mHandler = new Handler(){
            // 定义处理消息的方法
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0x123) {
                    username.setText(msg.getData().getString("username"));
                    sex.setText(msg.getData().getString("sex"));
                    age.setText(msg.getData().getString("age"));
                    phonenumber.setText(msg.getData().getString("phonenumber"));
                    address.setText(msg.getData().getString("address"));
                }
            }
        };

        InitView();
        Display();
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 功能：控件初始化
     */
    public void InitView() {
        username = (TextView) findViewById(R.id.username);
        sex = (TextView) findViewById(R.id.sex);
        age = (TextView) findViewById(R.id.age);
        phonenumber = (TextView) findViewById(R.id.phonenumber);
        address = (TextView) findViewById(R.id.address);

    }

    /**
     * 说明：显示数据
     */
    public void Display(){
        new Thread() {
            @Override
            public void run() {
                // 向主(UI)线程发送消息，更新UI
                Cursor cursor = db.rawQuery("select * from tb_user where username = ?", new String[]{account});
                if(cursor != null && cursor.moveToFirst()) {
                    Message msg = new Message();
                    msg.what = 0x123;
                    Bundle bundle = new Bundle();
                    bundle.putString("username", cursor.getString(cursor.getColumnIndex("username")));
                    bundle.putString("sex", cursor.getString(cursor.getColumnIndex("sex")));
                    bundle.putString("age", cursor.getString(cursor.getColumnIndex("age")));
                    bundle.putString("phonenumber", cursor.getString(cursor.getColumnIndex("phonenumber")));
                    bundle.putString("address", cursor.getString(cursor.getColumnIndex("address")));
                    msg.setData(bundle);
                    UserInfo.this.mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}
