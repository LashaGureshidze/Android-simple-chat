package com.example.lasha.homework5.controller;

import com.example.lasha.homework5.model.Contact;
import com.example.lasha.homework5.model.Message;

import java.util.List;

/**
 * Created by lasha on 5/4/2015.
 */
public interface ChatTransport {

    /**
     *
     * @param message
     */
    void sendMessage(Message message);

    /**
     * register chatEventListener
     * @param chatEventListener
     */
    void addChatEventListener(ChatEventListener chatEventListener);
    /**
     * stop listening chat events
     * @param chatEventListener
     */
    boolean removeChatEventListener(ChatEventListener chatEventListener);
}
