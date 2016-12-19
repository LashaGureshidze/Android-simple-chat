package com.example.lasha.homework5.model;

import android.content.IntentFilter;

/**
 * Created by lasha on 5/23/2015.
 */
public class MessagesDto {
    private Long id;
    private String name;
    private String lastMessage;
    private Integer isSeen;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Integer getIsSeen() {
        return isSeen;
    }
    public void setIsSeen(Integer isSeen) {
        this.isSeen = isSeen;
    }
}
