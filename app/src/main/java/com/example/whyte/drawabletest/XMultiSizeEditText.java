package com.example.whyte.drawabletest;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;


/**
 *
 * Created by luo_shuai on 2015/12/22 11:22.
 */
public class XMultiSizeEditText extends AppCompatEditText {

    /**
     * hint的字体大小
     */
    private int mHintTextSize;

    /**
     * 输入的字体大小
     */
    private int mInputTextSize;

    private boolean mPrevHasInput = false;

    public XMultiSizeEditText(Context context) {
        this(context, null);
    }

    public XMultiSizeEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public XMultiSizeEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int defaultHintTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, metrics);
        int defaultInputTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, metrics);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.XMultiSizeEditText, defStyle, 0);
        mHintTextSize = array.getDimensionPixelSize(R.styleable.XMultiSizeEditText_xHintTextSize, defaultHintTextSize);
        mInputTextSize = array.getDimensionPixelSize(R.styleable.XMultiSizeEditText_xInputTextSize, defaultInputTextSize);
        array.recycle();

        updateTextAndStyle(true);
    }

    protected void updateTextAndStyle(boolean forceUpdate) {
        // 有输入内容
        boolean hasInput = hasInput();
        if (forceUpdate || mPrevHasInput != hasInput) {
            mPrevHasInput = hasInput;
            setTextSize(TypedValue.COMPLEX_UNIT_PX, hasInput ? mInputTextSize : mHintTextSize);
        }
    }

    protected boolean hasInput() {
        return getText() != null && getText().length() > 0;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        updateTextAndStyle(false);
    }

}
