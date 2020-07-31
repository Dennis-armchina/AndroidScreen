package com.armchina.cph.screenutils;

import android.graphics.Point;

import com.armchina.cph.screenutils.TouchUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;



public class ScreenController {
    public ScreenController(){
    }

    public static void controlScreen(final Socket controlSocket){

                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(controlSocket.getInputStream()));
                    while (true) {
                        final String line = reader.readLine();
                        if (line == null) {
                            System.out.println(">>>control socket closed");
                            break;
//                          ScreenEncoder.end = true;
                        } else {
                            //key event inject
//                            System.out.println("recall to Main Thread\n");
                            System.out.println("parse key event\n");
                            if (line.startsWith("down")) {
                                handleDown(line.substring("down".length()));
                            } else if (line.startsWith("up")) {
                                handleUp(line.substring("up".length()));
                            } else if (line.startsWith("move")) {
                                handleMove(line.substring("move".length()));
                            }
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
    }

    private static void handleDown(String substring) {
        Point point = getIndex(substring);
        if (point != null) {
            System.out.printf("down: x-%d   y-%d\n", point.x, point.y);
            TouchUtils.touchDown(point.x, point.y);
        }
    }

    private static void handleUp(String substring) {
        Point point = getIndex(substring);
        if (point != null) {
            System.out.printf("up: x-%d   y-%d\n", point.x, point.y);
            TouchUtils.touchUp(point.x, point.y);
        }
    }

    private static void handleMove(String substring) {
        Point point = getIndex(substring);
        if (point != null) {
            System.out.printf("move: x-%d   y-%d\n", point.x, point.y);
            TouchUtils.touchMove(point.x, point.y);
        }
    }

    private static Point getIndex(String sub){
        try {
            Point point = new Point();
            String[] s = sub.split("%");
            Float indexX = Float.parseFloat(s[0]);
            Float indexY = Float.parseFloat(s[1]);
            point.x = Math.round(indexX);
            point.y = Math.round(indexY);
            return point;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return null;
    }

}



