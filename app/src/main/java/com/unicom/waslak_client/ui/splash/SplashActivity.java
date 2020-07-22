package com.unicom.waslak_client.ui.splash;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.unicom.waslak_client.MainApplication;
import com.unicom.waslak_client.R;
import com.unicom.waslak_client.databinding.ActivitySplashBinding;
import com.unicom.waslak_client.di.component.ApplicationComponent;
import com.unicom.waslak_client.di.component.SplashFragmentComponent;
import com.unicom.waslak_client.ui.guest.GuestActivity;
import com.unicom.waslak_client.ui.slider.SliderActivity;
import com.unicom.waslak_client.ui.user.UserActivity;
import com.unicom.waslak_client.utils.PreferenceUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    @Inject
    PreferenceUtils preference;
    private boolean notification;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // In Activity's onCreate() for instance
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // dagger
        ApplicationComponent applicationComponent = MainApplication.get(this).getApplicationComponent();
        SplashFragmentComponent splashFragmentComponent = applicationComponent.splashFragmentComponent();
        splashFragmentComponent.inject(this);


        Observable.timer(1 , TimeUnit.SECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long aLong) {
                if (preference.getTokenUser().equalsIgnoreCase("")) {
                    startActivity(new Intent(SplashActivity.this, SliderActivity.class));
                }else {
                    if (getIntent().getExtras() != null){
                        Bundle bundle = new Bundle();
                        bundle.putInt("id" ,  getIntent().getExtras().getInt("id"));
                        startActivity(new Intent(SplashActivity.this, UserActivity.class).putExtras(bundle));
                    }else {
                        startActivity(new Intent(SplashActivity.this, UserActivity.class));
                    }
                }
                overridePendingTransition(R.anim.animate_slide_in_left,
                        R.anim.animate_slide_out_right);
                finish();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(SplashActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
