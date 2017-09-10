package com.example.rkrde.awesometexteditor;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.example.rkrde.awesometexteditor.modal.AppDatabase;

/**
 * Created by rkrde on 10-09-2017.
 */

public class MyApp extends Application {

    private static AppDatabase appDatabase;
    public final static String DATABASE_NAME = "myDb";
    @Override
    public void onCreate() {
        super.onCreate();

        initAppDatabase();
    }

    private AppDatabase initAppDatabase(){
         appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DATABASE_NAME).build();
        return appDatabase;
    }

    public static AppDatabase getAppDatabase(Context context){
        if(appDatabase!=null)
            return appDatabase;
        else
            return  Room.databaseBuilder(context,
                    AppDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
    }
}
