package com.example.bighome.realm;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class RealmHelper {
    public static final String DATABASE_NAME = "BigHome.realm";

    public static Realm newRealmInstance(){
        return Realm.getInstance(new RealmConfiguration.Builder()
                .name(DATABASE_NAME)
                .deleteRealmIfMigrationNeeded()
                .build());
    }
}
