package com.jojo.devicetool.ui.map;


/**
 * 此demo用来展示如何进行驾车、步行、公交路线搜索并在地图使用RouteOverlay、TransitOverlay绘制
 * 同时展示如何进行节点浏览并弹出泡泡
 */
/*
public class RoutePlanActivity extends Activity implements BaiduMap.OnMapClickListener,
        OnGetRoutePlanResultListener {
    // 浏览路线节点相关
    private double startLongitude;
    private double startLatitude;
    private double endLongitude;
    private double endLatitude;
    private LocationClient locationClient;
    private BDLocationListener listener = new MyLocationListener();
    Button mBtnPre = null; // 上一个节点
    Button mBtnNext = null; // 下一个节点
    int nodeIndex = -1; // 节点索引,供浏览节点时使用
    RouteLine route = null;
    OverlayManager routeOverlay = null;
    boolean useDefaultIcon = false;
    private TextView popupText = null; // 泡泡view
    private LinkedList<LocationEntity> locationList = new LinkedList<LocationEntity>();
    // 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
    // 如果不处理touch事件，则无需继承，直接使用MapView即可
    MapView mMapView = null;    // 地图View
    BaiduMap mBaidumap = null;
    // 搜索相关
    RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_routeplan);
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(listener);
        initLocation();
        CharSequence titleLable = "路线规划";
        setTitle(titleLable);
        // 初始化地图
        mMapView = (MapView) findViewById(R.id.map);
        mBaidumap = mMapView.getMap();
        mBtnPre = (Button) findViewById(R.id.pre);
        mBtnNext = (Button) findViewById(R.id.next);
        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);
        // 地图点击事件处理
        mBaidumap.setOnMapClickListener(this);
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        mBaidumap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                endLatitude = latLng.latitude;
                endLongitude = latLng.longitude;
                mBaidumap.clear();
                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.location_end);
                OverlayOptions optionStart = new MarkerOptions().position(new LatLng(startLatitude, startLongitude)).icon(bitmap);
                mBaidumap.addOverlay(optionStart);
                BitmapDescriptor bitmap1 = BitmapDescriptorFactory.fromResource(R.drawable.location);
                OverlayOptions optionsEnd = new MarkerOptions().position(latLng).icon(bitmap1);
                mBaidumap.addOverlay(optionsEnd);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
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
        locationClient.setLocOption(option);
        locationClient.start();
    }

    */
/**
     * 发起路线规划搜索示例
     *
     * @param v
     *//*


    public void searchButtonProcess(View v) {
        // 重置浏览节点的路线数据
        route = null;
        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);
        mBaidumap.clear();
        // 处理搜索按钮响应
//        EditText editSt = (EditText) findViewById(R.id.start);
//        EditText editEn = (EditText) findViewById(R.id.end);
        // 设置起终点信息，对于tranist search 来说，城市名无意义
        Intent intent = getIntent();
       */
/* String st_latitude = intent.getStringExtra("st_latitude");
        String st_longitude= intent.getStringExtra("st_longitude");
        String en_latitude = intent.getStringExtra("en_latitude");
        String en_longitude= intent.getStringExtra("en_longitude");*//*

        if (endLatitude == 0 || endLongitude == 0) {
            Toast.makeText(RoutePlanActivity.this, "还没有选择终点~~~", Toast.LENGTH_SHORT).show();
            return;
        }
        PlanNode stNode = PlanNode.withLocation(new LatLng(Double.valueOf(startLatitude), Double.valueOf(startLongitude)));
        PlanNode enNode = PlanNode.withLocation(new LatLng(Double.valueOf(endLatitude), Double.valueOf(endLongitude)));

        // 实际使用中请对起点终点城市进行正确的设定
        if (v.getId() == R.id.drive) {
            mSearch.drivingSearch((new DrivingRoutePlanOption())
                    .from(stNode).to(enNode));
        } else if (v.getId() == R.id.transit) {
            mSearch.transitSearch((new TransitRoutePlanOption())
                    .from(stNode).city("广州").to(enNode));
        } else if (v.getId() == R.id.walk) {
            mSearch.walkingSearch((new WalkingRoutePlanOption())
                    .from(stNode).to(enNode));
        } else if (v.getId() == R.id.bike) {
            mSearch.bikingSearch((new BikingRoutePlanOption())
                    .from(stNode).to(enNode));
        }
    }

    */
/**
     * 节点浏览示例
     *
     * @param v
     *//*

    public void nodeClick(View v) {
        if (route == null || route.getAllStep() == null) {
            return;
        }
        if (nodeIndex == -1 && v.getId() == R.id.pre) {
            return;
        }
        // 设置节点索引
        if (v.getId() == R.id.next) {
            if (nodeIndex < route.getAllStep().size() - 1) {
                nodeIndex++;
            } else {
                return;
            }
        } else if (v.getId() == R.id.pre) {
            if (nodeIndex > 0) {
                nodeIndex--;
            } else {
                return;
            }
        }
        // 获取节结果信息
        LatLng nodeLocation = null;
        String nodeTitle = null;
        Object step = route.getAllStep().get(nodeIndex);
        if (step instanceof DrivingRouteLine.DrivingStep) {
            nodeLocation = ((DrivingRouteLine.DrivingStep) step).getEntrance().getLocation();
            nodeTitle = ((DrivingRouteLine.DrivingStep) step).getInstructions();
        } else if (step instanceof WalkingRouteLine.WalkingStep) {
            nodeLocation = ((WalkingRouteLine.WalkingStep) step).getEntrance().getLocation();
            nodeTitle = ((WalkingRouteLine.WalkingStep) step).getInstructions();
        } else if (step instanceof TransitRouteLine.TransitStep) {
            nodeLocation = ((TransitRouteLine.TransitStep) step).getEntrance().getLocation();
            nodeTitle = ((TransitRouteLine.TransitStep) step).getInstructions();
        } else if (step instanceof BikingRouteLine.BikingStep) {
            nodeLocation = ((BikingRouteLine.BikingStep) step).getEntrance().getLocation();
            nodeTitle = ((BikingRouteLine.BikingStep) step).getInstructions();
        }

        if (nodeLocation == null || nodeTitle == null) {
            return;
        }
        // 移动节点至中心
        mBaidumap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
        // show popup
        popupText = new TextView(RoutePlanActivity.this);
        popupText.setBackgroundResource(R.drawable.popup);
        popupText.setTextColor(0xFF000000);
        popupText.setText(nodeTitle);
        mBaidumap.showInfoWindow(new InfoWindow(popupText, nodeLocation, 0));

    }

    */
/**
     * 切换路线图标，刷新地图使其生效
     * 注意： 起终点图标使用中心对齐.
     *//*

    public void changeRouteIcon(View v) {
        if (routeOverlay == null) {
            return;
        }
        if (useDefaultIcon) {
            ((Button) v).setText("自定义起终点图标");
            Toast.makeText(this,
                    "将使用系统起终点图标",
                    Toast.LENGTH_SHORT).show();

        } else {
            ((Button) v).setText("系统起终点图标");
            Toast.makeText(this,
                    "将使用自定义起终点图标",
                    Toast.LENGTH_SHORT).show();

        }
        useDefaultIcon = !useDefaultIcon;
        routeOverlay.removeFromMap();
        routeOverlay.addToMap();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(RoutePlanActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            mBtnPre.setVisibility(View.VISIBLE);
            mBtnNext.setVisibility(View.VISIBLE);
            route = result.getRouteLines().get(0);
            WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaidumap);
            mBaidumap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {

        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(RoutePlanActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            mBtnPre.setVisibility(View.VISIBLE);
            mBtnNext.setVisibility(View.VISIBLE);
            route = result.getRouteLines().get(0);
            TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
            mBaidumap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(RoutePlanActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            mBtnPre.setVisibility(View.VISIBLE);
            mBtnNext.setVisibility(View.VISIBLE);
            route = result.getRouteLines().get(0);
            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
            routeOverlay = overlay;
            mBaidumap.setOnMarkerClickListener(overlay);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
        if (bikingRouteResult == null || bikingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(RoutePlanActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (bikingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (bikingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            mBtnPre.setVisibility(View.VISIBLE);
            mBtnNext.setVisibility(View.VISIBLE);
            route = bikingRouteResult.getRouteLines().get(0);
            BikingRouteOverlay overlay = new MyBikingRouteOverlay(mBaidumap);
            routeOverlay = overlay;
            mBaidumap.setOnMarkerClickListener(overlay);
            overlay.setData(bikingRouteResult.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    private class MyTransitRouteOverlay extends TransitRouteOverlay {

        public MyTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    private class MyBikingRouteOverlay extends BikingRouteOverlay {
        public MyBikingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }


    }

    @Override
    public void onMapClick(LatLng point) {
        mBaidumap.hideInfoWindow();
    }

    @Override
    public boolean onMapPoiClick(MapPoi poi) {
        return false;
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mSearch.destroy();
        mMapView.onDestroy();
        super.onDestroy();
    }

    // 定位监听
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation loc) {
            if (loc != null && (loc.getLocType() == 161 || loc.getLocType() == 66)) {
                Bundle locData = algorithm(loc);
                Message locMsg = locHandler.obtainMessage();
                if (locData != null) {
                    locData.putParcelable("loc", loc);
                    locMsg.setData(locData);
                    locHandler.sendMessage(locMsg);
*/
/*
                    if (!toastLoad) {
                        Toast.makeText(RoutePlanActivity.this, "正在加载地图...", Toast.LENGTH_SHORT).show();
                    }
                    toastLoad = true;*//*

                    locationClient.stop();
                    LatLng ll = new LatLng(loc.getLatitude(),
                            loc.getLongitude());

                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(15.0f);
                    BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.location_end);
                    OverlayOptions endOption = new MarkerOptions().position(ll).icon(bitmap);
                    mBaidumap.addOverlay(endOption);
                    mBaidumap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
            } else {
                Toast.makeText(RoutePlanActivity.this, "定位失败，请检查手机网络或设置！", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    */
/***
     * 接收定位结果消息，并显示在地图上
     *//*

    private Handler locHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                BDLocation location = msg.getData().getParcelable("loc");

                startLatitude = location.getLatitude();
                startLongitude = location.getLongitude();

                Log.d("time", location.getTime());
                Log.d("error code", location.getLocType() + "");
                Log.d("startLatitude", location.getLatitude() + "");
                Log.d("startLongitude", location.getLongitude() + "");
                Log.d("radius", location.getRadius() + "");
                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    Log.d("GPS", "gps定位成功");
                    Log.d("speed", location.getSpeed() + " 单位：公里每小时");
                    Log.d("satellite", location.getSatelliteNumber() + "");
                    Log.d("height", location.getAltitude() + " 单位：米");
                    Log.d("direction", location.getDirection() + " 单位度");
                    Log.d("addr", location.getAddrStr());
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    Log.d("网络定位结", "网络定位成功");
                    Log.d("addr", location.getAddrStr());
                    Log.d("operationers", "运营商信息：" + location.getOperators());
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                    Log.d("离线定位", "离线定位成功，离线定位结果也是有效的");
               */
/* } else if (location.getLocType() == BDLocation.TypeServerError) {
                    Toast.makeText(LocationActivity.this, "服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因", Toast.LENGTH_LONG).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    Toast.makeText(LocationActivity.this, "网络不同导致定位失败，请检查网络是否通畅", Toast.LENGTH_LONG).show();
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    Toast.makeText(LocationActivity.this, "无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机", Toast.LENGTH_LONG).show();
                }*//*

                    Log.d("locationdescribe", location.getLocationDescribe());

                    List<Poi> list = location.getPoiList();// POI数据
                    StringBuffer sb = new StringBuffer("POI数据：");
                    if (list != null && list.size() > 0) {
                        sb.append("\npoilist size = : ");
                        sb.append(list.size());
                        for (Poi p : list) {
                            sb.append("\npoi= : ");
                            sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                        }
                    }

                    Log.d("POI数据", sb.toString());

                    //infoText.setText("经度：" + location.getLongitude() + "，纬度" + location.getLatitude()
                    //   + "\n" + location.getAddrStr()
                    //  + "\n" + location.getLocationDescribe());

                    if (location != null) {
                        LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
                        // setMapOverlay(point);
                        // baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };

    class LocationEntity {
        BDLocation location;
        long time;
    }

    private Bundle algorithm(BDLocation location) {
        float[] EARTH_WEIGHT = {0.1f, 0.2f, 0.4f, 0.6f, 0.8f}; // 推算计算权重_地球
        Bundle locData = new Bundle();
        double curSpeed = 0;
        if (locationList.isEmpty() || locationList.size() < 2) {
            LocationEntity temp = new LocationEntity();
            temp.location = location;
            temp.time = System.currentTimeMillis();
            locData.putInt("iscalculate", 0);
            locationList.add(temp);
        } else {
            if (locationList.size() > 5)
                locationList.removeFirst();
            double score = 0;
            for (int i = 0; i < locationList.size(); ++i) {
                LatLng lastPoint = new LatLng(locationList.get(i).location.getLatitude(),
                        locationList.get(i).location.getLongitude());
                LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
                double distance = DistanceUtil.getDistance(lastPoint, curPoint);
                curSpeed = distance / (System.currentTimeMillis() - locationList.get(i).time) / 1000;
                score += curSpeed * EARTH_WEIGHT[i];
            }
            if (score > 0.00000999 && score < 0.00005) {
                location.setLongitude(
                        (locationList.get(locationList.size() - 1).location.getLongitude() + location.getLongitude())
                                / 2);
                location.setLatitude(
                        (locationList.get(locationList.size() - 1).location.getLatitude() + location.getLatitude())
                                / 2);
                locData.putInt("iscalculate", 1);
            } else {
                locData.putInt("iscalculate", 0);
            }
            LocationEntity newLocation = new LocationEntity();
            newLocation.location = location;
            newLocation.time = System.currentTimeMillis();
            //locationList.add(newLocation);

        }
        return locData;
    }

    // 根据经纬度查询位置
    private void getInfoFromLAL(final LatLng point) {
        // pb.setVisibility(View.VISIBLE);
        //infoText.setText("经度：" + point.latitudeE6 + "，纬度" + point.latitudeE6);
        GeoCoder gc = GeoCoder.newInstance();
        gc.reverseGeoCode(new ReverseGeoCodeOption().location(point));
        gc.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                //  pb.setVisibility(View.GONE);
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Log.e("发起反地理编码请求", "未能找到结果");
                } else {
                    //  infoText.setText("经度：" + point.latitudeE6 + "\n，纬度" + point.latitudeE6
                    // + "\n" + result.getAddress());
                }
            }

            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {

            }
        });
    }

}
*/
