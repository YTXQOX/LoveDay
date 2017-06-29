package com.ljstudio.android.loveday.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.views.particletextview.movingstrategy.RandomMovingStrategy;
import com.ljstudio.android.loveday.views.particletextview.object.ParticleTextViewConfig;
import com.ljstudio.android.loveday.views.particletextview.view.ParticleTextView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guoren on 2017/5/10 11:07
 * Usage
 */

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.id_splash_particle_text_view)
    ParticleTextView particleTextView;
    @BindView((R.id.id_splash_layout))
    LinearLayout bgLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        RandomMovingStrategy randomMovingStrategy = new RandomMovingStrategy();
        ParticleTextViewConfig config = new ParticleTextViewConfig.Builder()
                .setRowStep(8)
                .setColumnStep(8)
                .setTargetText(getString(R.string.app_name))
                .setReleasing(0.2)
                .setParticleRadius(4)
                .setMiniDistance(0.1)
                .setTextSize(180)
//                .setDelay(-1L)
                .setMovingStrategy(randomMovingStrategy)
                .instance();

        particleTextView.setConfig(config);

        particleTextView.startAnimation();

        if (particleTextView.isAnimationStop()) {
//            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//            startActivity(intent);
//            SplashActivity.this.finish();
        }

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int type = getSeason(month);
        setSeasonBg(type);
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
                bgLayout.setBackgroundResource(R.mipmap.ic_spring);
                break;
            case 2:
                bgLayout.setBackgroundResource(R.mipmap.ic_summer);
                break;
            case 3:
                bgLayout.setBackgroundResource(R.mipmap.ic_autumn);
                break;
            case 4:
                bgLayout.setBackgroundResource(R.mipmap.ic_winter);
                break;
            default:
                bgLayout.setBackgroundResource(R.mipmap.ic_spring);
                break;
        }
    }
}
