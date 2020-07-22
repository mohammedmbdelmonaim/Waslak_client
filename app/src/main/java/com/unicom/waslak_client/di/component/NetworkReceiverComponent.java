package com.unicom.waslak_client.di.component;

import com.unicom.waslak_client.di.scope.ReceiverScope;
import com.unicom.waslak_client.reciever.NetworkReceiver;

import dagger.Subcomponent;

@ReceiverScope
@Subcomponent
public interface NetworkReceiverComponent {

    void inject(NetworkReceiver networkReceiver);
}
