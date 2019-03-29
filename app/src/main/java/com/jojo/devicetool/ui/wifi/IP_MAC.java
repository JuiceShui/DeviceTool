package com.jojo.devicetool.ui.wifi;

import com.jojo.devicetool.ui.device.DeviceUtil;

import java.net.SocketException;

import androidx.annotation.Nullable;

/**
 * Description: Jojo on 2019/3/27
 */
public class IP_MAC {
    public String mIp;
    public String mMac;
    public String mManufacture;
    public String mDeviceName;

    public IP_MAC(String mIp, String mMac) {
        this.mIp = mIp;
        this.mMac = mMac;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        IP_MAC ip_mac = (IP_MAC) obj;

        return mIp.equals(ip_mac.mIp) && mMac.equals(ip_mac.mMac);
    }

    @Override
    public int hashCode() {
        int result = mIp.hashCode() + mMac.hashCode();
        return result;
    }

    public String getmIp() {
        return mIp;
    }

    public void setmIp(String mIp) {
        this.mIp = mIp;
    }

    public String getmMac() {
        if (mMac.equals("02:00:00:00:00:00")) {
            try {
                return DeviceUtil.macAddress();
            } catch (SocketException e) {
                e.printStackTrace();
                return mMac;
            }
        }
        return mMac;
    }

    public void setmMac(String mMac) {
        this.mMac = mMac;
    }

    public String getmManufacture() {
        return mManufacture;
    }

    public void setmManufacture(String mManufacture) {
        this.mManufacture = mManufacture;
    }

    public String getmDeviceName() {
        return mDeviceName;
    }

    public void setmDeviceName(String mDeviceName) {
        this.mDeviceName = mDeviceName;
    }
}
