package com.armchina.cph;


import android.os.Build;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.armchina.cph.screenutils.ScreenController;
import com.armchina.cph.screenutils.ScreenEncoder;
import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.AsyncServerSocket;
import com.koushikdutta.async.AsyncSocket;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.callback.ListenCallback;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import com.koushikdutta.async.util.Charsets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.internal.schedulers.ExecutorScheduler;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.subjects.PublishSubject;

import static android.content.ContentValues.TAG;
import static com.armchina.cph.screenutils.TouchUtils.touchDown;


/**
 * Created by Dennis Zhang
 */

public class Main {

    public static final int TYPE_ENCODE = 0;
    public static final int TYPE_CONTROL = 1;


    private Main() {
        // not instantiable
    }

    //h264发送参数
    public static void ScreenOn() throws Exception {
                try {
                    int bitRate = 1000000;
                    int maxFpx = 24;
                    System.out.println("---start---");

                    ServerSocket serverSocket = new ServerSocket(8080);

                    System.out.println("---listen---");

                    //get socket
                    Socket videoSocket = serverSocket.accept();
                    Socket controlSocket = serverSocket.accept();
                    System.out.println("---accept---");

                    System.out.println(">>>steam start");
                    ScreenEncoder screenEncoder = new ScreenEncoder(bitRate, maxFpx);
                    screenEncoder.streamScreen(videoSocket);

                    System.out.println(">>>control start");
                    ScreenController.controlScreen(controlSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
    }

    public static void main(String... args) throws Exception {
        ScreenOn();
    }
}
