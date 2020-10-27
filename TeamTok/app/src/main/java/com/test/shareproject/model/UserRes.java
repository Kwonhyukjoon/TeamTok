package com.test.shareproject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserRes {

    @SerializedName("success")
    boolean success;

    @SerializedName("user_id")
    int user_id;

    @SerializedName("email")
    String email;

    @SerializedName("token")
    String token;

    @SerializedName("nickname")
    String nickname;

    @SerializedName("items")
    @Expose
    private ArrayList<UserReq> items = null;

    @SerializedName("result")
    @Expose
    private ArrayList<UserReq> result = null;



    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public ArrayList<UserReq> getItems() {
        return items;
    }

    public void setItems(ArrayList<UserReq> items) {
        this.items = items;
    }

    public ArrayList<UserReq> getResult() {
        return result;
    }

    public void setResult(ArrayList<UserReq> result) {
        this.result = result;
    }
}
