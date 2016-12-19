package com.example.lasha.homework5.controller;

import com.example.lasha.homework5.model.Contact;
import com.example.lasha.homework5.model.Message;
import com.example.lasha.homework5.model.MessagesDto;

import java.util.List;

/**
 * Created by lasha on 5/4/2015.
 */
public interface InitializationListener {
    void onContactListLoaded(List<Contact> contacts);

    void onMessageListLoaded(List<MessagesDto> messages);

    void onAvatarLoaded(Contact avatar);

}
