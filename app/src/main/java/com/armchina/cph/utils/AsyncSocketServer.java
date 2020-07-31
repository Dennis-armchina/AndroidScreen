package com.armchina.cph.utils;

import android.util.Log;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.AsyncServerSocket;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.Util;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.callback.ListenCallback;
import com.koushikdutta.async.util.Charsets;

import static android.content.ContentValues.TAG;

public class AsyncSocketServer {

    public static void startServer(int port, int flag) {
        String type = getType(flag);
        AsyncServer asyncServer = new AsyncServer();
        asyncServer.listen(null, port, new ListenCallback() {
            @Override
            public void onAccepted(final com.koushikdutta.async.AsyncSocket socket) {
                System.out.println(type+"  >>Accepted");
                if(flag == 0) {
                    //video socket start
                    socket.setDataCallback(new DataCallback() {
                        @Override
                        public void onDataAvailable(DataEmitter emitter, ByteBufferList bb) {
                            Log.i(TAG, "------------startServer--------onAccepted--------------------" + bb.readString(Charsets.UTF_8));
                            System.out.println(type + " >>start");
                        }
                    });
                }else{
                    //control socket start

                }
            }
            @Override
            public void onListening(AsyncServerSocket socket) {
                Log.i(TAG, "------------startServer--------onListening--------------------");
            }

            @Override
            public void onCompleted(Exception ex) {
                Log.i(TAG, "------------startServer--------onCompleted--------------------");
                System.out.println(">>> The socket should close ");
            }
        });
    }

    private static String getType(int type){
        return (type == 1)?("Video Socket"):("Control Socket");
    }

}
