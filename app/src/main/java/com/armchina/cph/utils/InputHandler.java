package com.armchina.cph.utils;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;


public class InputHandler extends IoHandlerAdapter {

    public InputHandler() {
        super();
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        System.out.println("sessionCreated");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
        System.out.println("sessionIdle");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
        System.out.println("exceptionCaught:" + cause.toString());
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
        System.out.println("messageSent");
    }

    @Override
    public void inputClosed(IoSession session) throws Exception {
        super.inputClosed(session);
        System.out.println("inputClosed");
    }

//    @Override
//    public void event(IoSession session, FilterEvent event) throws Exception {
//        super.event(session, event);
//        System.out.println("event");
//    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
        System.out.println("sessionOpened");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        System.out.println("sessionClosed");
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);
        System.out.println("messageReceived:" + message.toString());
    }
}
