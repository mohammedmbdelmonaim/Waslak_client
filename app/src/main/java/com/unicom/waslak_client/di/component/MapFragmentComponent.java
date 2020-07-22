package com.unicom.waslak_client.di.component;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.unicom.waslak_client.di.module.OrderInfoViewModelModule;
import com.unicom.waslak_client.di.module.SharedViewModelModule;
import com.unicom.waslak_client.di.module.UserServiceModule;
import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.di.scope.FragmentScope;
import com.unicom.waslak_client.ui.user.MapFragment;
import com.unicom.waslak_client.ui.user.OrderInfoFragment;

import dagger.BindsInstance;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {SharedViewModelModule.class})
public interface MapFragmentComponent {

    void inject(MapFragment mapFragment);

    @Subcomponent.Builder
    interface Builder{
        @BindsInstance
        Builder getContext(@ActivityContext Context context);

        @BindsInstance
        Builder getFragment( Fragment fragment);

        @BindsInstance
        Builder listPermission(String[] permissions);

        MapFragmentComponent build();
    }
}
