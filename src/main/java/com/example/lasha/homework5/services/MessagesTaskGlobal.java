package com.example.lasha.homework5.services;

import android.app.Application;
import android.os.AsyncTask;

import com.example.lasha.homework5.application.MyApplication;
import com.example.lasha.homework5.db.DbService;
import com.example.lasha.homework5.generator.MessageGenerator;
import com.example.lasha.homework5.model.Message;

import java.util.Random;

/**
 * Global Task , that executes in every 1 minute, and sends message from Random USer
 *
 * Created by lasha on 5/23/2015.
 */
public class MessagesTaskGlobal extends AsyncTask<Void, Message, Void>{
    private MyApplication application;
    private DbService dbService;

    public MessagesTaskGlobal(Application application) {
        this.application = (MyApplication)application;
        dbService = this.application.getDbService();
    }

    @Override
    protected Void doInBackground(Void... params) {

        while (true) {
            try {
                Thread.sleep(1000 * 60);    //sleep 1 minute
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            Message incomingMessage = MessageGenerator.generateIncommingMessage(
                    Long.valueOf(1 + new Random().nextInt(100)),
                    null,
                    dbService);
            publishProgress(incomingMessage);

            dbService.saveMessage(incomingMessage);
        }
    }

    @Override
    protected void onProgressUpdate(Message... values) {
        Message incommingMessage = values[0];
        application.sendMessage(incommingMessage);
    }


}
