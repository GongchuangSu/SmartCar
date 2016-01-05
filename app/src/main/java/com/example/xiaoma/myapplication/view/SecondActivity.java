package com.example.xiaoma.myapplication.view;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.xiaoma.myapplication.R;
import com.example.xiaoma.myapplication.model.DoHttpPostJson;
import com.example.xiaoma.myapplication.model.MyDBOpenHelper;

/**
 * Created by xiaoma on 2015/12/22.
 */

public class SecondActivity extends Activity implements View.OnClickListener{
    private Context mContext;
    private SQLiteDatabase db;
    private MyDBOpenHelper myDBHelper;
    private Button btn_totalcar;     // 车辆类属性
    private Button btn_carattribute; // BMS类属性
    private Button btn_carrealtime;  // 车辆类实时
    private Button btn_bmsrealtime;  // BMS类实时
    // 定义IP地址
    String url_upload_totalcar = "http://192.168.0.115/totalcar.php";
    String url_upload_carattribute = "http://192.168.0.115/carattribute.php";
    String url_upload_carrealtime = "http://192.168.0.115/carrealtime.php";
    String url_upload_bmsrealtime = "http://192.168.0.115/bmsrealtime.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_main);
        // 控件初始化
        Init();
    }

    @Override
    public void onClick(View v) {
        DoHttpPostJson doHttpPostJson = new DoHttpPostJson();
        switch (v.getId())
        {
            case R.id.btn_totalcar: {
                doHttpPostJson.totalcar(mContext,url_upload_totalcar);
            }
                break;
            case R.id.btn_carattribute: {
                doHttpPostJson.carattribute(mContext,url_upload_carattribute);
            }
                break;
            case R.id.btn_carrealtime: {
                doHttpPostJson.carrealtime(mContext,url_upload_carrealtime);
            }
                break;
            case R.id.btn_bmsrealtime: {
                doHttpPostJson.bmsrealtime(mContext,url_upload_bmsrealtime);
            }
                break;
        }
    }

    /**
     * 功能：控件初始化
     */
    public void Init(){
        btn_totalcar = (Button) findViewById(R.id.btn_totalcar);
        btn_carattribute = (Button) findViewById(R.id.btn_carattribute);
        btn_carrealtime = (Button) findViewById(R.id.btn_carrealtime);
        btn_bmsrealtime = (Button) findViewById(R.id.btn_bmsrealtime);
        btn_totalcar.setOnClickListener(this);
        btn_carattribute.setOnClickListener(this);
        btn_carrealtime.setOnClickListener(this);
        btn_bmsrealtime.setOnClickListener(this);
        // 以下三条语句每次进行数据库操作时都必须先执行
        mContext = SecondActivity.this;
        myDBHelper = new MyDBOpenHelper(mContext, "db_ebike", null, 1);
        db = myDBHelper.getWritableDatabase();
        // 创建表
        myDBHelper.CreatTable(db);
    }

    /**
     * 功能：将数据写入本地数据库
     * 说明：1、属性类数据一般只传一次
     *       2、数据类数据需要定时传输一定的数据
     */
    public void Db_Insert(){
        new Thread() {
            @Override
            public void run() {
                /**
                 * 用户信息
                 */
//                String username = "神马代步车";               // 用户名
//                String password = "123";                     // 密码
//                String address = "123456@qq.com";            // 邮箱地址
//                String phonenumber = "12345678";             // 手机号
//                ContentValues values = new ContentValues();
//                values.put("username", username);
//                values.put("password", password);
//                values.put("address", address);
//                values.put("phonenumber", phonenumber);
//                db.insert("tb_user", null, values);
//                /**
//                 * 车辆类属性1
//                 */
                  String licensenumber = "车辆SN号";            // 车辆SN号
//                String product_date = "2015-12-21";  // 出厂日期
//                String type = "Shenma";                     // 车类型
//                String version = "V0.1";                  // 车辆版本号
//                ContentValues values = new ContentValues();
//                values.put("licensenumber", licensenumber);
//                values.put("product_date", product_date);
//                values.put("type", type);
//                values.put("version", version);
//                db.insert("tb_totalcar", null, values);
//                /**
//                 * BMS类属性1、2
//                 */
//                String bms_hardwareversion = "BMS2015";    // BMS硬件版本号
//                String bms_softwareversion = "BMS0.1";    // BMS软件版本号
//                int cell_cap_rated = 1000;             // 电池容量（额定）
//                String cell_type = "S123";              // 电池类型
//                int cell_box_nums = 100;              // 电池箱数
//                int cell_serial_nums = 100;           // 电池串数
//                double volalarmhigh = 21.5;            // 总电压上限报警
//                double volcutoffhigh = 21.5;           // 总电压上限切断
//                double volalarmlow = 21.5;             // 总电压欠压报警
//                double volcutofflow = 21.5;            // 总电压欠压切断
//                double volalarmhigh_single = 21.5;     // 单节电压上限报警
//                double volcutoffhigh_single = 21.5;    // 单节电压上限切断
//                double volalarmlow_single = 21.5;      // 单节电压欠压报警
//                double volcutofflow_single = 21.5;     // 单节电压欠压切断
//                double tmpalarmhigh = 21.5;            // 温度报警上限
//                double tmpcutoffhigh = 21.5;           // 温度切断上限
//                double tmpalarmlow = 21.5;             // 温度报警下限
//                double tmpcutofflow = 21.5;            // 温度切断下限
//                double in_elealarmhigh = 21.5;         // 充电过流报警
//                double in_elecutoffhigh = 21.5;        // 充电过流切断
//                double out_elealarmhigh = 21.5;        // 放电过流报警
//                double out_elecutoffhigh = 21.5;       // 放电过流切断
//                double voldiff_alarmhigh = 21.5;       // 压差过大报警
//                double voldiff_cutoffhigh = 21.5;      // 压差过大切断
//                ContentValues value1 = new ContentValues();
//                value1.put("licensenumber", licensenumber);
//                value1.put("bms_hardwareversion", bms_hardwareversion);
//                value1.put("bms_softwareversion", bms_softwareversion);
//                value1.put("cell_cap_rated", cell_cap_rated);
//                value1.put("cell_type", cell_type);
//                value1.put("cell_box_nums", cell_box_nums);
//                value1.put("cell_serial_nums", cell_serial_nums);
//                value1.put("volalarmhigh", volalarmhigh);
//                value1.put("volcutoffhigh", volcutoffhigh);
//                value1.put("volalarmlow", volalarmlow);
//                value1.put("volcutofflow", volcutofflow);
//                value1.put("volalarmhigh_single", volalarmhigh_single);
//                value1.put("volcutoffhigh_single", volcutoffhigh_single);
//                value1.put("volalarmlow_single", volalarmlow_single);
//                value1.put("volcutofflow_single", volcutofflow_single);
//                value1.put("tmpalarmhigh", tmpalarmhigh);
//                value1.put("tmpcutoffhigh", tmpcutoffhigh);
//                value1.put("tmpalarmlow", tmpalarmlow);
//                value1.put("tmpcutofflow", tmpcutofflow);
//                value1.put("in_elealarmhigh", in_elealarmhigh);
//                value1.put("in_elecutoffhigh", in_elecutoffhigh);
//                value1.put("out_elealarmhigh", out_elealarmhigh);
//                value1.put("out_elecutoffhigh", out_elecutoffhigh);
//                value1.put("voldiff_alarmhigh", voldiff_alarmhigh);
//                value1.put("voldiff_cutoffhigh", voldiff_cutoffhigh);
//                db.insert("tb_carattribute", null, value1);
//                /**
//                 * 车辆类实时1
//                 */
//                int speed = 10;             // 行车速度
//                int drive_dis_today = 10;   // 今日行驶里程
//                int drive_dis_total = 1000;   // 总的行驶里程
//                int drive_time_today = 250;  // 今日行驶时间
//                int drive_time_total = 1000;  // 总的行驶时间
//                int ischarge = 0;  // 车辆充电状态
//                int islock = 1;    // 车辆锁车状态
//                String gps = "HUST";           // GPS坐标
//                double air_quality = 255.5;    // 空气质量
//                ContentValues value2 = new ContentValues();
//                value2.put("licensenumber", licensenumber);
//                value2.put("speed", speed);
//                value2.put("drive_dis_today", drive_dis_today);
//                value2.put("drive_dis_total", drive_dis_total);
//                value2.put("drive_time_today", drive_time_today);
//                value2.put("drive_time_total", drive_time_total);
//                value2.put("ischarge", ischarge);
//                value2.put("islock", islock);
//                value2.put("gps", gps);
//                value2.put("air_quality", air_quality);
//                db.insert("tb_carrealtime", null, value2);
//                /**
//                 * BMS类实时1、2
//                 */
//                double max_vol = 0;             // 最大电压值
//                String max_vol_box = "L2";        // 最大电压箱号
//                String max_vol_position = "L2";   // 最大电压位置
//                double min_vol = 0;             // 最小电压值
//                String min_vol_box = "L2";        // 最小电压箱号
//                String min_vol_position = "L2";   // 最小电压位置
//                double max_temp = 0;            // 最大温度值
//                String max_temp_box = "L2";       // 最大温度箱号
//                String max_temp_position = "L2";  // 最大温度位置
//                double min_temp = 0;            // 最小温度值
//                String min_temp_box = "L2";       // 最小温度箱号
//                String min_temp_position = "L2";  // 最小温度位置
//                double max_difftemp = 0;        // 最大温度差
//                String max_difftemp_box = "L2";   // 最大温差箱号
//                double sys_totalvol = 0;        // 系统总电压
//                double soc = 0;                 // SOC
//                double soh = 0;                 // SOH
//                double cell_cap_left = 0;       // 电池剩余容量
//                int max_puttime = 0;            // 最大放置时间
//                double current_ele = 0;         // 当前电流
//                double remaining_power = 0;     // 剩余电量
//                double res_positive = 0;        // 对地绝缘电阻
//                double res_negative = 0;        // 对正绝缘电阻
//                double left_dis = 0;            // 剩余行驶里程
//                double cell_vol = 0;            // 电池电压
//                double cell_temp = 0;           // 单个温度
//                ContentValues value3 = new ContentValues();
//                value3.put("licensenumber", licensenumber);
//                value3.put("max_vol", max_vol);
//                value3.put("max_vol_box", max_vol_box);
//                value3.put("max_vol_position", max_vol_position);
//                value3.put("min_vol", min_vol);
//                value3.put("min_vol_box", min_vol_box);
//                value3.put("min_vol_position", min_vol_position);
//                value3.put("max_temp", max_temp);
//                value3.put("max_temp_box", max_temp_box);
//                value3.put("max_temp_position", max_temp_position);
//                value3.put("min_temp", min_temp);
//                value3.put("min_temp_box", min_temp_box);
//                value3.put("min_temp_position", min_temp_position);
//                value3.put("max_difftemp", max_difftemp);
//                value3.put("max_difftemp_box", max_difftemp_box);
//                value3.put("sys_totalvol", sys_totalvol);
//                value3.put("soc", soc);
//                value3.put("soh", soh);
//                value3.put("cell_cap_left", cell_cap_left);
//                value3.put("max_puttime", max_puttime);
//                value3.put("current_ele", current_ele);
//                value3.put("remaining_power", remaining_power);
//                value3.put("res_positive", res_positive);
//                value3.put("res_negative", res_negative);
//                value3.put("left_dis", left_dis);
//                value3.put("cell_vol", cell_vol);
//                value3.put("cell_temp", cell_temp);
//                db.insert("tb_bmsrealtime", null, value3);
            }
        }.start();
    }
}
