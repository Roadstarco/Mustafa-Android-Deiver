package com.roadstar.customerr.common.base;

/**
 * Created by bilal on 20/03/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.Toolbar;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

public class BadgeDrawerToggle extends ActionBarDrawerToggle {

    private BadgeDrawerArrowDrawable badgeDrawable;

    public BadgeDrawerToggle(Activity activity, DrawerLayout drawerLayout,
                             int openDrawerContentDescRes,
                             int closeDrawerContentDescRes) {
        super(activity, drawerLayout, openDrawerContentDescRes,
                closeDrawerContentDescRes);
        init(activity);
    }

    public BadgeDrawerToggle(Activity activity, DrawerLayout drawerLayout,
                             Toolbar toolbar, int openDrawerContentDescRes,
                             int closeDrawerContentDescRes) {
        super(activity, drawerLayout, toolbar, openDrawerContentDescRes,
                closeDrawerContentDescRes);
        init(activity);
    }

    private void init(Activity activity) {
        Context c = getThemedContext();
        if (c == null) {
            c = activity;
        }
        badgeDrawable = new BadgeDrawerArrowDrawable(c);
        setDrawerArrowDrawable(badgeDrawable);
    }

    public void setBadgeEnabled(boolean enabled) {
        badgeDrawable.setEnabled(enabled);
    }

    public boolean isBadgeEnabled() {
        return badgeDrawable.isEnabled();
    }

    public void setBadgeText(String text) {
        badgeDrawable.setText(text);
    }

    public String getBadgeText() {
        return badgeDrawable.getText();
    }

    public void setBadgeColor(int color) {
        badgeDrawable.setBackgroundColor(color);
    }

    public int getBadgeColor() {
        return badgeDrawable.getBackgroundColor();
    }

    public void setBadgeTextColor(int color) {
        badgeDrawable.setTextColor(color);
    }

    public int getBadgeTextColor() {
        return badgeDrawable.getTextColor();
    }

    private Context getThemedContext() {
        // Don't freak about the reflection. ActionBarDrawerToggle
        // itself is already using reflection internally.
        try {
            Field mActivityImplField = ActionBarDrawerToggle.class
                    .getDeclaredField("mActivityImpl");
            mActivityImplField.setAccessible(true);
            Object mActivityImpl = mActivityImplField.get(this);
            Method getActionBarThemedContextMethod = mActivityImpl.getClass()
                    .getDeclaredMethod("getActionBarThemedContext");
            return (Context) getActionBarThemedContextMethod.invoke(mActivityImpl);
        }
        catch (Exception e) {
            return null;
        }
    }
}


class BadgeDrawerArrowDrawable extends DrawerArrowDrawable {

    // Fraction of the drawable's intrinsic size we want the badge to be.
    private static final float SIZE_FACTOR = .3f;
    private static final float HALF_SIZE_FACTOR = SIZE_FACTOR / 2;

    private Paint backgroundPaint;
    private Paint textPaint;
    private String text;
    private boolean enabled = false;

    public BadgeDrawerArrowDrawable(Context context) {
        super(context);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.RED);
        backgroundPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(SIZE_FACTOR * getIntrinsicHeight());
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (!enabled) {
            return;
        }

        final Rect bounds = getBounds();
        final float x = (1 - HALF_SIZE_FACTOR) * bounds.width();
        final float y = HALF_SIZE_FACTOR * bounds.height();
        canvas.drawCircle(x, y, SIZE_FACTOR * bounds.width(), backgroundPaint);

        if (text == null || text.length() == 0) {
            return;
        }

        final Rect textBounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.drawText(text, x, y + textBounds.height() / 2, textPaint);
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            invalidateSelf();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setText(String text) {
        if (!Objects.equals(this.text, text)) {
            this.text = text;
            invalidateSelf();
        }
    }

    public String getText() {
        return text;
    }

    public void setBackgroundColor(int color) {
        if (backgroundPaint.getColor() != color) {
            backgroundPaint.setColor(color);
            invalidateSelf();
        }
    }

    public int getBackgroundColor() {
        return backgroundPaint.getColor();
    }

    public void setTextColor(int color) {
        if (textPaint.getColor() != color) {
            textPaint.setColor(color);
            invalidateSelf();
        }
    }

    public int getTextColor() {
        return textPaint.getColor();
    }
}