package com.shopping.basket.Util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.shopping.basket.R;
import com.wang.avi.AVLoadingIndicatorView;

import static android.support.v4.app.ActivityCompat.requestPermissions;

public class util {
   public static String Imageurl = "https://basket.qa/beta-api/public/";
    private static final int MainEXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static void loadingAnimation(AVLoadingIndicatorView avi , boolean status){
        if (status) {
            avi.setVisibility(View.VISIBLE);
            avi.show();
        }
        else {
            avi.setVisibility(View.GONE);
            avi.hide();
        }
    }

    public static void loadingFbAniamtion(ShimmerFrameLayout loading, boolean stsaus){

        if (stsaus){
            loading.setVisibility(View.VISIBLE);
            loading.startShimmerAnimation();
        }
        else {
            loading.setVisibility(View.GONE);
            loading.stopShimmerAnimation();
        }

    }



   public static int dpToPx(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * scale);
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean LightPermissionGrand(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle(context.getString(R.string.permission_nessary));
                    alertBuilder.setMessage(context.getString(R.string.sotrageis_improtant));
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, MainEXTERNAL_STORAGE_PERMISSION_CONSTANT);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, MainEXTERNAL_STORAGE_PERMISSION_CONSTANT);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


}
