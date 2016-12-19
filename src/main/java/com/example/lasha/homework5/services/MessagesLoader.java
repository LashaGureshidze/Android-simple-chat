package com.example.lasha.homework5.services;

import android.os.AsyncTask;

import com.example.lasha.homework5.application.MyApplication;
import com.example.lasha.homework5.controller.InitializationListener;
import com.example.lasha.homework5.db.DbService;
import com.example.lasha.homework5.model.MessagesDto;

import java.util.List;

/**
 * Created by lasha on 5/23/2015.
 */
public class MessagesLoader extends AsyncTask<Void, List, Void> {
    private InitializationListener listener;
    private DbService dbService;

    public MessagesLoader(InitializationListener listener) {
        this.listener = listener;
        this.dbService = ((MyApplication)listener).getDbService();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        List<MessagesDto> messages = dbService.getMessages();

        publishProgress(messages);
        return null;
    }

    @Override
    protected void onProgressUpdate(List... values) {
        listener.onMessageListLoaded(values[0]);
    }
}
