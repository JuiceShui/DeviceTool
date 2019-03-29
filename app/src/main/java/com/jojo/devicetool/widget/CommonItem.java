/*
 * Copyright (c) 2017 yeeyuntech. All rights reserved.
 */

package com.jojo.devicetool.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jojo.devicetool.R;
import com.jojo.devicetool.util.TextUtil;


/**
 * 通用的item布局
 * 支持左右文字、左右图标
 */
public class CommonItem extends LinearLayout {

    private Context mContext;
    private View mViewItem;
    private ImageView mIvLeftIcon;
    private ImageView mIvRightIcon;
    private TextView mTvLeftText;
    private TextView mTvLeftTip;
    private TextView mTvRightText;
    private TextView mTvBadge;
    private View mViewDivider;

    private int mBadgeNumber = -1;
    private boolean mDividerEnabled = true;

    public CommonItem(Context context) {
        this(context, null);
    }

    public CommonItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(VERTICAL);
        View view = LayoutInflater.from(mContext).inflate(R.layout.yy_widget_common_item, this, true);
        mViewItem = view.findViewById(R.id.yy_common_item_ll_item);
        mViewDivider = view.findViewById(R.id.yy_common_item_divider);
        mIvLeftIcon = (ImageView) view.findViewById(R.id.yy_common_item_iv_left_icon);
        mTvLeftText = (TextView) view.findViewById(R.id.yy_common_item_tv_left_text);
        mTvLeftTip = (TextView) view.findViewById(R.id.yy_common_item_tv_left_tip);
        mTvRightText = (TextView) view.findViewById(R.id.yy_common_item_tv_right_text);
        mTvBadge = (TextView) view.findViewById(R.id.yy_common_item_tv_badge);
        mIvRightIcon = (ImageView) view.findViewById(R.id.yy_common_item_iv_right_icon);

        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CommonItem);
        int itemPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.CommonItem_common_item_padding_left, -1);
        if (itemPaddingLeft > -1) {
            mViewItem.setPadding(itemPaddingLeft, mViewItem.getPaddingTop(),
                    mViewItem.getPaddingRight(), mViewItem.getPaddingBottom());
        }
        int itemPaddingRight = typedArray.getDimensionPixelSize(R.styleable.CommonItem_common_item_padding_right, -1);
        if (itemPaddingRight > -1) {
            mViewItem.setPadding(mViewItem.getPaddingLeft(), mViewItem.getPaddingTop(),
                    itemPaddingRight, mViewItem.getPaddingBottom());
        }

        String leftText = typedArray.getString(R.styleable.CommonItem_common_item_left_text);
        setLeftText(leftText);
        String leftTip = typedArray.getString(R.styleable.CommonItem_common_item_left_tip);
        setLeftTipText(leftTip);
        String rightText = typedArray.getString(R.styleable.CommonItem_common_item_right_text);
        setRightText(rightText);


        Drawable leftIcon = typedArray.getDrawable(R.styleable.CommonItem_common_item_left_icon);
        setLeftIcon(leftIcon);
        Drawable rightIcon = typedArray.getDrawable(R.styleable.CommonItem_common_item_right_icon);
        setRightIcon(rightIcon);
        int leftIconWidth = typedArray.getDimensionPixelSize(R.styleable.CommonItem_common_item_left_icon_width, -1);
        int leftIconSpace = typedArray.getDimensionPixelSize(R.styleable.CommonItem_common_item_left_icon_space, -1);
        if (leftIconWidth > -1 || leftIconSpace > -1) {
            LayoutParams params = (LayoutParams) mIvLeftIcon.getLayoutParams();
            if (leftIconWidth > -1) {
                params.width = leftIconWidth;
            }
            if (leftIconSpace > -1) {
                params.rightMargin = leftIconSpace;
            }
            mIvLeftIcon.setLayoutParams(params);
        }
        int rightIconWidth = typedArray.getDimensionPixelSize(R.styleable.CommonItem_common_item_right_icon_width, -1);
        int rightIconSpace = typedArray.getDimensionPixelSize(R.styleable.CommonItem_common_item_right_icon_space, -1);
        if (rightIconSpace > -1 || rightIconWidth > -1) {
            LayoutParams params = (LayoutParams) mIvRightIcon.getLayoutParams();
            if (rightIconWidth > -1) {
                params.width = rightIconWidth;
            }
            if (rightIconSpace > -1) {
                params.leftMargin = rightIconSpace;
            }
            mIvRightIcon.setLayoutParams(params);
        }

        int leftTextSpace = typedArray.getDimensionPixelSize(R.styleable.CommonItem_common_item_left_text_space, -1);
        if (leftTextSpace > -1) {
            LayoutParams params = (LayoutParams) mTvLeftText.getLayoutParams();
            params.rightMargin = leftTextSpace;
            mTvLeftText.setLayoutParams(params);
        }
        int leftTipSpace = typedArray.getDimensionPixelSize(R.styleable.CommonItem_common_item_left_tip_space, -1);
        if (leftTipSpace > -1) {
            LayoutParams params = (LayoutParams) mTvLeftTip.getLayoutParams();
            params.rightMargin = leftTipSpace;
            mTvLeftTip.setLayoutParams(params);
        }
        int rightTextSpace = typedArray.getDimensionPixelSize(R.styleable.CommonItem_common_item_right_text_space, -1);
        if (rightTextSpace > -1) {
            LayoutParams params = (LayoutParams) mTvRightText.getLayoutParams();
            params.leftMargin = rightTextSpace;
            mTvRightText.setLayoutParams(params);
        }
        int badgeSpace = typedArray.getDimensionPixelSize(R.styleable.CommonItem_common_item_badge_space, -1);
        if (badgeSpace > -1) {
            LayoutParams params = (LayoutParams) mTvBadge.getLayoutParams();
            params.leftMargin = badgeSpace;
            mTvBadge.setLayoutParams(params);
        }

        boolean dividerEnabled = typedArray.getBoolean(R.styleable.CommonItem_common_item_divider_enabled, true);
        setDividerEnabled(dividerEnabled);
        int dividerLeft = typedArray.getDimensionPixelSize(R.styleable.CommonItem_common_item_divider_left, -1);
        int dividerRight = typedArray.getDimensionPixelSize(R.styleable.CommonItem_common_item_divider_right, -1);
        if (dividerLeft > -1 || dividerRight > -1) {
            LayoutParams params = (LayoutParams) mViewDivider.getLayoutParams();
            if (dividerLeft > -1) {
                params.leftMargin = dividerLeft;
            }
            if (dividerRight > -1) {
                params.rightMargin = dividerRight;
            }
            mViewDivider.setLayoutParams(params);
        }

        int badgeSize = typedArray.getDimensionPixelSize(R.styleable.CommonItem_common_item_badge_size, -1);
        if (badgeSize > -1) {
            LayoutParams params = (LayoutParams) mTvBadge.getLayoutParams();
            params.width = badgeSize;
            params.height = badgeSize;
            mTvBadge.setLayoutParams(params);
        }
        float badgeTextSize = typedArray.getDimension(R.styleable.CommonItem_common_item_badge_text_size, -1);
        if (badgeTextSize > -1) {
            mTvBadge.setTextSize(TypedValue.COMPLEX_UNIT_PX, badgeTextSize);
        }
        int badgeNumber = typedArray.getInt(R.styleable.CommonItem_common_item_badge_number, -1);
        setBadgeNumber(badgeNumber);

        float leftTextTextSize = typedArray.getDimension(R.styleable.CommonItem_common_item_left_text_text_size, -1);
        if (leftTextTextSize > -1) {
            mTvLeftText.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextTextSize);
        }
        float leftTipTextSize = typedArray.getDimension(R.styleable.CommonItem_common_item_left_tip_text_size, -1);
        if (leftTipTextSize > -1) {
            mTvLeftTip.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTipTextSize);
        }
        float rightTextTextSize = typedArray.getDimension(R.styleable.CommonItem_common_item_right_text_text_size, -1);
        if (rightTextTextSize > -1) {
            mTvRightText.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextTextSize);
        }

        int leftTextTextColor = typedArray.getResourceId(R.styleable.CommonItem_common_item_left_text_text_color, -1);
        if (leftTextTextColor != -1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mTvLeftText.setTextColor(mContext.getColor(leftTextTextColor));
            } else {
                mTvLeftText.setTextColor(mContext.getResources().getColor(leftTextTextColor));
            }
        }
        int leftTipTextColor = typedArray.getResourceId(R.styleable.CommonItem_common_item_left_tip_text_color, -1);
        if (leftTipTextColor != -1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mTvLeftTip.setTextColor(mContext.getColor(leftTipTextColor));
            } else {
                mTvLeftTip.setTextColor(mContext.getResources().getColor(leftTipTextColor));
            }
        }
        int rightTextTextColor = typedArray.getResourceId(R.styleable.CommonItem_common_item_right_text_text_color, -1);
        if (rightTextTextColor != -1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mTvRightText.setTextColor(mContext.getColor(rightTextTextColor));
            } else {
                mTvRightText.setTextColor(mContext.getResources().getColor(rightTextTextColor));
            }
        }
        //设置默认字体
      /*  AssetManager mgr = mContext.getAssets();
        Typeface tf_left = Typeface.createFromAsset(mgr, "fonts/linotype_medium.ttf");
        mTvLeftText.setTypeface(tf_left);*/
        typedArray.recycle();
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }

    public void setLeftIcon(@DrawableRes int resId) {
        mIvLeftIcon.setImageResource(resId);
        mIvLeftIcon.setVisibility(VISIBLE);
    }

    public void setLeftIcon(Drawable drawable) {
        mIvLeftIcon.setImageDrawable(drawable);
        mIvLeftIcon.setVisibility(drawable != null ? VISIBLE : GONE);
    }

    public void setRightIcon(@DrawableRes int resId) {
        mIvRightIcon.setImageResource(resId);
        mIvRightIcon.setVisibility(VISIBLE);
    }

    public void setRightIcon(Drawable drawable) {
        mIvRightIcon.setImageDrawable(drawable);
        mIvRightIcon.setVisibility(drawable != null ? VISIBLE : GONE);
    }

    public void setLeftText(@StringRes int resId) {
        mTvLeftText.setText(resId);
        mTvLeftText.setVisibility(VISIBLE);
    }

    public void setLeftText(String text) {
        mTvLeftText.setText(text);
        mTvLeftText.setVisibility(TextUtils.isEmpty(text) ? GONE : VISIBLE);
    }

    public void setLeftTipText(@StringRes int resId) {
        mTvLeftTip.setText(resId);
        mTvLeftTip.setVisibility(VISIBLE);
    }

    public void setLeftTipText(String text) {
        mTvLeftTip.setText(text);
        mTvLeftTip.setVisibility(TextUtils.isEmpty(text) ? GONE : VISIBLE);
    }

    public void setRightText(@StringRes int resId) {
        mTvRightText.setText(resId);
        mTvRightText.setVisibility(VISIBLE);
    }

    public void setRightText(String text) {
        mTvRightText.setText(text);
        mTvRightText.setVisibility(TextUtils.isEmpty(text) ? GONE : VISIBLE);
    }

    public void setBadgeNumber(int number) {
        mBadgeNumber = number > 0 ? number : 0;
        mTvBadge.setText(TextUtil.format("%d", mBadgeNumber));
        mTvBadge.setVisibility(mBadgeNumber > 0 ? VISIBLE : GONE);
    }

    public void setDividerEnabled(boolean enabled) {
        mDividerEnabled = enabled;
        mViewDivider.setVisibility(mDividerEnabled ? VISIBLE : GONE);
    }

    public String getRightTextText() {
        return mTvRightText.getText().toString().trim();
    }

    public void setRightTextColor(int color) {
        mTvRightText.setTextColor(color);
    }

    public int getBadgeNumber() {
        return mBadgeNumber;
    }

    public boolean isDividerEnabled() {
        return mDividerEnabled;
    }

    public String getLeftText() {
        return mTvLeftText.getText().toString();
    }
}
