package com.ljstudio.android.loveday.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.utils.SystemOutUtil;
import com.ljstudio.android.loveday.views.MarqueeView;
import com.ljstudio.android.loveday.views.fonts.FontsManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowActivity extends AppCompatActivity {
    public static final String CONTENT = "content";
    public static final String FONT = "font";
    public static final String SIZE = "size";
    public static final String COLOR = "color";
    public static final String BG = "bg";
    public static final String SPEED = "speed";

    @BindView(R.id.id_show_content_marquee)
    MarqueeView tvContent;
    @BindView(R.id.id_show_layout)
    LinearLayout llShow;

    private String content;
    private String font;
    private float size;
    private int color;
    private int bg;
    private float fSpeed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        ButterKnife.bind(this);

        content = getIntent().getStringExtra(CONTENT);
        font = getIntent().getStringExtra(FONT);
        size = getIntent().getFloatExtra(SIZE, -1.0f);
        color = getIntent().getIntExtra(COLOR, -1);
        bg = getIntent().getIntExtra(BG, -1);
        fSpeed = getIntent().getFloatExtra(SPEED, 3.0f);

        SystemOutUtil.sysOut("content==>" + content);
        SystemOutUtil.sysOut("font==>" + font);
        SystemOutUtil.sysOut("size==>" + size);
        SystemOutUtil.sysOut("color==>" + color);
        SystemOutUtil.sysOut("bg==>" + bg);
        SystemOutUtil.sysOut("fSpeed==>" + fSpeed);

        FontsManager.initFormAssets(ShowActivity.this, "fonts/" + font + ".ttf");
        FontsManager.changeFonts(tvContent);

        llShow.setBackgroundColor(bg);
        tvContent.setContent(content);
        tvContent.setTextSize(size / 1.8f);
        tvContent.setTextColor(color, true);
        tvContent.setTextSpeed(fSpeed);
    }

}




