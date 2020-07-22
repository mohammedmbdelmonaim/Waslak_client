package com.unicom.waslak_client.viewmodel.user;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.model.user.OrderUser;
import com.unicom.waslak_client.model.user.ProductsStoreModel;
import com.unicom.waslak_client.model.user.StoresActivityModel;
import com.unicom.waslak_client.repository.UserRepository;
import com.unicom.waslak_client.utils.PreferenceUtils;

import java.util.List;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NewOrderViewModel extends ViewModel {
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    UserRepository userRepository;
    PreferenceUtils preference;
    private MutableLiveData<List<ProductsStoreModel.InnerDatum>> products;
    public MutableLiveData<StoresActivityModel.InnerDatum> storeLiveData = new MutableLiveData<>();
    private CompositeDisposable disposables = new CompositeDisposable();
    public MutableLiveData<OrderUser.OrderItem> orderItems = new MutableLiveData<>();
    @Inject
    @ActivityContext
    Context context;

    @Inject
    public NewOrderViewModel(PreferenceUtils preference, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.preference = preference;
    }

    public LiveData<List<ProductsStoreModel.InnerDatum>> getProducts() {
        if (products == null) {
            products = new MutableLiveData<>();
            retrieveProducts();
        }
        return products;
    }

    private void retrieveProducts() {
        isLoading.setValue(true);
        disposables.add(userRepository.getProducts("Bearer " + preference.getTokenUser(), storeLiveData.getValue().getVendorId())
                .subscribeOn(Schedulers.io())
                .map(new Function<ProductsStoreModel, List<ProductsStoreModel.InnerDatum>>() {
                    @Override
                    public List<ProductsStoreModel.InnerDatum> apply(ProductsStoreModel productsStoreModel) throws Throwable {
                        return productsStoreModel.getInnerData();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResults, this::handleError));
    }

    private void handleError(Throwable throwable) {
        Toasty.error(context, throwable.toString(), Toast.LENGTH_SHORT).show();
        isLoading.setValue(false);
    }

    private void handleResults(List<ProductsStoreModel.InnerDatum> productsList) {
        products.setValue(productsList);
        isLoading.setValue(false);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }

}
