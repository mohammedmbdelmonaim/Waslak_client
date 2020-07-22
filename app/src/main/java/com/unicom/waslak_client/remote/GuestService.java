package com.unicom.waslak_client.remote;


import com.unicom.waslak_client.model.guest.ForgetPassUSer;
import com.unicom.waslak_client.model.guest.ForgetPasswordModel;
import com.unicom.waslak_client.model.guest.LoginModel;
import com.unicom.waslak_client.model.guest.LoginUser;
import com.unicom.waslak_client.model.guest.RegisterModel;
import com.unicom.waslak_client.model.guest.RegisterUser;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GuestService {
    @POST("api/Account/SignInCustomerMobile")
    Observable<LoginModel> loginRequest(@Body LoginUser user);

    @POST("api/Account/RegisterCustomerMobileUser")
    Observable<RegisterModel> registerRequest(@Body RegisterUser user);

    @POST("/api/Account/CustomerForgetPassword")
    Observable<ForgetPasswordModel> forgetRequest(@Body ForgetPassUSer user);
}
