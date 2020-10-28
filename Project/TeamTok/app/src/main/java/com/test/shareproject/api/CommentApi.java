package com.test.shareproject.api;

import com.test.shareproject.model.BoardReq;
import com.test.shareproject.model.CommentReq;
import com.test.shareproject.model.CommentRes;
import com.test.shareproject.model.Res;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface CommentApi {

    // 댓글 보기
    @POST("/api/v1/comment")
    Call<CommentRes> getCommentlist(@Body CommentReq commentReq);

    // 댓글 달기
    @POST("/api/v1/comment/add")
    Call<CommentRes> addComment(@Header("Authorization") String token,
                                @Body CommentReq commentReq);

    // 댓글 삭제
    @HTTP(method = "DELETE", path = "/api/v1/comment/delete", hasBody = true)
    Call<CommentRes> deleteComment(@Header("Authorization") String token,
                          @Body CommentReq commentReq);

    // 댓글 수정
    @POST("/api/v1/comment/update")
    Call<CommentRes> updateComment(@Header("Authorization") String token,
                             @Body CommentReq CommentReq);

    // 질문 댓글 보기
    @POST("/api/v1/qcomment")
    Call<CommentRes> getquCommentlist(@Body CommentReq commentReq);

    // 질문 댓글 달기
    @POST("/api/v1/qcomment/add")
    Call<CommentRes> addqComment(@Header("Authorization") String token,
                                @Body CommentReq commentReq);

    // 질문 댓글 삭제
    @HTTP(method = "DELETE", path = "/api/v1/qcomment/delete", hasBody = true)
    Call<CommentRes> deleteqComment(@Header("Authorization") String token,
                                   @Body CommentReq commentReq);

    // 질문 댓글 수정
    @POST("/api/v1/qcomment/update")
    Call<CommentRes> updateqComment(@Header("Authorization") String token,
                                   @Body CommentReq CommentReq);

}
