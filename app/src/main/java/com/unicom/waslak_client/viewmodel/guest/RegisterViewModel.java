package com.unicom.waslak_client.viewmodel.guest;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.di.scope.FragmentScope;
import com.unicom.waslak_client.model.guest.RegisterModel;
import com.unicom.waslak_client.model.guest.RegisterUser;
import com.unicom.waslak_client.repository.GuestRepository;
import com.unicom.waslak_client.utils.KeyboardUtils;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

@FragmentScope
public class RegisterViewModel extends ViewModel {
    public MutableLiveData<String> emailAddress = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> firstName = new MutableLiveData<>();
    public MutableLiveData<String> lastName = new MutableLiveData<>();
    public MutableLiveData<String> mobile = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> error;
    private MutableLiveData<RegisterUser> userMutableLiveData;
    private MutableLiveData<RegisterModel> registerMutableLiveData;
    private Disposable disposable;
    public PublishSubject publishSubject = PublishSubject.create();
    public MutableLiveData<Boolean> enableButton = new MutableLiveData<>();
    private static final String TAG = "RegisterViewModel";
    RegisterUser user;

    GuestRepository guestRepository;
    KeyboardUtils keyboardUtils;
    @Inject
    @ActivityContext
    Context context;

    @Inject
    public RegisterViewModel(GuestRepository guestRepository , KeyboardUtils keyboardUtils) {
        this.guestRepository = guestRepository;
        this.keyboardUtils = keyboardUtils;
    }

    public MutableLiveData<RegisterUser> getUserMutableLiveData() {
        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();

        }
        return userMutableLiveData;
    }

    public void onRegisterClick() {
        keyboardUtils.hideKeyboard();
        enableButton.setValue(false);
        user = new RegisterUser(emailAddress.getValue(), password.getValue(), firstName.getValue(), lastName.getValue(), mobile.getValue());
        userMutableLiveData.setValue(user);
    }

    public MutableLiveData<RegisterModel> getRegisterMutableLiveData() {
        if (registerMutableLiveData == null) {
            registerMutableLiveData = new MutableLiveData<>();
            enableButton.setValue(true);
            isLoading.setValue(false);
            registerNetwork();
        }
        return registerMutableLiveData;
    }

    public MutableLiveData<String> getError() {
        if (error == null){
            error = new MutableLiveData<>();
        }
        return error;
    }

    public void registerNetwork() {
        disposable = publishSubject.switchMap(new Function() {
            @Override
            public Object apply(Object o) throws Throwable {
                return guestRepository.getRegisterRequest(user)
                        .subscribeOn(Schedulers.io());
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleResults, this::handleError);
    }

    private void handleResults(Object registerModel) {
        if (((RegisterModel)registerModel) != null && ((RegisterModel)registerModel).getIsSuccess()) {
            registerMutableLiveData.setValue(((RegisterModel)registerModel));
        }else{
            error.setValue(((RegisterModel)registerModel).getMessage());
        }
        isLoading.setValue(false);
        enableButton.setValue(true);
    }


    private void handleError(Object t) {
        Toast.makeText(context, ((Throwable)t).toString(), Toast.LENGTH_SHORT).show();
        isLoading.setValue(false);
        enableButton.setValue(true);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
