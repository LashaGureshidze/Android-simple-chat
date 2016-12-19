package com.example.lasha.homework5.model;

import java.io.Serializable;

/**
 * Created by lasha on 5/5/2015.
 */
public class Message implements Serializable{

    private Long time;
    private Long contactId;
    private String message;
    private Integer isIncoming;
    private Integer isSeen;
    private String contactName;


    public Long getTime() {
        return time;
    }
    public void setTime(Long time) {
        this.time = time;
    }

    public Long getContactId() {
        return contactId;
    }
    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getIsIncoming() {
        return isIncoming;
    }
    public void setIsIncoming(Integer isIncoming) {
        this.isIncoming = isIncoming;
    }

    public Integer getIsSeen() {
        return isSeen;
    }
    public void setIsSeen(Integer isSeen) {
        this.isSeen = isSeen;
    }

    public String getContactName() {
        return contactName;
    }
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}
