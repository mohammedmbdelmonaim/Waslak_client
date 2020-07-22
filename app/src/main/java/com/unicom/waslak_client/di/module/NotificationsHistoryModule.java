package com.unicom.waslak_client.di.module;

import androidx.recyclerview.widget.DiffUtil;

import com.unicom.waslak_client.di.scope.FragmentScope;
import com.unicom.waslak_client.model.user.NotificationHistoryModel;
import com.unicom.waslak_client.model.user.OrdersHistoryModel;

import dagger.Module;
import dagger.Provides;

@Module
public class NotificationsHistoryModule {
    @Provides
    @FragmentScope
    DiffUtil.ItemCallback<NotificationHistoryModel.InnerDatum> provideDiffUtil(){
        return NotificationHistoryModel.InnerDatum.itemCallback;
    }
}
