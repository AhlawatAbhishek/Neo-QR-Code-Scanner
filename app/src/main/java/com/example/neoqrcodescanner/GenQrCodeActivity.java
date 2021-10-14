package com.example.neoqrcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class GenQrCodeActivity extends AppCompatActivity {
    private TextView qrCodeTV;
    private ImageView qrCodeImg;
    private EditText dataEdit;
    private Button genQRBtn;
    private QRGEncoder qrgEncoder;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_qr_code);
        qrCodeTV = findViewById(R.id.QrCodeText);
        qrCodeImg = findViewById(R.id.genQrCodeImg);
        dataEdit = findViewById(R.id.genEdtTxt);
        genQRBtn = findViewById(R.id.gntCodeBtn);
        genQRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt = dataEdit.getText().toString();
                if(txt.isEmpty()){
                    Toast.makeText(GenQrCodeActivity.this, "Please enter some text first to generate the QR Code", Toast.LENGTH_SHORT).show();
                }else{
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display =  manager.getDefaultDisplay();
                    Point pt = new Point();
                    display.getSize(pt);
                    int width = pt.x;
                    int height = pt.y;
                    int dimen = width<height ? width:height;
                    dimen = dimen * 3/4;
                    qrgEncoder = new QRGEncoder(dataEdit.getText().toString(), null, QRGContents.Type.TEXT, dimen);
                    try {
                       bitmap = qrgEncoder.encodeAsBitmap();
                       qrCodeTV.setVisibility(View.GONE);
                       qrCodeImg.setImageBitmap(bitmap);
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                }
            }
        });
    }
}