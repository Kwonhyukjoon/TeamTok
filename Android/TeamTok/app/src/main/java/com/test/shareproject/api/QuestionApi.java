package com.test.shareproject.api;

import com.test.shareproject.model.BoardReq;
import com.test.shareproject.model.Res;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface QuestionApi {

    // 글 올리기
    @PUT("/api/v1/question")
    Call<Res> uploadQuestion(@Header("Authorization") String token,
                             @Body BoardReq boardReq);

    //최신글 가져오기
    @GET("/api/v1/question/latestQuestion")
    Call<Res> latestQuestion(@Query("limit") int limit);

    //글 상세보기
    @POST("/api/v1/question/view")
    Call<Res> viewQuestion(@Header("Authorization") String token,
                           @Body BoardReq boardReq);

    //글 상세보기(비로그인)
    @POST("/api/v1/question/nomem")
    Call<Res> qmemberBoard(@Body BoardReq boardReq);

    // 글 삭제하기
    @HTTP(method = "DELETE", path = "/api/v1/question/delete", hasBody = true)
    Call<Res> deleteQuestion(@Header("Authorization") String token,
                             @Body BoardReq boardReq);

    //글 수정하기
    @PUT("/api/v1/question/update")
    Call<Res> updateQuestion(@Header("Authorization") String token,
                             @Body BoardReq boardReq);

    //조회순 으로 가져오기
    @GET("/api/v1/question/qtopBoard")
    Call<Res> qtopBoard(@Query("order") String order,
                        @Query("limit") int limit);




}
