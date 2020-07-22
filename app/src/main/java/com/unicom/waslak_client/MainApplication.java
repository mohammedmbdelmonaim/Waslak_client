package com.unicom.waslak_client;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.unicom.waslak_client.di.component.ApplicationComponent;
import com.unicom.waslak_client.di.component.DaggerApplicationComponent;
import com.unicom.waslak_client.utils.ChangeLang;


public class MainApplication extends Application {
    ApplicationComponent applicationComponent;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(ChangeLang.setLocale(base));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder().getContext(this).build();
    }

    public static MainApplication get(Context context){
        return (MainApplication) context.getApplicationContext();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}