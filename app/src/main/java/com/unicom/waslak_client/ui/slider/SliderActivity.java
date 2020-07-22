package com.unicom.waslak_client.ui.slider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.unicom.waslak_client.R;
import com.unicom.waslak_client.adapter.SliderAdapter;
import com.unicom.waslak_client.databinding.ActivitySliderBinding;
import com.unicom.waslak_client.model.guest.SliderData;
import com.unicom.waslak_client.ui.BaseActivity;
import com.unicom.waslak_client.ui.guest.GuestActivity;
import com.unicom.waslak_client.ui.splash.SplashActivity;

import java.util.ArrayList;
import java.util.List;

public class SliderActivity extends BaseActivity {
    ActivitySliderBinding binding;
    List<SliderData> sliders = new ArrayList<>();
    SliderAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//         In Activity's onCreate() for instance
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        binding = ActivitySliderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SliderActivity.this, GuestActivity.class));
                overridePendingTransition(R.anim.animate_slide_in_left,
                        R.anim.animate_slide_out_right);
                finish();
            }
        });

        sliders.add(new SliderData(R.drawable.ic_illustration_1 , getString(R.string.slider1_title) , getString(R.string.slider1_text)));
        sliders.add(new SliderData(R.drawable.ic_illustration_2 , getString(R.string.slider2_title) , getString(R.string.slider2_text)));
        sliders.add(new SliderData(R.drawable.ic_illustration_3 , getString(R.string.slider3_title) , getString(R.string.slider3_text)));

        adapter = new SliderAdapter(this);
        binding.imageSlider.setSliderAdapter(adapter);
        //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.renewItems(sliders);
    }
}