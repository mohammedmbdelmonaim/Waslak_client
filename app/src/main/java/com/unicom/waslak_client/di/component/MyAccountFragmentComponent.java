package com.unicom.waslak_client.di.component;

import android.content.Context;

import com.unicom.waslak_client.di.module.MyAccountViewModelModule;
import com.unicom.waslak_client.di.module.UserServiceModule;
import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.di.scope.FragmentScope;
import com.unicom.waslak_client.ui.user.MyAccountFragment;

import dagger.BindsInstance;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {MyAccountViewModelModule.class , UserServiceModule.class})
public interface MyAccountFragmentComponent {

    void inject(MyAccountFragment myAccountFragment);

    @Subcomponent.Builder
    interface Builder{
        @BindsInstance
        Builder getContext(@ActivityContext Context context);

        MyAccountFragmentComponent build();
    }
}
