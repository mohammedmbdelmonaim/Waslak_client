package com.unicom.waslak_client.di.component;

import android.content.Context;

import com.unicom.waslak_client.adapter.MenuAdapter;
import com.unicom.waslak_client.di.module.NewOrderViewModelModule;
import com.unicom.waslak_client.di.module.OpenTimesModule;
import com.unicom.waslak_client.di.module.ProductsModule;
import com.unicom.waslak_client.di.module.UserServiceModule;
import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.di.scope.FragmentScope;
import com.unicom.waslak_client.ui.user.NewOrderFragment;

import dagger.BindsInstance;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {NewOrderViewModelModule.class , UserServiceModule.class , ProductsModule.class , OpenTimesModule.class})
public interface NewOrderFragmentComponent {

    void inject(NewOrderFragment newOrderFragment);

    @Subcomponent.Builder
    interface Builder{
        @BindsInstance
        Builder getContext(@ActivityContext Context context);

        @BindsInstance
        Builder getClickListener(MenuAdapter.ClickListener clickListener);


        NewOrderFragmentComponent build();
    }
}
