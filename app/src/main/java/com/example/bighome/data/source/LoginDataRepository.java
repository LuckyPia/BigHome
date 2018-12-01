package com.example.bighome.data.source;

import android.support.annotation.NonNull;

import com.example.bighome.data.LoginData;
import com.example.bighome.data.LoginDetailData;
import com.example.bighome.data.LoginType;

import io.reactivex.Observable;


public class LoginDataRepository implements LoginDataSource{
    @NonNull
    private LoginDataSource localDataSource;
    @NonNull
    private LoginDataSource remoteDataSource;

    private LoginDataRepository(@NonNull LoginDataSource localDataSource, @NonNull LoginDataSource remoteDataSource){
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }
    @NonNull
    private static LoginDataRepository INSTANCE;

    public static LoginDataRepository getInstance(@NonNull LoginDataSource localDataSource,@NonNull LoginDataSource remoteDataSource){
        if (INSTANCE == null) {
            INSTANCE = new LoginDataRepository(localDataSource, remoteDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Observable<LoginData> getRemoteLoginData(@NonNull String userName, @NonNull String password, @NonNull LoginType loginType) {
        return remoteDataSource.getRemoteLoginData(userName, password,loginType);
    }

    @Override
    public Observable<LoginDetailData> getLocalLoginData(@NonNull int userId) {
        return localDataSource.getLocalLoginData(userId);
    }




}
