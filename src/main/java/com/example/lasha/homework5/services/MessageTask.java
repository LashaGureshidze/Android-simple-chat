package com.example.lasha.homework5.services;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.example.lasha.homework5.application.MyApplication;
import com.example.lasha.homework5.db.DbService;
import com.example.lasha.homework5.generator.MessageGenerator;
import com.example.lasha.homework5.model.Message;

import java.util.Date;
import java.util.Random;

/**
 * Task, that executes when current user sends message to another, this task waits for 0-10 seconds, and send message back to current user
 *
 * Created by lasha on 5/21/2015.
 */
public class MessageTask extends AsyncTask<Message, Message, Void> {

    private MyApplication application;
    private DbService dbService;

    public MessageTask(Application application) {
        this.application = (MyApplication)application;
        dbService = this.application.getDbService();
    }

    @Override
    protected Void doInBackground(Message... params) {


        dbService.saveMessage(params[0]);

        try {
            Thread.sleep((long) new Random().nextInt(10000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Message incomingMessage = MessageGenerator.generateIncommingMessage(params[0].getContactId(), params[0].getContactName(), null);
        publishProgress(incomingMessage);

        dbService.saveMessage(incomingMessage);
        return null;
    }

    @Override
    protected void onProgressUpdate(Message... values) {
        Message incommingMessage = values[0];
        application.sendMessage(incommingMessage);
    }
}
