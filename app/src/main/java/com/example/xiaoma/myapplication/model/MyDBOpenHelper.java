package com.example.xiaoma.myapplication.model;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 自定义类MyDBOpenHelper继承SQLiteOpenHelper类
 */
public class MyDBOpenHelper extends SQLiteOpenHelper{
    @Override
    // 数据库第一次创建时被调用
    public void onCreate(SQLiteDatabase db) {

    }

    // 在构造函数中用super设置创建数据库及版本号
    public MyDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // 软件版本号发生改变时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("ALTER TABLE person ADD phone VARCHAR(12) NULL");
    }

    // 获取当前数据库版本号
    public int getVersion(SQLiteDatabase db) {
        return ((Long) DatabaseUtils.longForQuery(db, "PRAGMA user_version;", null)).intValue();
    }

    // 设置当前数据库版本号
    public void setVersion(SQLiteDatabase db, int version) {
        db.execSQL("PRAGMA user_version = " + version);
    }
    /**
     * 功能：创建表
     */
    public void CreatTable(final SQLiteDatabase db){
        // 新建一个线程
        new Thread() {
            @Override
            public void run() {
                if(!IsExist(db, "tb_user")) {
                    db.execSQL("CREATE TABLE tb_user " +
                            "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            " username      TEXT     NOT NULL, " +
                            " password      TEXT     NOT NULL, " +
                            " age           TEXT     NOT NULL, " +
                            " sex           TEXT     NOT NULL, " +
                            " address       TEXT     NOT NULL, " +
                            " phonenumber   TEXT     NOT NULL)" );
                }
                if(!IsExist(db, "tb_totalcar")) {
                    db.execSQL("CREATE TABLE tb_totalcar " +
                            "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            " licensenumber TEXT     NOT NULL, " +
                            " product_date  TEXT     NOT NULL, " +
                            " type          VARCHAR  NOT NULL, " +
                            " version       TEXT     NOT NULL)" );
                }
                if(!IsExist(db, "tb_carattribute")) {
                    db.execSQL("CREATE TABLE tb_carattribute " +
                            "(id INTEGER PRIMARY KEY  AUTOINCREMENT," +
                            " licensenumber  VARCHAR(50)   NOT NULL, " +
                            " bms_hardwareversion   VARCHAR(50)    NOT NULL, " +
                            " bms_softwareversion   VARCHAR(50)    NOT NULL, " +
                            " cell_cap_rated        INT            NOT NULL, " +
                            " cell_type             TEXT           NOT NULL, " +
                            " cell_box_nums         INT            NOT NULL, " +
                            " cell_serial_nums      INT            NOT NULL, " +
                            " volalarmhigh          DOUBLE         NOT NULL, " +
                            " volcutoffhigh         DOUBLE         NOT NULL, " +
                            " volalarmlow           DOUBLE         NOT NULL, " +
                            " volcutofflow          DOUBLE         NOT NULL, " +
                            " volalarmhigh_single   DOUBLE         NOT NULL, " +
                            " volcutoffhigh_single  DOUBLE         NOT NULL, " +
                            " volalarmlow_single    DOUBLE         NOT NULL, " +
                            " volcutofflow_single   DOUBLE         NOT NULL, " +
                            " tmpalarmhigh          DOUBLE         NOT NULL, " +
                            " tmpcutoffhigh         DOUBLE         NOT NULL, " +
                            " tmpalarmlow           DOUBLE         NOT NULL, " +
                            " tmpcutofflow          DOUBLE         NOT NULL, " +
                            " in_elealarmhigh       DOUBLE         NOT NULL, " +
                            " in_elecutoffhigh      DOUBLE         NOT NULL, " +
                            " out_elealarmhigh      DOUBLE         NOT NULL, " +
                            " out_elecutoffhigh     DOUBLE         NOT NULL, " +
                            " voldiff_alarmhigh     DOUBLE         NOT NULL, " +
                            " voldiff_cutoffhigh    DOUBLE         NOT NULL)" );
                }
                if(!IsExist(db, "tb_carrealtime")) {
                    db.execSQL("CREATE TABLE tb_carrealtime " +
                            "(id INTEGER PRIMARY KEY  AUTOINCREMENT," +
                            " licensenumber         VARCHAR(50)    NOT NULL, " +
                            " speed                 INT            NOT NULL, " +
                            " drive_dis_today       INT            NOT NULL, " +
                            " drive_dis_total       INT            NOT NULL, " +
                            " drive_time_today      INT            NOT NULL, " +
                            " drive_time_total      INT            NOT NULL, " +
                            " ischarge              INT            NOT NULL, " +
                            " islock                INT            NOT NULL, " +
                            " gps                   TEXT           NOT NULL, " +
                            " air_quality           DOUBLE         NOT NULL)" );
                }
                if(!IsExist(db, "tb_bmsrealtime")) {
                    db.execSQL("CREATE TABLE tb_bmsrealtime " +
                            "(id INTEGER PRIMARY KEY  AUTOINCREMENT," +
                            " licensenumber         VARCHAR(50)    NOT NULL, " +
                            " max_vol               DOUBLE         NOT NULL, " +
                            " max_vol_box           TEXT           NOT NULL, " +
                            " max_vol_position      TEXT           NOT NULL, " +
                            " min_vol               DOUBLE         NOT NULL, " +
                            " min_vol_box           TEXT           NOT NULL, " +
                            " min_vol_position      TEXT           NOT NULL, " +
                            " max_temp              DOUBLE         NOT NULL, " +
                            " max_temp_box          TEXT           NOT NULL, " +
                            " max_temp_position     TEXT           NOT NULL, " +
                            " min_temp              DOUBLE         NOT NULL, " +
                            " min_temp_box          TEXT           NOT NULL, " +
                            " min_temp_position     TEXT           NOT NULL, " +
                            " max_difftemp          DOUBLE         NOT NULL, " +
                            " max_difftemp_box      TEXT           NOT NULL, " +
                            " sys_totalvol          DOUBLE         NOT NULL, " +
                            " soc                   DOUBLE         NOT NULL, " +
                            " soh                   DOUBLE         NOT NULL, " +
                            " cell_cap_left         DOUBLE         NOT NULL, " +
                            " max_puttime           INT            NOT NULL, " +
                            " current_ele           DOUBLE         NOT NULL, " +
                            " remaining_power       DOUBLE         NOT NULL, " +
                            " res_positive          DOUBLE         NOT NULL, " +
                            " res_negative          DOUBLE         NOT NULL, " +
                            " left_dis              DOUBLE         NOT NULL, " +
                            " cell_vol              DOUBLE         NOT NULL, " +
                            " cell_temp             DOUBLE         NOT NULL)" );
                }
            }
        }.start();
    }

    /**
     * 功能：判断该表是否已存在于本地数据库中
     * @param "表名"
     */
    public boolean IsExist(final SQLiteDatabase db, String table){
        boolean exist = false;
        String sql = "select * from sqlite_master where name="+"'"+table+"'";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.getCount()!=0){
            exist = true;
        }
        return exist;
    }

}
