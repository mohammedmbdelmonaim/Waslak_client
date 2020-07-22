package com.unicom.waslak_client.viewmodel.user;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unicom.waslak_client.R;
import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.model.user.CreateOrderModel;
import com.unicom.waslak_client.model.user.LocationUser;
import com.unicom.waslak_client.model.user.NotificationUser;
import com.unicom.waslak_client.model.user.OrderUser;
import com.unicom.waslak_client.model.user.StoresActivityModel;
import com.unicom.waslak_client.repository.UserRepository;
import com.unicom.waslak_client.ui.user.NewOrderFragment;
import com.unicom.waslak_client.ui.user.RequestsFragment;
import com.unicom.waslak_client.utils.KeyboardUtils;
import com.unicom.waslak_client.utils.PreferenceUtils;
import com.unicom.waslak_client.utils.SingleLiveData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class OrderInfoFragmentViewModel extends ViewModel {
    public MutableLiveData<String> note = new MutableLiveData<>();
    public MutableLiveData<String> textOrder = new MutableLiveData<>();
    public MutableLiveData<String> address = new MutableLiveData<>();
    public MutableLiveData<LocationUser> locationUserMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<OrderUser.OrderItem>> ordersLiveData = new MutableLiveData<>();
    public MutableLiveData<StoresActivityModel.InnerDatum> storeLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private SingleLiveData<String> error;
    private MutableLiveData<CreateOrderModel> createOrderModelMutableLiveData;
    private CompositeDisposable disposables = new CompositeDisposable();
    private OrderUser orderUser;
    private SingleLiveData<OrderUser> orderUserMutableLive;
    public PublishSubject publishSubject = PublishSubject.create();
    public MutableLiveData<Boolean> enableButton = new MutableLiveData<>();
    @Inject
    @ActivityContext
    Context context;
    KeyboardUtils keyboardUtils;
    UserRepository userRepository;
    PreferenceUtils preference;

    @Inject
    public OrderInfoFragmentViewModel(PreferenceUtils preference , KeyboardUtils keyboardUtils, UserRepository userRepository) {
        this.keyboardUtils = keyboardUtils;
        this.userRepository = userRepository;
        this.preference = preference;
        isLoading.setValue(false);
        enableButton.setValue(true);
    }

    public MutableLiveData<CreateOrderModel> getCreateOrderModelMutableLiveData() {
        if (createOrderModelMutableLiveData == null){
            createOrderModelMutableLiveData = new MutableLiveData<>();
        }
        return createOrderModelMutableLiveData;
    }

    public LiveData<OrderUser> getOrderUserMutableLive() {
        if (orderUserMutableLive == null){
            orderUserMutableLive = new SingleLiveData<>();
            createOrder();
        }
        return orderUserMutableLive;
    }

    public MutableLiveData<String> getError() {
        if (error == null){
            error = new SingleLiveData<>();
        }
        return error;
    }

    public void onOrderClick(){
        enableButton.setValue(false);
        keyboardUtils.hideKeyboard();
        if (locationUserMutableLiveData.getValue() == null){
            error.setValue(context.getString(R.string.chose_location));
            enableButton.setValue(true);
            return;
        }
        isLoading.setValue(true);
        double total_price = 0;
        String name = storeLiveData.getValue().getName();
        int storeId = storeLiveData.getValue().getId();
        int vendorId = storeLiveData.getValue().getVendorId();
        for (OrderUser.OrderItem order : ordersLiveData.getValue()){
            total_price += (order.getPrice()* order.getQuantity());
        }
        orderUser = new OrderUser(address.getValue() , note.getValue() , locationUserMutableLiveData.getValue().getLat() , locationUserMutableLiveData.getValue().getLng() , total_price , textOrder.getValue(), name , storeId , vendorId , ordersLiveData.getValue() ,address.getValue() , RequestsFragment.activityClick.getId());
        orderUserMutableLive.setValue(orderUser);
    }

    public void createOrder(){
        disposables.add(publishSubject.switchMap(new Function() {
            @Override
            public Object apply(Object o) throws Throwable {
                return userRepository.createOrder("Bearer "+preference.getTokenUser() , orderUser)
                        .subscribeOn(Schedulers.io());
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleResults, this::handleError));
    }



    private void handleResults(Object o) {
        if (((CreateOrderModel)o) != null) {
            if (((CreateOrderModel)o).getIsSuccess()) {
                createOrderModelMutableLiveData.setValue(((CreateOrderModel)o));
            }else {
                error.setValue(((CreateOrderModel)o).getMessage());
            }
        }
        isLoading.setValue(false);
        enableButton.setValue(true);
    }

    private void handleError(Object o) {
        Toast.makeText(context, ((Throwable)o).toString(), Toast.LENGTH_SHORT).show();
        isLoading.setValue(false);
        enableButton.setValue(true);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }
}
