package com.unicom.waslak_client.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.unicom.waslak_client.R;
import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.di.scope.FragmentScope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

@FragmentScope
public class AppPermissions {
    private static final int PERMISSION_REQUEST_CODE = 1001;
    Context context;
    String[] appPermissions;
    Fragment fragment;

    @Inject
    public AppPermissions(@ActivityContext Context context, Fragment fragment , String[] appPermissions){
        this.context = context;
        this.fragment = fragment;
        this.appPermissions = appPermissions;
    }

    public boolean checkAndRequestPermission() {
        //check which permissions is granted
        List<String> listPermissionNedded = new ArrayList<>();
        for (String perm : appPermissions) {
            if (ContextCompat.checkSelfPermission(context, perm) != PackageManager.PERMISSION_GRANTED) {
                listPermissionNedded.add(perm);
            }
        }
        // Ask for non-granted permissions
        if (!listPermissionNedded.isEmpty()) {
            fragment.requestPermissions(listPermissionNedded.toArray(new String[listPermissionNedded.size()]), PERMISSION_REQUEST_CODE);
            return false;
        }
        //App has all permissions
        return true;
    }

    public boolean onRequestPermissionsResult(int requestCode, int[] grantResults, String[] permissions) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            HashMap<String, Integer> permissionResult = new HashMap<>();
            int deniedCount = 0;
            //permission grant results
            for (int i = 0; i < grantResults.length; i++) {
                // Add only permissions which are denied
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResult.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }
            // check if all permission are granted
            if (deniedCount == 0) {
                    return true;
            } else {
                for (Map.Entry<String, Integer> entry : permissionResult.entrySet()) {
                    String perName = entry.getKey();
                    int perResult = entry.getValue();
                    if (fragment.shouldShowRequestPermissionRationale(perName)) {
                        //show dialog of explanation
                        showDialog(context.getString(R.string.perm_support), context.getString(R.string.return_stores_close), context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertDialog.dismiss();
                                checkAndRequestPermission();
                            }
                        },  context.getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertDialog.dismiss();
//                                ((Activity) context).finish();
                            }
                        }, true);
                        alertDialog.show();
                        // permission is denied and never ask again is checked
                        //shouldShowRequestPermissionRationale return false
                    } else {
//                        showDialog("Settings", "Go To Settings", "settings", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                alertDialog.dismiss();
//                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.getPackageName(), null));
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                context.startActivity(intent);
//                                ((Activity) context).finish();
//                            }
//                        }, "No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                alertDialog.dismiss();
//                                ((Activity) context).finish();
//                            }
//                        }, false);
//                        alertDialog.show();
                    }
                }
            }
        }
        return false;
    }
    androidx.appcompat.app.AlertDialog alertDialog;
    public void showDialog(String title, String msg, String positiveLabel, DialogInterface.OnClickListener
            positiveOnClick, String negativeLabel, DialogInterface.OnClickListener negativeOnClick, boolean isCanclable) {
        MaterialAlertDialogBuilder builder =  new MaterialAlertDialogBuilder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setCancelable(isCanclable);
        builder.setPositiveButton(positiveLabel, positiveOnClick);
        builder.setNegativeButton(negativeLabel, negativeOnClick);
        alertDialog = builder.create();
    }
}
