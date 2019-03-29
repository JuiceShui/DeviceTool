package com.jojo.devicetool.ui.map.map;

import android.content.Context;

import com.baidu.mapapi.map.BaiduMap;

/**
 * Description:
 * Created by Juice_ on 2017/6/1.
 */

public class RouteUtils  {

    private Context mContext;
    private BaiduMap mMap;

    public RouteUtils(Context context, BaiduMap map) {
        this.mContext = context;
        this.mMap = map;
    }
}
