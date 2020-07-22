package com.unicom.waslak_client.viewmodel.user;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.model.user.OrdersHistoryModel;
import com.unicom.waslak_client.repository.UserRepository;
import com.unicom.waslak_client.utils.PreferenceUtils;

import java.util.List;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrdersHistoryViewModel extends ViewModel {
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> error;
    private MutableLiveData<List<OrdersHistoryModel.InnerDatum>> orderHistoryMutableLiveData;
    @Inject
    @ActivityContext
    Context context;
    UserRepository userRepository;
    PreferenceUtils preference;

    @Inject
    public OrdersHistoryViewModel(PreferenceUtils preference , UserRepository userRepository) {
        this.userRepository = userRepository;
        this.preference = preference;
    }

    public MutableLiveData<List<OrdersHistoryModel.InnerDatum>> getOrderHistoryMutableLiveData() {
        if (orderHistoryMutableLiveData == null){
            orderHistoryMutableLiveData = new MutableLiveData<>();
            retrieveOrdersHistory();
        }
        return orderHistoryMutableLiveData;
    }

    public MutableLiveData<String> getError() {
        if (error == null){
            error = new MutableLiveData<>();
        }
        return error;
    }

    private void retrieveOrdersHistory(){
        isLoading.setValue(true);
        userRepository.getOrdersHistory("Bearer "+preference.getTokenUser()).subscribeOn(Schedulers.io())

                .observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleResults, this::handleError);
    }

    private void handleResults(OrdersHistoryModel ordersHistoryModel) {
        if (ordersHistoryModel != null) {
            if (ordersHistoryModel.getIsSuccess()) {
                orderHistoryMutableLiveData.setValue(ordersHistoryModel.getInnerData());
            }else {
                error.setValue(ordersHistoryModel.getMessage());
            }
        }
        isLoading.setValue(false);
    }

    private void handleError(Throwable t) {
        Toasty.error(context, t.toString(), Toast.LENGTH_SHORT).show();
        isLoading.setValue(false);
    }
}
