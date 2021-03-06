package com.kaizen.hoymm.ufoinphoto;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by hoymm on 13.11.17.
 */

public class DynamicPermissions {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private static DialogInterface.OnClickListener listener;

    private static class PermissionsData {
        String explanationMessage, permissionKey;
        int requestCode;

        PermissionsData(String explanationMessage, String permissionKey, int requestCode) {
            this.explanationMessage = explanationMessage;
            this.permissionKey = permissionKey;
            this.requestCode = requestCode;
        }
    }

    static boolean isReadExternalStoragePermissionGranted(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isWriteExternalStoragePermissionGranted(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    static void askForReadExternalStoragePermission(Activity activity) {
        String explanationText = activity.getString(R.string.read_external_storage_explanation);
        String systemPermissionKey = Manifest.permission.READ_EXTERNAL_STORAGE;
        PermissionsData permissionDataReadExternalStorage =
                new PermissionsData(explanationText, systemPermissionKey, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

        askForPermission(activity, permissionDataReadExternalStorage);
    }

    public static void askForWriteExternalStoragePermission(Activity activity) {
        String explanationText = activity.getString(R.string.write_external_storage_explanation);
        String systemPermissionKey = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        PermissionsData permissionDataReadExternalStorage =
                new PermissionsData(explanationText, systemPermissionKey, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        askForPermission(activity, permissionDataReadExternalStorage);
    }

    private static void askForPermission(Activity activity, PermissionsData permissionsData) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionsData.permissionKey))
            showExplanationOkCancel(activity, permissionsData);
        else
            ActivityCompat.requestPermissions(activity, new String[]{permissionsData.permissionKey}, permissionsData.requestCode);
    }

    private static void showExplanationOkCancel(Activity activity, PermissionsData permissionsData) {
        initListener(activity, permissionsData);
        new AlertDialog.Builder(activity)
                .setMessage(permissionsData.explanationMessage)
                .setPositiveButton(R.string.ok, listener)
                .setNegativeButton(R.string.cancel, listener)
                .create()
                .show();
    }

    private static void initListener(final Activity activity, final PermissionsData permissionsData){
        listener = new DialogInterface.OnClickListener() {
            final int BUTTON_NEGATIVE = -2;
            final int BUTTON_POSITIVE = -1;

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case BUTTON_NEGATIVE:
                        // int which = -2
                        dialog.dismiss();
                        break;

                    case BUTTON_POSITIVE:
                        // int which = -1
                        ActivityCompat.requestPermissions
                                (activity, new String[]{permissionsData.permissionKey}, permissionsData.requestCode);
                        dialog.dismiss();
                        break;
                }
            }
        };
    }

}
