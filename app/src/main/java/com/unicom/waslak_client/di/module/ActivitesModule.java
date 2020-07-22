package com.unicom.waslak_client.di.module;

import android.content.Context;

import androidx.recyclerview.widget.DiffUtil;

import com.unicom.waslak_client.di.scope.ActivityScope;
import com.unicom.waslak_client.di.scope.FragmentScope;
import com.unicom.waslak_client.model.user.ActivitiesModel;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivitesModule {

    @Provides
    @FragmentScope
    DiffUtil.ItemCallback<ActivitiesModel.InnerDatum> provideDiffUtil(){
        return ActivitiesModel.InnerDatum.itemCallback;
    }

   
}
