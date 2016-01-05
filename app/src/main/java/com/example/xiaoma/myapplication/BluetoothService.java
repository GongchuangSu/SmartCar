package com.example.xiaoma.myapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.xiaoma.myapplication.model.MyDBOpenHelper;


public class BluetoothService extends Service{

    public boolean threadFlag = true;
    MyThread myThread;
    CommandReceiver cmdReceiver;//继承自BroadcastReceiver对象，用于得到Activity发送过来的命令

    Context mContext;
    MyDBOpenHelper myDBHelper;
    private SQLiteDatabase db;

    /**************service 命令*********/
    static final int CMD_STOP_SERVICE = 0x01;
    static final int CMD_SEND_DATA = 0x02;
    static final int CMD_SYSTEM_EXIT =0x03;
    static final int CMD_SHOW_TOAST =0x04;

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private InputStream  inStream = null;
    public  boolean bluetoothFlag  = true;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //private static String address = "98:D3:31:40:16:BA"; // <==要连接的蓝牙设备MAC地址
    private  String myadress;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
    //前台Activity调用startService时，该方法自动执行
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        cmdReceiver = new CommandReceiver();
        IntentFilter filter = new IntentFilter();//创建IntentFilter对象
        filter.addAction("android.intent.action.cmd");
        registerReceiver(cmdReceiver, filter);

        mContext = BluetoothService.this;
        myDBHelper = new MyDBOpenHelper(mContext, "db_ebike", null, 1);
        db = myDBHelper.getWritableDatabase();
        myDBHelper.CreatTable(db);

        doJob();//调用方法启动线程
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        this.unregisterReceiver(cmdReceiver);//取消注册的CommandReceiver
        threadFlag = false;
        boolean retry = true;
        while(retry){
            try{
                myThread.join();
                retry = false;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    //启动连接的线程
    public class MyThread extends Thread{
        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            connectDevice();//连接蓝牙设备
            while(threadFlag){
                readByte();
                //showToast("enen");
                try{
                    Thread.sleep(50);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void doJob(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
       /* if (mBluetoothAdapter == null) {
            DisplayToast("蓝牙设备不可用，请打开蓝牙！");
            //mBluetoothAdapter.enable();
            bluetoothFlag  = false;
            return;
        }*/
        if (!mBluetoothAdapter.isEnabled()) {//意外关闭蓝牙的情况
            DisplayToast("请打开蓝牙并重新运行程序！");
            bluetoothFlag  = false;
            stopService();
            // showToast("请打开蓝牙并重新运行程序！");
            return;
        }
        if (mBluetoothAdapter.isEnabled()){
            //showToast("搜索到蓝牙设备!");
            mBluetoothAdapter.startDiscovery();
            Object[] lstDevice = mBluetoothAdapter.getBondedDevices().toArray();
            for (int i = 0; i < lstDevice.length; i++) {
                BluetoothDevice device = (BluetoothDevice) lstDevice[i];
                System.out.println("目前的循环是：    "+i);
                System.out.println(device.getName()+"111111111111111111111111111111111111");
                String abc=device.getName();

                if(abc.contains("HC-05")){
                    System.out.println(abc+"222222222222222222222222222222221");
                    myadress=device.getAddress();

                }
                String str = "已配对|" + device.getName() + "|"
                        + device.getAddress();
                //lstDevices.add(str); // 获取设备名称和mac地址
                // adtDevices.notifyDataSetChanged();
                Log.d("find!!!",str);
            }
            threadFlag = true;
            myThread = new MyThread();
            myThread.start();//在这里面开启线程
        }
    }
    public  void connectDevice(){
        DisplayToast("正在尝试连接蓝牙设备，请稍后····");
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(myadress);
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            DisplayToast("套接字创建失败！");
            bluetoothFlag = false;
        }
        DisplayToast("成功连接蓝牙设备！");
        mBluetoothAdapter.cancelDiscovery();
        try {
            btSocket.connect();//这里实现连接
            DisplayToast("连接成功建立，可以开始操控了!");
            //showToast("连接成功建立，可以开始操控了!");
            String[]my=new String[]{"1","2"};

            bluetoothFlag = true;
        } catch (IOException e) {
            try {
                btSocket.close();
                bluetoothFlag = false;
            } catch (IOException e2) {
                DisplayToast("连接没有建立，无法关闭套接字！");
            }
        }
        if(bluetoothFlag){
            try {
                DisplayToast("连接成功建立，2222222222222!");
                inStream = btSocket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            } //绑定读接口

            try {
                outStream = btSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            } //绑定写接口

        }
    }

    public void sendCmd(byte cmd, int value)//串口发送数据
    {
        if(!bluetoothFlag){
            return;
        }
        byte[] msgBuffer = new byte[5];
        msgBuffer[0] = cmd;
        msgBuffer[1] = (byte)(value >> 0  & 0xff);
        msgBuffer[2] = (byte)(value >> 8  & 0xff);
        msgBuffer[3] = (byte)(value >> 16 & 0xff);
        msgBuffer[4] = (byte)(value >> 24 & 0xff);

        try {
            outStream.write(msgBuffer, 0, 5);
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void  readByte(){//return -1 if no data
        int licensenumber=12345;
        Byte myCommand=0;
        int ret = -1;
        if(!bluetoothFlag){
            //return ret;
        }
        try {
            int count = 0;
            int readCount=0;
            while (count == 0) {
                count = inStream.available();
            }
            byte[] bytes = new byte[count];
            while(readCount<count ){
                readCount+=inStream.read(bytes,readCount,count-readCount);}
            System.out.println("count=:   " + count);
            if(count>1){
                System.out.println("count1234=:   "+count);
                switch (bytes[1]){
                    case 16:
                        int[] lic=new int[4];
                        for (int i=0;i<3;i+=4){
                            byte[] linshi=new byte[]{bytes[3+i],bytes[4+i],bytes[5+i],bytes[6+i]};
                            lic[i]=byte2int(linshi);
                        }

                        licensenumber = lic[0];            // 车辆SN号
                        int product_date = lic[1];  // 出厂日期
                        int type = lic[2];                     // 车类型
                        int version = lic[3];                  // 车辆版本号
                        ContentValues values = new ContentValues();
                        values.put("licensenumber", licensenumber);
                        values.put("product_date", product_date);
                        values.put("type", type);
                        values.put("version", version);
                        db.insert("tb_totalcar", null, values);

                        break;
                    case 21:
                        System.out.println("pandingtiaojian:  "+bytes[1]);
                        int[] shuju=new int[7];
                        for (int i=0;i<7;i+=4){
                            byte[] linshi=new byte[]{bytes[3+i],bytes[4+i],bytes[5+i],bytes[6+i]};
                            shuju[i]=byte2int(linshi);
                        }
                        int speed = shuju[0];             // 行车速度
                        int drive_dis_today = shuju[1];   // 今日行驶里程
                        int drive_can_today =shuju[2];    //可行驶里程
                        int drive_dis_total = shuju[3];   // 总的行驶里程
                        int drive_time_today = shuju[4];  // 今日行驶时间
                        int drive_time_total = shuju[5];  // 总的行驶时间
                        int power=shuju[6];   //电量
                        int ischarge = 0;  // 车辆充电状态
                        int islock = 1;    // 车辆锁车状态
                        String gps = "HUST";           // GPS坐标
                        double air_quality = 255.5;    // 空气质量
                        ContentValues value2 = new ContentValues();
                        value2.put("licensenumber", licensenumber);
                        value2.put("speed", speed);
                        value2.put("drive_dis_today", drive_dis_today);
                        value2.put("drive_dis_total", drive_dis_total);
                        value2.put("drive_time_today", drive_time_today);
                        value2.put("drive_time_total", drive_time_total);
                        value2.put("ischarge", ischarge);
                        value2.put("islock", islock);
                        value2.put("gps", gps);
                        value2.put("air_quality", air_quality);
                        db.insert("tb_carrealtime", null, value2);

                        Intent mainData=new Intent();
                        mainData.setAction("android.intent.action.mainData");
                        mainData.putExtra("speed", speed);
                        mainData.putExtra("todayRide",drive_dis_today);
                        mainData.putExtra("canRide",drive_can_today);
                        mainData.putExtra("todayTime",drive_time_today);
                        mainData.putExtra("power",power);
                        sendBroadcast(mainData);
                        break;
                    case 32:
                        int[] shuju1=new int[6];
                        for (int i=0;i<6;i+=4){
                            byte[] linshi=new byte[]{bytes[3+i],bytes[4+i],bytes[5+i],bytes[6+i]};
                            shuju1[i]=byte2int(linshi);
                        }

                        int[] batt=new int[18];
                        for (int i=0;i<17;i+=1){
                            byte linshi1= bytes[27+i];
                            batt[i]=(int)linshi1;
                        }


                        int bms_hardwareversion = shuju1[0];    // BMS硬件版本号
                        int bms_softwareversion = shuju1[1];    // BMS软件版本号
                        int cell_cap_rated = shuju1[2];             // 电池容量（额定）
                        int cell_type = shuju1[3];              // 电池类型
                        int cell_box_nums = shuju1[4];              // 电池箱数
                        int cell_serial_nums = shuju1[5];           // 电池串数
                        int volalarmhigh = batt[0];            // 总电压上限报警
                        int volcutoffhigh = batt[1];           // 总电压上限切断
                        int volalarmlow = batt[2];             // 总电压欠压报警
                        int volcutofflow = batt[3];            // 总电压欠压切断
                        int volalarmhigh_single = batt[4];     // 单节电压上限报警
                        int volcutoffhigh_single = batt[5];    // 单节电压上限切断
                        int volalarmlow_single = batt[6];      // 单节电压欠压报警
                        int volcutofflow_single = batt[7];     // 单节电压欠压切断
                        int tmpalarmhigh = batt[8];            // 温度报警上限
                        int tmpcutoffhigh = batt[9];           // 温度切断上限
                        int tmpalarmlow = batt[10];             // 温度报警下限
                        int tmpcutofflow = batt[11];            // 温度切断下限
                        int in_elealarmhigh = batt[12];         // 充电过流报警
                        int in_elecutoffhigh = batt[13];        // 充电过流切断
                        int out_elealarmhigh = batt[14];        // 放电过流报警
                        int out_elecutoffhigh = batt[15];       // 放电过流切断
                        int voldiff_alarmhigh = batt[16];       // 压差过大报警
                        int voldiff_cutoffhigh = batt[17];      // 压差过大切断
                        ContentValues value1 = new ContentValues();
                        value1.put("licensenumber", licensenumber);
                        value1.put("bms_hardwareversion", bms_hardwareversion);
                        value1.put("bms_softwareversion", bms_softwareversion);
                        value1.put("cell_cap_rated", cell_cap_rated);
                        value1.put("cell_type", cell_type);
                        value1.put("cell_box_nums", cell_box_nums);
                        value1.put("cell_serial_nums", cell_serial_nums);
                        value1.put("volalarmhigh", volalarmhigh);
                        value1.put("volcutoffhigh", volcutoffhigh);
                        value1.put("volalarmlow", volalarmlow);
                        value1.put("volcutofflow", volcutofflow);
                        value1.put("volalarmhigh_single", volalarmhigh_single);
                        value1.put("volcutoffhigh_single", volcutoffhigh_single);
                        value1.put("volalarmlow_single", volalarmlow_single);
                        value1.put("volcutofflow_single", volcutofflow_single);
                        value1.put("tmpalarmhigh", tmpalarmhigh);
                        value1.put("tmpcutoffhigh", tmpcutoffhigh);
                        value1.put("tmpalarmlow", tmpalarmlow);
                        value1.put("tmpcutofflow", tmpcutofflow);
                        value1.put("in_elealarmhigh", in_elealarmhigh);
                        value1.put("in_elecutoffhigh", in_elecutoffhigh);
                        value1.put("out_elealarmhigh", out_elealarmhigh);
                        value1.put("out_elecutoffhigh", out_elecutoffhigh);
                        value1.put("voldiff_alarmhigh", voldiff_alarmhigh);
                        value1.put("voldiff_cutoffhigh", voldiff_cutoffhigh);
                        db.insert("tb_carattribute", null, value1);

                        break;
                    case 33:
                        break;
                    case 37:
                        int[] so=new int[17];
                        for (int i=0;i<16;i+=1) {
                            byte linshi2 = bytes[3 + i];
                            so[i] = (int) linshi2;
                        }
                        int[] tem=new int[7];
                        for (int i=0;i<6;i+=4){
                            byte[] linshi=new byte[]{bytes[21+i],bytes[22+i],bytes[23+i],bytes[24+i]};
                            tem[i]=byte2int(linshi);
                        }

                        int max_vol = so[0];             // 最大电压值
                        int max_vol_box = so[1];        // 最大电压箱号
                        int max_vol_position = so[2];   // 最大电压位置
                        int min_vol = so[3];             // 最小电压值
                        int min_vol_box = so[4];        // 最小电压箱号
                        int min_vol_position = so[5];   // 最小电压位置
                        int max_temp = so[6];            // 最大温度值
                        int max_temp_box = so[7];       // 最大温度箱号
                        int max_temp_position = so[8];  // 最大温度位置
                        int min_temp = so[9];            // 最小温度值
                        int min_temp_box = so[10];       // 最小温度箱号
                        int min_temp_position = so[11];  // 最小温度位置
                        int max_difftemp = so[12];        // 最大温度差
                        int max_difftemp_box = so[13];   // 最大温差箱号
                        int sys_totalvol = so[14];        // 系统总电压
                        int soc = so[15];                 // SOC
                        int soh = so[16];// SOH

                        int cell_cap_left = tem[0];       // 电池剩余容量
                        int max_puttime = tem[1];            // 最大放置时间
                        int current_ele = tem[2];         // 当前电流
                        int remaining_power = tem[3];     // 剩余电量
                        int res_positive = tem[4];        // 对地绝缘电阻
                        int res_negative = tem[5];        // 对正绝缘电阻
                        int left_dis = tem[6];            // 剩余行驶里程
                        int cell_vol = 12;            // 电池电压
                        int cell_temp = 13;           // 单个温度
                        ContentValues value3 = new ContentValues();
                        value3.put("licensenumber", licensenumber);
                        value3.put("max_vol", max_vol);
                        value3.put("max_vol_box", max_vol_box);
                        value3.put("max_vol_position", max_vol_position);
                        value3.put("min_vol", min_vol);
                        value3.put("min_vol_box", min_vol_box);
                        value3.put("min_vol_position", min_vol_position);
                        value3.put("max_temp", max_temp);
                        value3.put("max_temp_box", max_temp_box);
                        value3.put("max_temp_position", max_temp_position);
                        value3.put("min_temp", min_temp);
                        value3.put("min_temp_box", min_temp_box);
                        value3.put("min_temp_position", min_temp_position);
                        value3.put("max_difftemp", max_difftemp);
                        value3.put("max_difftemp_box", max_difftemp_box);
                        value3.put("sys_totalvol", sys_totalvol);
                        value3.put("soc", soc);
                        value3.put("soh", soh);
                        value3.put("cell_cap_left", cell_cap_left);
                        value3.put("max_puttime", max_puttime);
                        value3.put("current_ele", current_ele);
                        value3.put("remaining_power", remaining_power);
                        value3.put("res_positive", res_positive);
                        value3.put("res_negative", res_negative);
                        value3.put("left_dis", left_dis);
                        value3.put("cell_vol", cell_vol);
                        value3.put("cell_temp", cell_temp);
                        db.insert("tb_bmsrealtime", null, value3);
                        break;


                    default:
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopService(){//停止服务
        threadFlag = false;//停止线程
        stopSelf();//停止服务
    }

    //发送给MainActivity的广播
    public void showToast(String str){//显示提示信息
        Intent intent = new Intent();
        intent.putExtra("cmd", CMD_SHOW_TOAST);
        intent.putExtra("str", str);
        intent.setAction("android.intent.action.lxx");
        sendBroadcast(intent);
    }

    public void DisplayToast(String str)
    {
        Log.d("Season", str);
    }

    //接收Activity传送过来的命令
    private class CommandReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("android.intent.action.cmd")){
                int cmd = intent.getIntExtra("cmd", -1);//获取Extra信息
                if(cmd == CMD_STOP_SERVICE){
                    stopService();
                }

                if(cmd == CMD_SEND_DATA)
                {
                    byte command = intent.getByteExtra("command", (byte) 0);
                    int value =  intent.getIntExtra("value", 0);
                    sendCmd(command,value);
                }
            }
        }
    }
    //转化字符串为十六进制编码
    public static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }
    // 转化十六进制编码为字符串
    public static String toStringHex1(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(
                        i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "utf-8");// UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    public static String bytesToHexString(byte[] bytes) {
        String result = "";
        for (int i = 0; i < bytes.length; i++) {
            String hexString = Integer.toHexString(bytes[i] & 0xFF);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            result += hexString.toUpperCase();
        }
        return result;
    }
    public static int byte2int(byte[] res) {
// 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000

        int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | 表示安位或
                | ((res[2] << 24) >>> 8) | (res[3] << 24);
        return targets;
    }
}