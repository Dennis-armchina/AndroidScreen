package com.armchina.cph.screenutils;

import android.hardware.input.InputManager;
import android.os.SystemClock;
import androidx.core.view.InputDeviceCompat;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.lang.reflect.Method;

public class TouchUtils {

    static InputManager sIm;
    static Method sInjectInputEventMethod;
    static long downTime;


    static {

        try {
            sIm = (InputManager) InputManager.class.getDeclaredMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
            MotionEvent.class.getDeclaredMethod("obtain").setAccessible(true);
            sInjectInputEventMethod =
                    InputManager.class.getMethod("injectInputEvent", InputEvent.class, Integer.TYPE);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    static void touchUp(float clientX, float clientY) {
        injectMotionEvent(KeyEvent.ACTION_UP, downTime,
                SystemClock.uptimeMillis(), clientX, clientY);
    }

    static void touchMove(float clientX, float clientY) {
        injectMotionEvent(2, downTime,
                SystemClock.uptimeMillis(), clientX, clientY);
    }

    public static void touchDown(float clientX, float clientY) {
        downTime = SystemClock.uptimeMillis();
        injectMotionEvent(KeyEvent.ACTION_DOWN, downTime, downTime, clientX,
                clientY);

    }

    static void menu() {
        sendKeyEvent(KeyEvent.KEYCODE_MENU);
    }

    static void back() {
        sendKeyEvent(KeyEvent.KEYCODE_BACK);
    }


    static void home() {
        sendKeyEvent(KeyEvent.KEYCODE_HOME);
    }


    static void injectMotionEvent(int action,
                                  long downTime, long eventTime, float x, float y) {
        MotionEvent event = MotionEvent.obtain(downTime, eventTime, action, x, y, (float) 1.0, 1.0f, 0, 1.0f, 1.0f, 0, 0);
        event.setSource(InputDeviceCompat.SOURCE_TOUCHSCREEN);
        try {
            sInjectInputEventMethod.invoke(sIm, event, 0);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }


    static void sendKeyEvent(int keyCode) {
        long now = SystemClock.uptimeMillis();
        injectKeyEvent(new KeyEvent(now, now, 0, keyCode, 0, 0, -1, 0, 0, InputDeviceCompat.SOURCE_KEYBOARD));
        injectKeyEvent(new KeyEvent(now, now, 1, keyCode, 0, 0, -1, 0, 0, InputDeviceCompat.SOURCE_KEYBOARD));
    }

    static void injectKeyEvent(KeyEvent event) {
        try {
            sInjectInputEventMethod.invoke(sIm, event, 0);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}
