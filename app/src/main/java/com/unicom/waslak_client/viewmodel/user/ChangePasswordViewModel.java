package com.unicom.waslak_client.viewmodel.user;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.model.user.ChangePassModel;
import com.unicom.waslak_client.model.user.ChangePassUser;
import com.unicom.waslak_client.repository.UserRepository;
import com.unicom.waslak_client.utils.KeyboardUtils;
import com.unicom.waslak_client.utils.PreferenceUtils;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class ChangePasswordViewModel extends ViewModel {
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> passwordNew = new MutableLiveData<>();
    public MutableLiveData<String> passwordConfirm = new MutableLiveData<>();
    private MutableLiveData<ChangePassUser> userMutableLiveData;
    private MutableLiveData<ChangePassModel> changePassModelMutableLiveData;
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> error;
    public PublishSubject publishSubject = PublishSubject.create();
    public MutableLiveData<Boolean> enableButton = new MutableLiveData<>();

    Context context;
    private Disposable disposable;
    UserRepository userRepository;
    PreferenceUtils preference;
    KeyboardUtils keyboardUtils;
    ChangePassUser changePassUser;

    @Inject
    public ChangePasswordViewModel(@ActivityContext Context context, UserRepository userRepository, PreferenceUtils preference, KeyboardUtils keyboardUtils) {
        this.context = context;
        this.userRepository = userRepository;
        this.preference = preference;
        this.keyboardUtils = keyboardUtils;
        isLoading.setValue(false);
        enableButton.setValue(true);
    }

    public MutableLiveData<ChangePassUser> getUser() {
        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;
    }

    public void onChangeClick(){
        enableButton.setValue(false);
        keyboardUtils.hideKeyboard();
        changePassUser = new ChangePassUser(password.getValue() , passwordNew.getValue());
        userMutableLiveData.setValue(changePassUser);
    }

    public MutableLiveData<ChangePassModel> getLChangePassNetworkResponse() {
        if (changePassModelMutableLiveData == null) {
            changePassModelMutableLiveData = new MutableLiveData<>();
            changeNetwork();
        }
        return changePassModelMutableLiveData;
    }

    public MutableLiveData<String> getError() {
        if (error == null){
            error = new MutableLiveData<>();
        }
        return error;
    }

    public void changeNetwork(){
        disposable = publishSubject.switchMap(new Function() {
            @Override
            public Object apply(Object o) throws Throwable {
                return userRepository.getChangePass("Bearer "+preference.getTokenUser(),changePassUser)
                        .subscribeOn(Schedulers.io());
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleResults, this::handleError);
    }

    private void handleError(Object o) {
        Toasty.error(context, ((Throwable)o).toString(), Toast.LENGTH_SHORT).show();
        isLoading.setValue(false);
        enableButton.setValue(true);
    }

    private void handleResults(Object o) {
        if (((ChangePassModel)o) != null && ((ChangePassModel)o).getIsSuccess()){
            changePassModelMutableLiveData.setValue(((ChangePassModel)o));
        }else {
            error.setValue(((ChangePassModel)o).getMessage());
        }
        isLoading.setValue(false);
        enableButton.setValue(true);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null){
            disposable.dispose();
        }

    }
}