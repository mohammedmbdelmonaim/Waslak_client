package com.unicom.waslak_client.di.component;

import android.content.Context;
import android.content.SharedPreferences;

import com.unicom.waslak_client.di.module.SharedViewModelModule;
import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.di.scope.ActivityScope;
import com.unicom.waslak_client.ui.BaseActivity;
import com.unicom.waslak_client.utils.ChangeLang;

import dagger.BindsInstance;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent
public interface BaseActivityComponent {

    void inject(BaseActivity activity);

    @Subcomponent.Builder
    interface Builder{
        @BindsInstance
        Builder getContext(@ActivityContext Context context);

        BaseActivityComponent build();
    }
}
