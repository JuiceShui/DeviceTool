package com.jojo.devicetool.ui.map.map;

import android.content.Context;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Created by Juice_ on 2017/6/1.
 */

public class AddOverLay {
    private Context mContext;
    private BaiduMap mMap;

    public AddOverLay(Context context, BaiduMap mMap) {
        this.mContext = context;
        this.mMap = mMap;
    }

    public void addGroundOverlay() {
        LatLng westSouth = new LatLng(30.651519781435443, 104.05130211645293);
        LatLng south = new LatLng(30.650455702780807, 104.05351194800748);
        LatLng eastSouth = new LatLng(30.650968326090975, 104.05642245785981);
        LatLng east = new LatLng(30.653228496132613, 104.0564494070251);
        LatLng eastNorth = new LatLng(30.654067308548743, 104.05635957647412);
        LatLng northHole = new LatLng(30.654440111707434, 104.05538940652333);
        LatLng northSharp = new LatLng(30.65480514672665, 104.05554211846002);
        LatLng north = new LatLng(30.655356579966636, 104.05423059241546);

/*
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(westSouth)
                .include(south)
                .include(eastSouth)
                .include(east)
                .include(eastNorth)
                .include(northHole)
                .include(northSharp)
                .include(north)
                .build();
        //定义Ground显示的图片
        BitmapDescriptor bdGround = BitmapDescriptorFactory
                .fromResource(R.drawable.wuhouci);
        //定义Ground覆盖物选项
        OverlayOptions ooGround = new GroundOverlayOptions()
                .positionFromBounds(bounds)
                .image(bdGround)
                .transparency(0.5f);
//在地图中添加Ground覆盖物
        mMap.addOverlay(ooGround);*/
        List<LatLng> pts = new ArrayList<LatLng>();
        pts.add(westSouth);
        pts.add(south);
        pts.add(eastSouth);
        pts.add(east);
        pts.add(eastNorth);
        pts.add(northHole);
        pts.add(northSharp);
        pts.add(north);
        //构建用户绘制多边形的Option对象
        OverlayOptions polygonOption = new PolygonOptions()
                .points(pts)
                .stroke(new Stroke(8, 0xAA00FF00))
                .fillColor(0xAAFFFF00);
//在地图上添加多边形Option，用于显示
        mMap.addOverlay(polygonOption);
    }
}
