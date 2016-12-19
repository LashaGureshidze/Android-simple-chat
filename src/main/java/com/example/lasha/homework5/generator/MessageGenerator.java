package com.example.lasha.homework5.generator;

import com.example.lasha.homework5.db.DbService;
import com.example.lasha.homework5.model.Message;

import java.util.Date;

/**
 * Created by lasha on 5/22/2015.
 */
public class MessageGenerator {


    /**
     * generate random message
     * @param contactIdFrom
     * @param contactName contact name. if it is null, then must provide DBService, to recive name from database
     * @param dbService
     * @return
     */
    public static Message generateIncommingMessage(Long contactIdFrom, String contactName, DbService dbService) {
        //TODO crete good generator
        Message message = new Message();
        message.setTime(new Date().getTime());
        message.setContactId(contactIdFrom);
        message.setIsIncoming(1);
        message.setIsSeen(0);
        message.setMessage("shemtxveviti shetyobineba?!111");
        if (contactName != null) {
            message.setContactName(contactName);
        } else {
            message.setContactName(dbService.getContactNameById(contactIdFrom));
        }

        return message;
    }
}
