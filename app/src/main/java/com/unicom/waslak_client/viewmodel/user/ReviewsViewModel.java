package com.unicom.waslak_client.viewmodel.user;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.model.user.RateModel;
import com.unicom.waslak_client.model.user.StoresActivityModel;
import com.unicom.waslak_client.repository.UserRepository;
import com.unicom.waslak_client.utils.PreferenceUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ReviewsViewModel extends ViewModel {

    UserRepository userRepository;
    PreferenceUtils preference;
    private MutableLiveData<List<RateModel.CustomersRate>> customer_rates;
    public MutableLiveData<StoresActivityModel.InnerDatum> storeLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    Disposable disposable;
    @Inject
    @ActivityContext
    Context context;

    @Inject
    public ReviewsViewModel(PreferenceUtils preference, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.preference = preference;
    }

    public LiveData<List<RateModel.CustomersRate>> getCustomer_rates() {
        if (customer_rates == null) {
            customer_rates = new MutableLiveData<>();
        }
        retrieveReviews();
        return customer_rates;
    }

    private void retrieveReviews() {
        isLoading.setValue(true);
        disposable = userRepository.getRates("Bearer " + preference.getTokenUser(), storeLiveData.getValue().getId())
                .map(new Function<RateModel, List<RateModel.CustomersRate>>() {
                    @Override
                    public List<RateModel.CustomersRate> apply(RateModel rateModel) throws Throwable {
                        return rateModel.getInnerData().getCustomersRate();
                    }
                })
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleResults, this::handleError);
    }

    private void handleResults(List<RateModel.CustomersRate> customersRates) {
        isLoading.setValue(false);
        customer_rates.setValue(customersRates);

    }

    private void handleError(Throwable throwable) {
        Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
        isLoading.setValue(false);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }

}
