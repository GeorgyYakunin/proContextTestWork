package com.example.procontexttestwork;

import android.content.Context;

import java.io.File;
import java.util.Observable;

class DBObservable extends Observable {
    private boolean IS_DB_IS_READY = false;
    private static final String TAG = "DBObservable";

    public DBObservable(Context mContext) {
        File dbTest = new File(String.valueOf(mContext.getDatabasePath("myDB")));
        if(dbTest.exists()){
            IS_DB_IS_READY = true;
        }
    }

    public boolean getDBState(){
        return IS_DB_IS_READY;
    }

    public void setNotificationFromDBHelper (boolean isReady) {
        IS_DB_IS_READY = isReady;
        setChanged();
        notifyObservers();
    }
}
