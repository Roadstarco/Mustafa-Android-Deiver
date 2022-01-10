package com.roadstar.customerr.common.base;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.AutoTransition;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.roadstar.customerr.R;
import com.roadstar.customerr.app.business.OnContentChangeListener;
import com.roadstar.customerr.app.business.OnNetworkTaskListener;
import com.roadstar.customerr.app.data.UserManager;
import com.roadstar.customerr.app.data.preferences.SharedPreferenceManager;
import com.roadstar.customerr.app.module.ui.SplashActivity;
import com.roadstar.customerr.app.network.AppNetworkTask;
import com.roadstar.customerr.app.network.HttpResponseItem;
import com.roadstar.customerr.common.utils.AppUtils;
import com.roadstar.customerr.common.utils.BadgeDrawable;
import com.roadstar.customerr.common.utils.BorderErrorEditText;
import com.roadstar.customerr.common.utils.Logger;
import com.roadstar.customerr.common.utils.NetworkUtils;
import com.roadstar.customerr.common.utils.ViewUtils;
import com.roadstar.customerr.common.views.CustomSnackBar;
import com.roadstar.customerr.common.views.VizImageView;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

abstract public class BaseActivity extends AppCompatActivity implements OnContentChangeListener, OnNetworkTaskListener {
    public Dialog dialog;

    private static final String TAG = "BaseActivity";
    //******** MP *******//
    public boolean doStartService = true;
    public static final int CALL_REQUEST = 1001;
    public String phoneNumber = "";
    // region VARIABLESÃ¸
    private RelativeLayout toolbar;
    private CharSequence appTitle;
    private boolean allowedToExit = false;
    public int width = 0, height = 0;
    // endregion

    public DrawerLayout mDrawerLayout;
    public BadgeDrawerToggle mDrawerToggle;


    //  private JobRequestFragment requestFragment;

    // region NETWORK_RECEIVER
    private BroadcastReceiver networkConnectivityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            onConnectivityChanged();
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("BaseActivity:", "Activity BaseActivity called");
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        this.dialog = getProgressDialog(false);

    }


    @Override
    public Toolbar getToolbar() {
        return null;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    private void onConnectivityChanged() {

    }
    // endregion

    //region LIFE_CYCLE
    @Override
    protected void onStart() {
        super.onStart();
        if (UserManager.isUserLoggedIn() && doStartService) {

        }
    }


    @Override
    public void onBackPressed() {

        AppUtils.hideSoftKeyboard(this);
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount <= 1) {
            if (allowedToExit || !isTaskRoot()) {

                finish(this);
                return;
            } else {
                allowedToExit = true;
                ViewUtils.showToast(BaseActivity.this, "Press again to exit", Toast.LENGTH_SHORT);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        allowedToExit = false;
                    }
                }, 1000);
                return;
            }
        }
        // we have more than 1 fragments in back stack
        if (backStackEntryCount > 1) {
            AppUtils.hideSoftKeyboard(BaseActivity.this);
            onRemoveCurrentFragment();

        } else {
            super.onBackPressed();
            // finish(this);
        }

    }


    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        toolbar = null;

        clearNotification();
        Log.i("Base Activity", "onDestory");
    }

    private void clearNotification() {
        NotificationManager nManager = ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));
        nManager.cancelAll();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void setTitle(CharSequence title) {
        appTitle = title;
        if (toolbar != null && findViewById(R.id.tv_title) != null)
            ((TextView) toolbar.findViewById(R.id.tv_title)).setText(appTitle);
    }
    // endregion

    public String getLogTag() {
        return getClass().getSimpleName();
    }

    // region CONTENT_CHANGE_LISTENER


    /**
     * Get {@link Toolbar} of {@link BaseActivity}
     *
     * @return Toolbar
     */
//    @Override
//    public Toolbar getToolbar() {
//        return toolbar;
//    }

    /**
     * Remove current fragment from {@link FragmentTransaction}
     */
    @Override
    public void onRemoveCurrentFragment() {
        // some times FragmentActivity.getLoaderManager returns null
        try {
            AppUtils.hideSoftKeyboard(this);
            getSupportFragmentManager().popBackStackImmediate();
            getSupportFragmentManager().beginTransaction().commit();
        } catch (Exception e) {
            Logger.caughtException(e);
        }
    }

    /**
     * Remove given fragment from {@link FragmentTransaction}
     *
     * @param fragment {@link Fragment}
     */
    @Override
    public void onRemoveCurrentFragment(Fragment fragment) {
        // some time FragmentActivity.getLoaderManager returns null
        try {
            if (fragment == null) return;
            View view = fragment.getView();
            if (view != null) {
                ((ViewGroup) view).removeAllViews();
                view.invalidate();
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(fragment).commit();
            AppUtils.hideSoftKeyboard(this);
        } catch (Exception e) {
            Logger.caughtException(e);
        }
    }

    /**
     * Add fragment in given {@link android.widget.FrameLayout}
     *
     * @param fragment  {@link Fragment}
     * @param contentId Id of {@link android.widget.FrameLayout}
     */
    @Override
    public void onAddFragment(Fragment fragment, int contentId) {
        onAddFragment(fragment, contentId, false);
    }

    /**
     * Add fragment in given {@link android.widget.FrameLayout}
     *
     * @param fragment   {@link Fragment}
     * @param addToStack true add else do not add
     */
    @Override
    public void onAddFragment(Fragment fragment, boolean addToStack) {
        onAddFragment(fragment, R.id.content_frame, addToStack);
    }

    /**
     * Add fragment in given {@link android.widget.FrameLayout}
     *
     * @param fragment       {@link Fragment}
     * @param contentId      Id of {@link android.widget.FrameLayout}
     * @param addToBackStack true add else do not add
     */
    @Override
    public void onAddFragment(Fragment fragment, int contentId, boolean addToBackStack) {
        if (fragment == null) return;
        String tag = fragment.getClass().getSimpleName().toLowerCase(Locale.getDefault());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(contentId, fragment, tag);
        if (addToBackStack) transaction.addToBackStack(tag);
        transaction.commit();
    }

    /**
     * Replace fragment in given {@link android.widget.FrameLayout}
     *
     * @param fragment  {@link Fragment}
     * @param contentId Id of {@link android.widget.FrameLayout}
     */
    @Override
    public void onReplaceFragment(Fragment fragment, int contentId) {
        onReplaceFragment(fragment, contentId, false);
    }

    /**
     * Replace fragment in given {@link android.widget.FrameLayout}
     *
     * @param fragment   {@link Fragment}
     * @param addToStack true add else do not add
     */
    @Override
    public void onReplaceFragment(Fragment fragment, boolean addToStack) {
//      onRemoveCurrentFragment();

        onReplaceFragment(fragment, R.id.content_frame, addToStack);
    }

    /**
     * Replace fragment in given {@link android.widget.FrameLayout}
     *
     * @param fragment   {@link Fragment}
     * @param contentId  Id of {@link android.widget.FrameLayout}
     * @param addToStack true add else do not add
     */

    @Override
    public void onReplaceFragment(Fragment fragment, int contentId, boolean addToStack) {
        if (fragment == null) return;
        String tag = fragment.getClass().getSimpleName().toLowerCase(Locale.getDefault());
        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(tag, 0);
        if (fragmentPopped || manager.findFragmentByTag(tag) != null) { //fragment   in back stack, or Current Fragment is   visible  .
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        if (getSupportFragmentManager().getBackStackEntryCount() < 1)
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                android.R.anim.fade_in, android.R.anim.fade_out);
//        else
//            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
//                    R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.setReorderingAllowed(true);
        transaction.replace(contentId, fragment, tag);
        if (addToStack)
            transaction.addToBackStack(tag);
        transaction.commit();
        // changeTollBar();
    }

    /**
     * Remove current fragment from {@link FragmentTransaction}
     * & Replace fragment in given {@link android.widget.FrameLayout}
     *
     * @param curFragment    Current Fragment
     * @param newFragment    {@link Fragment}
     * @param addToBackStack true add else do not add
     */
    public void onRemoveAndReplaceFragment(Fragment curFragment, Fragment newFragment, boolean addToBackStack) {
        onRemoveCurrentFragment();
        onReplaceFragment(newFragment, addToBackStack);
    }

    /**
     * Get fragment from given id of {@link android.widget.FrameLayout}
     *
     * @param contentId Id of {@link android.widget.FrameLayout}
     * @return Fragment
     */
    @Override
    public Fragment getCurrentFragment(int contentId) {
        return getSupportFragmentManager().findFragmentById(contentId);
    }

    /**
     * Get Fragment from default {@link android.widget.FrameLayout}
     *
     * @return Fragment
     */
    @Override
    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.content_frame);
    }

    @Override
    public void onClearBackStack() {
        try {
            FragmentManager manager = getSupportFragmentManager();
            if (manager.getBackStackEntryCount() > 0) {
                FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
                manager.popBackStackImmediate(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        } catch (Exception e) {
            Logger.error("onClearBackStack", e.toString());
        }
    }

    /**
     * @param title title of {@link NavigationView}
     */
    @Override
    public void setDrawerTitle(CharSequence title) {
        if (title != null && !title.toString().isEmpty())
            setTitle(title);
        else
            setTitle("");
    }

    /**
     * Get parent view of {@link BaseActivity}
     *
     * @return View
     */
    @Override
    public View getParentView() {
        View view = findViewById(R.id.layout_main);
        if (view == null)
            view = findViewById(android.R.id.content).getRootView();
        if (view == null)
            view = getWindow().getDecorView().findViewById(android.R.id.content);
        if (view == null)
            view = ((ViewGroup) this
                    .findViewById(android.R.id.content)).getChildAt(0);
        return view;
    }
    //endregion

    // region VIEW
    public void setActionBar() {
        setActionBar("");
    }

    public void setActionBar(String title) {
        toolbar = findViewById(R.id.rl_toolbar);

        setTitle(title);
        ImageView ivBack = findViewById(R.id.iv_back);
        if (ivBack != null) {
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onBackPressed();
                }
            });
        }

        try {
            if (toolbar.findViewById(R.id.iv_menu) != null) {
                ImageView ivMenu = toolbar.findViewById(R.id.iv_menu);
                if (ivMenu != null) {
                    ivMenu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            openDrawer();


                        }
                    });
                }
            }
        } catch (Exception e) {

        }
    }


    public void setImageActionBar(String title, String subTitle, String imageUrl) {
        toolbar = findViewById(R.id.rl_toolbar);
        if (toolbar != null) {
//            toolbar.setElevation(0f);
//            setSupportActionBar(toolbar);
            findToolbarTextView(R.id.tv_title).setText(title);
            findToolbarTextView(R.id.tv_sub_title).setText(subTitle);
            findToolbarVizImageView(R.id.iv_viz_toolbar).setVisibility(View.VISIBLE);
            findToolbarVizImageView(R.id.iv_viz_toolbar).setImage(imageUrl);
            findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    public VizImageView findToolbarVizImageView(@IdRes int viewId) {
        return ((VizImageView) toolbar.findViewById(viewId));
    }

    public TextView findToolbarTextView(@IdRes int viewId) {
        return ((TextView) toolbar.findViewById(viewId));
    }

    public void setActionBarImage(int resourceId) {
        if (toolbar != null) {
            ImageView ivToolbar = findViewById(R.id.iv_toolbar);
            ivToolbar.setBackgroundResource(resourceId);
        }
    }

    public void setSubTitle(String subTitle) {
        if (toolbar != null) {
            TextView tvSubTitle = findViewById(R.id.tv_sub_title);
            tvSubTitle.setText(subTitle);
            tvSubTitle.setVisibility(AppUtils.ifNotNullEmpty(subTitle) ? View.VISIBLE : View.GONE);
        }
    }

    public void hideBackButton() {
        if (toolbar != null) {
            findViewById(R.id.iv_back).setVisibility(View.INVISIBLE);
        }
    }


    public void setDrawerActionBarWithBadge(String title, boolean showBadge, int badgeCount) {
        toolbar = findViewById(R.id.toolbar);
        setActionBar(title);
    }

    public void updateDrawerBadgeCount(int badgeCount) {
        if (mDrawerToggle == null)
            return;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(setBadgeCount(this, R.drawable.btn_menu, badgeCount));
        }
//        TextView tvNot = findViewById(R.id.tv_not_count);
//        if (tvNot != null) {
//            tvNot.setText(badgeCount > 99 ? "99+" : badgeCount + "");
//            tvNot.setVisibility(badgeCount > 0 ? View.VISIBLE : View.GONE);
//        }
    }

    private Drawable setBadgeCount(Context context, int res, int badgeCount) {
        LayerDrawable icon = (LayerDrawable) ContextCompat.getDrawable(context, R.drawable.badge_placeholders);
        Drawable mainIcon = ContextCompat.getDrawable(context, res);
        BadgeDrawable badge = new BadgeDrawable(context);
        if (badgeCount > 0)
            badge.setCount("");
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
        icon.setDrawableByLayerId(R.id.ic_main_icon, mainIcon);
        return icon;
    }

    /**
     * Set action bar drawer toggle
     */
    public void setDrawerToggle() {
        mDrawerToggle = new BadgeDrawerToggle(this, mDrawerLayout,
                R.string.str_open, R.string.str_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                AppUtils.hideSoftKeyboard(BaseActivity.this);
                ActivityCompat.invalidateOptionsMenu(BaseActivity.this);
                syncState();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                setTitle(appTitle);
                ActivityCompat.invalidateOptionsMenu(BaseActivity.this);
                syncState();
                int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
                // backStackCount == 1 we choose fragment from drawer
                if (backStackCount > 1)
                    // we have more than 2 fragments so we must show back button instead of hum burger icon
                    animDrawerIcon(backStackCount < 2);
                BaseActivity.this.onDrawerClosed();
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                // boolean isDrawerOpen = mDrawerLayout.isDrawerOpen(GravityCompat.START);
                if (getSupportFragmentManager().getBackStackEntryCount() > 1)// && isDrawerOpen)
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                else //if (!isDrawerOpen)
                    mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        };


        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.syncState();

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }


    public void onDrawerClosed() {
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    final public void setBackStackListener() {
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {

            @Override
            public void onBackStackChanged() {
                // int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
                int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
                Log.e("count", "" + backStackCount);
                if (backStackCount == 0)
                    return;
                onDrawerEnabled(backStackCount == 1, backStackCount);
                changeTollBar();
            }
        });
    }

    private void onDrawerEnabled(boolean isEnabled, int backStackCount) {
        Log.e("count", "" + isEnabled);
        if (mDrawerToggle != null && backStackCount <= 2)
            animDrawerIcon(isEnabled);
    }

    public void setCount(int count) {
        if (mDrawerToggle == null)
            return;
        if (count == 0) {
            mDrawerToggle.setBadgeEnabled(false);
            mDrawerToggle.setBadgeText("");
        } else {
            mDrawerToggle.setBadgeEnabled(true);
            mDrawerToggle.setBadgeText("" + count);
        }
    }

    private void animDrawerIcon(boolean isEnabled) {
        ValueAnimator anim = ValueAnimator.ofFloat(isEnabled ? 0 : 1, isEnabled ? 0 : 1);
        Log.e("count", "" + (isEnabled ? 1 : 0) + "-->" + (isEnabled ? 0 : 1));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float slideOffset = (Float) valueAnimator.getAnimatedValue();
                if (mDrawerLayout != null && mDrawerToggle != null)
                    mDrawerToggle.onDrawerSlide(mDrawerLayout, slideOffset);
            }
        });
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(0);
        anim.start();
    }

    private void changeTollBar() {
        Fragment oldFragment = getCurrentFragment();
        if (oldFragment instanceof BaseFragment) {
            BaseFragment fragment = (BaseFragment) oldFragment;
            // fragment.onChangeOptionsMenuVisibility(true);
            setTitle(AppUtils.ifNotNullEmpty(fragment.getTitle()) ? fragment.getTitle() : "");
        } else
            setTitle("");
    }


    // region NETWORK_OPERATIONS

    /**
     * @return true is connected else not
     */
    @Override
    final public boolean isNetworkConnected() {
        return NetworkUtils.hasNetworkConnection(BaseActivity.this, true);
    }

    /**
     * HTTP response call back from {@link AppNetworkTask}
     *
     * @param response {@link HttpResponseItem}
     */
    @Override
    public void onNetworkResponse(HttpResponseItem response) {
        boolean status = response.getResponseCode() == HttpURLConnection.HTTP_OK;
        if (status)
            onNetworkSuccess(response);
        else
            onNetworkError(response);
    }

    /**
     * HTTP network operation is successfully completed
     *
     * @param response {@link HttpResponseItem}
     */
    @Override
    public void onNetworkSuccess(HttpResponseItem response) {
        Logger.info(getLogTag(), "Network operation (" + response.getHttpRequestUrl() + ") successfully completed");
        Logger.info(getLogTag(), "Network operation Response " + response.getResponse());

    }

    /**
     * For some reasons there is/are some error(s) in network operation
     *
     * @param response {@link HttpResponseItem}
     */
    @Override
    public void onNetworkError(HttpResponseItem response) {
        Logger.error(getLogTag(), response.getDefaultResponse() + "(network error)");
        if (response.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED){
            Toast.makeText(this, "invalid email or password", Toast.LENGTH_SHORT).show();
        }
        else
            showSnackBar(getString(R.string.error_message), Snackbar.LENGTH_SHORT);

    }

    /**
     * For some reasons network operation has been cancelled
     *
     * @param response {@link HttpResponseItem}
     */
    @Override
    final public void onNetworkCanceled(HttpResponseItem response) {
        Logger.error(getLogTag(), response.getDefaultResponse() + " (operation cancelled by user)");
    }
    // endregion

    /**
     * Show snackBar in current view
     *
     * @param message  message to be displayed
     * @param duration defined snackBar duration flags
     */
    public void showSnackBar(String message, int duration) {
        ViewGroup viewGroup = findViewById(R.id.layout_main);
        if (viewGroup == null) {
            View view = getParentView();
            if (view == null)
                return;
            Snackbar.make(view, message, (duration <= 0 && duration >= -2 ? duration : Snackbar.LENGTH_SHORT)).show();
        } else {
            CustomSnackBar customSnackbar = CustomSnackBar.make(viewGroup, duration);
            customSnackbar.setText(message);
            customSnackbar.show();
        }
    }

    /**
     * Show snackBar in current view
     *
     * @param message message to be displayed
     */
    public void showSnackBar(String message) {
        showSnackBar(message, Snackbar.LENGTH_SHORT);
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * If server response code is unauthorized (session expired)
     */
    private void showSessionExpiredDialog() {
        if (isFinishing()) {
            return;
        }
        SharedPreferenceManager.getInstance().clearPreferences();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Session expired");
        builder.setMessage("Your session got expired kindly sign in again.");
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(BaseActivity.this, SplashActivity.class));
                finish();
            }
        });
        builder.create().show();
    }

    /**
     * Create and return a waiting progress dialog
     *
     * @param cancelable is dialog cancelable
     * @return a new progress dialog
     */
//    public ProgressDialog getProgressDialog(boolean cancelable) {
//        ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setMessage("Please wait!");
//        dialog.setCancelable(cancelable);
//        return dialog;
//    }
    public Dialog getProgressDialog(boolean cancelable) {
        return getProgressDialog(cancelable, false);
    }

    public Dialog getProgressDialog(boolean cancelable, boolean isTransparent) {
        Dialog dialog;
        if (isTransparent) {
            dialog = new Dialog(this, R.style.ThemeDialog);
            dialog.setContentView(R.layout.app_dialog_trans);
        } else {
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.app_dialog);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(cancelable);
        return dialog;
    }

    public void showProgressDialog() {
        if (dialog != null)
            dialog.show();
    }

    public void hideProgressDialog() {
        if (dialog != null)
            dialog.hide();
    }


    public void animate() {
        if (getParentView() != null && getParentView() instanceof ViewGroup)
            TransitionManager.beginDelayedTransition((ViewGroup) getParentView());
    }

    public void animate(int view_group_id) {
        View viewGroup = findViewById(view_group_id);
        Transition transition = new AutoTransition();
        transition.setStartDelay(100);
        transition.setDuration(400);

        if (viewGroup != null && viewGroup instanceof ViewGroup)
            TransitionManager.beginDelayedTransition((ViewGroup) viewGroup, transition);
    }

    public void animateViewChange(int view_group_id) {
        View viewGroup = findViewById(view_group_id);
        if (viewGroup != null && viewGroup instanceof ViewGroup)
            TransitionManager.beginDelayedTransition((ViewGroup) viewGroup);
    }

    public void animate(int view_group_id, Transition transition) {
        View viewGroup = findViewById(view_group_id);
        if (viewGroup != null && viewGroup instanceof ViewGroup)
            TransitionManager.beginDelayedTransition((ViewGroup) viewGroup);
    }


    public void onBindService() {
    }

    public void setBackgroundResource(Context context, View layout, int drawable) {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            layout.setBackgroundDrawable(ContextCompat.getDrawable(context, drawable));
        } else {
            layout.setBackground(ContextCompat.getDrawable(context, drawable));
        }
    }

    /**
     * This method is responsible make a call and also
     * checking run time permissions for CALL_PHONE
     */
    public void callPhoneNumber(Context context, String phoneNumber) {
        if (!AppUtils.ifNotNullEmpty(phoneNumber))
            return;
        try {
            this.phoneNumber = phoneNumber;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST);
                    return;
                }
            }
            AppUtils.initiateCall(context, phoneNumber);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public BorderErrorEditText findBorderErrorFieldById(int resourceID) {
        return findViewById(resourceID);
    }

    public EditText findFieldById(int resourceID) {
        return findViewById(resourceID);
    }

    public AppCompatCheckBox findCheckBox(int resourceID) {
        return findViewById(resourceID);
    }

    public TextView findTextViewById(int resourceID) {
        return findViewById(resourceID);
    }

    public AppCompatButton findButtonById(int resourceID) {
        return findViewById(resourceID);
    }

    public RatingBar findRatingBarByID(int resourceID) {

        return findViewById(resourceID);
    }

    public RadioButton findRadioButtonById(int resourceID) {

        return findViewById(resourceID);
    }

    public SwitchCompat findSwitchByID(int resourceID) {
        return findViewById(resourceID);
    }

    public VizImageView findVizImageView(@IdRes int viewId) {
        return ((VizImageView) findViewById(viewId));
    }

    public ImageView findImageView(int viewId) {
        return findViewById(viewId);
    }

    public AppCompatImageView findAppCompImageView(int viewId) {
        return findViewById(viewId);
    }

    public AppCompatEditText findEditTextById(int resourceID) {
        return findViewById(resourceID);
    }

    public TextView findTextviewTextById(int resourceID) {
        return findViewById(resourceID);
    }

    public TextInputLayout findTextInputLayout(int resourceID) {
        return findViewById(resourceID);
    }


    public void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static void startActivityWithNoHistory(Activity from, Class<?> to) {
        Intent intent = new Intent(from, to);
        //  intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //  intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        from.startActivity(intent);
        from.overridePendingTransition(R.anim.enter_activity, R.anim.hold_activity);
    }

    public static void startActivityWithNoHistory(Activity from, Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        from.startActivity(intent);
        from.overridePendingTransition(R.anim.enter_activity, R.anim.hold_activity);
    }

    public static void startActivity(Activity from, Class<?> to) {
        Intent intent = new Intent(from, to);
        from.startActivity(intent);
        from.overridePendingTransition(R.anim.enter_activity, R.anim.hold_activity);
    }

    public static void startActivity(Activity from, Intent intent) {
        from.startActivity(intent);
        from.overridePendingTransition(R.anim.enter_activity, R.anim.hold_activity);
    }

    public static void finish(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(0, R.anim.exit_activity);
    }

    public static void replaceFragment(AppCompatActivity from, Fragment fragment, int containerId) {
        FragmentTransaction ft = from.getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_enter, R.anim.fragment_exit);
        ft.replace(containerId, fragment)
                .addToBackStack(null).commit();
    }

    public void resetScreen(ViewGroup root) {

        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof EditText) {
                ((EditText) v).setText("");
            } else if (v instanceof ViewGroup) {
                resetScreen((ViewGroup) v);
            }
        }

    }

    public void setHeaderData() {
        findTextViewById(R.id.tv_user_name).setText(UserManager.getFirstname() + " " + UserManager.getLastName());
        findTextViewById(R.id.tv_email).setText(UserManager.getEmail());
        findVizImageView(R.id.iv_user_image).setImage(UserManager.getImage());
    }

    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.

    String[] permissions = new String[]{
            // Manifest.permission.WRITE_EXTERNAL_STORAGE,
            //  Manifest.permission.READ_EXTERNAL_STORAGE,

            //  Manifest.permission.CAMERA,
            //Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.CALL_PHONE,
    };


    public boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (getCurrentFragment() instanceof BaseFragment)
            getCurrentFragment().onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    String permissionsDenied = "";
                    for (String per : permissionsList) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            permissionsDenied += "\n" + per;

                        }

                    }
                    // Show permissionsDenied

                }
                return;
            }
        }
    }


    public void tintImageView(int imageId, int tintColor) {
        findAppCompImageView(imageId).setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), tintColor)));
    }
}