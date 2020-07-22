package com.unicom.waslak_client.viewmodel.guest;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.di.scope.FragmentScope;
import com.unicom.waslak_client.model.guest.LoginModel;
import com.unicom.waslak_client.model.guest.LoginUser;
import com.unicom.waslak_client.repository.GuestRepository;
import com.unicom.waslak_client.utils.KeyboardUtils;
import com.unicom.waslak_client.utils.PreferenceUtils;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;


@FragmentScope
public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginUser> userMutableLiveData;
    private MutableLiveData<LoginModel> loginMutableLiveData;
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> error;
    public MutableLiveData<String> emailAddress = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public PublishSubject publishSubject = PublishSubject.create();
    public MutableLiveData<Boolean> enableButton = new MutableLiveData<>();

    @Inject
    @ActivityContext
    Context context;

    private CompositeDisposable disposables = new CompositeDisposable();
    GuestRepository guestRepository;
    PreferenceUtils preference;
    KeyboardUtils keyboardUtils;
    LoginUser loginUser;

    @Inject
    public LoginViewModel(GuestRepository guestRepository, PreferenceUtils preference, KeyboardUtils keyboardUtils) {
        this.guestRepository = guestRepository;
        this.preference = preference;
        this.keyboardUtils = keyboardUtils;
    }

    public MutableLiveData<LoginUser> getUser() {

        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();

        }
        return userMutableLiveData;
    }

    public void onLoginClick() {
        keyboardUtils.hideKeyboard();
        enableButton.setValue(false);
        loginUser = new LoginUser(emailAddress.getValue(), password.getValue(), preference.getTokenDevice(), "android");
        userMutableLiveData.setValue(loginUser);
    }

    public MutableLiveData<LoginModel> getLoginNetworkResponse() {
        if (loginMutableLiveData == null) {
            loginMutableLiveData = new MutableLiveData<>();
            enableButton.setValue(true);
            isLoading.setValue(false);
            loginNetwork();
        }
        return loginMutableLiveData;
    }

    public MutableLiveData<String> getError() {
        if (error == null) {
            error = new MutableLiveData<>();
        }
        return error;
    }

    public void loginNetwork() {
        publishSubject.switchMap(new Function() {
            @Override
            public Object apply(Object o) throws Throwable {
                return guestRepository.getLoginRequest(loginUser)
                        .subscribeOn(Schedulers.io());
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleResults, this::handleError);
    }

    private void handleResults(Object loginModel) {
        if (loginModel != null && ((LoginModel) loginModel).getSuccess()) {
            preference.saveTokenUser(((LoginModel) loginModel).getInnerdata().getToken());
            loginMutableLiveData.setValue(((LoginModel) loginModel));
        } else {
            error.setValue(((LoginModel) loginModel).getMessage());
        }
        enableButton.setValue(true);
        isLoading.setValue(false);
    }

    private void handleError(Object t) {
        Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
        enableButton.setValue(true);
        isLoading.setValue(false);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
//    @Header("Authorization") String token