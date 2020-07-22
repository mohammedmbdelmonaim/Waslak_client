package com.unicom.waslak_client.model.guest;

import android.util.Patterns;

import com.google.gson.annotations.SerializedName;

public class LoginUser {
    @SerializedName("email")
    private String strEmailAddress;
    @SerializedName("password")
    private String strPassword;
    @SerializedName("deviceToken")
    private String deviceToken;
    @SerializedName("deviceType")
    private String deviceType;


    public LoginUser(String strEmailAddress, String Password, String deviceToken, String deviceType) {
        this.strEmailAddress = strEmailAddress;
        this.strPassword = Password;
        this.deviceToken = deviceToken;
        this.deviceType = deviceType;
    }

    public String getStrEmailAddress() {
        return strEmailAddress;
    }

    public String getStrPassword() {
        return strPassword;
    }


    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public boolean isEmailValid() {
        return Patterns.EMAIL_ADDRESS.matcher(getStrEmailAddress()).matches();
    }

    public boolean isPasswordValid() {
        return getStrPassword().length() < 6 || getStrPassword().length() > 20;
    }
}
