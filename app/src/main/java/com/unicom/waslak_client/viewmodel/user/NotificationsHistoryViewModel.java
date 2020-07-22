package com.unicom.waslak_client.viewmodel.user;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.model.user.NotificationHistoryModel;
import com.unicom.waslak_client.repository.UserRepository;
import com.unicom.waslak_client.utils.PreferenceUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NotificationsHistoryViewModel extends ViewModel {
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> error;
    private MutableLiveData<List<NotificationHistoryModel.InnerDatum>> notificationHistoryMutableLiveData;
    @Inject
    @ActivityContext
    Context context;
    UserRepository userRepository;
    PreferenceUtils preference;

    @Inject
    public NotificationsHistoryViewModel(PreferenceUtils preference, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.preference = preference;
    }

    public MutableLiveData<List<NotificationHistoryModel.InnerDatum>> getOrderHistoryMutableLiveData() {
        if (notificationHistoryMutableLiveData == null) {
            notificationHistoryMutableLiveData = new MutableLiveData<>();
            retrieveNotificationsHistory();
        }
        return notificationHistoryMutableLiveData;
    }

    public MutableLiveData<String> getError() {
        if (error == null) {
            error = new MutableLiveData<>();
        }
        return error;
    }

    private void retrieveNotificationsHistory() {
        isLoading.setValue(true);
        userRepository.getNotificationsHistory("Bearer " + preference.getTokenUser()).subscribeOn(Schedulers.io())

                .observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleResults, this::handleError);
    }

    private void handleResults(NotificationHistoryModel notificationHistoryModel) {
        if (notificationHistoryModel != null) {
            if (notificationHistoryModel.getIsSuccess()) {
                notificationHistoryMutableLiveData.setValue(notificationHistoryModel.getInnerData());
            } else {
                error.setValue(notificationHistoryModel.getMessage());
            }
        }
        isLoading.setValue(false);
    }

    private void handleError(Throwable t) {
        Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
        isLoading.setValue(false);
    }
}
