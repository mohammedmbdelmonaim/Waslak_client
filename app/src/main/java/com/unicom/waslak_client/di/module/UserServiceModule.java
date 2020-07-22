package com.unicom.waslak_client.di.module;

import com.unicom.waslak_client.di.scope.FragmentScope;
import com.unicom.waslak_client.remote.UserService;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class UserServiceModule {

    @Provides
    @FragmentScope
    UserService provideUserService(Retrofit retrofit){
        return retrofit.create(UserService.class);
    }
}
