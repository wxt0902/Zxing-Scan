package com.example.xch.scanzxing;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xch.scanzxing.zxing.android.CaptureActivity;

public class MainActivity extends AppCompatActivity  {
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_SCAN = 0x0000;

    private Button btn;
    private TextView xinxi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xinxi = (TextView) findViewById(R.id.tv_scanResult); //接受信息和按钮注册
        btn = (Button) findViewById(R.id.buttonone);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.buttonone:
                        //动态权限申请
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                        } else {
                            Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                            startActivityForResult(intent, REQUEST_CODE_SCAN);//实现页面跳转
                        }
                        break;
                    default:
                        break;
                }
            }
        });

    } //检测摄像头是否存在
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){

            return true;
        } else {

            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);//实现页面跳转
                    startActivityForResult(intent, REQUEST_CODE_SCAN);//实现扫码
                } else {
                    Toast.makeText(this, "拒绝扫码", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override//扫描完成之后的返回数据内容
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);//返回请求码，结果吗，与数据信息。
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                //返回数据内容
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                //返回的BitMap图像
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);

                xinxi.setText("内容：" + content);
            }
        }
    }
}
