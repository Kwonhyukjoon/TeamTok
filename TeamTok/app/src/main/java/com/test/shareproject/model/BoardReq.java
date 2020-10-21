package com.test.shareproject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class BoardReq implements Serializable{

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("board_id")
    @Expose
    private Integer board_id;

    @SerializedName("question_id")
    @Expose
    private Integer question_id;

    @SerializedName("user_id")
    @Expose
    private int user_id;

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("nickname")
    @Expose
    private String nickname;

    @SerializedName("starttime")
    @Expose
    private String starttime;

    @SerializedName("endtime")
    @Expose
    private String endtime;

    @SerializedName("view_cnt")
    @Expose
    private int view_cnt;

    @SerializedName("com_cnt")
    @Expose
    private int com_cnt;

    @SerializedName("is_favorite")
    @Expose
    private int is_favorite;

    @SerializedName("type")
    @Expose
    private String type;

    public BoardReq(){

    }



    public BoardReq(Integer board_id, String category, String title, String content, String starttime, String endtime) {
        this.board_id = board_id;
        this.category = category;
        this.title = title;
        this.content = content;
        this.starttime = starttime;
        this.endtime = endtime;
    }

    public BoardReq(String category, String title, String content, String starttime, String endtime) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.starttime = starttime;
        this.endtime = endtime;
    }

    public BoardReq(String starttime, String endtime) {
        this.starttime = starttime;
        this.endtime = endtime;
    }

    public BoardReq(String category, String title, String content) {
        this.category = category;
        this.title = title;
        this.content = content;
    }

    public BoardReq(Integer question_id, String category, String title, String content) {
        this.question_id = question_id;
        this.category = category;
        this.title = title;
        this.content = content;
    }

    public BoardReq(Integer board_id) {
        this.board_id = board_id;
    }

    public BoardReq(Integer question_id,Integer com_cnt) {
        this.question_id = question_id;
    }

    public BoardReq(String category) {
        this.category = category;
    }








    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getBoard_id() {
        return board_id;
    }

    public int getBoardId() {
        try {
            return getBoard_id();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void setBoard_id(Integer board_id) {
        this.board_id = board_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public Integer getQuestion_id() {
        return question_id;
    }

    public int getQuestionId() {
        try {
            return getQuestion_id();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void setQuestion_id(Integer question_id) {
        this.question_id = question_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getEndDate() {
        try {
            return getEndtime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public int getView_cnt() {
        return view_cnt;
    }

    public void setView_cnt(int view_cnt) {
        this.view_cnt = view_cnt;
    }

    public int getCom_cnt() {
        return com_cnt;
    }

    public void setCom_cnt(int com_cnt) {
        this.com_cnt = com_cnt;
    }

    public int getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(int is_favorite) {
        this.is_favorite = is_favorite;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
