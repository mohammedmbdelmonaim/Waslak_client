package com.unicom.waslak_client.model.guest;

import android.util.Patterns;

import com.google.gson.annotations.SerializedName;

public class ForgetPassUSer {
    @SerializedName("email")
    private String strEmailAddress;

    public ForgetPassUSer(String strEmailAddress) {
        this.strEmailAddress = strEmailAddress;
    }

    public String getStrEmailAddress() {
        return strEmailAddress;
    }

    public void setStrEmailAddress(String strEmailAddress) {
        this.strEmailAddress = strEmailAddress;
    }
    public boolean isEmailValid() {
        return Patterns.EMAIL_ADDRESS.matcher(getStrEmailAddress()).matches();
    }

}
