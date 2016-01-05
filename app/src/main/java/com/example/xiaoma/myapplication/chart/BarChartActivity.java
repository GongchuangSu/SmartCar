
package com.example.xiaoma.myapplication.chart;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.example.xiaoma.myapplication.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class BarChartActivity extends DemoBase implements OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    protected BarChart mChart;

    private Typeface mTf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_barchart);

        mChart = (BarChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBorders(false);       // 设置是否显示边框
        mChart.setDrawBarShadow(false);     // 设置是否显示阴影
        mChart.setDrawValueAboveBar(true);  // 设置数值显示位置
        mChart.setDescription("");          // 设置数据描述
        mChart.setTouchEnabled(true);       // 设置是否可以触摸
        mChart.setPinchZoom(true);          // if false, scaling can only be done on x- and y-axis separately
        mChart.setMaxVisibleValueCount(60); // 设置数值显示的数目上限值
        mChart.setDrawGridBackground(true); // 设置是否显示表格颜色
        // 设置表格背景颜色
        mChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF);
        // 设置字体
        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);

        // 设置数字显示格式
        YAxisValueFormatter custom = new MyYAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(10, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(10, false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);

        // 设置标示（DataSet）
        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.BELOW_CHART_LEFT);
        l.setForm(LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // 设置数据
        setData(12, 90);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar, menu);
        return true;
    }

    /**
     * 功能：横、纵坐标赋值
     */
    private void setData(int count, float range) {
        // 横坐标赋值
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add(mMonths[i % 12]);
        }
        // 纵坐标赋值
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < count; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult);
            yVals1.add(new BarEntry(val, i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "DataSet");
        // 设置bar之间空白区域的占比
        set1.setBarSpacePercent(50f);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);
        data.setValueTypeface(mTf);

        mChart.setData(data);
    }

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        return true;

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    public void onNothingSelected() {
    };
}
