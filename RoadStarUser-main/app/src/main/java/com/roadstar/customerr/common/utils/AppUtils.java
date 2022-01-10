package com.roadstar.customerr.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;

import com.roadstar.customerr.app.data.UserManager;
import com.roadstar.customerr.app.data.preferences.PreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.webkit.URLUtil.isNetworkUrl;

/**
 * Contains utility methods used in application
 */
public class AppUtils {

    public static void hideSoftKeyboard(Activity activity) {
        if (activity == null)
            return;
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null)
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSoftKeyboard(Activity activity) {
        if (activity == null)
            return;
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null) {
            view.requestFocus();
            inputMethodManager.showSoftInput(view, 0);
        }
    }

    public static String formatAndRoundOff(double amount) {
        if (amount == 0) {
            return "0";
        }
        return String.valueOf(Math.round(amount));

    }

    public static String formatAndRoundOffDigits(double amount, int precision) {
        if (amount == 0) {
            return "0";
        }
        int scale = (int) Math.pow(10, precision);
        return String.valueOf(Math.round(amount * scale) / scale);

    }

    public static boolean validateUri(Uri uri) {
        if (uri == null)
            return false;
        else {
            String path = uri.getPath();
            return !(uri.equals(Uri.EMPTY) || path == null || path.equals("null"));
        }
    }

    public static boolean ifNotNullEmpty(String text) {
        return text != null && !text.isEmpty();
    }

    public static String toTitleCase(String givenString) {
        if (!AppUtils.ifNotNullEmpty(givenString) || givenString.equalsIgnoreCase(" "))
            return "";
        else {
            String[] arr = givenString.trim().split("\\s+");
            StringBuilder sb = new StringBuilder();
            for (String anArr : arr)
                sb.append(Character.toUpperCase(anArr.charAt(0)))
                        .append(anArr.substring(1)).append(" ");
            return sb.toString().trim();
        }
    }

    public static boolean validateEmptyEditText(EditText et) {
        return !et.getText().toString().equals("");
    }
    public static String getEditTextString(EditText et) {
      return   et.getText().toString().trim();
    }
    public static boolean validateEmptyString(String string) {
        return !string.equals("");
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;
        if (ifNotNullEmpty(email)) {
            try {
                String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
                Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(email);
                if (matcher.matches())
                    isValid = true;
            } catch (Exception e) {
                Logger.error("isEmailValid", e.toString());
            }
        }
        return isValid;
    }

    public static String preparePathForPicture(String path, String imageType) {
        String preparedLink;
        if (isNetworkUrl(path)) {
            String[] arr = path.split("\\.(?=[^\\.]+$)");
            preparedLink = arr[0] + imageType + ".jpg";
        } else
            preparedLink = path;
        return preparedLink;
    }

    public static Map<String, String> getHeaderParams() {
        Map<String, String> map = new HashMap<>();
       // map.put("cookie", SharedPreferenceManager.getInstance().read(PreferenceUtils.COOKIE, ""));  // Add required header parameters (Cookies) here
        map.put("Authorization", "Bearer " + UserManager.getToken());  // Add required header parameters (Cookies) here
       map.put("X-Requested-With", "XMLHttpRequest");

        Log.e(PreferenceUtils.ACCESS_TOKEN, map.get("Authorization"));
        return map;
    }

    /**
     * Converting dp to pixel
     */
    public static int dpToPx(int dp, Context context) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static String getStringFromJson(JSONObject data, String key) {
        try {
            return data.has(key) && !data.get(key).equals(null) ?
                    data.getString(key) : "";
        } catch (JSONException e) {
            Logger.caughtException(e);
        }
        return "";
    }

    public static int getIntFromJson(JSONObject data, String key) {
        try {
            return data.has(key) && !data.get(key).equals(null) ?
                    data.getInt(key) : 0;
        } catch (JSONException e) {
            Logger.caughtException(e);
        }
        return 0;
    }

    public static long getLongFromJson(JSONObject data, String key) {
        try {
            return data.has(key) && !data.get(key).equals(null) ?
                    data.getLong(key) : 0;
        } catch (JSONException e) {
            Logger.caughtException(e);
        }
        return 0;
    }

    public static long getLongDateFromJson(JSONObject data, String key) {
        try {
            return data.has(key) && !data.get(key).equals(null) ?
                    data.getLong(key) : 0;
        } catch (JSONException e) {
            Logger.caughtException(e);
        }
        return System.currentTimeMillis();
    }

    public static double getDoubleFromJson(JSONObject data, String key, double defaultValue) {
        try {
            return data.has(key) && !data.get(key).equals(null) ?
                    data.getDouble(key) : defaultValue;
        } catch (JSONException e) {
            Logger.caughtException(e);
        }
        return 0;
    }

    public static double getDoubleFromJson(JSONObject data, String key) {
        return getDoubleFromJson(data, key, 0);
    }

    public static boolean getBooleanFromJson(JSONObject data, String key) {
        try {
            return (data.has(key) && !data.get(key).equals(null)) && data.getBoolean(key);
        } catch (JSONException e) {
            Logger.caughtException(e);
        }
        return false;
    }

    public static void showWebDialog(Context context, String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);

    }

    public static void showFacebookDialog(Context context, String pageUrl, String packageID) {
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = getFacebookPageURL(context, pageUrl, packageID);
        facebookIntent.setData(Uri.parse(facebookUrl));
        try {
            context.startActivity(facebookIntent);
        } catch (ActivityNotFoundException ex) {
            // start web browser and the facebook mobile page as fallback
            String uriMobile = "http://touch.facebook.com/pages/x/" + packageID;
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uriMobile));
            context.startActivity(i);
        }
    }

    public static void showInstaGramDialog(Context context, String url) {
        Intent intent = getInstaGramIntent(context, url);
        if (intent == null)
            return;
        context.startActivity(intent);
    }

    private static String getFacebookPageURL(Context context, String pageUrl, String packageID) {

        String FACEBOOK_URL = pageUrl;
        String FACEBOOK_PAGE_ID = packageID;
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            boolean activated = packageManager.getApplicationInfo("com.facebook.katana", 0).enabled;
            if (activated) {
//                if ((versionCode >= 3002850)) {
//                    return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
//                } else {
                return "fb://page/" + FACEBOOK_PAGE_ID;
//                }
            } else {
                return FACEBOOK_URL;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL;
        }
    }

    private static Intent getInstaGramIntent(Context context, String url) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager.getPackageInfo("com.instagram.android", 0) != null) {
                if (url.endsWith("/")) {
                    url = url.substring(0, url.length() - 1);
                }
                final String username = url.substring(url.lastIndexOf("/") + 1);
                // http://stackoverflow.com/questions/21505941/intent-to-open-instagram-user-profile-on-android
                intent.setData(Uri.parse("http://instagram.com/_u/" + username));
                intent.setPackage("com.instagram.android");
                return intent;
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        intent.setData(Uri.parse(url));
        return intent;
    }

    public static void openMailIntent(Context context, String emailTo, String subject) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", emailTo, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    public static void openShareDialog(Context context, String message) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);
        context.startActivity(Intent.createChooser(share, ""));
    }

    public static int parseStringToInteger(String value) {
        if (value.trim().equalsIgnoreCase(""))
            return 0;
        try {
            int intValue = Integer.parseInt(value);
            return intValue;
        } catch (Exception e) {
            return 0;
        }
    }

    public static void disableField(EditText etField) {
        etField.setEnabled(false);
        etField.setClickable(false);
        etField.setFocusable(false);
        etField.setFocusableInTouchMode(false);
    }

    public static boolean isNotFieldEmpty(AppCompatEditText editText) {
        if (editText == null)
            return false;
        return ifNotNullEmpty(editText.getText().toString());
    }

    public static boolean isNotFieldEmpty(TextView editText) {
        if (editText == null)
            return false;
        return ifNotNullEmpty(editText.getText().toString());
    }

    public static Bitmap createDrawableFromView(View view, Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        //   view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static String getLocationLabel(int position) {
        String[] labels = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        return position < labels.length ? labels[position] : "";
    }

    public static void setResultAndFinish(Activity activity) {
        Intent intent = new Intent();
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    public static String formatNumberToTwoDigits(double amount) {
        return String.format("%.2f", amount);
//        if (amount == 0) {
//            return "0.00";
//        }
//        NumberFormat formatter = NumberFormat.getNumberInstance();
//        formatter.setMinimumFractionDigits(2);
//        formatter.setMaximumFractionDigits(2);
//        String outputAmount = formatter.format(amount);
//        return outputAmount;

    }

    public static String formatNumberToOneDigits(double amount) {
        return String.format("%.1f", amount);
//        if (amount == 0) {
//            return "0.0";
//        }
//        NumberFormat formatter = NumberFormat.getNumberInstance();
//        formatter.setMinimumFractionDigits(1);
//        formatter.setMaximumFractionDigits(1);
//        String outputAmount = formatter.format(amount);
//        return outputAmount;
    }

    public static double convertStringToDouble(String text) {
        if (text.trim().equalsIgnoreCase(""))
            return 0;
        double number = 0;
        try {
            number = Double.parseDouble(text.trim());
        } catch (Exception e) {
            number = 0;
        }
        return number;
    }

    public static int convertStringToInteger(String text) {
        if (text.trim().equalsIgnoreCase(""))
            return 0;
        int number = 0;
        try {
            number = Integer.parseInt(text.trim());
        } catch (Exception e) {
            number = 0;
        }
        return number;
    }

    @SuppressLint("MissingPermission")
    public static void initiateCall(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }


    public static String convertSecToMin(int totalSecs) {
        if (totalSecs == 0)
            return "";
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;
        String timeString = String.format("%02d:%02d", minutes, seconds);
        return timeString;
    }



    public static String stringForTime(int timeMs) {
        StringBuilder mFormatBuilder;
        Formatter mFormatter;
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

}
