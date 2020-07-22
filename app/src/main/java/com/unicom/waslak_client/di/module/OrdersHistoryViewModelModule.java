package com.unicom.waslak_client.di.module;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.unicom.waslak_client.di.key.ViewModelKey;
import com.unicom.waslak_client.viewmodel.ViewModelFactory;
import com.unicom.waslak_client.viewmodel.user.OrdersHistoryViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class OrdersHistoryViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(OrdersHistoryViewModel.class)
    abstract ViewModel bindViewModel(OrdersHistoryViewModel historyViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindFactory(ViewModelFactory factory);
}
