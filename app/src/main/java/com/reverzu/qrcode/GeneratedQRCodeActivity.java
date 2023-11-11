package com.reverzu.qrcode;

import static android.content.ContentValues.TAG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class GeneratedQRCodeActivity extends AppCompatActivity {

    private TextView tvQrGenerateText;
    private ImageView ivGeneratedQrImage;
    private TextInputEditText tietQrInputData;
    private Button btnGenerateQr;
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    String data, fileName;
    private QRGEncoder qrgEncoder;
    private Bitmap bitmap;
    private ImageButton btnShareQr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_qrcode);
        tvQrGenerateText = (TextView) findViewById(R.id.tv_qr_generate_text);
        ivGeneratedQrImage = (ImageView) findViewById(R.id.iv_qr_generate_qr);
        tietQrInputData = (TextInputEditText) findViewById(R.id.tiet_qr_input_text);
        btnGenerateQr = (Button) findViewById(R.id.btn_generate_qr_code);
        btnShareQr = (ImageButton) findViewById(R.id.ib_share_button);

        btnGenerateQr.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                data = tietQrInputData.getText().toString().trim();
                if(data.isEmpty()){
                    Toast.makeText(GeneratedQRCodeActivity.this, "Please input data to Generate QR Code", Toast.LENGTH_LONG).show();
                }else{
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int dimen = (height>width)?width:height;
//                    dimen*=(3/4);
                    qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, dimen);
                    qrgEncoder.setColorBlack(Color.WHITE);
                    qrgEncoder.setColorWhite(Color.BLACK);
                    try {
                        // Getting QR-Code as Bitmap
                        bitmap = qrgEncoder.getBitmap();
                        tvQrGenerateText.setVisibility(View.GONE);
                        ivGeneratedQrImage.setImageBitmap(bitmap);
                        boolean save;
                        String result;
                            QRGSaver qrgSaver = new QRGSaver();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            savePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() + "/QRCode/";
                        }
                        try {
                            Long tsLong = System.currentTimeMillis()/1000;
                            String ts = tsLong.toString();
                            fileName = data.charAt(0)+data.charAt(1)+ts;
                            save = qrgSaver.save(savePath, fileName, bitmap, QRGContents.ImageType.IMAGE_JPEG);
                            result = save ? "Image Saved" : "Image Not Saved";
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        Log.v(TAG, e.toString());
                    }
                }
            }
        });

        btnShareQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_SEND);
                Uri qrUri = Uri.parse(savePath + fileName + ".jpg");
                intent.setDataAndType(qrUri, "image/*");
                intent.putExtra(Intent.EXTRA_STREAM, qrUri);
//                    startActivity(chooserIntent);
                    startActivity(Intent.createChooser(intent, "Share image using"));
                }catch (Exception ex){
                    Log.v(TAG, ex.toString());
                }
            }
        });
    }
}