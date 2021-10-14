package com.example.neoqrcodescanner;

import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission_group.CAMERA;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import eu.livotov.labs.android.camview.ScannerLiveView;
import eu.livotov.labs.android.camview.scanner.decoder.zxing.ZXDecoder;

public class ScanQRCodeActivity extends AppCompatActivity {
    private ScannerLiveView scannerLiveView;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);
        scannerLiveView = findViewById(R.id.camView);
        textView = findViewById(R.id.dataScanned);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        if(checkPermission()){
            Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
        }else{
            requestPermission();
        }
        scannerLiveView.setScannerViewEventListener(new ScannerLiveView.ScannerViewEventListener() {
            @Override
            public void onScannerStarted(ScannerLiveView scanner) {
                Toast.makeText(ScanQRCodeActivity.this, "Scanner started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerStopped(ScannerLiveView scanner) {
                Toast.makeText(ScanQRCodeActivity.this, "Scanner stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerError(Throwable err) {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCodeScanned(String data) {
                textView.setText(data);

                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(500);
            }
        });
    }
    private boolean checkPermission() {
        int vibPer = ContextCompat.checkSelfPermission(ScanQRCodeActivity.this, Manifest.permission.VIBRATE);
        int camPer = ContextCompat.checkSelfPermission(ScanQRCodeActivity.this, Manifest.permission.CAMERA);
        return camPer == PackageManager.PERMISSION_GRANTED && vibPer == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission(){
        int perCode = 200;
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.VIBRATE}, perCode);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(ScanQRCodeActivity.this, "Camera and Vibrate Permission Granted", Toast.LENGTH_SHORT) .show();
            }
            else {
                Toast.makeText(ScanQRCodeActivity.this, "Camera and Vibrate Permission Denied", Toast.LENGTH_SHORT) .show();
            }
    }
    @Override
    protected void onPause() {
        scannerLiveView.stopScanner();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ZXDecoder decoder = new ZXDecoder();
        decoder.setScanAreaPercent(0.8);
        scannerLiveView.setDecoder(decoder);
        scannerLiveView.startScanner();
    }
}