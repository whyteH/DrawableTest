package com.example.whyte.drawabletest;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

public class PrefixedEditText extends XMultiSizeEditText {

    private Drawable mCompoundDrawableLeft, mCompoundDrawableTop,
            mCompoundDrawableRight, mCompoundDrawableBottom;

    private ColorStateList mPrefixTextColor;
    private float mPrefixTextSize;

    private String mPrefix;
    private OnPrefixChangeListener mOnPrefixChangeListener;

    public PrefixedEditText(Context context) {
        this(context, null);
    }

    public PrefixedEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public PrefixedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPrefixTextColor = getTextColors();
        mPrefixTextSize = getTextSize();

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PrefixedEditText, defStyle, 0);
        if (array.hasValue(R.styleable.PrefixedEditText_prefixTextColor)) {
            int prefixColor = array.getColor(R.styleable.PrefixedEditText_prefixTextColor, Color.BLACK);
            setPrefixTextColor(prefixColor);
        }
        if (array.hasValue(R.styleable.PrefixedEditText_prefixTextSize)) {
            float prefixSize = array.getDimensionPixelSize(R.styleable.PrefixedEditText_prefixTextSize, 0);
            setPrefixTextSize(prefixSize);
        }
        array.recycle();
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);
        if (left != null) {
            mCompoundDrawableLeft = left;
        }
        mCompoundDrawableTop = top;
        mCompoundDrawableRight = right;
        mCompoundDrawableBottom = bottom;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new PrefixedInputConnection(super.onCreateInputConnection(outAttrs), true);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.prefix = mPrefix;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setPrefix(ss.prefix);
    }

    @Override
    protected boolean hasInput() {
        return super.hasInput() || hasPrefix();
    }

    public void setOnPrefixChangeListener(OnPrefixChangeListener l) {
        mOnPrefixChangeListener = l;
    }

    public void setPrefix(String prefix) {
        setPrefix(prefix, true);
    }

    public void setPrefix(String prefix, boolean triggerCallback) {
        final String oldPrefix = mPrefix;
        mPrefix = prefix;
        final boolean changed = checkChanged(oldPrefix, prefix);

        if (TextUtils.isEmpty(prefix)) {
            super.setCompoundDrawables(mCompoundDrawableLeft, mCompoundDrawableTop, mCompoundDrawableRight, mCompoundDrawableBottom);

            if (triggerCallback && changed) {
                if (mOnPrefixChangeListener != null) {
                    mOnPrefixChangeListener.onPrefixChanged(false, null);
                }
            }
        } else {
            super.setCompoundDrawables(new TextDrawable(mCompoundDrawableLeft, prefix), mCompoundDrawableTop, mCompoundDrawableRight, mCompoundDrawableBottom);

            if (triggerCallback && changed) {
                if (mOnPrefixChangeListener != null) {
                    mOnPrefixChangeListener.onPrefixChanged(true, mPrefix);
                }
            }
        }

        updateTextAndStyle(false);
    }

    public String getPrefix() {
        return mPrefix;
    }

    public boolean hasPrefix() {
        return hasPrefix(mPrefix);
    }

    public void setPrefixTextColor(int color) {
        mPrefixTextColor = ColorStateList.valueOf(color);
    }

    public void setPrefixTextColor(ColorStateList color) {
        mPrefixTextColor = color;
    }

    public void setPrefixTextSize(float prefixTextSize) {
        mPrefixTextSize = prefixTextSize;
    }

    public float getPrefixTextSize() {
        return mPrefixTextSize;
    }

    protected boolean hasPrefix(String prefix) {
        return prefix != null && prefix.length() > 0;
    }

    private boolean checkChanged(String oldPrefix, String newPrefix) {
        return !(oldPrefix != null && oldPrefix.equals(newPrefix) || (oldPrefix == null && newPrefix == null));
    }

    public interface OnPrefixClickListener {
        void onClick(View view);
    }

    OnPrefixClickListener listener;

    public void setPrefixClickListener(OnPrefixClickListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (listener != null) {
                    Drawable drawableLeft = getCompoundDrawables()[0];
                    int left = ((View) getParent()).getLeft();
                    if (drawableLeft != null && event.getRawX() <= (left + drawableLeft.getBounds().width()) &&
                            event.getRawX() >= left + mCompoundDrawableLeft.getBounds().width()) {
                        listener.onClick(this);
                        return true;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private class TextDrawable extends Drawable {
        private Drawable mDrawable;
        private String mText = "";
        private Paint mTextPaint;
        private Paint mLinePaint;
        private final int mTextWidth;

        public TextDrawable(Drawable drawable, String text) {
            mTextPaint = new Paint(getPaint());
            mTextPaint.setTextSize(getTextSize());
            mTextPaint.setColor(mPrefixTextColor.getColorForState(getDrawableState(), 0));

            mLinePaint = new Paint(getPaint());
            mLinePaint.setColor(Color.parseColor("#FFEAEAEA"));
            mLinePaint.setStrokeWidth(2);

            mDrawable = drawable;
            mText = text;
            mTextWidth = (int) (mTextPaint.measureText(mText) + DensityUtil.dp2px(getContext(), 8));
            int textHeight = (int) mTextPaint.getTextSize();

            int lineWidth = (int) mLinePaint.getStrokeWidth() + DensityUtil.dp2px(getContext(), 10);
            if (mDrawable == null) {
                setBounds(0, 0, mTextWidth, textHeight);
            } else {
                setBounds(0, 0, mDrawable.getIntrinsicWidth() + mTextWidth + lineWidth, Math.min(mDrawable.getIntrinsicHeight(), textHeight));
            }
        }

        @Override
        public void draw(Canvas canvas) {
            int lineBaseline = getLineBounds(0, null);
            int start = 0;
            if (mDrawable != null) {
                Rect bounds = getBounds();
                mDrawable.setBounds(bounds.left, bounds.top, mDrawable.getIntrinsicWidth(), bounds.bottom);
                mDrawable.draw(canvas);

                start = mDrawable.getBounds().right;
                start += getCompoundDrawablePadding();
            }

            canvas.drawText(mText, start, canvas.getClipBounds().top + lineBaseline, mTextPaint);
            start += mTextWidth;
            canvas.drawLine(start, getBounds().top - DensityUtil.dp2px(getContext(), 1), start, getBounds().bottom + DensityUtil.dp2px(getContext(), 2), mLinePaint);
        }

        @Override
        public void setAlpha(int alpha) {/* Not supported */}

        @Override
        public void setColorFilter(ColorFilter colorFilter) {/* Not supported */}

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }
    }

    private class PrefixedInputConnection extends InputConnectionWrapper {

        public PrefixedInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                // Un-comment if you wish to cancel the backspace:
//                 return false;

                int currentLength = length();
                if (currentLength == 0) {
                    if (!TextUtils.isEmpty(mPrefix)) {
                        setPrefix(null);
                    }
                }

            }
            return super.sendKeyEvent(event);
        }

    }

    public static class SavedState extends View.BaseSavedState {

        private String prefix;

        public SavedState(Parcel source) {
            super(source);
            prefix = source.readString();
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(prefix);
        }

        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

    }

    public interface OnPrefixChangeListener {
        void onPrefixChanged(boolean add, CharSequence prefix);
    }

}