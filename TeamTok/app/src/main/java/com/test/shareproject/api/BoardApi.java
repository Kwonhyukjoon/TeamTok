package com.test.shareproject.api;

import com.test.shareproject.model.BoardReq;
import com.test.shareproject.model.Res;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface BoardApi {

    // 글 올리기
    @POST("/api/v1/board")
    Call<Res> BoardUpload(@Header("Authorization") String token,
                          @Body BoardReq boardReq);

    //최신글 가져오기
    @GET("/api/v1/board")
    Call<Res> getBoardlist(@Query("order") String order,
                           @Query("limit") int limit);

    //글 상세보기(로그인)
    @POST("/api/v1/board/view")
    Call<Res> viewBoard(@Header("Authorization") String token,
                        @Body BoardReq boardReq);

    //글 상세보기(비 로그인)
    @POST("/api/v1/board/nomem")
    Call<Res> nomemeberBoard(@Body BoardReq boardReq);


    //글 검색하기(자유 질문 둘다)
    @POST("/api/v1/board/search")
    Call<Res> searchBoard(@Query("category") String category,
                          @Query("keyword") String keyword);


    // 글 삭제하기
    @HTTP(method = "DELETE", path = "/api/v1/board/delete", hasBody = true)
    Call<Res> deleteBoard(@Header("Authorization") String token,
                          @Body BoardReq boardReq);


    //글 수정하기
    @POST("/api/v1/board/update")
    Call<Res> updateBoard(@Header("Authorization") String token,
                          @Body BoardReq boardReq);


    // 마감직전
    @POST("/api/v1/board/deadline")
    Call<Res> DeadlineBoard(@Query("limit") int limit);

    // 인기순으로 글 가져오기기
    @GET("/api/v1/board/topboard")
    Call<Res> topboard(@Query("order") String order,
                       @Query("limit") int limit);

}



