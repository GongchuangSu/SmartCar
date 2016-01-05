package com.example.xiaoma.myapplication.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.xiaoma.myapplication.R;
import com.example.xiaoma.myapplication.chart.BarChartActivity;
import com.example.xiaoma.myapplication.chart.ContentItem;
import com.example.xiaoma.myapplication.chart.MyAdapter;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/4.
 */
public class ChartActivity extends Activity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        setTitle("MPAndroidChart Example");

        // initialize the utilities
        Utils.init(this);

        ArrayList<ContentItem> objects = new ArrayList<ContentItem>();

        objects.add(new ContentItem("Line Chart", "A simple demonstration of the linechart."));
        objects.add(new ContentItem("Line Chart (Dual YAxis)",
                "Demonstration of the linechart with dual y-axis."));
        objects.add(new ContentItem("Bar Chart", "A simple demonstration of the bar chart."));
        objects.add(new ContentItem("Horizontal Bar Chart",
                "A simple demonstration of the horizontal bar chart."));
        objects.add(new ContentItem("Combined Chart",
                "Demonstrates how to create a combined chart (bar and line in this case)."));
        objects.add(new ContentItem("Pie Chart", "A simple demonstration of the pie chart."));
        objects.add(new ContentItem("Scatter Chart", "A simple demonstration of the scatter chart."));
        objects.add(new ContentItem("Bubble Chart", "A simple demonstration of the bubble chart."));
        objects.add(new ContentItem("Stacked Bar Chart",
                "A simple demonstration of a bar chart with stacked bars."));
        objects.add(new ContentItem("Stacked Bar Chart Negative",
                "A simple demonstration of stacked bars with negative and positive values."));
        objects.add(new ContentItem("Another Bar Chart",
                "Implementation of a BarChart that only shows values at the bottom."));
        objects.add(new ContentItem("Multiple Lines Chart",
                "A line chart with multiple DataSet objects. One color per DataSet."));
        objects.add(new ContentItem("Multiple Bars Chart",
                "A bar chart with multiple DataSet objects. One multiple colors per DataSet."));
        objects.add(new ContentItem(
                "Charts in ViewPager Fragments",
                "Demonstration of charts inside ViewPager Fragments. In this example the focus was on the design and look and feel of the chart."));
        objects.add(new ContentItem(
                "BarChart inside ListView",
                "Demonstrates the usage of a BarChart inside a ListView item."));
        objects.add(new ContentItem(
                "Multiple charts inside ListView",
                "Demonstrates the usage of different chart types inside a ListView."));
        objects.add(new ContentItem(
                "Inverted Line Chart",
                "Demonstrates the feature of inverting the y-axis."));
        objects.add(new ContentItem(
                "Candle Stick Chart",
                "Demonstrates usage of the CandleStickChart."));
        objects.add(new ContentItem(
                "Cubic Line Chart",
                "Demonstrates cubic lines in a LineChart."));
        objects.add(new ContentItem(
                "Radar Chart",
                "Demonstrates the use of a spider-web like (net) chart."));
        objects.add(new ContentItem(
                "Colored Line Chart",
                "Shows a LineChart with different background and line color."));
        objects.add(new ContentItem(
                "Realtime Chart",
                "This chart is fed with new data in realtime. It also restrains the view on the x-axis."));
        objects.add(new ContentItem(
                "Dynamical data adding",
                "This Activity demonstrates dynamical adding of Entries and DataSets (real time graph)."));
        objects.add(new ContentItem(
                "Performance Line Chart",
                "Renders up to 30.000 objects smoothly."));
        objects.add(new ContentItem(
                "Sinus Bar Chart",
                "A Bar Chart plotting the sinus function with 8.000 values."));
        objects.add(new ContentItem(
                "Chart in ScrollView",
                "This demonstrates how to use a chart inside a ScrollView."));

        MyAdapter adapter = new MyAdapter(this, objects);

        ListView lv = (ListView) findViewById(R.id.listView1);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> av, View v, int pos, long arg3) {
        Intent i;

        switch (pos) {
//            case 0:
//                i = new Intent(this, LineChartActivity1.class);
//                startActivity(i);
//                break;
//            case 1:
//                i = new Intent(this, LineChartActivity2.class);
//                startActivity(i);
//                break;
            case 2:
                i = new Intent(this, BarChartActivity.class);
                startActivity(i);
                break;
//            case 3:
//                i = new Intent(this, HorizontalBarChartActivity.class);
//                startActivity(i);
//                break;
//            case 4:
//                i = new Intent(this, CombinedChartActivity.class);
//                startActivity(i);
//                break;
//            case 5:
//                i = new Intent(this, PieChartActivity.class);
//                startActivity(i);
//                break;
//            case 6:
//                i = new Intent(this, ScatterChartActivity.class);
//                startActivity(i);
//                break;
//            case 7:
//                i = new Intent(this, BubbleChartActivity.class);
//                startActivity(i);
//                break;
//            case 8:
//                i = new Intent(this, StackedBarActivity.class);
//                startActivity(i);
//                break;
//            case 9:
//                i = new Intent(this, StackedBarActivityNegative.class);
//                startActivity(i);
//                break;
//            case 10:
//                i = new Intent(this, AnotherBarActivity.class);
//                startActivity(i);
//                break;
//            case 11:
//                i = new Intent(this, MultiLineChartActivity.class);
//                startActivity(i);
//                break;
//            case 12:
//                i = new Intent(this, BarChartActivityMultiDataset.class);
//                startActivity(i);
//                break;
//            case 13:
//                i = new Intent(this, SimpleChartDemo.class);
//                startActivity(i);
//                break;
//            case 14:
//                i = new Intent(this, ListViewBarChartActivity.class);
//                startActivity(i);
//                break;
//            case 15:
//                i = new Intent(this, ListViewMultiChartActivity.class);
//                startActivity(i);
//                break;
//            case 16:
//                i = new Intent(this, InvertedLineChartActivity.class);
//                startActivity(i);
//                break;
//            case 17:
//                i = new Intent(this, CandleStickChartActivity.class);
//                startActivity(i);
//                break;
//            case 18:
//                i = new Intent(this, CubicLineChartActivity.class);
//                startActivity(i);
//                break;
//            case 19:
//                i = new Intent(this, RadarChartActivitry.class);
//                startActivity(i);
//                break;
//            case 20:
//                i = new Intent(this, LineChartActivityColored.class);
//                startActivity(i);
//                break;
//            case 21:
//                i = new Intent(this, RealtimeLineChartActivity.class);
//                startActivity(i);
//                break;
//            case 22:
//                i = new Intent(this, DynamicalAddingActivity.class);
//                startActivity(i);
//                break;
//            case 23:
//                i = new Intent(this, PerformanceLineChart.class);
//                startActivity(i);
//                break;
//            case 24:
//                i = new Intent(this, BarChartActivitySinus.class);
//                startActivity(i);
//                break;
//            case 25:
//                i = new Intent(this, ScrollViewActivity.class);
//                startActivity(i);
//                break;
//            case 26:
//                i = new Intent(this, RealmMainActivity.class);
//                startActivity(i);
//                break;
        }

        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
    }
}