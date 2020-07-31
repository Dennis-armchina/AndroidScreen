package com.armchina.cph;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;



public class Client extends JFrame {
    static JLabel label;
    BufferedWriter controlWriter;
    boolean isMove = false;
    public Client() throws IOException {
        setLayout(new BorderLayout(0, 0));

        //set ip and port address
        JPanel ipPanel = new JPanel(new BorderLayout(5, 5));
        final JTextField ipField = new JTextField();
        //enter the ip address of the android devices
        //or use adb forward and set address to 127.0.0.1
        ipField.setText("127.0.0.1");
        ipPanel.add(ipField, BorderLayout.CENTER);

        final JTextField portField = new JTextField();
        portField.setText("8080");
        ipPanel.add(portField, BorderLayout.SOUTH);

        //set connect button
        JPanel btnPanel = new JPanel(new BorderLayout(5, 5));
        JButton btn = new JButton("Link ");
        btnPanel.add(btn, BorderLayout.CENTER);
        JPanel SubPanel = new JPanel(new BorderLayout(5, 5));
        SubPanel.add(ipPanel, BorderLayout.NORTH);
        SubPanel.add(btnPanel, BorderLayout.SOUTH);
        add(SubPanel, BorderLayout.NORTH);

        //add JLabel for display
        label = new JLabel();
        label.setBorder(new EmptyBorder(5, 5, 5, 5));
        add(label, BorderLayout.CENTER);

        //Frame Settings
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(360, 20, 800, 600);
        setTitle("Android Controller");

        //ScreenCap utility
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Socket videoSocket = new Socket(ipField.getText(), Integer.parseInt(portField.getText()));
                    Socket controlSocket = new Socket(ipField.getText(), Integer.parseInt(portField.getText()));
                    Decoder cameraDecoder = new Decoder(videoSocket);
                    System.out.print("\nvideo recording\n");

                    OutputStream outputStream = new DataOutputStream(controlSocket.getOutputStream());
                    controlWriter = new BufferedWriter(new OutputStreamWriter(controlSocket.getOutputStream()));

                    new Thread(cameraDecoder).start();

                } catch (UnknownHostException unknownHostException) {
                    unknownHostException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });


        /*Screen Touch utility
        responsible for controlling*/
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                int x = mouseEvent.getX();
                int y = mouseEvent.getY();
                System.out.printf("x-coord:%d, y-coord:%d\n", x, y);
                try {
                    String paramX = Float.toString(x * 1.0f * 1920 / label.getWidth());
                    String paramY = Float.toString(y * 1.0f * 1080 / label.getHeight());
                    controlWriter.write("down" + paramX + "%" + paramY);
                    System.out.println("down" + paramX + "%" + paramY);
                    controlWriter.newLine();
                    controlWriter.write("up" + paramX + "%" + paramY);
                    System.out.println("up" + paramX + "%" + paramY);
                    controlWriter.newLine();
                    controlWriter.flush();
                } catch (Exception e) {
                }
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                super.mouseReleased(mouseEvent);
                int x = mouseEvent.getX();
                int y = mouseEvent.getY();
                try {
                    String paramX = Float.toString(x * 1.0f * 1920 / label.getWidth());
                    String paramY = Float.toString(y * 1.0f * 1080 / label.getHeight());
                    controlWriter.write("up" + paramX + "%" + paramY);
                    System.out.println("up" + paramX + "%" + paramY + "\n");
                    controlWriter.newLine();
                    controlWriter.flush();
                    isMove = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        label.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                super.mouseDragged(mouseEvent);
                int x = mouseEvent.getX();
                int y = mouseEvent.getY();
                try {
                    String paramX = Float.toString(x * 1.0f * 1920 / label.getWidth());
                    String paramY = Float.toString(y * 1.0f * 1080 / label.getHeight());
                    if (!isMove) {
                        isMove = true;
                        controlWriter.write("down" + paramX + "%" + paramY);
                        System.out.println("down" + paramX + "%" + paramY + "\n");
                    } else {
                        controlWriter.write("move" + paramX + "%" + paramY);
                        System.out.println("move" + paramX + "%" + paramY + "\n");
                    }
                    controlWriter.newLine();
                    controlWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

        private int byteCalculate ( byte[] src){
            int sum = 0;
            int x;
            for (int i = 0; i < src.length; i++) {
                sum += byteToInt(src[i]) * Math.pow(256, src.length - i - 1);
            }
            return sum;
        }

        private static int byteToInt ( byte b){
            int x = b & 0xff;
            return x;
        }


        public static void main (String[]args) throws IOException {
            new Client().setVisible(true);
        }

    }



