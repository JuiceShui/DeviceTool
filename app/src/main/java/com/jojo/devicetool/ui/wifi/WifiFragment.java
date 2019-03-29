package com.jojo.devicetool.ui.wifi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.jojo.devicetool.R;
import com.jojo.devicetool.ui.BaseFragment;
import com.jojo.devicetool.ui.device.DeviceUtil;
import com.jojo.devicetool.util.NetworkUtil;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;

import static android.content.Context.WIFI_SERVICE;

/**
 * Description: Jojo on 2019/3/26
 */
public class WifiFragment extends BaseFragment {
    @BindView(R.id.tv_device_name)
    TextView tvDeviceName;
    @BindView(R.id.tv_device_ip)
    TextView tvDeviceIp;
    @BindView(R.id.tv_device_mac)
    TextView tvDeviceMac;
    @BindView(R.id.tv_wifi_name)
    TextView tvWifiName;
    @BindView(R.id.tv_wifi_quality)
    TextView tvWifiQuality;
    @BindView(R.id.tv_wifi_speed)
    TextView tvWifiSpeed;
    @BindView(R.id.tv_wifi_hz)
    TextView tvWifiHz;
    @BindView(R.id.tv_device_shindo)
    TextView tvDeviceShindo;
    @BindView(R.id.tv_router_name)
    TextView tvRouterName;
    @BindView(R.id.tv_router_ip)
    TextView tvRouterIp;
    @BindView(R.id.tv_router_mac)
    TextView tvRouterMac;
    @BindView(R.id.tv_dns_mask)
    TextView tvDnsMask;
    @BindView(R.id.tv_local_net_device)
    TextView tvLocalNetDevice;
    @BindView(R.id.chart)
    CombinedChart chart;
    @BindView(R.id.ll_wifi_list_container)
    LinearLayout llWifiListContainer;
    Unbinder unbinder;
    private List<ScanResult> scanResults = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.fragment_wifi;
    }

    @Override
    protected void initView() {
        tvLocalNetDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, DevicesActivity.class);
                mActivity.startActivity(intent);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initData() {
        WifiManager wifiManager = (WifiManager) mActivity.getApplicationContext().getSystemService(WIFI_SERVICE);
        assert wifiManager != null;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        tvDeviceIp.setText(getIp(ipAddress));
        if (wifiInfo.getMacAddress().equals("02:00:00:00:00:00")) {
            try {
                tvDeviceMac.setText(DeviceUtil.macAddress());
            } catch (SocketException e) {
                e.printStackTrace();
            }
        } else {
            tvDeviceMac.setText(wifiInfo.getMacAddress());
        }
        tvDeviceName.setText(DeviceUtil.getDevice());

        tvWifiName.setText(wifiInfo.getSSID());
        tvWifiQuality.setText(wifiInfo.getRssi() + "dbm");
        tvWifiHz.setText(wifiInfo.getFrequency() + "MHZ");
        tvWifiSpeed.setText(wifiInfo.getLinkSpeed() + "Mbps");
        tvDeviceShindo.setText("信道：" + DeviceUtil.getChannelByFrequency(wifiInfo.getFrequency()) + "");

        tvRouterName.setText(wifiInfo.getNetworkId() + "");
        tvRouterIp.setText(NetworkUtil.getGateWayIp(mActivity));
        tvRouterMac.setText(wifiInfo.getBSSID());
        scanResults = wifiManager.getScanResults();
        for (int i = 0; i < scanResults.size(); i++) {
            ScanResult item = scanResults.get(i);
            View view = getWifiView();
            ((TextView) view.findViewById(R.id.tv_wifi_name)).setText(item.SSID);
            ((TextView) view.findViewById(R.id.tv_wifi_device)).setText(item.BSSID);
            ((TextView) view.findViewById(R.id.tv_wifi_quality)).setText(item.level + "");
            ((TextView) view.findViewById(R.id.tv_wifi_hz)).setText(item.frequency + "");
            ((TextView) view.findViewById(R.id.tv_wifi_shindao)).setText("" + DeviceUtil.getChannelByFrequency(item.frequency));
            llWifiListContainer.addView(view);
        }
        //tvDeviceIp.setText();
    }

    private View getWifiView() {
        View view = LayoutInflater.
                from(mActivity).inflate(R.layout.layout_wifi_list_item,
                null, false);
        return view;
    }

    private String getIp(int ipAddress) {
        // 获得IP地址的方法一：
        String ipString = "";
        if (ipAddress != 0) {
            ipString = ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
                    + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
            // tvDeviceIp.setText(ipString);
        }
        return ipString;
    }
}
