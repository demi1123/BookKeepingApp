package com.example.bookkeepingdemo;

import android.app.Application;

import com.example.bookkeepingdemo.db.DBManager;

public class UniteApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DBManager.initDB(getApplicationContext());
    }

}
