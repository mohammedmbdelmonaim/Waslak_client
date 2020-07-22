package com.unicom.waslak_client.ui;

import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.unicom.waslak_client.MainApplication;
import com.unicom.waslak_client.di.component.ApplicationComponent;
import com.unicom.waslak_client.di.component.BaseActivityComponent;
import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.reciever.NetworkReceiver;
import com.unicom.waslak_client.ui.guest.GuestActivity;
import com.unicom.waslak_client.utils.ChangeLang;

import java.util.Locale;

import javax.inject.Inject;

public class BaseActivity extends AppCompatActivity {
    @Inject
    NetworkReceiver networkreceiver;

    BaseActivityComponent baseActivityComponent;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ChangeLang.setLocale(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationComponent applicationComponent = MainApplication.get(this).getApplicationComponent();
        baseActivityComponent = applicationComponent.baseActivityComponent().getContext(this).build();
        baseActivityComponent.inject(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(networkreceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkreceiver);
    }
}
