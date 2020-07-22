package com.unicom.waslak_client.model.guest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {
    @SerializedName("innerData")
    private TokenModel innerdata;
    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("message")
    private String message;

    @SerializedName("statusCode")
    private int status;

    public TokenModel getInnerdata() {
        return innerdata;
    }

    public void setInnerdata(TokenModel innerdata) {
        this.innerdata = innerdata;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        status = status;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }
}
