package com.unicom.waslak_client;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unicom.waslak_client.model.user.LocationUser;

import javax.inject.Inject;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<LocationUser> locationUserMutableLiveData;

    @Inject
    public SharedViewModel() {
    }

    public MutableLiveData<LocationUser> getLocationUserMutableLiveData() {
        if (locationUserMutableLiveData == null){
            locationUserMutableLiveData = new MutableLiveData<>();
        }
        return locationUserMutableLiveData;
    }
}
