package com.unicom.waslak_client.viewmodel.user;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.model.user.ActivitiesModel;
import com.unicom.waslak_client.model.user.RequestStore;
import com.unicom.waslak_client.model.user.StoresActivityModel;
import com.unicom.waslak_client.repository.UserRepository;
import com.unicom.waslak_client.utils.PreferenceUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.ReplaySubject;

public class RequestsViewModel extends ViewModel {
    UserRepository userRepository;
    PreferenceUtils preference;
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<List<ActivitiesModel.InnerDatum>> activitiesLiveData;
    private MutableLiveData<List<StoresActivityModel.InnerDatum>> storesLiveData;
    private static final String TAG = "RequestsViewModel";
    private CompositeDisposable disposables = new CompositeDisposable();
    private PublishSubject<ActivitiesModel.InnerDatum> publishSubject = PublishSubject.create(); // for selecting a activity
    public MutableLiveData<String> lat = new MutableLiveData<>();
    public MutableLiveData<String> lng = new MutableLiveData<>();

    @Inject
    @ActivityContext
    Context context;

    @Inject
    public RequestsViewModel(UserRepository userRepository , PreferenceUtils preference) {
        this.userRepository = userRepository;
        this.preference = preference;
    }


    public LiveData<List<ActivitiesModel.InnerDatum>> getActivities() {
        if (activitiesLiveData == null) {
            activitiesLiveData = new MutableLiveData<>();
            retrieveActivities();
        }
        return activitiesLiveData;
    }

    private void retrieveActivities() {
        isLoading.setValue(true);
        userRepository.getActivities("Bearer "+preference.getTokenUser())
                .subscribeOn(Schedulers.io())
                .map(new Function<ActivitiesModel, List<ActivitiesModel.InnerDatum>>() {
                    @Override
                    public List<ActivitiesModel.InnerDatum> apply(ActivitiesModel activitiesModel) throws Throwable {
                        return activitiesModel.getInnerData();
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<ActivitiesModel.InnerDatum>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposables.add(d);
            }

            @Override
            public void onNext(@NonNull List<ActivitiesModel.InnerDatum> innerData) {
                handleResults(innerData);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                handleError(e);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void handleError(Throwable throwable) {
        Toast.makeText(context, throwable.toString(), Toast.LENGTH_SHORT).show();
        isLoading.setValue(false);
    }

    private void handleResults(List<ActivitiesModel.InnerDatum> activities) {
        activitiesLiveData.setValue(activities);
        publishSubject.onNext(activities.get(0));
    }


    public LiveData<List<StoresActivityModel.InnerDatum>> getStores() {
        if (storesLiveData == null) {
            storesLiveData = new MutableLiveData<>();
            retrieveStores();
        }
        return storesLiveData;
    }

    private void retrieveStores() {
        isLoading.setValue(true);
        publishSubject
                // apply switchmap operator so only one Observable can be used at a time.
                // it clears the previous one
                .switchMap(new Function<ActivitiesModel.InnerDatum, ObservableSource<List<StoresActivityModel.InnerDatum>>>() {
                    @Override
                    public ObservableSource<List<StoresActivityModel.InnerDatum>> apply(ActivitiesModel.InnerDatum innerDatum) throws Throwable {
                        return userRepository.getStoresActivity("Bearer "+preference.getTokenUser() , new RequestStore(lat.getValue() , lng.getValue() , innerDatum.getId()))
                                .subscribeOn(Schedulers.io())
                                .map(new Function<StoresActivityModel, List<StoresActivityModel.InnerDatum>>() {
                                    @Override
                                    public List<StoresActivityModel.InnerDatum> apply(StoresActivityModel storesActivityModel) throws Throwable {
                                        return storesActivityModel.getInnerData();
                                    }
                                });
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<StoresActivityModel.InnerDatum>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposables.add(d);
            }

            @Override
            public void onNext(@NonNull List<StoresActivityModel.InnerDatum> stores) {
                handleStoresResult(stores);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                handleStoresError(e);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void handleStoresError(Throwable e) {
        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        isLoading.setValue(false);
    }

    private void handleStoresResult(List<StoresActivityModel.InnerDatum> stores) {
        storesLiveData.setValue(stores);
        isLoading.setValue(false);
    }

    public void onclick(ActivitiesModel.InnerDatum activity) {
//        retrieveStores();
        publishSubject.onNext(activity);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }
}
