package com.test.shareproject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CommentRes {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("items")
    @Expose
    private ArrayList<CommentReq> items = null;

    @SerializedName("cnt")
    @Expose
    private Integer cnt;

    @SerializedName("data")
    @Expose
    private ArrayList<CommentReq> data = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public ArrayList<CommentReq> getItems() {
        return items;
    }

    public void setItems(ArrayList<CommentReq> items) {
        this.items = items;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public ArrayList<CommentReq> getData() {
        return data;
    }

    public void setData(ArrayList<CommentReq> data) {
        this.data = data;
    }
}
