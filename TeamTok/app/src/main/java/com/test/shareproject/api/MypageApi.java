package com.test.shareproject.api;

import com.test.shareproject.model.Res;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MypageApi {

    // 마이페이지 나의 글 가져오기
    @POST("/api/v1/mypage/mywrite")
    Call<Res> myWrite(@Header("Authorization") String token,
                      @Query("order") String order);

    // 마이페이지 내가 댓글 단 글 모두 가져오기
    @GET("/api/v1/mypage/mycomment")
    Call<Res> getMycomment(@Header("Authorization") String token);

    //내가 좋아하기 누른 글 모두 가져오기
    @GET("/api/v1/mypage/mylike")
    Call<Res> mylikeBoard(@Header("Authorization") String token);
}
