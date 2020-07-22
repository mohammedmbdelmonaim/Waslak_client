package com.unicom.waslak_client.di.module;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.unicom.waslak_client.di.qualifier.ApplicationContext;
import com.unicom.waslak_client.di.scope.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class AppPreferenceModule {

    @Provides
    @ApplicationScope
    public SharedPreferences providePreference(@ApplicationContext Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
