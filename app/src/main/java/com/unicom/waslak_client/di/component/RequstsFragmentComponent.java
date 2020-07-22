package com.unicom.waslak_client.di.component;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.unicom.waslak_client.adapter.ActivitiesAdapter;
import com.unicom.waslak_client.adapter.ActivityStoresAdapter;
import com.unicom.waslak_client.di.module.ActivitesModule;
import com.unicom.waslak_client.di.module.ActivityStoresModule;
import com.unicom.waslak_client.di.module.RequestsViewModelModule;
import com.unicom.waslak_client.di.module.UserServiceModule;
import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.di.scope.FragmentScope;
import com.unicom.waslak_client.ui.user.RequestsFragment;

import dagger.BindsInstance;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {RequestsViewModelModule.class , UserServiceModule.class , ActivitesModule.class , ActivityStoresModule.class})
public interface RequstsFragmentComponent {

    void inject(RequestsFragment requestsFragment);

    @Subcomponent.Builder
    interface Builder{
        @BindsInstance
        Builder getContext(@ActivityContext Context context);

        @BindsInstance
        Builder getFragment(Fragment fragment);

        @BindsInstance
        Builder getClickListener(ActivitiesAdapter.ClickListener clickListener);

        @BindsInstance
        Builder getClickListenerOrder(ActivityStoresAdapter.ClickListener clickListener);

        @BindsInstance
        Builder listPermission(String[] permissions);

        RequstsFragmentComponent build();
    }
}
