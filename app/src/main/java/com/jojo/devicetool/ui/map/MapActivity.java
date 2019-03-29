package com.jojo.devicetool.ui.map;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapBaseIndoorMapInfo;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.jojo.devicetool.R;
import com.jojo.devicetool.ui.map.map.AddOverLay;
import com.jojo.devicetool.ui.map.overlayutil.DrivingRouteOverlay;
import com.jojo.devicetool.ui.map.overlayutil.OverlayManager;
import com.jojo.devicetool.ui.map.overlayutil.TransitRouteOverlay;
import com.jojo.devicetool.ui.map.overlayutil.WalkingRouteOverlay;
import com.jojo.devicetool.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description: 地图
 * Created by Juice_ on 2017/5/31.
 */

public class MapActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.mv_map_main)
    MapView mvMapMain;
    @BindView(R.id.iv_location)
    ImageView ivLocation;
    @BindView(R.id.iv_begin_search)
    ImageView ivBeginSearch;
    @BindView(R.id.tv_bus)
    TextView tvBus;
    @BindView(R.id.tv_car)
    TextView tvCar;
    @BindView(R.id.tv_walk)
    TextView tvWalk;
    @BindView(R.id.tv_eat)
    TextView tvEat;
    @BindView(R.id.tv_sleep)
    TextView tvSleep;
    @BindView(R.id.tv_2d)
    TextView tv2D;
    @BindView(R.id.tv_3d)
    TextView tv3D;
    private BaiduMap mMap;
    private LocationClient mLocationClient;
    private BDLocationListener myListener = new MyLocationListener();
    private LocationClientOption option = new LocationClientOption();
    private boolean mIsFirst = true;
    private double mLan, mLon;
    private AddOverLay mOverlayHelper;
    private double mEndLan, mEndLon;
    private RoutePlanSearch mSearch;//路线规划
    private OverlayManager mOverlay;//路线图覆盖
    private RouteLine mRouteLine;//路线
    private boolean mMenuShowingFlag = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        if (mvMapMain == null) {
            mvMapMain = findViewById(R.id.mv_map_main);
        }
        mMap = mvMapMain.getMap();
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        initLocation();
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(new MyRoutePlanResultListener());
        ivLocation.setOnClickListener(this);
        ivBeginSearch.setOnClickListener(this);
        tv2D.setOnClickListener(this);
        tv3D.setOnClickListener(this);
        // mOverlayHelper = new AddOverLay(this, mMap);
        mMap.setOnMapClickListener(new MyMapClickListener());
        mMap.setOnMapLongClickListener(new MyMapLongClickListenr());
        mMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                if (mMenuShowingFlag) {
                    mMenuShowingFlag = !mMenuShowingFlag;
                    ivBeginSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {

            }
        });
        //mOverlayHelper.addGroundOverlay();

        mMap.setIndoorEnable(true);
        mMap.setOnBaseIndoorMapListener(new BaiduMap.OnBaseIndoorMapListener() {
            @Override
            public void onBaseIndoorMapMode(boolean b, MapBaseIndoorMapInfo mapBaseIndoorMapInfo) {

            }
        });
    }

    private void initLocation() {
        // 初始化定位参数
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
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
        mLocationClient.start();
    }

    /**
     * 重置
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_location:
                if (mLan != 0 && mLon != 0) {
                    setMapOverlay(new LatLng(mLan, mLon), false);
                }
                break;
            case R.id.iv_begin_search:
                if (mEndLan == 0) {
                    ToastUtil.showLongToast("还没选择终点呀");
                    return;
                }
                mMap.clear();
                PlanNode stNode = PlanNode.withLocation(new LatLng(Double.valueOf(mLan), Double.valueOf(mLon)));
                PlanNode enNode = PlanNode.withLocation(new LatLng(Double.valueOf(mEndLan), Double.valueOf(mEndLon)));
                // 实际使用中请对起点终点城市进行正确的设定
                //if () 判断是什么情况？？？步行？汽车？公交？？
                mSearch.walkingSearch(new WalkingRoutePlanOption().from(stNode).to(enNode));
                break;
            case R.id.tv_2d:
                mMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            case R.id.tv_3d:
                mMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
        }
    }

    /**
     * 回调
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            mLan = location.getLatitude();
            mLon = location.getLongitude();
            if (mIsFirst) {
                mIsFirst = false;
                LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
                setMapOverlay(point, true);
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    /**
     * 设点
     *
     * @param point
     * @param isFirst
     */
    private void setMapOverlay(LatLng point, boolean isFirst) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("onClick", false);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_location);
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap).extraInfo(bundle);
        mMap.addOverlay(option);
        mMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
    }

    /**
     * mapClick
     */
    private class MyMapClickListener implements BaiduMap.OnMapClickListener {

        @Override
        public void onMapClick(LatLng latLng) {
            if (!mMenuShowingFlag) {
                ivBeginSearch.setVisibility(View.VISIBLE);
                mMenuShowingFlag = !mMenuShowingFlag;
            }
        }

        @Override
        public boolean onMapPoiClick(MapPoi mapPoi) {
            return false;
        }
    }

    /**
     * mapLongClick
     */
    private class MyMapLongClickListenr implements BaiduMap.OnMapLongClickListener {

        @Override
        public void onMapLongClick(LatLng latLng) {
            System.out.println("Lan:" + latLng.latitude + "-------Lon:" + latLng.longitude);
            mEndLan = latLng.latitude;
            mEndLon = latLng.longitude;
            mMap.clear();
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_location);
            OverlayOptions optionStart = new MarkerOptions().position(new LatLng(mLan, mLon)).icon(bitmap);
            mMap.addOverlay(optionStart);
            BitmapDescriptor bitmap1 = BitmapDescriptorFactory.fromResource(R.drawable.icon_location_end);
            OverlayOptions optionsEnd = new MarkerOptions().position(latLng).icon(bitmap1);
            mMap.addOverlay(optionsEnd);
        }
    }

    /*
     * 路线图回调
     */
    private class MyRoutePlanResultListener implements OnGetRoutePlanResultListener {

        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
            if (walkingRouteResult == null || walkingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                ToastUtil.showLongToast("抱歉，未找到结果");
            }
            if (walkingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                // result.getSuggestAddrInfo()
                return;
            }
            if (walkingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                //nodeIndex = -1;
                //mBtnPre.setVisibility(View.VISIBLE);
                // mBtnNext.setVisibility(View.VISIBLE);
                mRouteLine = walkingRouteResult.getRouteLines().get(0);
                WalkingRouteOverlay overlay = new WalkingRouteOverlay(mMap);
                mMap.setOnMarkerClickListener(overlay);
                mOverlay = overlay;
                overlay.setData(walkingRouteResult.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }
        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
            if (transitRouteResult == null || transitRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                ToastUtil.showLongToast("抱歉，未找到结果");
            }
            if (transitRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                // result.getSuggestAddrInfo()
                return;
            }
            if (transitRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                mRouteLine = transitRouteResult.getRouteLines().get(0);
                TransitRouteOverlay overlay = new TransitRouteOverlay(mMap);
                mMap.setOnMarkerClickListener(overlay);
                mOverlay = overlay;
                overlay.setData(transitRouteResult.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }
        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
            if (drivingRouteResult == null | drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                ToastUtil.showLongToast("抱歉，未找到结果");
            }
            if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                // result.getSuggestAddrInfo()
                return;
            }
            if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                mRouteLine = drivingRouteResult.getRouteLines().get(0);
                DrivingRouteOverlay overlay = new DrivingRouteOverlay(mMap);
                mMap.setOnMarkerClickListener(overlay);
                mOverlay = overlay;
                overlay.setData(drivingRouteResult.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }
        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mvMapMain.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mvMapMain.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mvMapMain.onDestroy();
        mSearch.destroy();
    }


}
