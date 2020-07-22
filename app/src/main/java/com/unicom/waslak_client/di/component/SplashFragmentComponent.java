package com.unicom.waslak_client.di.component;

import com.unicom.waslak_client.di.scope.ActivityScope;
import com.unicom.waslak_client.ui.splash.SplashActivity;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent
public interface SplashFragmentComponent {

    void inject(SplashActivity activity);

}
