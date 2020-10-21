package com.test.shareproject.api;

import com.test.shareproject.model.BoardReq;
import com.test.shareproject.model.Res;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface FavoritesApi {

    //좋아요
    @POST("/api/v1/favorites")
    Call<Res> addFavorite(@Header("Authorization") String token,
                          @Body BoardReq boardReq);

    //좋아요 삭제
    @HTTP(method = "DELETE", path = "/api/v1/favorites", hasBody = true)
    Call<Res> deleteFavorite(@Header("Authorization") String token,
                             @Body BoardReq boardReq);
}
