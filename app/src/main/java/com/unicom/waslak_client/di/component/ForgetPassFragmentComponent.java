package com.unicom.waslak_client.di.component;

import android.content.Context;

import com.unicom.waslak_client.di.module.ForgetPassViewModelModule;
import com.unicom.waslak_client.di.module.GuestServiceModule;
import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.di.scope.FragmentScope;
import com.unicom.waslak_client.ui.guest.forget.ForgetPasswordFragment;

import dagger.BindsInstance;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {ForgetPassViewModelModule.class , GuestServiceModule.class})
public interface ForgetPassFragmentComponent {

    void inject(ForgetPasswordFragment loginFragment);

    @Subcomponent.Builder
    interface Builder{
        @BindsInstance
        Builder getContext(@ActivityContext Context context);

        ForgetPassFragmentComponent build();
    }
}
