/*
 * Copyright (C) 2015-2018 University of South  Florida, Sean J. Barbeau
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jojo.devicetool.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.TimeUnit;

import static com.jojo.devicetool.ui.location.GpsSkyView.MAX_VALUE_CN0;
import static com.jojo.devicetool.ui.location.GpsSkyView.MAX_VALUE_SNR;
import static com.jojo.devicetool.ui.location.GpsSkyView.MIN_VALUE_CN0;
import static com.jojo.devicetool.ui.location.GpsSkyView.MIN_VALUE_SNR;

/**
 * Utilities for processing user inteface elements
 */

public class UIUtils {

    /**
     * Formats a view so it is ignored for accessible access
     */
    public static void setAccessibilityIgnore(View view) {
        view.setClickable(false);
        view.setFocusable(false);
        view.setContentDescription("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        }
    }

    /**
     * Converts screen dimension units from dp to pixels, based on algorithm defined in
     * http://developer.android.com/guide/practices/screens_support.html#dips-pels
     *
     * @param dp value in dp
     * @return value in pixels
     */
    public static int dpToPixels(Context context, float dp) {
        // Get the screen's density scale
        final float scale = context.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * scale + 0.5f);
    }

    /**
     * Returns true if the activity is still active and dialogs can be managed (i.e., displayed
     * or dismissed), or false if it is not
     *
     * @param activity Activity to check for displaying/dismissing a dialog
     * @return true if the activity is still active and dialogs can be managed, or false if it is
     * not
     */
    public static boolean canManageDialog(Activity activity) {
        if (activity == null) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return !activity.isFinishing() && !activity.isDestroyed();
        } else {
            return !activity.isFinishing();
        }
    }

    /**
     * Returns true if the fragment is attached to the activity, or false if it is not attached
     *
     * @param f fragment to be tested
     * @return true if the fragment is attached to the activity, or false if it is not attached
     */
    public static boolean isFragmentAttached(Fragment f) {
        return f.getActivity() != null && f.isAdded();
    }

    /**
     * Returns a human-readable description of the time-to-first-fix, such as "38 sec"
     *
     * @param ttff time-to-first fix, in milliseconds
     * @return a human-readable description of the time-to-first-fix, such as "38 sec"
     */
    public static String getTtffString(int ttff) {
        if (ttff == 0) {
            return "";
        } else {
            return TimeUnit.MILLISECONDS.toSeconds(ttff) + " sec";
        }
    }

    /**
     * Converts the provided SNR values to a left margin value (pixels) for the avg SNR indicator ImageViews in gps_sky_signal
     * Left margin range for the SNR indicator ImageViews in gps_sky_signal is determined by dimens.xml
     * cn0_meter_width (based on device screen width) and cn0_indicator_min_left_margin values.
     * <p>
     * This is effectively an affine transform - https://math.stackexchange.com/a/377174/554287.
     *
     * @param snr signal-to-noise ratio of the satellite in dB (from GpsSatellite)
     * @return left margin value in pixels for the SNR indicator ImageViews
     */
    public static int snrToIndicatorLeftMarginPx(float snr, int minIndicatorMarginPx, int maxIndicatorMarginPx) {
        return (int) MathUtils.mapToRange(snr, MIN_VALUE_SNR, MAX_VALUE_SNR, minIndicatorMarginPx, maxIndicatorMarginPx);
    }

    /**
     * Converts the provided SNR values to a left margin value (pixels) for the avg SNR TextViews in gps_sky_signal
     * Left margin range for the SNR indicator TextView in gps_sky_signal is determined by dimens.xml
     * cn0_meter_width (based on device screen width) and cn0_textview_min_left_margin values.
     * <p>
     * This is effectively an affine transform - https://math.stackexchange.com/a/377174/554287.
     *
     * @param snr signal-to-noise ratio of the satellite in dB (from GpsSatellite)
     * @return left margin value in dp for the SNR TextViews
     */
    public static int snrToTextViewLeftMarginPx(float snr, int minTextViewMarginPx, int maxTextViewMarginPx) {
        return (int) MathUtils.mapToRange(snr, MIN_VALUE_SNR, MAX_VALUE_SNR, minTextViewMarginPx, maxTextViewMarginPx);
    }

    /**
     * Converts the provided C/N0 values to a left margin value (dp) for the avg C/N0 indicator ImageViews in gps_sky_signal
     * Left margin range for the C/N0 indicator ImageViews in gps_sky_signal is determined by dimens.xml
     * cn0_meter_width (based on device screen width) and cn0_indicator_min_left_margin values.
     * <p>
     * This is effectively an affine transform - https://math.stackexchange.com/a/377174/554287.
     *
     * @param cn0 carrier-to-noise density at the antenna of the satellite in dB-Hz (from GnssStatus)
     * @return left margin value in dp for the C/N0 indicator ImageViews
     */
    public static int cn0ToIndicatorLeftMarginPx(float cn0, int minIndicatorMarginPx, int maxIndicatorMarginPx) {
        return (int) MathUtils.mapToRange(cn0, MIN_VALUE_CN0, MAX_VALUE_CN0, minIndicatorMarginPx, maxIndicatorMarginPx);
    }

    /**
     * Converts the provided C/N0 values to a left margin value (dp) for the avg C/N0 TextViews in gps_sky_signal
     * Left margin range for the C/N0 indicator TextView in gps_sky_signal is determined by dimens.xml
     * cn0_meter_width (based on device screen width) and cn0_textview_min_left_margin values.
     * <p>
     * This is effectively an affine transform - https://math.stackexchange.com/a/377174/554287.
     *
     * @param cn0 carrier-to-noise density at the antenna of the satellite in dB-Hz (from GnssStatus)
     * @return left margin value in dp for the C/N0 TextViews
     */
    public static int cn0ToTextViewLeftMarginPx(float cn0, int minTextViewMarginPx, int maxTextViewMarginPx) {
        return (int) MathUtils.mapToRange(cn0, MIN_VALUE_CN0, MAX_VALUE_CN0, minTextViewMarginPx, maxTextViewMarginPx);
    }

    /**
     * Sets the margins for a given view
     *
     * @param v View to set the margin for
     * @param l left margin, in pixels
     * @param t top margin, in pixels
     * @param r right margin, in pixels
     * @param b bottom margin, in pixels
     */
    public static void setMargins(View v, int l, int t, int r, int b) {
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        p.setMargins(l, t, r, b);
        v.setLayoutParams(p);
    }

    /**
     * Opens email apps based on the given email address
     * @param email address
     * @param location string that shows the current location
     */

    /**
     * Returns the provided latitude or longitude value in Degrees Minutes Seconds (DMS) format
     * @param latOrLon latitude or longitude to convert to DMS format
     * @return the provided latitude or longitude value in Degrees Minutes Seconds (DMS) format
     */

    /**
     * Converts the provide value in meters to the corresponding value in feet
     *
     * @param meters value in meters to convert to feet
     * @return the provided meters value converted to feet
     */
    public static double toFeet(double meters) {
        return meters * 1000d / 25.4d / 12d;
    }

    /**
     * Converts the provide value in meters per second to the corresponding value in kilometers per hour
     *
     * @param metersPerSecond value in meters per second to convert to kilometers per hour
     * @return the provided meters per second value converted to kilometers per hour
     */
    public static float toKilometersPerHour(float metersPerSecond) {
        return metersPerSecond * 3600f / 1000f;
    }

    /**
     * Converts the provide value in meters per second to the corresponding value in miles per hour
     *
     * @param metersPerSecond value in meters per second to convert to miles per hour
     * @return the provided meters per second value converted to miles per hour
     */
    public static float toMilesPerHour(float metersPerSecond) {
        return toKilometersPerHour(metersPerSecond) / 1.6093440f;
    }
}
