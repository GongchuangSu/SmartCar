package com.example.xiaoma.myapplication.view;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xiaoma.myapplication.MainActivity;
import com.example.xiaoma.myapplication.R;
import com.example.xiaoma.myapplication.model.MyDBOpenHelper;

/**
 * Created by SGC on 2015/12/27.
 */

public class RegisterActivity extends Activity implements View.OnClickListener {
    private Context mContext;
    private SQLiteDatabase db;
    private MyDBOpenHelper myDBHelper;
    private Button mBtnRegister;
    private EditText email;
    private EditText username;
    private EditText sex;
    private EditText age;
    private EditText password;
    private EditText password_again;
    private EditText phonenumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        InitView();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.register_btn:
                Save();
                break;
            default:
                break;
        }
    }

    /**
     * 说明：控件初始化
     */
    public void InitView()
    {
        mBtnRegister = (Button) findViewById(R.id.register_btn);
        email = (EditText) findViewById(R.id.email);
        username = (EditText) findViewById(R.id.username);
        sex = (EditText) findViewById(R.id.sex);
        age = (EditText) findViewById(R.id.age);
        password = (EditText) findViewById(R.id.password);
        password_again = (EditText) findViewById(R.id.password_again);
        phonenumber = (EditText) findViewById(R.id.phonenumber);

        mBtnRegister.setOnClickListener(this);
        email.setOnClickListener(this);
        username.setOnClickListener(this);
        sex.setOnClickListener(this);
        age.setOnClickListener(this);
        password.setOnClickListener(this);
        password_again.setOnClickListener(this);
        phonenumber.setOnClickListener(this);

        // 以下三条语句每次进行数据库操作时都必须先执行
        mContext = RegisterActivity.this;
        myDBHelper = new MyDBOpenHelper(mContext, "db_ebike", null, 1);
        db = myDBHelper.getWritableDatabase();
    }

    /**
     * 说明：用户信息保存至数据库
     */
    public void Save(){
        if(email.getText().toString().length() == 0){
            Toast.makeText(RegisterActivity.this, "请输入邮箱！",
                    Toast.LENGTH_SHORT).show();
        }else if(username.getText().toString().length() == 0){
            Toast.makeText(RegisterActivity.this, "请输入用户名！",
                    Toast.LENGTH_SHORT).show();
        }else if(sex.getText().toString().length() == 0){
            Toast.makeText(RegisterActivity.this, "请输入性别！",
                    Toast.LENGTH_SHORT).show();
        }else if(age.getText().toString().length() == 0){
            Toast.makeText(RegisterActivity.this, "请输入年龄！",
                    Toast.LENGTH_SHORT).show();
        }else if(password.getText().toString().length() == 0){
            Toast.makeText(RegisterActivity.this, "请输入密码！",
                    Toast.LENGTH_SHORT).show();
        }else if(password_again.getText().toString().length() == 0){
            Toast.makeText(RegisterActivity.this, "请再次输入密码！",
                    Toast.LENGTH_SHORT).show();
        }else if(phonenumber.getText().toString().length() == 0){
            Toast.makeText(RegisterActivity.this, "请输入手机号！",
                    Toast.LENGTH_SHORT).show();
        }else{
            if(!password.getText().toString().equals(password_again.getText().toString())) {
                Toast.makeText(RegisterActivity.this, "密码前后输入不一致！",
                        Toast.LENGTH_SHORT).show();
            }
            else{
                Cursor cursor = db.rawQuery("select * from tb_user where username = ?", new String[]{username.getText().toString()});
                if(cursor.getCount() == 0) {
                    ContentValues values = new ContentValues();
                    values.put("username", username.getText().toString());
                    values.put("sex", sex.getText().toString());
                    values.put("age", age.getText().toString());
                    values.put("password", password.getText().toString());
                    values.put("address", email.getText().toString());
                    values.put("phonenumber", phonenumber.getText().toString());
                    db.insert("tb_user", null, values);
                    Toast.makeText(RegisterActivity.this, "注册成功！",
                            Toast.LENGTH_SHORT).show();
                    // 关闭该窗口，打开主窗口
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(RegisterActivity.this, "该用户名已被注册！",
                            Toast.LENGTH_SHORT).show();
                }

            }
        }
    }


}
