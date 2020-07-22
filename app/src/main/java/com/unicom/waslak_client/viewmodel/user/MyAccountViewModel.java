package com.unicom.waslak_client.viewmodel.user;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.model.user.AccountModel;
import com.unicom.waslak_client.model.user.LogoutModel;
import com.unicom.waslak_client.model.user.NotificationChangeModel;
import com.unicom.waslak_client.model.user.NotificationUser;
import com.unicom.waslak_client.repository.UserRepository;
import com.unicom.waslak_client.utils.PreferenceUtils;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class MyAccountViewModel extends ViewModel {
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> error;
    private MutableLiveData<AccountModel.InnerData> accountMutableLiveData;
    private MutableLiveData<LogoutModel> logoutMutableLiveData;
    private CompositeDisposable disposables = new CompositeDisposable();
    public MutableLiveData<Boolean> notification = new MutableLiveData<>();
    public PublishSubject publishSubject = PublishSubject.create();
    @Inject
    @ActivityContext
    Context context;
    UserRepository userRepository;
    PreferenceUtils preference;

    @Inject
    public MyAccountViewModel(UserRepository userRepository, PreferenceUtils preference) {
        this.userRepository = userRepository;
        this.preference = preference;
    }

    public MutableLiveData<AccountModel.InnerData> getAccountMutableLiveData() {
        if (accountMutableLiveData == null) {
            accountMutableLiveData = new MutableLiveData<>();
            retrieveAccount();
            onChangeNotification();
        }
        return accountMutableLiveData;
    }

    public MutableLiveData<String> getError() {
        if (error == null) {
            error = new MutableLiveData<>();
        }
        return error;
    }

    private void retrieveAccount() {
        isLoading.setValue(true);
        Disposable disposable = userRepository.getAccount("Bearer " + preference.getTokenUser()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleResults, this::handleError);

        disposables.add(disposable);
    }

    private void handleResults(AccountModel accountModel) {
        if (accountModel != null) {
            if (accountModel.getIsSuccess()) {
                accountMutableLiveData.setValue(accountModel.getInnerData());
            } else {
                error.setValue(accountModel.getMessage());
            }
        }
        isLoading.setValue(false);
    }

    private void handleError(Throwable t) {
        Toasty.error(context, t.toString(), Toast.LENGTH_SHORT).show();
        isLoading.setValue(false);
    }

    public MutableLiveData<LogoutModel> getLogoutMutableLiveData() {
        if (logoutMutableLiveData == null) {
            logoutMutableLiveData = new MutableLiveData<>();
        }
        logoutService();
        return logoutMutableLiveData;
    }

    private void logoutService() {
        isLoading.setValue(true);
        Disposable disposable = userRepository.getLogout("Bearer " + preference.getTokenUser()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleResultsLogout, this::handleErrorLogout);
        disposables.add(disposable);
    }

    private void handleErrorLogout(Throwable throwable) {
        Toasty.error(context, throwable.toString(), Toast.LENGTH_SHORT).show();
        isLoading.setValue(false);
    }

    private void handleResultsLogout(LogoutModel logoutModel) {
        if (logoutModel != null) {
            if (logoutModel.getIsSuccess()) {
                logoutMutableLiveData.setValue(logoutModel);
            } else {
                Toasty.error(context, logoutModel.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        isLoading.setValue(false);
    }

    public void onChangeNotification() {
        disposables.add(publishSubject.switchMap(new Function() {
            @Override
            public Object apply(Object o) throws Throwable {
                isLoading.setValue(true);
                return userRepository.getChangeNotification("Bearer " + preference.getTokenUser(), new NotificationUser(notification.getValue(), preference.getTokenDevice()))
                        .subscribeOn(Schedulers.io());
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleResultsNotifi, this::handleErrorNotifi));
    }

    private void handleErrorNotifi(Object o) {
        Toasty.error(context, ((Throwable) o).getMessage(), Toast.LENGTH_SHORT).show();
        isLoading.setValue(false);
    }

    private void handleResultsNotifi(Object o) {
        if (((NotificationChangeModel) o).getIsSuccess()) {
            preference.saveNotificationState(notification.getValue());
        } else {
            Toasty.error(context, (((NotificationChangeModel) o).getMessage()), Toast.LENGTH_SHORT).show();
        }
        isLoading.setValue(false);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }
}
