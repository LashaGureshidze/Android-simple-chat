package com.example.lasha.homework5.services;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.example.lasha.homework5.activity.ChatActivity;
import com.example.lasha.homework5.adapter.ChatListViewAdapter;
import com.example.lasha.homework5.application.MyApplication;
import com.example.lasha.homework5.db.DbService;
import com.example.lasha.homework5.model.Message;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by lasha on 5/23/2015.
 */
public class MessageByContactLoader extends AsyncTask<Long, Message, Void> {
    private WeakReference<ChatListViewAdapter> adapterWeakReference;
    private DbService dbService;

    public MessageByContactLoader(WeakReference<ChatListViewAdapter> adapterWeakReference, DbService dbService) {
        this.adapterWeakReference = adapterWeakReference;
        this.dbService = dbService;
    }

    @Override
    protected Void doInBackground(Long... longs) {
        Message[] messages = dbService.getMessagesByContactId(longs[0]);

        publishProgress(messages);

        if (messages.length > 1) {
            markMessagesAsReadByContactId(messages[0].getContactId());
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Message... values) {
        if (adapterWeakReference.get() == null) return;

        ChatListViewAdapter adapter = adapterWeakReference.get();
        adapter.setData(values);
    }

    private void markMessagesAsReadByContactId(final Long contactId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                dbService.markMessagesAsReadByContactId(contactId);
            }
        }).start();
    }
}
