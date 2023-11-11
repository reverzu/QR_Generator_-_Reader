package com.reverzu.qrcode;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private Button qrCodeGenerator, qrCodeReader;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int READ_STORAGE_PERMISSION_CODE = 101;
    private static final int WRITE_STORAGE_PERMISSION_CODE = 101;


    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        qrCodeGenerator = (Button) findViewById(R.id.btn_generate_qr_code);
        qrCodeReader = (Button) findViewById(R.id.btn_read_qr_code);
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);

        qrCodeGenerator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, GeneratedQRCodeActivity.class));
            }
        });


        qrCodeReader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ScanQRCodeActivity.class));
            }
        });

    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(HomeActivity.this, permission) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(HomeActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, permission)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setMessage("This App requires Camera and Storage permissions")
                    .setTitle("Permissions Required")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{permission}, requestCode);
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            builder.show();
        } else {
            // Requesting the permission
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(HomeActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, Manifest.permission.CAMERA)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("This App requires Camera and Storage permissions, Please allow the necessary permissions from settings.")
                        .setTitle("Permissions Required")
                        .setCancelable(false)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
//                                ActivityCompat.requestPermissions(HomeActivity.this, new String[] { permission }, requestCode);
                                dialogInterface.dismiss();
                            }
                        });
                builder.show();
            } else {
                Toast.makeText(HomeActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
                checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);

            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(HomeActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            }
//            else if (!ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
//                builder.setMessage("This App requires Camera and Storage permissions, Please allow the necessary permissions from settings.")
//                        .setTitle("Permissions Required")
//                        .setCancelable(false)
//                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                            }
//                        })
//                        .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                Uri uri = Uri.fromParts("package", getPackageName(), null);
//                                intent.setData(uri);
//                                startActivity(intent);
////                                ActivityCompat.requestPermissions(HomeActivity.this, new String[] { permission }, requestCode);
//                                dialogInterface.dismiss();
//                            }
//                        });
//                builder.show();
//            }
        } else {
            Toast.makeText(HomeActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
        }
    }
}