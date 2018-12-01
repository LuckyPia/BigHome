package com.example.bighome.data.source.local;

import android.support.annotation.NonNull;
import android.util.Log;


import com.example.bighome.data.LoginData;
import com.example.bighome.data.LoginDetailData;
import com.example.bighome.data.LoginType;
import com.example.bighome.data.source.LoginDataSource;
import com.example.bighome.realm.RealmHelper;

import io.reactivex.Observable;
import io.realm.Realm;

public class LoginDataLocalSource implements LoginDataSource {
    @NonNull
    private static LoginDataLocalSource INSTANCE;


    private LoginDataLocalSource() {

    }

    public static LoginDataLocalSource getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new LoginDataLocalSource();
        }
        return INSTANCE;
    }


    @Override
    public Observable<LoginData> getRemoteLoginData(@NonNull String userName, @NonNull String password, @NonNull LoginType loginType) {
        //Not required because the {@link LoginDataRemoteSource}  has handled it
        return null;
    }

    @Override
    public Observable<LoginDetailData> getLocalLoginData(@NonNull int userId) {
        Realm realm = RealmHelper.newRealmInstance();
        Log.d("网络","本地");
        LoginDetailData loginDetailData = realm.copyFromRealm(
                realm.where(LoginDetailData.class)
                        .equalTo("id", userId)
                        .findFirst());
        return Observable.just(loginDetailData);
    }

}
