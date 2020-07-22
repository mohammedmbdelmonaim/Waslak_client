package com.unicom.waslak_client.di.module;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.unicom.waslak_client.di.key.ViewModelKey;
import com.unicom.waslak_client.viewmodel.ViewModelFactory;
import com.unicom.waslak_client.viewmodel.guest.ForgetPasswordViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ForgetPassViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ForgetPasswordViewModel.class)
    abstract ViewModel bindViewModel(ForgetPasswordViewModel forgetPasswordViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindFactory(ViewModelFactory factory);
}
