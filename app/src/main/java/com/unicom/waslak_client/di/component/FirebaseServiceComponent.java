package com.unicom.waslak_client.di.component;

import com.unicom.waslak_client.di.scope.ServiceScope;
import com.unicom.waslak_client.fcm.MyFCMService;

import dagger.Subcomponent;

@ServiceScope
@Subcomponent
public interface FirebaseServiceComponent {

    void inject(MyFCMService myFCMService);
}
