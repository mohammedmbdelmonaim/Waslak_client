package com.unicom.waslak_client.viewmodel.user;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.model.user.OrderDetailModel;
import com.unicom.waslak_client.model.user.RequestStore;
import com.unicom.waslak_client.model.user.StoresActivityModel;
import com.unicom.waslak_client.model.user.UpdateOrder;
import com.unicom.waslak_client.model.user.UpdateOrderModel;
import com.unicom.waslak_client.repository.UserRepository;
import com.unicom.waslak_client.utils.KeyboardUtils;
import com.unicom.waslak_client.utils.PreferenceUtils;

import java.util.List;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class OrderDetailViewModel extends ViewModel {
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public MutableLiveData<Integer> orderId = new MutableLiveData<>();
    public MutableLiveData<UpdateOrderModel> updateOrderLiveData = new MutableLiveData<>();
    private MutableLiveData<String> error;
    private MutableLiveData<OrderDetailModel> orderDetailMutableLiveData;
    private MutableLiveData<List<StoresActivityModel.InnerDatum>> storesLiveData;
    private CompositeDisposable disposables = new CompositeDisposable();
    public PublishSubject publishSubject = PublishSubject.create();
    public MutableLiveData<Boolean> enableButton = new MutableLiveData<>();
    @Inject
    @ActivityContext
    Context context;
    KeyboardUtils keyboardUtils;
    UserRepository userRepository;
    PreferenceUtils preference;

    @Inject
    public OrderDetailViewModel(PreferenceUtils preference, KeyboardUtils keyboardUtils, UserRepository userRepository) {
        this.keyboardUtils = keyboardUtils;
        this.userRepository = userRepository;
        this.preference = preference;
        enableButton.setValue(true);
    }


    public MutableLiveData<OrderDetailModel> getOrderDetailMutableLive() {
        if (orderDetailMutableLiveData == null) {
            orderDetailMutableLiveData = new MutableLiveData<>();
            retrieveOrderDetail();
            onCancelOrder();
        }
        return orderDetailMutableLiveData;
    }

    public MutableLiveData<String> getError() {
        if (error == null) {
            error = new MutableLiveData<>();
        }
        return error;
    }

    private void retrieveOrderDetail() {
        isLoading.setValue(true);
        disposables.add(userRepository.getOrderDetail("Bearer " + preference.getTokenUser(), orderId.getValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResults, this::handleError));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }


    private void handleResults(OrderDetailModel orderDetailModel) {
        if (orderDetailModel != null) {
            if (orderDetailModel.getIsSuccess()) {
                orderDetailMutableLiveData.setValue(orderDetailModel);
            } else {
                error.setValue(orderDetailModel.getMessage());
            }
        }
        isLoading.setValue(false);
    }

    private void handleError(Throwable t) {
        Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
        isLoading.setValue(false);
    }

    public LiveData<List<StoresActivityModel.InnerDatum>> getStores() {
        if (storesLiveData == null) {
            storesLiveData = new MutableLiveData<>();
        }
        return storesLiveData;
    }

    public void retrieveStores(Integer activityId, String lat, String lng) {
        isLoading.setValue(true);
        disposables.add(userRepository.getStoresActivity("Bearer " + preference.getTokenUser(), new RequestStore(lat, lng, activityId == null ? 1 : activityId))
                .subscribeOn(Schedulers.io())
                .map(new Function<StoresActivityModel, List<StoresActivityModel.InnerDatum>>() {
                    @Override
                    public List<StoresActivityModel.InnerDatum> apply(StoresActivityModel storesActivityModel) throws Throwable {
                        return storesActivityModel.getInnerData();
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleStoreResults, this::handleStoreError));
    }

    private void handleStoreResults(List<StoresActivityModel.InnerDatum> innerData) {
        storesLiveData.setValue(innerData);
        isLoading.setValue(false);
    }

    private void handleStoreError(Throwable e) {
        Toasty.error(context, e.toString(), Toast.LENGTH_SHORT).show();
        isLoading.setValue(false);
    }

    public void onCancelOrder() {
        disposables.add(publishSubject.switchMap(new Function() {
            @Override
            public Object apply(Object o) throws Throwable {
                enableButton.setValue(false);
                UpdateOrder updateOrder = new UpdateOrder(orderDetailMutableLiveData.getValue().getInnerData().getId(), 6);
                isLoading.setValue(true);
                return userRepository.getUpdateOrder("Bearer " + preference.getTokenUser(), updateOrder)
                        .subscribeOn(Schedulers.io());
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleResultsUpdateOrder, this::handleErrorUpdateOrder));
    }

    private void handleErrorUpdateOrder(Object o) {
        Toast.makeText(context, ((Throwable)o).toString(), Toast.LENGTH_SHORT).show();
        isLoading.setValue(false);
        enableButton.setValue(true);
    }

    private void handleResultsUpdateOrder(Object updateOrderModel) {
        if (((UpdateOrderModel)updateOrderModel) != null) {
            updateOrderLiveData.setValue(((UpdateOrderModel)updateOrderModel));
        }
        isLoading.setValue(false);
        enableButton.setValue(true);
    }
}