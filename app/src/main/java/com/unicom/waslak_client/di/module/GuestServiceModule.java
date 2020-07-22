package com.unicom.waslak_client.di.module;

import com.unicom.waslak_client.di.scope.FragmentScope;
import com.unicom.waslak_client.remote.GuestService;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class GuestServiceModule {

    @Provides
    @FragmentScope
    GuestService provideGuestService(Retrofit retrofit){
        return retrofit.create(GuestService.class);
    }
}
