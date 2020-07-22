package com.unicom.waslak_client.viewmodel.guest;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.di.scope.FragmentScope;
import com.unicom.waslak_client.model.guest.ForgetPassUSer;
import com.unicom.waslak_client.model.guest.ForgetPasswordModel;
import com.unicom.waslak_client.repository.GuestRepository;
import com.unicom.waslak_client.utils.KeyboardUtils;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

@FragmentScope
public class ForgetPasswordViewModel extends ViewModel {
    public MutableLiveData<String> email = new MutableLiveData<>();
    private MutableLiveData<String> error;
    private Disposable disposable;
    private MutableLiveData<ForgetPassUSer> userMutableLiveData;
    private MutableLiveData<ForgetPasswordModel> forgetMutableLiveData;
    public PublishSubject publishSubject = PublishSubject.create();
    public MutableLiveData<Boolean> enableButton = new MutableLiveData<>();
    ForgetPassUSer forgetPassUSer;
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    KeyboardUtils keyboardUtils;
    GuestRepository guestRepository;
    @Inject
    @ActivityContext
    Context context;

    @Inject
    public ForgetPasswordViewModel(KeyboardUtils keyboardUtils, GuestRepository guestRepository) {
        this.keyboardUtils = keyboardUtils;
        this.guestRepository = guestRepository;
        isLoading.setValue(true);
    }

    public MutableLiveData<ForgetPassUSer> getUser() {
        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();

        }
        return userMutableLiveData;
    }

    public void onSendClick() {
        keyboardUtils.hideKeyboard();
        enableButton.setValue(false);
        forgetPassUSer = new ForgetPassUSer(email.getValue());
        userMutableLiveData.setValue(forgetPassUSer);
    }

    public MutableLiveData<ForgetPasswordModel> getForgetNetworkResponse() {
        if (forgetMutableLiveData == null) {
            forgetMutableLiveData = new MutableLiveData<>();
            enableButton.setValue(true);
            isLoading.setValue(false);
            forgetNetwork();
        }
        return forgetMutableLiveData;
    }

    public MutableLiveData<String> getError() {
        if (error == null){
            error = new MutableLiveData<>();
        }
        return error;
    }
    public void forgetNetwork() {
        disposable = publishSubject.switchMap(new Function() {
            @Override
            public Object apply(Object o) throws Throwable {
                return guestRepository.getForgetRequest(forgetPassUSer)
                        .subscribeOn(Schedulers.io());
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleResults, this::handleError);
    }
    private void handleResults(Object forgetPasswordModel) {
        if (((ForgetPasswordModel)forgetPasswordModel) != null) {
            if (((ForgetPasswordModel)forgetPasswordModel).getIsSuccess()) {
                forgetMutableLiveData.setValue(((ForgetPasswordModel)forgetPasswordModel));
            }else {
                error.setValue(((ForgetPasswordModel)forgetPasswordModel).getMessage());
            }
        }
    }

    private void handleError(Object t) {
        Toast.makeText(context, ((Throwable)t).toString(), Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
