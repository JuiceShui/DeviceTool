package com.jojo.devicetool.ui.device;

/**
 * Description: Jojo on 2019/3/25
 */
public class DeviceItem {
    private String label;
    private String value;
    private boolean isTitle = false;

    public DeviceItem(String label) {
        this.label = label;
        this.isTitle = true;
    }

    public DeviceItem(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }
}
