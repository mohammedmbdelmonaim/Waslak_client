package com.unicom.waslak_client.di.component;

import android.content.Context;

import com.unicom.waslak_client.MainApplication;
import com.unicom.waslak_client.di.module.AppPreferenceModule;
import com.unicom.waslak_client.di.module.NetworkModule;
import com.unicom.waslak_client.di.qualifier.ApplicationContext;
import com.unicom.waslak_client.di.scope.ApplicationScope;

import dagger.BindsInstance;
import dagger.Component;

@ApplicationScope
@Component(modules = {NetworkModule.class , AppPreferenceModule.class})
public interface ApplicationComponent {

    LoginFragmentComponent.Builder loginFragmentComponentBuilder();

    RegisterFragmentComponent.Builder registerFragmentComponentBuilder();

    ForgetPassFragmentComponent.Builder forgetPassFragmentComponentBuilder();

    RequstsFragmentComponent.Builder requstsFragmentComponentBuilder();

    NewOrderFragmentComponent.Builder newOrderFragmentComponentBuilder();

    OrdersFragmentComponent.Builder orderFragmentComponentBuilder();

    OrderInfoFragmentComponent.Builder orderInfoFragmentComponentBuilder();

    MapFragmentComponent.Builder mapFragmentComponentBuilder();

    OrderHistoryFragmentComponent.Builder orderHistoryFragmentComponentBuilder();

    MyAccountFragmentComponent.Builder myAccountFragmentComponentBuilder();

    ChangePasswordFragmentComponent.Builder changePasswordFragmentComponentBuilder();

    SplashFragmentComponent splashFragmentComponent();

    BaseActivityComponent.Builder baseActivityComponent();

    ReviewsComponent.Builder reviewsComponentComponentBuilder();

    OrderDetailFragmentComponent.Builder orderDetailComponentBuilder();

    NotificationsHistoryFragmentComponent.Builder notificationComponentComponentBuilder();

    FirebaseServiceComponent firebaseServiceComponent();

    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder getContext(@ApplicationContext Context context);

        ApplicationComponent build();
    }
}
