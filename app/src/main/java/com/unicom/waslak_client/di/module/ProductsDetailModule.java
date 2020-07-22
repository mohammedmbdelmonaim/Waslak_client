package com.unicom.waslak_client.di.module;

import androidx.recyclerview.widget.DiffUtil;

import com.unicom.waslak_client.di.scope.FragmentScope;
import com.unicom.waslak_client.model.user.OrderDetailModel;
import com.unicom.waslak_client.model.user.ProductsStoreModel;

import dagger.Module;
import dagger.Provides;

@Module
public class ProductsDetailModule {

    @Provides
    @FragmentScope
    DiffUtil.ItemCallback<OrderDetailModel.OrderItem> provideDiffUtil(){
        return OrderDetailModel.OrderItem.itemCallback;
    }
}
