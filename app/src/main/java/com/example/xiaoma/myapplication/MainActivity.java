package com.example.xiaoma.myapplication;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.example.xiaoma.myapplication.chart.BarChartActivity;
import com.example.xiaoma.myapplication.view.AttrActivity;
import com.example.xiaoma.myapplication.view.LoginActivity;
import com.example.xiaoma.myapplication.view.SecondActivity;
import com.example.xiaoma.myapplication.view.UserInfo;
import com.github.glomadrian.velocimeterlibrary.VelocimeterView;

import java.util.List;
import java.util.logging.Handler;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    MyReceiver receiver;

    static final int CMD_STOP_SERVICE = 0x01;
    static final int CMD_SEND_DATA = 0x02;
    static final int CMD_SYSTEM_EXIT =0x03;
    static final int CMD_SHOW_TOAST =0x04;
    private int progress = 0;
    int data;
    private ProgressBar mProgressBar;
    TextView textView;
    int value = 0;
    BluetoothAdapter actBluetoothAdapter;
    private Context mContext;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private String username = "";


    private MapView mMapView = null;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    //仪表盘
    private VelocimeterView velocimeter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;

        //侧滑菜单
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //地图相关
        mMapView = (MapView) findViewById(R.id.bmapView);
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initLocation();
        mLocationClient.start();



        actBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if (!actBluetoothAdapter.isEnabled()){

            System.out.println("121212121212");
            //alert = null;
            builder = new AlertDialog.Builder(mContext);
            alert = builder
                    //setIcon(R.mipmap.ic_icon_fish)
                    .setTitle("系统提示：")
                    .setMessage("设备请求开启蓝牙")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(mContext, "你点击了取消按钮~", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(mContext, "你点击了确定按钮~", Toast.LENGTH_SHORT).show();
                            actBluetoothAdapter.enable();
                            showToast("蓝牙开启成功！");
                            value=1;
                        }
                    })
                    .create();             //创建AlertDialog对象
            alert.show();
        }else {
            value=1;
        }
        velocimeter = (VelocimeterView) findViewById(R.id.velocimeter1);
        Handler handler1;
        Button mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "click");
               /* if (actBluetoothAdapter.isEnabled() && value == 1) {
                    Intent intentService = new Intent(MainActivity.this, BluetoothService.class);
                    //showToast("3");
                    startService(intentService);
                    showToast("4");
                    value = 2;
                }
                byte command = 45;
                int value = 0x12345;
                sendCmd(command, value);*/
                //绑定Service
               velocimeter.setValue(10);
            }
        });

        // 登录点击事件
        View headerView = navigationView.getHeaderView(0);
        final TextView Login = (TextView) headerView.findViewById(R.id.Login);
        Login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if("未登录".equals(Login.getText())) {
                    Intent loginactivity = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(loginactivity);

                }
                else{
                    Intent userinfo = new Intent(MainActivity.this, UserInfo.class);
                    // 通过Bundle对象存储需要传递的数据
                    Bundle bundle = new Bundle();
                    bundle.putString("username", Login.getText().toString());
                    // 把bundle对象assign给Intent
                    userinfo.putExtras(bundle);
                    startActivity(userinfo);
                }
            }
        });
        // 传递用户信息
        TextView Login_exp=(TextView) headerView.findViewById(R.id.Login_exp);
        username = this.getIntent().getStringExtra("username");
        if(username != null ){
            Login.setText(username);
            Login_exp.setText("");
        }
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        receiver = new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("android.intent.action.lxx");
        registerReceiver(receiver, filter);
        mMapView.onResume();
    }
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if(intent.getAction().equals("android.intent.action.lxx")){
                Bundle bundle = intent.getExtras();
                int cmd = bundle.getInt("cmd");

                if(cmd == CMD_SHOW_TOAST){
                    String str = bundle.getString("str");
                    showToast(str);
                }

                else if(cmd == CMD_SYSTEM_EXIT){
                    System.exit(0);
                }
            }
        }
    }

    public void showToast(String str){//显示提示信息
        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
    }
    public void sendCmd(byte command, int value){
        Intent intent = new Intent();//创建Intent对象
        intent.setAction("android.intent.action.cmd");
        intent.putExtra("cmd", CMD_SEND_DATA);
        intent.putExtra("command", command);
        intent.putExtra("value", value);
        sendBroadcast(intent);//发送广播
    }

    @Override
    protected void onDestroy() {
        if(receiver!=null){
           unregisterReceiver(receiver);
        }
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.attributes) {
            Intent attribute=new Intent(MainActivity.this, AttrActivity.class);
            startActivity(attribute);
        }
        if (id == R.id.nav_gallery) {
            Intent web=new Intent(MainActivity.this, SecondActivity.class);
            startActivity(web);
        }
        if (id == R.id.statistics) {
            Intent  statistic = new Intent(this, BarChartActivity.class);
            startActivity(statistic);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //百度地图的相关方法和时间监听
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            //Log.i("BaiduLocationApiDem", sb.toString());
        }
    }
}















