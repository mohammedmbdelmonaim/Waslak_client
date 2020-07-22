package com.unicom.waslak_client.di.component;

import android.content.Context;

import com.unicom.waslak_client.di.module.GuestServiceModule;
import com.unicom.waslak_client.di.module.LoginViewModelModule;
import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.di.scope.FragmentScope;
import com.unicom.waslak_client.ui.guest.login.LoginFragment;

import dagger.BindsInstance;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {LoginViewModelModule.class , GuestServiceModule.class})
public interface LoginFragmentComponent {

    void inject(LoginFragment loginFragment);

    @Subcomponent.Builder
    interface Builder{
        @BindsInstance
        Builder getContext(@ActivityContext Context context);

        LoginFragmentComponent build();
    }
}
