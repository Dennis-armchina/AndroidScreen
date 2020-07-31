package com.armchina.cph.screenutils;

import android.graphics.Rect;

import com.armchina.cph.screenutils.Size;

public final class ScreenInfo {
    //Physical device size
    private final Rect contentRect;
    //Video Size
    private final Size VideoSize;

    public ScreenInfo(Rect contentRect, Size VideoSize) {
        this.contentRect = contentRect;
        this.VideoSize = VideoSize;
    }

    public Rect getContentRect() {
        return contentRect;
    }

    public Size getVideoSize() {
        return VideoSize;
    }
}
