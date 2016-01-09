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
import android.support.v4.view.ViewPager;
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
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.xiaoma.myapplication.chart.BarChartActivity;
import com.example.xiaoma.myapplication.chart.HorizontalBarChartActivity;
import com.example.xiaoma.myapplication.chart.PieChartActivity;
import com.example.xiaoma.myapplication.view.AttrActivity;
import com.example.xiaoma.myapplication.view.LoginActivity;
import com.example.xiaoma.myapplication.view.SecondActivity;
import com.example.xiaoma.myapplication.view.UserInfoActivity;
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
    BaiduMap mBaiduMap;
    boolean isFirstLoc =true;// 是否首次定位
    Button GPS_btn;
    // 仪表盘
    private VelocimeterView velocimeter;
    // 引导页
    private View view1,view2,view3;
    private List<View> viewList;
    ViewPager mViewPager;

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
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMaxAndMinZoomLevel(19, 11);//设置缩放级别

        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数

        mLocationClient.requestLocation();//定位请求

        //LocationClientOption option = new LocationClientOption();
        initLocation();

        // option.setOpenGps(true);// 打开GPS
        //option.setCoorType("bd09ll"); // 设置坐标类型
        //option.setScanSpan(1000);
        mLocationClient.start();


        GPS_btn=(Button)findViewById(R.id.location);
        GPS_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                isFirstLoc = true;
                // 开启定位图层
               // mBaiduMap.setMyLocationEnabled(true);
                // 定位初始化
               // mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
                //mLocationClient.registerLocationListener(myListener);    //注册监听函数

                //mLocationClient.requestLocation();//定位请求

                //LocationClientOption option = new LocationClientOption();
                //initLocation();

                // option.setOpenGps(true);// 打开GPS
                //option.setCoorType("bd09ll"); // 设置坐标类型
                //option.setScanSpan(1000);
                mLocationClient.start();
            }
        });

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
                velocimeter.setValue(60);
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
                    Intent userinfo = new Intent(MainActivity.this, UserInfoActivity.class);
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
        if (id == R.id.horizontalbarchart) {
            Intent  horizontalbarchart = new Intent(this, HorizontalBarChartActivity.class);
            startActivity(horizontalbarchart);
        }
        if (id == R.id.piechartactivity) {
            Intent  piechartactivity = new Intent(this, PieChartActivity.class);
            startActivity(piechartactivity);
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
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().build()));
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
        }
        public void onReceivePoi(BDLocation poiLocation) {
        }

    }
}















