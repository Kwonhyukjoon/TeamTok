package com.test.shareproject.api;

import com.test.shareproject.model.UserReq;
import com.test.shareproject.model.UserRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserApi {

    // 회원가입
    @POST("/api/v1/users")
    Call<UserRes> createUser(@Body UserReq userReq);

    // 로그인
    @POST("/api/v1/users/login")
    Call<UserRes> loginUser(@Body UserReq userReq);

    // 로그아웃
    @DELETE("/api/v1/users/logout")
    Call<UserRes> logout(@Header("Authorization") String token);

    //이메일 중복
    @POST("/api/v1/users/checkid")
    Call<UserRes> checkId(@Body UserReq userReq);

    //닉네임 중복
    @POST("/api/v1/users/checknik")
    Call<UserRes> checkNickName(@Body UserReq userReq);

    //내 정보 가져오기
    @GET("/api/v1/users/Mypage")
    Call<UserRes> Mypage(@Header("Authorization") String token);

    //내 비밀번호 변경하기
    @POST("/api/v1/users/resetPasswd")
    Call<UserRes> resetPasswd(@Body UserReq userReq);

    //내 닉네임 변경하기
    @POST("/api/v1/users/changeMyNik")
    Call<UserRes> changeMyNik(@Header("Authorization") String token,
                               @Body UserReq userReq);

}
