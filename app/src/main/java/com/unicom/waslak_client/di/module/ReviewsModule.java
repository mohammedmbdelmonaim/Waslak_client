package com.unicom.waslak_client.di.module;

import androidx.recyclerview.widget.DiffUtil;

import com.unicom.waslak_client.di.scope.FragmentScope;
import com.unicom.waslak_client.model.user.ActivitiesModel;
import com.unicom.waslak_client.model.user.RateModel;

import dagger.Module;
import dagger.Provides;

@Module
public class ReviewsModule {

    @Provides
    @FragmentScope
    DiffUtil.ItemCallback<RateModel.CustomersRate> provideDiffUtil(){
        return RateModel.CustomersRate.itemCallback;
    }
}
