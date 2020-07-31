package com.armchina.cph.screenutils;

import android.graphics.Rect;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Surface;

import com.armchina.cph.Main;
import com.armchina.cph.wrappers.SurfaceControl;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataSink;
import com.koushikdutta.async.Util;
import com.koushikdutta.async.callback.CompletedCallback;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

public class ScreenEncoder {

    public static boolean end = false;
    //I帧
    private static final int DEFAULT_I_FRAME_INTERVAL = 10;
    //间隔100ms
    private static final int REPEAT_FRAME_DELAY_US = 100_000;
    private static final String KEY_MAX_FPS_TO_ENCODER = "max-fps-to-encoder";


    private static final int width = 1920;
    private static final int height= 1080;
    private static final Rect contentRect = new Rect(0, 0, width, height);
    private static final Rect VideoRect = new Rect(0, 0, width, height);
    private static final Size videoSize = new Size(width, height);
    private static final int layerStack = 0;

    private int bitRate;
    private int maxFps;
    public ScreenEncoder(int bitRate, int maxFps){
        this.bitRate = bitRate;
        this.maxFps  = maxFps;
    }


    public void streamScreen(final Socket socket) throws IOException {
        new Thread() {
            public void run() {
                super.run();
                try {
                    MediaFormat format = createFormat(bitRate, maxFps);
                    boolean alive;
                    do {
                        MediaCodec codec = createCodec();
                        IBinder display = createDisplay();
                        ScreenInfo screenInfo = new ScreenInfo(contentRect, videoSize);
                        setSize(format, width, height);
                        configure(codec, format);
                        Surface surface = codec.createInputSurface();
                        setDisplaySurface(display, surface, contentRect, VideoRect, layerStack);
                        codec.start();
                        try {
                            alive = encode(codec, socket);
                            // do not call stop() on exception, it would trigger an IllegalStateException
                            codec.stop();
                        } finally {
                            destroyDisplay(display);
                            codec.release();
                            surface.release();
                        }
                    } while (alive);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private boolean encode(MediaCodec codec, Socket socket) throws IOException {
        boolean eof = false;
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();

        while ( !eof) {
            int outputBufferId = codec.dequeueOutputBuffer(bufferInfo, -1);
            eof = (bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0;
            try {
                if (outputBufferId >= 0) {
                    ByteBuffer codecBuffer = codec.getOutputBuffer(outputBufferId);
                    if (socket.isClosed()){break;}
                    writeFrame(socket, codecBuffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (outputBufferId >= 0) {
                    codec.releaseOutputBuffer(outputBufferId, false);
                }
            }
        }
        System.out.println(">>>video socket closed");
        return false;
    }

    private void writeFrame(Socket socket, ByteBuffer codecBuffer) throws IOException {
        BufferedOutputStream stream = new BufferedOutputStream(socket.getOutputStream());
        System.out.println("codecBuffer.remaining()"+codecBuffer.remaining());
        writeBuffer(codecBuffer, stream);
        stream.flush();
        System.out.println("\nframe send");
    }

    private void writeBuffer(ByteBuffer buffer, OutputStream stream) throws IOException {
        WritableByteChannel channel = Channels.newChannel(stream);
        channel.write(buffer);
    }


    private static void configure(MediaCodec codec, MediaFormat format) {
        codec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
    }

    private static IBinder createDisplay() {
        return SurfaceControl.createDisplay("androidScreen",true);
    }

    private static MediaCodec createCodec() throws IOException {
        return MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
    }

    private static MediaFormat createFormat(int bitRate, int maxFps) {
        MediaFormat format = new MediaFormat();
        format.setString(MediaFormat.KEY_MIME, MediaFormat.MIMETYPE_VIDEO_AVC);
        format.setInteger(MediaFormat.KEY_BIT_RATE,bitRate);
        format.setInteger(MediaFormat.KEY_FRAME_RATE, 60);
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, DEFAULT_I_FRAME_INTERVAL);
        format.setLong(MediaFormat.KEY_REPEAT_PREVIOUS_FRAME_AFTER, REPEAT_FRAME_DELAY_US);
        format.setFloat(KEY_MAX_FPS_TO_ENCODER, maxFps);
        return format;
    }

    private static void setSize(MediaFormat format, int width, int height) {
        format.setInteger(MediaFormat.KEY_WIDTH, width);
        format.setInteger(MediaFormat.KEY_HEIGHT, height);
    }

    private static void setDisplaySurface(IBinder display, Surface surface, Rect deviceRect, Rect displayRect, int layerStack) {
        SurfaceControl.openTransaction();
        try {
            SurfaceControl.setDisplaySurface(display, surface);
            SurfaceControl.setDisplayProjection(display,  0, deviceRect, displayRect);
            SurfaceControl.setDisplayLayerStack(display, layerStack);
        } finally {
            SurfaceControl.closeTransaction();
        }
    }

    private static void destroyDisplay(IBinder display) {

        SurfaceControl.destroyDisplay(display);
    }
}

