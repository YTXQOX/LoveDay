package com.ljstudio.android.loveday.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.utils.DateFormatUtil;
import com.ljstudio.android.loveday.utils.NetworkUtil;
import com.ljstudio.android.loveday.views.fish.FishDrawableView;
import com.ljstudio.android.loveday.views.particletextview.view.ParticleTextView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import yanzhikai.textpath.SyncTextPathView;

import static com.ljstudio.android.loveday.utils.FileUtil.getSDCardFolderPath;

/**
 * Created by guoren on 2017/5/10 11:07
 * Usage
 */

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.id_splash_particle_text_view)
    ParticleTextView particleTextView;
    @BindView(R.id.id_fish_drawable_view)
    FishDrawableView fishDrawableView;
    @BindView(R.id.id_splash_layout)
    RelativeLayout bgLayout;
    @BindView(R.id.id_splash_bg)
    ImageView bgImage;
    @BindView(R.id.id_splash_date)
    TextView tvDate;
    @BindView(R.id.id_splash_week)
    TextView tvWeek;
    @BindView(R.id.id_splash_hint)
    SyncTextPathView tvHint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        tvDate.setText(DateFormatUtil.getCurrentDate(DateFormatUtil.sdfDate4));
        tvWeek.setText(DateFormatUtil.getCurrentWeek(DateFormatUtil.sdfWeek));

        tvHint.setText("努力，奋斗");
        tvHint.startAnimation(0, 1);

        fishDrawableView.setOnAnimationFinishedListener(new FishDrawableView.OnAnimationFinishedListener() {
            @Override
            public void onFinished() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        });

//        RandomMovingStrategy randomMovingStrategy = new RandomMovingStrategy();
//        ParticleTextViewConfig config = new ParticleTextViewConfig.Builder()
//                .setRowStep(8)
//                .setColumnStep(8)
//                .setTargetText(getString(R.string.app_name))
//                .setReleasing(0.2)
//                .setParticleRadius(4)
//                .setMiniDistance(0.1)
//                .setTextSize(180)
//                .setDelay(-1L)
//                .setMovingStrategy(randomMovingStrategy)
//                .instance();
//
//        particleTextView.setConfig(config);
//        particleTextView.startAnimation();
//
//        if (particleTextView.isAnimationStop()) {
//            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//            startActivity(intent);
//            SplashActivity.this.finish();
//        }

        /**
         * wifi is connected, get bg from bing else local image
         */
        if (NetworkUtil.isWifiAvailable(SplashActivity.this)) {
//            String url = "https://cn.bing.com/az/hprichbg/rb/Sparrowhawk_ZH-CN9288842659_1920x1080.jpg";
            String url = "http://api.dujin.org/bing/1366.php";
//            String url = "http://api.dujin.org/bing/1920.php";

            Glide.with(SplashActivity.this).load(url).into(bgImage);

//            download(url);
        } else {
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH) + 1;
            int type = getSeason(month);
            setSeasonBg(type);
        }
    }

    /**
     * 下载并显示
     * @param url
     */
    private void download(String url) {
        File filePath = getSDCardFolderPath("Download");
        if (!filePath.exists()) {
            filePath.mkdirs();
        }

        String path = filePath.getAbsolutePath();
        String name = DateFormatUtil.getCurrentDate(DateFormatUtil.sdfDate1) + ".png";
        File file = new File(path, name);
        if (file.exists()) {
//                Bitmap bitmap = BitmapFactory.decodeFile(path);
            bgLayout.setBackground(Drawable.createFromPath(path));
        } else {
            OkHttpUtils.get().url(url).build()
                    .execute(new FileCallBack(path, name) {

                        @Override
                        public void onBefore(Request request, int id) {
                            super.onBefore(request, id);
                        }

                        @Override
                        public void onAfter(int id) {
                            super.onAfter(id);
                        }

                        @Override
                        public void inProgress(float progress, long total, int id) {
                            super.inProgress(progress, total, id);
                        }

                        @Override
                        public boolean validateReponse(Response response, int id) {
                            return super.validateReponse(response, id);
                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(File response, int id) {
                            bgLayout.setBackground(Drawable.createFromPath(response.getAbsolutePath()));
                        }
                    });
        }
    }

    private int getSeason(int month) {
        int type;
        if (1 == month || 2 == month || 12 == month) {
            type = 4;   // winter
        } else if (3 == month || 4 == month || 5 == month) {
            type = 1;   // spring
        } else if (6 == month || 7 == month || 8 == month) {
            type = 2;   // summer
        } else if (9 == month || 10 == month || 11 == month) {
            type = 3;   // autumn
        } else {
            type = 1;   // default spring
        }

//        date.getMonth();
//        Random random = new Random();
//        type = random.nextInt(5);
        return type;
    }

    private void setSeasonBg(int type) {
        switch (type) {
            case 1:
//                bgLayout.setBackgroundResource(R.mipmap.ic_spring);
                bgImage.setImageResource(R.mipmap.ic_spring);
                break;
            case 2:
//                bgLayout.setBackgroundResource(R.mipmap.ic_summer);
                bgImage.setImageResource(R.mipmap.ic_summer);
                break;
            case 3:
//                bgLayout.setBackgroundResource(R.mipmap.ic_autumn);
                bgImage.setImageResource(R.mipmap.ic_autumn);
                break;
            case 4:
//                bgLayout.setBackgroundResource(R.mipmap.ic_winter);
                bgImage.setImageResource(R.mipmap.ic_winter);
                break;
            default:
//                bgLayout.setBackgroundResource(R.mipmap.ic_spring);
                bgImage.setImageResource(R.mipmap.ic_spring);
                break;
        }
    }
}
