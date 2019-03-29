package com.jojo.devicetool.ui.network.test;

import com.jojo.devicetool.ui.network.NetworkItem;

import java.util.Collections;
import java.util.List;

/**
 * Description: Jojo on 2019/3/28
 */
public class Sort {
    public static void sort(List<NetworkItem> dataSource) {
        int j;
        for (int i = 1; i < dataSource.size(); i++) {
            NetworkItem entity = dataSource.get(i);
            j = i;
            while (j > 0 && entity.getDistance() < dataSource.get(j - 1).getDistance()) {
                Collections.swap(dataSource, j, j - 1); // 币当前值小的向后y
                j--;
            }
        }
    }
}
