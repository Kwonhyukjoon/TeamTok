package com.test.shareproject.model;

import java.io.Serializable;

public class UserReq implements Serializable {

    private String email;
    private String passwd;
    private String nickname;
    private String name;
    private int cnt;

    public UserReq(String email) {
        this.email = email;
    }


    public UserReq(String email, String passwd) {
        this.email = email;
        this.passwd = passwd;
    }

    public UserReq(String email, String passwd, String nickname, String name) {
        this.email = email;
        this.passwd = passwd;
        this.nickname = nickname;
        this.name = name;
    }


    public UserReq(String nickname , int cnt){
        this.nickname = nickname;
    }





    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

}
