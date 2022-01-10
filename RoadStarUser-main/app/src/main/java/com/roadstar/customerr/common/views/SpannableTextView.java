package com.roadstar.customerr.common.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.View;

import com.roadstar.customerr.R;


/**
 * Created by JunaidAhmed on 1/17/2019.
 */

public class SpannableTextView extends androidx.appcompat.widget.AppCompatTextView {

    private OnSpanTextClickListener onSpanTextClickListener = null;
    private String spanText;
    private int spanTextColor;
    private boolean isSpanTextClickable;
    private boolean underlineSpan;
    private boolean boldSpan;
    private boolean italicSpan;
    private SpannableString sb;

    public interface OnSpanTextClickListener {
        void onSpanTextClick(View view, int subStringIndex);
    }

    public SpannableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(attrs);
    }

    private void setAttributes(AttributeSet attrs) {

        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.SpannableTextView, 0, 0);
        try {
            String spanText = typedArray.getString(R.styleable.SpannableTextView_span_text);
            spanTextColor = typedArray.getColor(R.styleable.SpannableTextView_span_text_color, Color.BLACK);
            isSpanTextClickable = typedArray.getBoolean(R.styleable.SpannableTextView_set_span_clickable, false);
            underlineSpan = typedArray.getBoolean(R.styleable.SpannableTextView_set_span_underlined, false);
            boldSpan = typedArray.getBoolean(R.styleable.SpannableTextView_set_span_bold, false);
            italicSpan = typedArray.getBoolean(R.styleable.SpannableTextView_set_span_italic, false);
            if (spanText != null && spanText.length() > 0) {
                setSpannableProperties(spanText, 0, spanTextColor, isSpanTextClickable, underlineSpan, boldSpan, italicSpan);
            }
        } finally {
            typedArray.recycle();
        }
    }

    private void setSpannableProperties(String spanText, final int subStringIndex, int spanTextColor, boolean isSpanTextClickable, boolean underlineSpan, boolean boldSpan, boolean italicSpan) {
        String completeText = getText().toString();
        if (sb == null)
            sb = new SpannableString(completeText);

        int startIndex = completeText.indexOf(spanText);
        int endIndex = completeText.indexOf(spanText) + spanText.length();

        sb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                if (onSpanTextClickListener != null) {

                    onSpanTextClickListener.onSpanTextClick(view, subStringIndex);
                }
            }
        }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        sb.setSpan(new ForegroundColorSpan(spanTextColor), startIndex, endIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        if (underlineSpan)
            sb.setSpan(new UnderlineSpan(), startIndex, endIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        if (boldSpan)
            sb.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (italicSpan)
            sb.setSpan(new StyleSpan(Typeface.ITALIC), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        setText(sb, BufferType.SPANNABLE);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setOnSpanTextClickListener(OnSpanTextClickListener onSpanTextClickListener) {
        this.onSpanTextClickListener = onSpanTextClickListener;
    }

    public String getSpanText() {
        return this.spanText;
    }

    public void setSubString(String[] subStrings) {
        for (int subStringIndex = 0; subStringIndex < subStrings.length; subStringIndex++) {
            setSpannableProperties(subStrings[subStringIndex], subStringIndex, spanTextColor, isSpanTextClickable, underlineSpan, boldSpan, italicSpan);
        }
    }

}
