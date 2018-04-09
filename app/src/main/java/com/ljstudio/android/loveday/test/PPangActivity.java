package com.ljstudio.android.loveday.test;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;

import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.utils.SystemOutUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by chenjianbin on 2018-4-9.
 */

public class PPangActivity extends AppCompatActivity {

    private WebView webView;
    private Button button;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppang);

        webView = findViewById(R.id.id_ppang_web_view);
        button = findViewById(R.id.id_ppang_button);
    }

    @Override
    protected void onResume() {
        super.onResume();


        String strColor = "#000000";
        Bitmap bitmap = getBitmapFromView(button);
        if (null != bitmap) {
            int pixel = bitmap.getPixel(200, 20);
            //获取颜色
            int redValue = Color.red(pixel);
            int greenValue = Color.green(pixel);
            int blueValue = Color.blue(pixel);
            strColor = "#" + Integer.toHexString(pixel).toUpperCase();
            bitmap.recycle();
        }

        setStatusBar(Color.parseColor(strColor));
    }

    private Bitmap getBitmapFromView(View v) {
        SystemOutUtil.sysOut("v.getWidth()==>" + v.getWidth());
        SystemOutUtil.sysOut("v.getHeight()==>" + v.getHeight());

        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        // Draw background
        Drawable bgDrawable = v.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(c);
        } else {
            c.drawColor(Color.WHITE);
        }
        // Draw view to canvas
        v.draw(c);
        return b;
    }

    private void setStatusBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(false);
            tintManager.setTintColor(color);
        }
    }
}