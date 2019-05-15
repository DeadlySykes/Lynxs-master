package com.jonathanlnb.lynxs.Herramientas;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.ArrayMap;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by user on 29/07/2017.
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class Tools {
    private static ArrayMap<String, ProgressDialog> progressDialogs = new ArrayMap<>();
    private static SecureRandom random = new SecureRandom();
    private static Snackbar snackbar;

    public static void showMessage(Activity activity, String string) {
        snackbar = Snackbar
                .make(activity.findViewById(android.R.id.content), string, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static void fullScreen(Window window) {
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public static void showTMessage(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }

    public static String showProgress(Context context,
                                      int stringResourceId,
                                      boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(cancelable);
        progressDialog.setMessage(context.getString(stringResourceId));
        progressDialog.show();
        String progressName = getMyId();
        progressDialogs.put(progressName, progressDialog);
        return progressName;
    }

    public static ProgressDialog getProgressDialog(String progressId) {
        return progressDialogs.get(progressId);
    }

    public static String getMyId() {
        return new BigInteger(32, random).toString(32);
    }

    public static void dismissProgress(String progressDialogName) {
        if(progressDialogs.get(progressDialogName)!=null) {
            progressDialogs.get(progressDialogName).dismiss();
            progressDialogs.remove(progressDialogName);
        }
    }
    public static void checkContactoPermissions(Activity activity, int PERMISSION_REQUEST) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST);
    }

}
