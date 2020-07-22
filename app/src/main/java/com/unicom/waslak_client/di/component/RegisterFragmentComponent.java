package com.unicom.waslak_client.di.component;

import android.content.Context;

import com.unicom.waslak_client.di.module.GuestServiceModule;
import com.unicom.waslak_client.di.module.RegisterViewModelModule;
import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.di.scope.FragmentScope;
import com.unicom.waslak_client.ui.guest.register.RegisterFragment;

import dagger.BindsInstance;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {RegisterViewModelModule.class , GuestServiceModule.class})
public interface RegisterFragmentComponent {

    void inject(RegisterFragment registerFragment);

    @Subcomponent.Builder
    interface Builder{
        @BindsInstance
        Builder getContext(@ActivityContext Context context);

        RegisterFragmentComponent build();
    }
}
