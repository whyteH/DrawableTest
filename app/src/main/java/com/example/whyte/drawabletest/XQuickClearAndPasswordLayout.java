package com.example.whyte.drawabletest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by luo_shuai on 2015/12/22 9:08.
 */
public class XQuickClearAndPasswordLayout extends FrameLayout {

    static final String TAG = "XQuickClearLayout";

    private LinearLayout mRightIconWrapper;
    private ImageView mIvQuickClear;

    /**
     * 允许使用快速清除功能
     */
    private boolean mAllowQuickClear;

    private Drawable mQuickClearDrawable;

    private EditText mEditText;

    // 记录mEditText原始的padding
    private Rect mEditTextPadding;

    // 即使没有输入内容，也显示叉叉按钮
    private boolean mForceQuickClearVisible;

    private OnClickListener mQuickClearBtnClickListener;
    private OnXQuickTextChangeListener mXQuickTextChangeListener;
    private ImageView mIvPwd;
    private Drawable mPwdHideDrawable;
    private Drawable mPwdVisibleDrawable;
    private boolean mAllowPwd;
    /**
     * 是否显示密码
     */
    private boolean mPwdVisible;

    public XQuickClearAndPasswordLayout(Context context) {
        this(context, null);
    }

    public XQuickClearAndPasswordLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XQuickClearAndPasswordLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.QuickClearAndPwdLayout);
        mAllowQuickClear = array.getBoolean(R.styleable.QuickClearAndPwdLayout_ShowQuickClear, true);
        mAllowPwd = array.getBoolean(R.styleable.QuickClearAndPwdLayout_ShowPwd, true);
        mQuickClearDrawable = array.getDrawable(R.styleable.QuickClearAndPwdLayout_QuickClearIcon);
        mPwdHideDrawable = array.getDrawable(R.styleable.QuickClearAndPwdLayout_PwdHideIcon);
        mPwdVisibleDrawable = array.getDrawable(R.styleable.QuickClearAndPwdLayout_PwdVisibleIcon);
        array.recycle();

        // 设置一个默认的清除按钮
        if (mQuickClearDrawable == null) {
            mQuickClearDrawable = getResources().getDrawable(R.mipmap.ic_clear);
        }
        addRightIconBtn(context);
    }

    private void addRightIconBtn(Context context) {
        mRightIconWrapper = new LinearLayout(context);
        mRightIconWrapper.setOrientation(LinearLayout.HORIZONTAL);
        if (mAllowQuickClear) {
            //快速清除的
            LinearLayout.LayoutParams lpClear = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lpClear.gravity = Gravity.CENTER;
            mIvQuickClear = new ImageView(context);
            mIvQuickClear.setScaleType(ImageView.ScaleType.CENTER);
            mIvQuickClear.setImageDrawable(mQuickClearDrawable);
            mRightIconWrapper.addView(mIvQuickClear, lpClear);
            mIvQuickClear.setOnClickListener(new QuickClearClickListener());
        }

        if (mAllowPwd) {
            //密码的
            mIvPwd = new ImageView(context);
            mIvPwd.setScaleType(ImageView.ScaleType.CENTER);
            mIvPwd.setImageDrawable(mPwdHideDrawable);
            mIvPwd.setPadding(10, 0, 10, 0);
            LinearLayout.LayoutParams lpPwd = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lpPwd.gravity = Gravity.CENTER_VERTICAL;
            mRightIconWrapper.addView(mIvPwd, lpPwd);
            mIvPwd.setOnClickListener(new PwdClickListener());
        }

        updateIconBackground();
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT | Gravity.END;
        addView(mRightIconWrapper, lp);
        updateQuickClearBtnVisible();
        updatePwdVisible();
    }

    private void updateIconBackground() {
        if (mIvQuickClear != null) {
            mIvQuickClear.setBackgroundResource(0);
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(R.attr.selectableItemBackgroundBorderless, outValue, true);
            if (outValue.resourceId != -1) {
                mIvQuickClear.setBackgroundResource(outValue.resourceId);
            }
        }

        if (mIvPwd != null) {
            mIvPwd.setBackgroundResource(0);
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(R.attr.selectableItemBackgroundBorderless, outValue, true);
            if (outValue.resourceId != -1) {
                mIvPwd.setBackgroundResource(outValue.resourceId);
            }
        }
    }

    private void removeQuickClearIcon() {
        if (mRightIconWrapper != null && mIvQuickClear != null) {
            mAllowQuickClear = false;
            mRightIconWrapper.removeView(mIvQuickClear);
            mIvQuickClear = null;
        }
    }

    private void removePwdIcon() {
        if (mRightIconWrapper != null && mIvPwd != null) {
            mAllowPwd = false;
            mRightIconWrapper.removeView(mIvPwd);
            mIvPwd = null;
        }
    }

    private void updatePwdVisible() {
        if (mEditText != null && mAllowPwd) {
            setPasswordVisible(mPwdVisible);
        }
        updateEditTextPadding();
    }

    private void updateQuickClearBtnVisible() {
        if (mEditText != null) {
            if (mForceQuickClearVisible || mEditText.getText().length() > 0) {
                mIvQuickClear.setVisibility(View.VISIBLE);
            } else {
                mIvQuickClear.setVisibility(View.INVISIBLE);
            }
        } else {
            mIvQuickClear.setVisibility(View.INVISIBLE);
        }
        mForceQuickClearVisible = false;
        updateEditTextPadding();
    }

    private void updateEditTextPadding() {
        if (mEditText != null) {
            if (mEditTextPadding == null) {
                mEditTextPadding = new Rect(mEditText.getPaddingLeft(), mEditText.getPaddingTop(),
                        mEditText.getPaddingRight(), mEditText.getPaddingBottom());
            }
            if (mRightIconWrapper.getVisibility() == View.GONE) {
                mEditText.setPadding(mEditTextPadding.left, mEditTextPadding.top, mEditTextPadding.right, mEditTextPadding.bottom);
            } else {
                mEditText.setPadding(mEditTextPadding.left, mEditTextPadding.top,
                        mEditTextPadding.right + mRightIconWrapper.getMeasuredWidth(), mEditTextPadding.bottom);
            }
        }
    }

    protected void setEditText(EditText editText) {
        mEditText = editText;

        if (editText != null) {
            editText.addTextChangedListener(new XQuickTextWatcher());
            editText.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    GradientDrawable gradientDrawable1 = new GradientDrawable();// 形状-圆角矩形
                    gradientDrawable1.setColor(Color.TRANSPARENT);
                    if (b) {
                        gradientDrawable1.setStroke(1, Color.RED);
                    } else {
                        gradientDrawable1.setStroke(1, Color.GRAY);
                    }
                    LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{gradientDrawable1});
                    layerDrawable.setLayerInset(0, -2, -2, -2, 0);
                    setBackground(layerDrawable);
                }
            });
        }
        updateQuickClearBtnVisible();

        updatePwdVisible();
    }


    public void setQuickClearBtnClickListener(OnClickListener listener) {
        mQuickClearBtnClickListener = listener;
    }

    public void setXQuickTextChangeListener(OnXQuickTextChangeListener listener) {
        mXQuickTextChangeListener = listener;
    }

    public void setForceQuickClearVisible(boolean visible) {
        mForceQuickClearVisible = visible;
        updateQuickClearBtnVisible();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View v = getChildAt(i);
            if (v instanceof EditText) {
                setEditText((EditText) v);
                break;
            }
        }
        if (mRightIconWrapper != null) {
            mRightIconWrapper.bringToFront();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getMeasuredHeight();
        if (mRightIconWrapper != null) {
            ViewGroup.LayoutParams RightIconWrapperParams = mRightIconWrapper.getLayoutParams();
            if (RightIconWrapperParams != null) {
                RightIconWrapperParams.width = Math.max(geIconWidth(), Math.min(height, (geIconWidth()) * 2));
                RightIconWrapperParams.height = height;
            }
        }
        updateEditTextPadding();
    }

    private int geIconWidth() {
        if (mAllowPwd && mAllowQuickClear)
            return mIvQuickClear.getMeasuredWidth() + mIvPwd.getMeasuredWidth();
        else if (mAllowQuickClear) {
            return mIvQuickClear.getMeasuredWidth();
        } else {
            return mIvPwd.getMeasuredWidth();
        }
    }

    private class QuickClearClickListener implements OnClickListener {

        @Override
        public void onClick(final View v) {
            if (mQuickClearBtnClickListener != null) {
                mQuickClearBtnClickListener.onClick(v);
            }

            if (mEditText != null) {
                mEditText.setText(null);
                mEditText.requestFocus();
            }
            updateIconBackground();
        }
    }

    private class PwdClickListener implements OnClickListener {

        @Override
        public void onClick(final View v) {
            setPasswordVisible(mPwdVisible = !mPwdVisible);
            mIvPwd.setImageDrawable(mPwdVisible ? mPwdVisibleDrawable : mPwdHideDrawable);
            updateIconBackground();
        }
    }

    public void setPasswordVisible(boolean visible) {
        if (visible) {//显示明文
            mEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {//显示密文
            mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        mEditText.setSelection(mEditText.getText().length());
    }

    private class XQuickTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mXQuickTextChangeListener != null) {
                mForceQuickClearVisible = mXQuickTextChangeListener.shouldForceShowQuickClear(s, start, before, count);
            }
            updateQuickClearBtnVisible();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public interface OnXQuickTextChangeListener {
        boolean shouldForceShowQuickClear(CharSequence s, int start, int before, int count);
    }

}
