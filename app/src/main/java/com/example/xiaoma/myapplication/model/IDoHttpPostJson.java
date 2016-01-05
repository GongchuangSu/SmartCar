package com.example.xiaoma.myapplication.model;

import android.content.Context;

/**
 * Created by SGC on 2015/12/23.
 */
public interface IDoHttpPostJson {
    /**
     * 功能：通过Post向服务器提交Json数据
     * 说明：车辆类属性1
     */
    public void totalcar(final Context context,final String url);

    /**
     * 功能：通过Post向服务器提交Json数据
     * 说明：BMS类属性1、2
     */
    public void carattribute(final Context context,final String url);

    /**
     * 功能：通过Post向服务器提交Json数据
     * 说明：车辆类实时1
     */
    public void carrealtime(final Context context,final String url);

    /**
     * 功能：通过Post向服务器提交Json数据
     * 说明：BMS类实时1、2
     */
    public void bmsrealtime(final Context context,final String url);
}
