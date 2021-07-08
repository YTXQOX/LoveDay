package com.ljstudio.android.loveday.activity;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.ljstudio.android.loveday.R;
import com.ljstudio.android.loveday.utils.ScreenUtil;
import com.ljstudio.android.loveday.utils.SystemOutUtil;
import com.ljstudio.android.loveday.views.ColorPickView;
import com.ljstudio.android.loveday.views.fonts.FontsManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewShowActivity extends AppCompatActivity {

    public static final String CONTENT = "content";

    @BindView(R.id.id_preview_show_toolbar)
    Toolbar toolbar;
    @BindView(R.id.id_preview_show_content)
    TextView tvPreviewContent;
    @BindView(R.id.id_preview_show_text_input_layout)
    TextInputLayout textInputLayout;
    @BindView(R.id.id_preview_show_text)
    EditText etText;
    @BindView(R.id.id_preview_show_color)
    View vColor;
    @BindView(R.id.id_preview_show_bg)
    View vBg;
    @BindView(R.id.id_preview_show_font)
    Spinner spinnerFont;
    @BindView(R.id.id_preview_show_speed)
    SeekBar seekBarSpeed;
    @BindView(R.id.id_preview_show_size)
    SeekBar seekBarSize;
    @BindView(R.id.id_preview_show_button)
    TextView tvShow;

    private String strContent;

    private static int mColor;
    private static int mBg;
    private String[] mItems;
    private String font;
    private float speed = 5.0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_show);

        ButterKnife.bind(this);

        toolbar.setTitle("全屏预览");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(R.mipmap.ic_action_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreviewShowActivity.this.finish();
            }
        });

        setStatusBar(ContextCompat.getColor(this, R.color.colorPrimary));

        strContent = getIntent().getStringExtra(CONTENT);

        mItems = getResources().getStringArray(R.array.font);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mItems);
        spinnerFont.setAdapter(adapter);

        float pxSize = ScreenUtil.sp2px(PreviewShowActivity.this, 15.0f);
        tvPreviewContent.setTextSize(pxSize);
        tvPreviewContent.setText(strContent);

        mColor = getResources().getColor(R.color.colorBlack);
        mBg = getResources().getColor(R.color.colorGrayLight);
        vColor.setBackgroundColor(mColor);
        vBg.setBackgroundColor(mBg);

        etText.setText(strContent);
        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvPreviewContent.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        vColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDialog colorDialog = MyDialog.newInstance(tvPreviewContent, view, 1, 0, mColor);
                showDialog(colorDialog);
            }
        });

        vBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDialog bgDialog = MyDialog.newInstance(tvPreviewContent, view, 1, 1, mBg);
                showDialog(bgDialog);
            }
        });

        spinnerFont.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                font = mItems[position];
                SystemOutUtil.sysOut("font==>" + font);

                FontsManager.initFormAssets(PreviewShowActivity.this, "fonts/" + font + ".ttf");
                FontsManager.changeFonts(tvPreviewContent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        seekBarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speed = progress / 10.0f + 2;
                SystemOutUtil.sysOut("speedValue==>" + speed);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float spValue = progress / 2 * 1.0f + 5;
                SystemOutUtil.sysOut("spValue==>" + spValue);

                float pxValue = ScreenUtil.sp2px(PreviewShowActivity.this, spValue);
                SystemOutUtil.sysOut("pxValue==>" + pxValue);

                tvPreviewContent.setTextSize(pxValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemOutUtil.sysOut("tvPreviewContent.getTextSize()==>" + tvPreviewContent.getTextSize());
                Intent intent = new Intent(PreviewShowActivity.this, ShowActivity.class);
                intent.putExtra(ShowActivity.CONTENT, tvPreviewContent.getText().toString());
                intent.putExtra(ShowActivity.FONT, font);
                intent.putExtra(ShowActivity.SIZE, tvPreviewContent.getTextSize());
                intent.putExtra(ShowActivity.COLOR, mColor);
                intent.putExtra(ShowActivity.BG, mBg);
                intent.putExtra(ShowActivity.SPEED, speed);
                startActivity(intent);
            }
        });
    }

    public static class MyDialog extends DialogFragment {

        private static final String STYLE = "style";
        private static final String TYPE = "type";
        private static final String COLOR = "color";

        private int typeNum;
        private int color;
        private static View mView;
        private static TextView mPreviewContent;

        public static MyDialog newInstance(TextView previewContent, View view, int style, int type, int color) {
            MyDialog dialogFragment = new MyDialog();

            mView = view;
            mPreviewContent = previewContent;

            Bundle bundle = new Bundle();
            bundle.putInt(STYLE, style);
            bundle.putInt(TYPE, type);
            bundle.putInt(COLOR, color);
            dialogFragment.setArguments(bundle);
            return dialogFragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            int styleNum = getArguments().getInt(STYLE, 0);
            typeNum = getArguments().getInt(TYPE, 0);
            color = getArguments().getInt(COLOR, 0);

            int style = 0;
            switch (styleNum) {
                case 0:
                    style = DialogFragment.STYLE_NORMAL;//默认样式
                    break;
                case 1:
                    style = DialogFragment.STYLE_NO_TITLE;//无标题样式
                    break;
                case 2:
                    style = DialogFragment.STYLE_NO_FRAME;//无边框样式
                    break;
                case 3:
                    style = DialogFragment.STYLE_NO_INPUT;//不可输入，不可获得焦点样式
                    break;
            }

            setStyle(style, 0);//设置样式
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragent_dialog, container);

            final View colorView = view.findViewById(R.id.id_dialog_color_show);
            ColorPickView colorPickView = view.findViewById(R.id.id_dialog_color_view);
            TextView tvCancel = view.findViewById(R.id.id_dialog_layout_cancel);
            TextView tvConfirm = view.findViewById(R.id.id_dialog_layout_confirm);

            colorView.setBackgroundColor(color);
            colorPickView.setBarListener(new ColorPickView.OnColorBarListener() {
                @Override
                public void moveBar(int color) {
                    SystemOutUtil.sysOut("color==>" + color);
                    colorView.setBackgroundColor(color);

                    if (0 == typeNum) {
                        mColor = color;
                    } else if (1 == typeNum) {
                        mBg = color;
                    }
                }
            });

            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mView = null;
                    mPreviewContent = null;

                    dismiss();
                }
            });

            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (0 == typeNum) { //文字
                        mView.setBackgroundColor(mColor);
                        mPreviewContent.setTextColor(mColor);
                    } else if (1 == typeNum) { //背景
                        mView.setBackgroundColor(mBg);
                        mPreviewContent.setBackgroundColor(mBg);
                    }

                    mView = null;
                    mPreviewContent = null;

                    dismiss();
                }
            });

            return view;
        }
    }

    private void showDialog(DialogFragment dialogFragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag("DialogFragment");
        if (null != fragment) {
            fragmentTransaction.remove(fragment);
        }

        dialogFragment.show(fragmentTransaction, "DialogFragment");
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




