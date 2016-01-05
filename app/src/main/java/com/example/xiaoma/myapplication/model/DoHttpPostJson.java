package com.example.xiaoma.myapplication.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/23.
 */
public class DoHttpPostJson implements IDoHttpPostJson{
    private MyDBOpenHelper myDBHelper;
    private SQLiteDatabase db;
    SharedHelper sharedhelper = new SharedHelper();

    public DoHttpPostJson() {

    }

    /**
     * 功能：通过Post向服务器提交Json数据
     * 说明：车辆类属性1
     */
    @Override
    public void totalcar(final Context context ,final String url) {
        new Thread() {
            @Override
            public void run(){
                final String Url = url;
                Context mContext = context;
                myDBHelper = new MyDBOpenHelper(mContext, "db_ebike", null, 1);
                db = myDBHelper.getWritableDatabase();
                /**
                 * 提交的是Json字符串时，用JSONObject对象
                 */
                // int id ;
                String licensenumber = "";            // 车辆SN号
                String product_date = "";             // 出厂日期
                String type = "";                     // 车类型
                String version = "";                  // 车辆版本号
                JSONObject jsonObj = new JSONObject();
                Cursor cursor = db.query("tb_totalcar", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        //id = cursor.getInt(cursor.getColumnIndex("id"));
                        licensenumber = cursor.getString(cursor.getColumnIndex("licensenumber"));
                        product_date = cursor.getString(cursor.getColumnIndex("product_date"));
                        type = cursor.getString(cursor.getColumnIndex("type"));
                        version = cursor.getString(cursor.getColumnIndex("version"));
                        try {
                            jsonObj.put("licensenumber", licensenumber)
                                    .put("product_date", product_date)
                                    .put("type", type)
                                    .put("version", version);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } while (cursor.moveToNext());
                    //sharedhelper.save("id", "");
                }
                cursor.close();
                String jsonString = jsonObj.toString();

                // 指定Post参数
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("data", jsonString));

                try {
                    //发出HTTP请求
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(Url);
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    System.out.println("executing request " + httpPost.getURI());

                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    try {
                        HttpEntity httpEntity = httpResponse.getEntity();
                        if (httpEntity != null) {
                            System.out.println("--------------------------------------");
                            System.out.println("Response content: " + EntityUtils.toString(httpEntity, "UTF-8"));
                            System.out.println("--------------------------------------");
                        }
                    }
                    finally {
                        System.out.println("--------------------------------------");
                    }
                }
                catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 功能：通过Post向服务器提交Json数据
     * 说明：BMS类属性1、2
     */
    @Override
    public void carattribute(final Context context,final String url) {
        new Thread() {
            @Override
            public void run(){
                final String Url = url;
                Context mContext = context;
                myDBHelper = new MyDBOpenHelper(mContext, "db_ebike", null, 1);
                db = myDBHelper.getWritableDatabase();
                /**
                 * 提交的是Json字符串时，用JSONObject对象
                 */
                String licensenumber = "";          // 车牌号
                String bms_hardwareversion = "";    // BMS硬件版本号
                String bms_softwareversion = "";    // BMS软件版本号
                int cell_cap_rated = 0;             // 电池容量（额定）
                String cell_type = "";              // 电池类型
                int cell_box_nums = 0;              // 电池箱数
                int cell_serial_nums = 0;           // 电池串数
                double volalarmhigh = 0;            // 总电压上限报警
                double volcutoffhigh = 0;           // 总电压上限切断
                double volalarmlow = 0;             // 总电压欠压报警
                double volcutofflow = 0;            // 总电压欠压切断
                double volalarmhigh_single = 0;     // 单节电压上限报警
                double volcutoffhigh_single = 0;    // 单节电压上限切断
                double volalarmlow_single = 0;      // 单节电压欠压报警
                double volcutofflow_single = 0;     // 单节电压欠压切断
                double tmpalarmhigh = 0;            // 温度报警上限
                double tmpcutoffhigh = 0;           // 温度切断上限
                double tmpalarmlow = 0;             // 温度报警下限
                double tmpcutofflow = 0;            // 温度切断下限
                double in_elealarmhigh = 0;         // 充电过流报警
                double in_elecutoffhigh = 0;        // 充电过流切断
                double out_elealarmhigh = 0;        // 放电过流报警
                double out_elecutoffhigh = 0;       // 放电过流切断
                double voldiff_alarmhigh = 0;       // 压差过大报警
                double voldiff_cutoffhigh = 0;      // 压差过大切断
                JSONObject jsonObj = new JSONObject();
                Cursor cursor = db.query("tb_carattribute", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        //int id = cursor.getInt(cursor.getColumnIndex("id"));
                        licensenumber = cursor.getString(cursor.getColumnIndex("licensenumber"));
                        bms_hardwareversion = cursor.getString(cursor.getColumnIndex("bms_hardwareversion"));
                        bms_softwareversion = cursor.getString(cursor.getColumnIndex("bms_softwareversion"));
                        cell_cap_rated = cursor.getInt(cursor.getColumnIndex("cell_cap_rated"));
                        cell_type = cursor.getString(cursor.getColumnIndex("cell_type"));
                        cell_box_nums = cursor.getInt(cursor.getColumnIndex("cell_box_nums"));
                        cell_serial_nums = cursor.getInt(cursor.getColumnIndex("cell_serial_nums"));
                        volalarmhigh = cursor.getDouble(cursor.getColumnIndex("volalarmhigh"));
                        volcutoffhigh = cursor.getDouble(cursor.getColumnIndex("volcutoffhigh"));
                        volalarmlow = cursor.getDouble(cursor.getColumnIndex("volalarmlow"));
                        volcutofflow = cursor.getDouble(cursor.getColumnIndex("volcutofflow"));
                        volalarmhigh_single = cursor.getDouble(cursor.getColumnIndex("volalarmhigh_single"));
                        volcutoffhigh_single = cursor.getDouble(cursor.getColumnIndex("volcutoffhigh_single"));
                        volalarmlow_single = cursor.getDouble(cursor.getColumnIndex("volalarmlow_single"));
                        volcutofflow_single = cursor.getDouble(cursor.getColumnIndex("volcutofflow_single"));
                        tmpalarmhigh = cursor.getDouble(cursor.getColumnIndex("tmpalarmhigh"));
                        tmpcutoffhigh = cursor.getDouble(cursor.getColumnIndex("tmpcutoffhigh"));
                        tmpalarmlow = cursor.getDouble(cursor.getColumnIndex("tmpalarmlow"));
                        tmpcutofflow = cursor.getDouble(cursor.getColumnIndex("tmpcutofflow"));
                        in_elealarmhigh = cursor.getDouble(cursor.getColumnIndex("in_elealarmhigh"));
                        in_elecutoffhigh = cursor.getDouble(cursor.getColumnIndex("in_elecutoffhigh"));
                        out_elealarmhigh = cursor.getDouble(cursor.getColumnIndex("out_elealarmhigh"));
                        out_elecutoffhigh = cursor.getDouble(cursor.getColumnIndex("out_elecutoffhigh"));
                        voldiff_alarmhigh = cursor.getDouble(cursor.getColumnIndex("voldiff_alarmhigh"));
                        voldiff_cutoffhigh = cursor.getDouble(cursor.getColumnIndex("voldiff_cutoffhigh"));
                        try {
                            jsonObj.put("licensenumber", licensenumber)
                                    .put("bms_hardwareversion", bms_hardwareversion)
                                    .put("bms_softwareversion", bms_softwareversion)
                                    .put("cell_cap_rated", cell_cap_rated)
                                    .put("cell_type", cell_type)
                                    .put("cell_box_nums", cell_box_nums)
                                    .put("cell_serial_nums", cell_serial_nums)
                                    .put("volalarmhigh", volalarmhigh)
                                    .put("volcutoffhigh", volcutoffhigh)
                                    .put("volalarmlow", volalarmlow)
                                    .put("volcutofflow", volcutofflow)
                                    .put("volalarmhigh_single", volalarmhigh_single)
                                    .put("volcutoffhigh_single", volcutoffhigh_single)
                                    .put("volalarmlow_single", volalarmlow_single)
                                    .put("volcutofflow_single", volcutofflow_single)
                                    .put("tmpalarmhigh", tmpalarmhigh)
                                    .put("tmpcutoffhigh", tmpcutoffhigh)
                                    .put("tmpalarmlow", tmpalarmlow)
                                    .put("tmpcutofflow", tmpcutofflow)
                                    .put("in_elealarmhigh", in_elealarmhigh)
                                    .put("in_elecutoffhigh", in_elecutoffhigh)
                                    .put("out_elealarmhigh", out_elealarmhigh)
                                    .put("out_elecutoffhigh", out_elecutoffhigh)
                                    .put("voldiff_alarmhigh", voldiff_alarmhigh)
                                    .put("voldiff_cutoffhigh", voldiff_cutoffhigh);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } while (cursor.moveToNext());
                }
                cursor.close();
                String jsonString = jsonObj.toString();

                // 指定Post参数
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("data", jsonString));

                try {
                    //发出HTTP请求
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(Url);
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    System.out.println("executing request " + httpPost.getURI());

                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    try {
                        HttpEntity httpEntity = httpResponse.getEntity();
                        if (httpEntity != null) {
                            System.out.println("--------------------------------------");
                            System.out.println("Response content: " + EntityUtils.toString(httpEntity, "UTF-8"));
                            System.out.println("--------------------------------------");
                        }
                    }
                    finally {
                        System.out.println("--------------------------------------");
                    }
                }
                catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 功能：通过Post向服务器提交Json数据
     * 说明：车辆类实时1
     */
    @Override
    public void carrealtime(final Context context,final String url) {
        new Thread() {
            @Override
            public void run(){
                final String Url = url;
                Context mContext = context;
                myDBHelper = new MyDBOpenHelper(mContext, "db_ebike", null, 1);
                db = myDBHelper.getWritableDatabase();
                /**
                 * 提交的是Json数组时，用JSONArray数组
                 */
                String licensenumber = ""; // 车辆SN号
                int speed = 0;             // 行车速度
                int drive_dis_today = 0;   // 今日行驶里程
                int drive_dis_total = 0;   // 总的行驶里程
                int drive_time_today = 0;  // 今日行驶时间
                int drive_time_total = 0;  // 总的行驶时间
                int ischarge = 0;          // 车辆充电状态
                int islock = 0;            // 车辆锁车状态
                String gps = "";           // GPS坐标
                double air_quality = 0;    // 空气质量
                String uploadtime = "";
                JSONArray jsonArray = new JSONArray();
                Cursor cursor = db.query("tb_carrealtime", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        //int id = cursor.getInt(cursor.getColumnIndex("id"));
                        licensenumber = cursor.getString(cursor.getColumnIndex("licensenumber"));
                        speed = cursor.getInt(cursor.getColumnIndex("speed"));
                        drive_dis_today = cursor.getInt(cursor.getColumnIndex("drive_dis_today"));
                        drive_dis_total = cursor.getInt(cursor.getColumnIndex("drive_dis_total"));
                        drive_time_today = cursor.getInt(cursor.getColumnIndex("drive_time_today"));
                        drive_time_total = cursor.getInt(cursor.getColumnIndex("drive_time_total"));
                        ischarge = cursor.getInt(cursor.getColumnIndex("ischarge"));
                        islock = cursor.getInt(cursor.getColumnIndex("islock"));
                        gps = cursor.getString(cursor.getColumnIndex("gps"));
                        air_quality = cursor.getDouble(cursor.getColumnIndex("air_quality"));
                        try {
                            jsonArray.put(new JSONObject().put("licensenumber", licensenumber)
                                    .put("speed", speed)
                                    .put("drive_dis_today", drive_dis_today)
                                    .put("drive_dis_total", drive_dis_total)
                                    .put("drive_time_today", drive_time_today)
                                    .put("drive_time_total", drive_time_total)
                                    .put("ischarge", ischarge)
                                    .put("islock", islock)
                                    .put("gps", gps)
                                    .put("air_quality", air_quality)
                                    .put("uploadtime", uploadtime));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } while (cursor.moveToNext());
                }
                cursor.close();
                String jsonString = jsonArray.toString();

                // 指定Post参数
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("data", jsonString));

                try {
                    //发出HTTP请求
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(Url);
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    System.out.println("executing request " + httpPost.getURI());

                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    try {
                        HttpEntity httpEntity = httpResponse.getEntity();
                        if (httpEntity != null) {
                            System.out.println("--------------------------------------");
                            System.out.println("Response content: " + EntityUtils.toString(httpEntity, "UTF-8"));
                            System.out.println("--------------------------------------");
                        }
                    }
                    finally {
                        System.out.println("--------------------------------------");
                    }
                }
                catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 功能：通过Post向服务器提交Json数据
     * 说明：BMS类实时1、2
     */
    @Override
    public void bmsrealtime(final Context context,final String url) {
        new Thread() {
            @Override
            public void run(){
                final String Url = url;
                Context mContext = context;
                myDBHelper = new MyDBOpenHelper(mContext, "db_ebike", null, 1);
                db = myDBHelper.getWritableDatabase();
                /**
                 * 提交的是Json数组时，用JSONArray数组
                 */
                String licensenumber = "";  // 车辆SN号
                double max_vol = 0;             // 最大电压值
                String max_vol_box = "";        // 最大电压箱号
                String max_vol_position = "";   // 最大电压位置
                double min_vol = 0;             // 最小电压值
                String min_vol_box = "";        // 最小电压箱号
                String min_vol_position = "";   // 最小电压位置
                double max_temp = 0;            // 最大温度值
                String max_temp_box = "";       // 最大温度箱号
                String max_temp_position = "";  // 最大温度位置
                double min_temp = 0;            // 最小温度值
                String min_temp_box = "";       // 最小温度箱号
                String min_temp_position = "";  // 最小温度位置
                double max_difftemp = 0;        // 最大温度差
                String max_difftemp_box = "";   // 最大温差箱号
                double sys_totalvol = 0;        // 系统总电压
                double soc = 0;                 // SOC
                double soh = 0;                 // SOH
                double cell_cap_left = 0;       // 电池剩余容量
                int max_puttime = 0;            // 最大放置时间
                double current_ele = 0;         // 当前电流
                double remaining_power = 0;     // 剩余电量
                double res_positive = 0;        // 对地绝缘电阻
                double res_negative = 0;        // 对正绝缘电阻
                double left_dis = 0;            // 剩余行驶里程
                double cell_vol = 0;            // 电池电压
                double cell_temp = 0;           // 单个温度
                String uploadtime = "";
                JSONArray jsonArray = new JSONArray();
                Cursor cursor = db.query("tb_bmsrealtime", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        //int id = cursor.getInt(cursor.getColumnIndex("id"));
                        licensenumber = cursor.getString(cursor.getColumnIndex("licensenumber"));
                        max_vol = cursor.getDouble(cursor.getColumnIndex("max_vol"));
                        max_vol_box = cursor.getString(cursor.getColumnIndex("max_vol_box"));
                        max_vol_position = cursor.getString(cursor.getColumnIndex("max_vol_position"));
                        min_vol = cursor.getDouble(cursor.getColumnIndex("min_vol"));
                        min_vol_box = cursor.getString(cursor.getColumnIndex("min_vol_box"));
                        min_vol_position = cursor.getString(cursor.getColumnIndex("min_vol_position"));
                        max_temp = cursor.getDouble(cursor.getColumnIndex("max_temp"));
                        max_temp_box = cursor.getString(cursor.getColumnIndex("max_temp_box"));
                        max_temp_position = cursor.getString(cursor.getColumnIndex("max_temp_position"));
                        min_temp = cursor.getDouble(cursor.getColumnIndex("min_temp"));
                        min_temp_box = cursor.getString(cursor.getColumnIndex("min_temp_box"));
                        min_temp_position = cursor.getString(cursor.getColumnIndex("min_temp_position"));
                        max_difftemp = cursor.getDouble(cursor.getColumnIndex("max_difftemp"));
                        max_difftemp_box = cursor.getString(cursor.getColumnIndex("max_difftemp_box"));
                        sys_totalvol = cursor.getDouble(cursor.getColumnIndex("sys_totalvol"));
                        soc = cursor.getDouble(cursor.getColumnIndex("soc"));
                        soh = cursor.getDouble(cursor.getColumnIndex("soh"));
                        cell_cap_left = cursor.getDouble(cursor.getColumnIndex("cell_cap_left"));
                        max_puttime = cursor.getInt(cursor.getColumnIndex("max_puttime"));
                        current_ele = cursor.getDouble(cursor.getColumnIndex("current_ele"));
                        remaining_power = cursor.getDouble(cursor.getColumnIndex("remaining_power"));
                        res_positive = cursor.getDouble(cursor.getColumnIndex("res_positive"));
                        res_negative = cursor.getDouble(cursor.getColumnIndex("res_negative"));
                        left_dis = cursor.getDouble(cursor.getColumnIndex("left_dis"));
                        cell_vol = cursor.getDouble(cursor.getColumnIndex("cell_vol"));
                        cell_temp = cursor.getDouble(cursor.getColumnIndex("cell_temp"));
                        try {
                            jsonArray.put(new JSONObject().put("licensenumber", licensenumber)
                                    .put("max_vol", max_vol)
                                    .put("max_vol_box", max_vol_box)
                                    .put("max_vol_position", max_vol_position)
                                    .put("min_vol", min_vol)
                                    .put("min_vol_box", min_vol_box)
                                    .put("min_vol_position", min_vol_position)
                                    .put("max_temp", max_temp)
                                    .put("max_temp_box", max_temp_box)
                                    .put("max_temp_position", max_temp_position)
                                    .put("min_temp", min_temp)
                                    .put("min_temp_box", min_temp_box)
                                    .put("min_temp_position", min_temp_position)
                                    .put("max_difftemp", max_difftemp)
                                    .put("max_difftemp_box", max_difftemp_box)
                                    .put("sys_totalvol", sys_totalvol)
                                    .put("soc", soc)
                                    .put("soh", soh)
                                    .put("cell_cap_left", cell_cap_left)
                                    .put("max_puttime", max_puttime)
                                    .put("current_ele", current_ele)
                                    .put("remaining_power", remaining_power)
                                    .put("res_positive", res_positive)
                                    .put("res_negative", res_negative)
                                    .put("left_dis", left_dis)
                                    .put("cell_vol", cell_vol)
                                    .put("cell_temp", cell_temp)
                                    .put("uploadtime", uploadtime));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } while (cursor.moveToNext());
                }
                cursor.close();
                String jsonString = jsonArray.toString();
                // 指定Post参数
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("data", jsonString));
                try {
                    //发出HTTP请求
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(Url);
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    System.out.println("executing request " + httpPost.getURI());
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    try {
                        HttpEntity httpEntity = httpResponse.getEntity();
                        if (httpEntity != null) {
                            System.out.println("--------------------------------------");
                            System.out.println("Response content: " + EntityUtils.toString(httpEntity, "UTF-8"));
                            System.out.println("--------------------------------------");
                        }
                    }
                    finally {
                        System.out.println("--------------------------------------");
                    }
                }
                catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
