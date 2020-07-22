package com.unicom.waslak_client.repository;

import com.unicom.waslak_client.di.scope.FragmentScope;
import com.unicom.waslak_client.model.guest.ForgetPassUSer;
import com.unicom.waslak_client.model.guest.ForgetPasswordModel;
import com.unicom.waslak_client.model.guest.LoginModel;
import com.unicom.waslak_client.model.guest.LoginUser;
import com.unicom.waslak_client.model.guest.RegisterModel;
import com.unicom.waslak_client.model.guest.RegisterUser;
import com.unicom.waslak_client.remote.GuestService;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@FragmentScope
public class GuestRepository {

    private GuestService guestService;

    @Inject
    public GuestRepository(GuestService guestService) {
        this.guestService = guestService;
    }

    public Observable<LoginModel> getLoginRequest(LoginUser user) {
        return guestService.loginRequest(user);
    }

    public Observable<RegisterModel> getRegisterRequest(RegisterUser user) {
        return guestService.registerRequest(user);
    }

    public Observable<ForgetPasswordModel> getForgetRequest(ForgetPassUSer user) {
        return guestService.forgetRequest(user);
    }
}
