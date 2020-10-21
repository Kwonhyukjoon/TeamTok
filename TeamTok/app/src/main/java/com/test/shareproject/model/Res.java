package com.test.shareproject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Res {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("items")
    @Expose
    private ArrayList<BoardReq> items = null;

    @SerializedName("cnt")
    @Expose
    private Integer cnt;

    @SerializedName("data")
    @Expose
    private ArrayList<BoardReq> data = null;

    @SerializedName("message")
    @Expose
    private String message;


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public ArrayList<BoardReq> getItems() {
        return items;
    }

    public void setItems(ArrayList<BoardReq> items) {
        this.items = items;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public ArrayList<BoardReq> getData() {
        return data;
    }

    public void setData(ArrayList<BoardReq> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
