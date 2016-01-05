package com.example.xiaoma.myapplication.view;

import android.app.Activity;
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

public class LoginActivity extends Activity implements View.OnClickListener  {
    private Context mContext;
    private SQLiteDatabase db;
    private MyDBOpenHelper myDBHelper;
    private Button mBtnRegister;
    private Button mBtnLogin;
    private EditText accounts;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        InitView();
    }

    /**
     * 功能:按钮点击事件
     */
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.regist:
                goRegisterActivity();
                break;
            case R.id.login:
                Login();
                break;
            default:
                break;
        }
    }

    /**
     * 功能：控件初始化
     */
    public void InitView() {
        mBtnRegister = (Button) findViewById(R.id.regist);
        mBtnLogin = (Button) findViewById(R.id.login);
        accounts = (EditText) findViewById(R.id.accounts);
        password = (EditText) findViewById(R.id.password);

        mBtnRegister.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        accounts.setOnClickListener(this);
        password.setOnClickListener(this);

        mContext = LoginActivity.this;
        myDBHelper = new MyDBOpenHelper(mContext, "db_ebike", null, 1);
        db = myDBHelper.getWritableDatabase();
    }

    /**
     * 功能：打开注册窗口
     */
    public void goRegisterActivity() {
        Intent intent = new Intent();
        intent.setClass(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * 功能：判断是否登录成功
     */
    public void Login(){
        if(accounts.getText().toString().length() == 0){
            Toast.makeText(LoginActivity.this, "请输入您的账户！",
                    Toast.LENGTH_SHORT).show();
        }else if(password.getText().toString().length() == 0){
            Toast.makeText(LoginActivity.this, "请输入登录密码！",
                    Toast.LENGTH_SHORT).show();
        }else{
            String username = accounts.getText().toString();
            Cursor cursor = db.rawQuery("select * from tb_user where username = ?", new String[]{username});
            if(cursor != null && cursor.moveToFirst()) {
                if(!cursor.getString(cursor.getColumnIndex("password")).equals(password.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "输入密码错误，请重新输入！",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    // 登录成功！跳转主界面
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    // 通过Bundle对象存储需要传递的数据
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    // 把bundle对象assign给Intent
                    intent.putExtras(bundle);
                    startActivity(intent);
                    // 销毁该窗口
                    finish();
                }
            }
            if(cursor.getCount() == 0){
                Toast.makeText(LoginActivity.this, "不存在该账号，请注册后再登录！",
                        Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        }
    }
}