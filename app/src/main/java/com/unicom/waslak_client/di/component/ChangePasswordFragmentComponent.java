package com.unicom.waslak_client.di.component;

import android.content.Context;

import com.unicom.waslak_client.di.module.ChangePassViewModelModule;
import com.unicom.waslak_client.di.module.UserServiceModule;
import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.di.scope.FragmentScope;
import com.unicom.waslak_client.ui.user.AddReviewFragment;
import com.unicom.waslak_client.ui.user.ChangePasswordFragment;

import dagger.BindsInstance;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {ChangePassViewModelModule.class , UserServiceModule.class})
public interface ChangePasswordFragmentComponent {

    void inject(ChangePasswordFragment changePasswordFragment);
    void inject(AddReviewFragment addReviewFragment);

    @Subcomponent.Builder
    interface Builder{
        @BindsInstance
        Builder getContext(@ActivityContext Context context);

        ChangePasswordFragmentComponent build();
    }
}
