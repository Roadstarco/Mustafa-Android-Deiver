package com.roadstar.customerr.common.utils;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.transition.TransitionManager;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.roadstar.customerr.R;

public class ErrorEditText extends AppCompatEditText {
    private static final int DURATION = 500;
    boolean animationPlayed = false;

    @IdRes
    int errorID;

    public ErrorEditText(Context context) {
        super(context);
    }

    public ErrorEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ErrorEditText);

        errorID = typedArray.getResourceId(R.styleable.ErrorEditText_errorTextId, -1);
    }

    public void setError(@StringRes int error) {
        setError(getResources().getString(error));
    }

    @Override
    public void setError(CharSequence error) {
        if (errorID != -1) {
            animateViewChange();
            findTextViewById(errorID).setVisibility(VISIBLE);
            findTextViewById(errorID).setText(error);
        }else {
            super.setError(error);
        }
    }

    private TextView findTextViewById(@IdRes int id) {
        return getRootView().findViewById(id);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (text.length() > 0) {
            animateViewChange();
            findTextViewById(errorID).setVisibility(GONE);
            findTextViewById(errorID).setText("");
        }
    }

    public void animateViewChange() {
        View viewGroup = getRootView();
        if (viewGroup != null && viewGroup instanceof ViewGroup)
            TransitionManager.beginDelayedTransition((ViewGroup) viewGroup);
    }

    public void fadeIn(final TextView view) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(DURATION);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                view.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(alphaAnimation);

    }

    public void fadeOut(final TextView view) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(DURATION);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animationPlayed = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                animationPlayed = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(alphaAnimation);

    }
}
