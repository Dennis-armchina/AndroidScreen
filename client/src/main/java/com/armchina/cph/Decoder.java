package com.armchina.cph;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;


import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.Java2DFrameUtils;

import javax.imageio.ImageIO;
import javax.swing.*;


public class Decoder implements Runnable{
    private final String LOCK = "LOCK";
    private Socket cameraSocket;
    private static BufferedImage bufferedImage;
    Decoder(Socket socket) throws IOException{
        this.cameraSocket = socket;
    }
    String getLOCK(){
        return LOCK;
    }

    @Override
    public void run(){
        try{
            InputStream cameraStream = cameraSocket.getInputStream();
            System.out.print("get videoSocket...\n");
            FFmpegFrameGrabber  frameGrabber = new FFmpegFrameGrabber(cameraStream);
            frameGrabber.setFrameRate(60);
            frameGrabber.setFormat("h264");
            frameGrabber.setVideoBitrate(1000000);
            frameGrabber.setVideoOption("preset", "ultrafast");
            System.out.print("display...\n");
            frameGrabber.start(false);
            System.out.print("start\n");
            while (true){
                Frame frame = frameGrabber.grab();
                System.out.print("grab frame...\n");
                System.out.print(frame);
                Java2DFrameConverter converter = new Java2DFrameConverter();
                BufferedImage bufferedImage = converter.convert(frame);
                setBufferedImage(bufferedImage);
                Client.label.setIcon(new ScaleIcon(new ImageIcon(bufferedImage)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setBufferedImage(BufferedImage image) {
        bufferedImage = image;
    }
    BufferedImage getBufferedImage(){
        return bufferedImage;
    }
}

