package com.example.bighome.retrofit;

import com.example.bighome.data.BannerData;
import com.example.bighome.data.LoginData;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;


public interface RetrofitService {

    @FormUrlEncoded
    @POST()
    Observable<LoginData> login(@Url String path, @Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST()
    Observable<LoginData> register(@Url String path,@Field("username") String username, @Field("password") String password, @Field("repassword") String repassword);

    @GET()
    Observable<BannerData> getBanner(@Url String path);
}
