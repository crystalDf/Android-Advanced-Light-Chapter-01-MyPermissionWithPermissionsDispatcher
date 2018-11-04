package com.star.mypermissionwithpermissionsdispatcher;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private Button mCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCall = findViewById(R.id.call);
        mCall.setOnClickListener(v ->
                MainActivityPermissionsDispatcher
                        .makeACallWithPermissionsWithPermissionCheck(this));
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    public void makeACallWithPermissions() {

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + "10086"));

        try {
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @OnShowRationale(Manifest.permission.CALL_PHONE)
    public void showRationale(final PermissionRequest request) {

        new AlertDialog.Builder(this)
                .setMessage("提示用户为何要开启此权限")
                .setPositiveButton("知道了", (dialog, which) -> request.proceed())
                .create()
                .show();
    }

    @OnPermissionDenied(Manifest.permission.CALL_PHONE)
    public void showPermissionDenied() {

        Toast.makeText(this, "用户选择拒绝时的提示", Toast.LENGTH_LONG).show();
    }

    @OnNeverAskAgain(Manifest.permission.CALL_PHONE)
    public void showNeverAskAgain() {

        new AlertDialog.Builder(this)
                .setMessage("该功能需要访问电话的权限，不开启将无法正常工作！")
                .setPositiveButton("确定", (dialog, which) -> {

                })
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        MainActivityPermissionsDispatcher
                .onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
