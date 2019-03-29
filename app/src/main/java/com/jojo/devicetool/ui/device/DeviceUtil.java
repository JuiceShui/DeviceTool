package com.jojo.devicetool.ui.device;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.io.File;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Objects;

/**
 * Description: Jojo on 2019/3/25
 */
public class DeviceUtil {

    //获取厂商名
    public static String getFactoryName() {
        return Build.MANUFACTURER;
    }

    //获取产品名
    public static String getProductName() {
        return Build.PRODUCT;
    }

    //获取手机品牌
    public static String getBrand() {
        return Build.BRAND;
    }

    //获取手机型号
    public static String getModel() {
        return Build.MODEL;
    }

    //获取手机硬件
    public static String getHardwareInfo() {
        return Build.HARDWARE;
    }

    //获取display
    public static String getDisPlay() {
        return Build.DISPLAY;
    }

    //获取设备id
    public static String getID() {
        return Build.ID;
    }

    //获取产品
    public static String getProduct() {
        return Build.PRODUCT;
    }

    //获取设备
    public static String getDevice() {
        return Build.DEVICE;
    }

    //获取基带
    public static String getJidai() {
        try {

            Class cl = Class.forName("android.os.SystemProperties");

            Object invoker = cl.newInstance();

            Method m = cl.getMethod("get", new Class[]{String.class, String.class});

            Object result = m.invoke(invoker, new Object[]{"gsm.version.baseband", "no message"});

            return (String) result;

        } catch (Exception e) {
            return "";
        }
    }

    //获取屏幕物理尺寸
    public static String getScreenSize(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        manager.getDefaultDisplay().getRealSize(point);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int densityDpi = dm.densityDpi;//得到屏幕的密度值，但是该密度值只能作为参考，因为他是固定的几个密度值。
        double x = Math.pow(point.x / dm.xdpi, 2);//dm.xdpi是屏幕x方向的真实密度值，比上面的densityDpi真实。
        double y = Math.pow(point.y / dm.ydpi, 2);//dm.xdpi是屏幕y方向的真实密度值，比上面的densityDpi真实。
        double screenInches = Math.sqrt(x + y);
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String res = df.format(screenInches);
        return res + " inches";
    }

    //获取支持的abi
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static String getABI() {
        StringBuilder stringBuilder = new StringBuilder();
        String[] abis = Build.SUPPORTED_ABIS;
        for (String item : abis) {
            stringBuilder.append(item).append(" ");
        }
        return stringBuilder.toString();
    }

    //获取手机主板名
    public static String getBoard() {
        return Build.BOARD;
    }

    //获取设备名
    public static String getDeviceName() {
        return Build.DEVICE;
    }

    //获取屏幕分辨率
    public static String getScreenPixel(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels + "X" +
                context.getResources().getDisplayMetrics().heightPixels + "pixels";
    }

    //屏幕像素密度
    public static String getScreenDpi(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi + "dpi";
    }

    //获取设备序列号
    public static String getDeviceSerial() {
        return Build.SERIAL;
    }

    //获取系统sdk int
    public static String getSDK_INT() {
        return Build.VERSION.SDK_INT + "";
    }

    //获取Android版本
    public static String getRelease() {
        return Build.VERSION.RELEASE;
    }

    //获取Android版本名
    public static String getVersionName() {
        return Build.VERSION.CODENAME;
    }

    //获取BootLoader
    public static String getBootLoader() {
        return Build.BOOTLOADER;
    }

    //获取java VM
    public static String getJavaVm() {
        boolean isArt;
        // catch version 2 and above
// false for Dalvik, true for current ART, true for any new runtimes
        try {
           /* isArt = Integer.parseInt(Objects.requireNonNull(System.getProperty("java.vm.version"))
                    .split(".")[0]) >= 2;*/
            String version = System.getProperty("java.vm.version");
            assert version != null;
            String[] sp = version.split(".");
            isArt = Integer.parseInt(sp[0]) >= 2;
        } catch (Exception e) {
            // we suppress the exception and fall back to checking only for current ART
            isArt = Objects.requireNonNull(System.getProperty("java.vm.version")).startsWith("2.");
        }
        String jvm = "";
        if (isArt) {
            jvm = "ART";
        } else {
            jvm = "Dalvik";
        }
        return jvm + System.getProperty("java.vm.version");
    }

    public static String getOSArch() {
        return System.getProperty("os.arch");
    }

    public static String getOSArchVersion() {
        return System.getProperty("os.version");
    }

    public static String getRuntimeVersion() {
        return "";
    }

    //获取IMEI
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(
                Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission")
        String deviceId = telephonyManager.getDeviceId();
        if (deviceId == null) {
            return "未知";
        }
        return deviceId;

    }

    //获取RAM可用信息
    public static String getRAMInfo(Context context) {
        long totalSize = 0, availableSize = 0, usedSize;
        ActivityManager activityManager
                = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        assert activityManager != null;
        activityManager.getMemoryInfo(memoryInfo);
        totalSize = memoryInfo.totalMem;
        availableSize = memoryInfo.availMem;
        usedSize = totalSize - availableSize;
        return Formatter.formatFileSize(context, usedSize) + "/" +
                Formatter.formatFileSize(context, totalSize);
    }

    //获取存储情况
    public static String getROMInfo(Context context, int type) {
        String path = getStoragePath(context, type);
        if (!isSdMount() || TextUtils.isEmpty(path) || path == null) {
            return "";
        }
        File file = new File(path);
        StatFs statFs = new StatFs(file.getPath());
        String storageInfo;
        long blockCount = statFs.getBlockCountLong();
        long blockSize = statFs.getBlockSizeLong();
        long totalSpace = blockCount * blockSize;

        long availableBlocks = statFs.getAvailableBlocksLong();
        long availableSize = availableBlocks * blockSize;
        long usedSize = totalSpace - availableSize;
        storageInfo = Formatter.formatFileSize(context, usedSize)
                + "/" + Formatter.formatFileSize(context, totalSpace);
        return storageInfo;
    }

    public static String getBatteryCapacity(Context context) {
        Object mPowerProfile;
        double batteryCapacity = 0;
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class)
                    .newInstance(context);

            batteryCapacity = (double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getBatteryCapacity")
                    .invoke(mPowerProfile);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(batteryCapacity + " mAh");
    }

    /**
     * 获取储存卡地址
     *
     * @param context
     * @param type    0 内部  1 外部
     * @return
     */
    private static String getStoragePath(Context context, int type) {
        StorageManager storageManager
                = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            assert storageManager != null;
            Method getPathMethod = storageManager.getClass()
                    .getMethod("getVolumePaths");
            String[] path = (String[]) getPathMethod.invoke(storageManager);
            switch (type) {
                case 0://内置存储卡
                    return path[type];
                case 1://
                    if (path.length > 1) {
                        return path[type];
                    }
                    return null;
            }
        } catch (Exception ignored) {

        }
        return null;
    }

    /**
     * 判断是否挂载sd卡
     *
     * @return
     */
    public static boolean isSdMount() {
        return Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
    }


    public static int getChannelByFrequency(int frequency) {
        int channel = -1;
        switch (frequency) {
            case 2412:
                channel = 1;
                break;
            case 2417:
                channel = 2;
                break;
            case 2422:
                channel = 3;
                break;
            case 2427:
                channel = 4;
                break;
            case 2432:
                channel = 5;
                break;
            case 2437:
                channel = 6;
                break;
            case 2442:
                channel = 7;
                break;
            case 2447:
                channel = 8;
                break;
            case 2452:
                channel = 9;
                break;
            case 2457:
                channel = 10;
                break;
            case 2462:
                channel = 11;
                break;
            case 2467:
                channel = 12;
                break;
            case 2472:
                channel = 13;
                break;
            case 2484:
                channel = 14;
                break;
            case 5745:
                channel = 149;
                break;
            case 5765:
                channel = 153;
                break;
            case 5785:
                channel = 157;
                break;
            case 5805:
                channel = 161;
                break;
            case 5825:
                channel = 165;
                break;
        }
        return channel;
    }

    public static String getSimCompany(Context context) {
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = telManager.getSimOperator();

        if (operator != null)

        {
            if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {
                return "中国移动";
//中国移动

            } else if (operator.equals("46001")) {
                return "中国联通";
//中国联通

            } else if (operator.equals("460003")) {
                return "中国电信";
//中国电信
            }
        }
        return "未知";
    }

    public static String getIMSI(Context context) {
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String operator = telManager.getSubscriberId();
        return operator;
    }

    public static String getICCID(Context context) {
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String iccid = telManager.getSimSerialNumber();
        return iccid;
    }

    public static String getMCC(Context context) {
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String operator = telManager.getSubscriberId();
        return operator.substring(0, 3);
    }

    public static String getMNC(Context context) {
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String operator = telManager.getSubscriberId();
        return operator.substring(3, 5);
    }

    public static String macAddress() throws SocketException {
        String address = null;
        // 把当前机器上的访问网络接口的存入 Enumeration集合中
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        Log.d("TEST_BUG", " interfaceName = " + interfaces);
        while (interfaces.hasMoreElements()) {
            NetworkInterface netWork = interfaces.nextElement();
            // 如果存在硬件地址并可以使用给定的当前权限访问，则返回该硬件地址（通常是 MAC）。
            byte[] by = netWork.getHardwareAddress();
            if (by == null || by.length == 0) {
                continue;
            }
            StringBuilder builder = new StringBuilder();
            for (byte b : by) {
                builder.append(String.format("%02X:", b));
            }
            if (builder.length() > 0) {
                builder.deleteCharAt(builder.length() - 1);
            }
            String mac = builder.toString();
            Log.d("TEST_BUG", "interfaceName=" + netWork.getName() + ", mac=" + mac);
            // 从路由器上在线设备的MAC地址列表，可以印证设备Wifi的 name 是 wlan0
            if (netWork.getName().equals("wlan0")) {
                Log.d("TEST_BUG", " interfaceName =" + netWork.getName() + ", mac=" + mac);
                address = mac;
            }
        }
        return address;
    }
}
