package com.unicom.waslak_client.model.guest;

import android.util.Patterns;

import com.google.gson.annotations.SerializedName;

public class RegisterUser {
    @SerializedName("email")
    private String strEmailAddress;
    @SerializedName("password")
    private String strPassword;
    @SerializedName("firstName")
    private String strFirstName;
    @SerializedName("lastName")
    private String strLastName;
    @SerializedName("mobile")
    private String strMobile;

    public RegisterUser(String strEmailAddress, String strPassword, String strFirstName, String strLastName, String strMobile) {
        this.strEmailAddress = strEmailAddress;
        this.strPassword = strPassword;
        this.strFirstName = strFirstName;
        this.strLastName = strLastName;
        this.strMobile = strMobile;
    }

    public String getStrEmailAddress() {
        return strEmailAddress;
    }

    public void setStrEmailAddress(String strEmailAddress) {
        this.strEmailAddress = strEmailAddress;
    }

    public String getStrPassword() {
        return strPassword;
    }

    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }

    public String getStrFirstName() {
        return strFirstName;
    }

    public void setStrFirstName(String strFirstName) {
        this.strFirstName = strFirstName;
    }

    public String getStrLastName() {
        return strLastName;
    }

    public void setStrLastName(String strLastName) {
        this.strLastName = strLastName;
    }

    public String getStrMobile() {
        return strMobile;
    }

    public void setStrMobile(String strMobile) {
        this.strMobile = strMobile;
    }

    public boolean isEmailValid() {
        return Patterns.EMAIL_ADDRESS.matcher(getStrEmailAddress()).matches();
    }


    public boolean isPasswordValid() {
        return getStrPassword().length() < 6 || getStrPassword().length() > 20;
    }

    public boolean isMobileValid() {
        return getStrMobile().length() == 11;
    }
}
