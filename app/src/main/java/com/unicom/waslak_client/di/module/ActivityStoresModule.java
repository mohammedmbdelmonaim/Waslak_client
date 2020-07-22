package com.unicom.waslak_client.di.module;

import androidx.recyclerview.widget.DiffUtil;

import com.unicom.waslak_client.di.scope.FragmentScope;
import com.unicom.waslak_client.model.user.StoresActivityModel;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityStoresModule {

    @Provides
    @FragmentScope
    DiffUtil.ItemCallback<StoresActivityModel.InnerDatum> provideDiffUtil(){
        return StoresActivityModel.InnerDatum.itemCallback;
    }
}
