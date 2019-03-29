package com.jojo.devicetool.ui.wifi.device;

import android.text.TextUtils;
import android.util.Log;

import com.jojo.devicetool.App;
import com.jojo.devicetool.R;
import com.jojo.devicetool.ui.wifi.IP_MAC;
import com.jojo.devicetool.util.Constant;
import com.jojo.devicetool.util.NetworkUtil;

import java.io.IOException;

/**
 * Description: Jojo on 2019/3/27. 针对确定的IP进行ping操作和端口（一些常用的端口）检测，看是否在线
 */
public class DeviceScanTask {
    private static final String tag = DeviceScanTask.class.getSimpleName();

    private DeviceScanGroup mDeviceScanGroup;
    private DeviceInfo mDeviceInfo;
    public Thread mThread;
    public DeviceScanRunnable mRunnable;

    private IP_MAC mIpMac;
    private DeviceScanHandler mDeviceScanHandler;


    public DeviceScanTask(DeviceScanGroup group) {
        this.mDeviceScanGroup = group;
        mRunnable = new DeviceScanRunnable();
        mDeviceInfo = new DeviceInfo();
    }


    public void setParams(IP_MAC ip_mac, DeviceScanHandler handler) {
        this.mIpMac = ip_mac;
        this.mDeviceScanHandler = handler;
    }


    private class DeviceScanRunnable implements Runnable {
        public void run() {
            if (NetworkUtil.isPingOk(mIpMac.mIp) ||
                    NetworkUtil.isAnyPortOk(mIpMac.mIp)) {
                String manufacture = parseHostInfo(mIpMac.mMac); //解析机器名称
                Log.e(tag, "the device is in wifi : " + mIpMac.toString() + "" +
                        " manufacture = " + manufacture);
                if (!TextUtils.isEmpty(manufacture)) {
                    mIpMac.mManufacture = manufacture;
                }

                try {
                    NetBios nb = new NetBios(mIpMac.mIp);
                    String deviceName = nb.getNbName();
                    Log.d(tag, "device name = " + deviceName);
                    if (!TextUtils.isEmpty(deviceName)) {
                        mIpMac.mDeviceName = deviceName;
                    } else {
                        mIpMac.mDeviceName = App.get().getResources()
                                .getString(R.string.unknown);
                    }
                } catch (IOException e) {
                    mIpMac.mDeviceName = App.get().getResources()
                            .getString(R.string.unknown);
                    e.printStackTrace();
                }

                if (mDeviceScanHandler != null) {
                    mDeviceScanHandler.sendMessage(
                            mDeviceScanHandler.obtainMessage(
                                    Constant.MSG.SCAN_ONE, mIpMac));
                }
            }
        }
    }


    private String parseHostInfo(String mac) {
        return Manufacture.getInstance()
                .getManufacture(mac, App.get().getApplicationContext());
    }
}
