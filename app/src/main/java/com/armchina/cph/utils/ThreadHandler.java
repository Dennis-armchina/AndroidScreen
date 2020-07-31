package com.armchina.cph.utils;

import android.os.Looper;

public class ThreadHandler {
    private ThreadHandler(){
        // not instantiable
    }

    public static void prepareMainLooper(){
        Looper.prepareMainLooper();
    }
}
