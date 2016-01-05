package com.example.xiaoma.myapplication.view;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.example.xiaoma.myapplication.R;
import com.example.xiaoma.myapplication.model.MyDBOpenHelper;

/**
 * Created by Administrator on 2015/12/26.
 */
public class AttrActivity extends Activity implements View.OnClickListener {
    private Context mContext;
    private SQLiteDatabase db;
    private MyDBOpenHelper myDBHelper;
    private Handler mHandler;
    private TextView car_type;
    private TextView car_sn;
    private TextView car_date;
    private TextView car_version;
    private TextView bms_hardwareversion;
    private TextView bms_softwareversion;
    private TextView cell_cap_rated;
    private TextView cell_type;
    private TextView cell_box_nums;
    private TextView cell_serial_nums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attribute);

        // 以下三条语句每次进行数据库操作时都必须先执行
        mContext = AttrActivity.this;
        myDBHelper = new MyDBOpenHelper(mContext, "db_ebike", null, 1);
        db = myDBHelper.getWritableDatabase();

        // 接收子线程传来的消息
        mHandler = new Handler(){
                // 定义处理消息的方法
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what == 0x123) {
                        car_type.setText(msg.getData().getString("car_type"));
                        car_sn.setText(msg.getData().getString("car_sn"));
                        car_version.setText(msg.getData().getString("car_version"));
                        car_date.setText(msg.getData().getString("car_date"));
                        bms_hardwareversion.setText(msg.getData().getString("bms_hardwareversion"));
                        bms_softwareversion.setText(msg.getData().getString("bms_softwareversion"));
                        cell_cap_rated.setText(msg.getData().getString("cell_cap_rated"));
                        cell_type.setText(msg.getData().getString("cell_type"));
                        cell_box_nums.setText(msg.getData().getString("cell_box_nums"));
                        cell_serial_nums.setText(msg.getData().getString("cell_serial_nums"));
                    }
                }
            };
        Init();
        Display();

    }


    @Override
    public void onClick(View v) {

    }

    /**
     * 说明：控件初始化
     */
    public void Init(){
        car_type = (TextView)findViewById(R.id.car_type);
        car_sn = (TextView)findViewById(R.id.car_sn);
        car_version = (TextView)findViewById(R.id.car_version);
        car_date = (TextView)findViewById(R.id.car_date);
        bms_hardwareversion = (TextView)findViewById(R.id.bms_hardwareversion);
        bms_softwareversion = (TextView)findViewById(R.id.bms_softwareversion);
        cell_cap_rated = (TextView)findViewById(R.id.cell_cap_rated);
        cell_type = (TextView)findViewById(R.id.cell_type);
        cell_box_nums = (TextView)findViewById(R.id.cell_box_nums);
        cell_serial_nums = (TextView)findViewById(R.id.cell_serial_nums);
    }

    /**
     * 说明：显示数据
     */
    public void Display(){
        new Thread() {
            @Override
            public void run() {
                String car_type = "";
                String car_sn = "";
                String car_version = "";
                String car_date = "";
                String bms_hardwareversion = "";
                String bms_softwareversion = "";
                String cell_cap_rated = "";
                String cell_type = "";
                String cell_box_nums = "";
                String cell_serial_nums = "";
                // 向主(UI)线程发送消息，更新UI
                Cursor cursor = db.query("tb_totalcar", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        car_sn = cursor.getString(cursor.getColumnIndex("licensenumber"));
                        car_date = cursor.getString(cursor.getColumnIndex("product_date"));
                        car_type = cursor.getString(cursor.getColumnIndex("type"));
                        car_version = cursor.getString(cursor.getColumnIndex("version"));
                    } while (cursor.moveToNext());
                }
                cursor.close();
                cursor = db.query("tb_carattribute", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        bms_hardwareversion = cursor.getString(cursor.getColumnIndex("bms_hardwareversion"));
                        bms_softwareversion = cursor.getString(cursor.getColumnIndex("bms_softwareversion"));
                        cell_cap_rated = cursor.getString(cursor.getColumnIndex("cell_cap_rated"));
                        cell_type = cursor.getString(cursor.getColumnIndex("cell_type"));
                        cell_box_nums = cursor.getString(cursor.getColumnIndex("cell_box_nums"));
                        cell_serial_nums = cursor.getString(cursor.getColumnIndex("cell_serial_nums"));
                    } while (cursor.moveToNext());
                }
                cursor.close();
                Message msg = new Message();
                msg.what = 0x123;
                Bundle bundle = new Bundle();
                bundle.putString("car_type", car_type);
                bundle.putString("car_sn", car_sn);
                bundle.putString("car_version", car_version);
                bundle.putString("car_date", car_date);
                bundle.putString("bms_hardwareversion", bms_hardwareversion);
                bundle.putString("bms_softwareversion", bms_softwareversion);
                bundle.putString("cell_cap_rated", cell_cap_rated);
                bundle.putString("cell_type", cell_type);
                bundle.putString("cell_box_nums", cell_box_nums);
                bundle.putString("cell_serial_nums", cell_serial_nums);
                msg.setData(bundle);
                AttrActivity.this.mHandler.sendMessage(msg);
            }
        }.start();
    }
}
