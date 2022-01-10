package com.roadstar.customerr.common.utils;

import android.content.Context;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.roadstar.customerr.R;

public class BorderErrorEditText extends AppCompatEditText {
    public BorderErrorEditText(Context context) {
        super(context);
    }

    public BorderErrorEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BorderErrorEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setError(CharSequence error) {
        setBackgroundResource(R.drawable.bg_border_error);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (text.length() > 0) {
            setBackgroundResource(R.drawable.bg_border_filled);
        }
    }
}
