package com.unicom.waslak_client.di.module;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.unicom.waslak_client.SharedViewModel;
import com.unicom.waslak_client.di.key.ViewModelKey;
import com.unicom.waslak_client.viewmodel.ViewModelFactory;
import com.unicom.waslak_client.viewmodel.user.ChangePasswordViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class SharedViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SharedViewModel.class)
    abstract ViewModel bindViewModel(SharedViewModel sharedViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindFactory(ViewModelFactory factory);
}
