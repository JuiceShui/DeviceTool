package com.jojo.devicetool.ui.location;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.location.GnssStatus;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jojo.devicetool.R;
import com.jojo.devicetool.ui.BaseFragment;
import com.jojo.devicetool.widget.CommonItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.LOCATION_SERVICE;
import static android.support.constraint.Constraints.TAG;

/**
 * Description: Jojo on 2019/3/27
 */
public class LocationFragment extends BaseFragment {
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    LocationManager mLocationManager;
    @BindView(R.id.ll_gps)
    LinearLayout llGps;


    @Override
    protected int getLayout() {
        return R.layout.fragment_location;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    @Override
    protected void initView() {
        mLocationManager = (LocationManager) mActivity.getSystemService(LOCATION_SERVICE);
        Location myLocation = getLastKnownLocation();
        CommonItem type = createCommon("坐标类型", "WGS-84");
        CommonItem lon = createCommon("经度", myLocation.getLongitude() + "");
        CommonItem lat = createCommon("维度", myLocation.getLatitude() + "");
        CommonItem pro = createCommon("定位方式", myLocation.getProvider() + "");
        CommonItem quality = createCommon("精度", myLocation.getAccuracy() + "");
        CommonItem hor = createCommon("海拔", myLocation.getAltitude() + "");

        CommonItem address = createCommon("地址", getLocationAddress(myLocation));
        CommonItem bering = createCommon("方向", myLocation.getBearing() + "");
        /*if (myLocation.hasBearing()) {// 偏离正北方向的角度
            sb.append("\n方向：" + myLocation.getBearing());
        }*/
        String spe;
        /*if (myLocation.hasSpeed()) {
            if (myLocation.getSpeed() * 3.6 < 5) {
                sb.append("\n速度：0.0km/h");
            } else {
                sb.append("\n速度：" + myLocation.getSpeed() * 3.6 + "km/h");
            }

        }*/
        if (myLocation.getSpeed() * 3.6 < 5) {
            spe = "0.0km/h";
        } else {
            spe = myLocation.getSpeed() * 3.6 + "km/h";
        }
        CommonItem speed = createCommon("速度", spe);
        llContainer.addView(type);
        llContainer.addView(lon);
        llContainer.addView(lat);
        llContainer.addView(pro);
        llContainer.addView(quality);
        llContainer.addView(hor);
        llContainer.addView(speed);
        llContainer.addView(bering);
        llContainer.addView(address);
        mLocationManager.addGpsStatusListener(statusListener);
        mLocationManager.addGpsStatusListener(listener);
        mLocationManager.registerGnssStatusCallback(new GnssStatus.Callback() {
            @Override
            public void onStarted() {
                super.onStarted();
            }

            @Override
            public void onStopped() {
                super.onStopped();
            }

            @Override
            public void onFirstFix(int ttffMillis) {
                super.onFirstFix(ttffMillis);
            }

            @Override
            public void onSatelliteStatusChanged(GnssStatus status) {
                super.onSatelliteStatusChanged(status);
            }
        });
    }

    private Location getLastKnownLocation() {
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    protected void initData() {

    }

    private CommonItem createCommon(String label, String value) {
        CommonItem commonItem = new CommonItem(mActivity);
        commonItem.setLeftText(label);
        if (TextUtils.isEmpty(value)) {
            value = "";
        }
        commonItem.setRightText(value);
        return commonItem;
    }

    private String getLocationAddress(Location location) {
        String add = "";
        Geocoder geoCoder = new Geocoder(mContext, Locale.CHINESE);
        try {
            List<Address> addresses = geoCoder.getFromLocation(
                    location.getLatitude(), location.getLongitude(),
                    100);
            Address address = addresses.get(0);
            Log.i("TT", "getLocationAddress: " + address.toString());
            // Address[addressLines=[0:"中国",1:"北京市海淀区",2:"华奥饭店公司写字间中关村创业大街"]latitude=39.980973,hasLongitude=true,longitude=116.301712]
            int maxLine = address.getMaxAddressLineIndex();
            if (maxLine >= 2) {
                add = address.getAdminArea() +
                        address.getLocality() +
                        address.getAddressLine(1)
                        + address.getAddressLine(2);
            } else {
                add = address.getAdminArea() +
                        address.getLocality()
                        + address.getAddressLine(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return add;
    }

    private List<GpsSatellite> numSatelliteList = new ArrayList<GpsSatellite>(); // 卫星信号

    private final GpsStatus.Listener statusListener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) { // GPS状态变化时的回调，如卫星数
            if (!getUserVisibleHint() || llContainer == null || llGps == null) {
                return;
            }
            @SuppressLint("MissingPermission")
            GpsStatus status = mLocationManager.getGpsStatus(null); //取当前状态
            String satelliteInfo = updateGpsStatus(event, status);
            //tv_satellites.setText(null);
            //tv_satellites.setText(satelliteInfo);
            CommonItem item = (CommonItem) llContainer.getChildAt(llContainer.getChildCount() - 1);
            if (item.getLeftText().equals("卫星数量")) {
                llContainer.removeView(item);
            }
            CommonItem commonItem = createCommon("卫星数量", satelliteInfo + "");
            llContainer.addView(commonItem);
            llGps.removeAllViews();
            for (GpsSatellite gps : numSatelliteList) {
                View view = createStaList(gps);
                llGps.addView(view);
            }
        }
    };
    GpsStatus.Listener listener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            switch (event) {
                //第一次定位
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    Log.i(TAG, "第一次定位");
                    break;
                //卫星状态改变
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    Log.i(TAG, "卫星状态改变");
                    //获取当前状态
                    @SuppressLint("MissingPermission")
                    GpsStatus gpsStatus = mLocationManager.getGpsStatus(null);
                    //获取卫星颗数的默认最大值
                    int maxSatellites = gpsStatus.getMaxSatellites();
                    //创建一个迭代器保存所有卫星
                    Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                    int count = 0;
                    while (iters.hasNext() && count <= maxSatellites) {
                        GpsSatellite s = iters.next();
                        count++;
                    }
                    System.out.println("搜索到：" + count + "颗卫星");
                    break;
                //定位启动
                case GpsStatus.GPS_EVENT_STARTED:
                    Log.i(TAG, "定位启动");
                    break;
                //定位结束
                case GpsStatus.GPS_EVENT_STOPPED:
                    Log.i(TAG, "定位结束");
                    break;
            }
        }

        ;
    };

    private String updateGpsStatus(int event, GpsStatus status) {
        StringBuilder sb2 = new StringBuilder("");
        if (status == null) {
            sb2.append("" + 0);
        } else if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
            int maxSatellites = status.getMaxSatellites();
            Iterator<GpsSatellite> it = status.getSatellites().iterator();
            numSatelliteList.clear();
            int count = 0;
            while (it.hasNext() && count <= maxSatellites) {
                GpsSatellite s = it.next();
                numSatelliteList.add(s);
                count++;
            }
            sb2.append("" + numSatelliteList.size());
        }

        return sb2.toString();
    }

    private View createStaList(GpsSatellite satellite) {
        LinearLayout view = (LinearLayout) LayoutInflater.from(mActivity).
                inflate(R.layout.item_sta_list, null, false);
        ViewHolder holder = new ViewHolder(view);
        holder.tvFangweijao.setText(satellite.getAzimuth() + "");
        holder.tvGaodujiao.setText(satellite.getElevation() + "");
        holder.tvSvid.setText(satellite.getPrn() + "");
        holder.tvXinzaobi.setText(satellite.getSnr() + "");
        holder.tvLishu.setText(satellite.hasAlmanac() + "");
        holder.tvXingli.setText(satellite.hasEphemeris() + "");
        holder.tvUsed.setText(satellite.usedInFix() + "");
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.tv_fangweijao)
        TextView tvFangweijao;
        @BindView(R.id.tv_gaodujiao)
        TextView tvGaodujiao;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_svid)
        TextView tvSvid;
        @BindView(R.id.tv_xinzaobi)
        TextView tvXinzaobi;
        @BindView(R.id.tv_lishu)
        TextView tvLishu;
        @BindView(R.id.tv_xingli)
        TextView tvXingli;
        @BindView(R.id.tv_used)
        TextView tvUsed;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
