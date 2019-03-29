package com.jojo.devicetool.ui.wifi;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jojo.devicetool.R;
import com.jojo.devicetool.ui.wifi.device.DeviceScanManager;
import com.jojo.devicetool.ui.wifi.device.DeviceScanResult;
import com.jojo.devicetool.util.NetworkUtil;
import com.jojo.devicetool.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description: Jojo on 2019/3/27
 */
public class DevicesActivity extends AppCompatActivity {
    @BindView(R.id.rv_wifi_device)
    RecyclerView rvWifiDevice;
    private DeviceScanManager manager;
    private DeviceAdapter mAdapter;
    private List<IP_MAC> mDeviceList = new ArrayList<IP_MAC>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_device);
        ButterKnife.bind(this);
        if (!NetworkUtil.isWifiConnected(this)) {
            ToastUtils.showTextToast(this,
                    getString(R.string.connect_wifi_please));
            finish();
            return;
        }
        manager = new DeviceScanManager();
        manager.startScan(getApplicationContext(), new DeviceScanResult() {
            @Override
            public void deviceScanResult(IP_MAC ip_mac) {
                if (!mDeviceList.contains(ip_mac)) {
                    mDeviceList.add(ip_mac);
                    mAdapter.notifyDataSetChanged();
                    /*mToolbar.setTitle(
                            getString(R.string.title_activity_device_scan) +
                                    Integer.toString(mDeviceList.size()));*/
                }
            }
        });

        String localIp = NetworkUtil.getLocalIp();
        String gateIp = NetworkUtil.getGateWayIp(this);
        String localMac = NetworkUtil.getLocalMac(this);
        IP_MAC myself = new IP_MAC(localIp, localMac);
        myself.mManufacture = Build.MANUFACTURER;
        myself.mDeviceName = Build.MODEL;
        mDeviceList.add(myself);
        mAdapter = new DeviceAdapter(mDeviceList, this, localIp, gateIp);
        rvWifiDevice.setLayoutManager(new LinearLayoutManager(this));
        rvWifiDevice.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (manager != null) {
            manager.stopScan();
        }
    }
}
