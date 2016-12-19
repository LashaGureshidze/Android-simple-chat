package com.example.lasha.homework5.application;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.lasha.homework5.R;
import com.example.lasha.homework5.activity.ChatActivity;
import com.example.lasha.homework5.activity.ContactListFragment;
import com.example.lasha.homework5.controller.ChatEventListener;
import com.example.lasha.homework5.controller.ChatTransport;
import com.example.lasha.homework5.controller.InitializationListener;
import com.example.lasha.homework5.db.DBHelper;
import com.example.lasha.homework5.db.DbService;
import com.example.lasha.homework5.model.Contact;
import com.example.lasha.homework5.model.Message;
import com.example.lasha.homework5.model.MessagesDto;
import com.example.lasha.homework5.services.ContactListLoader;
import com.example.lasha.homework5.services.MessagesLoader;

import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lasha on 5/1/2015.
 */
public class MyApplication extends Application implements ChatTransport, InitializationListener{
    private DbService dbService;

    private List<WeakReference<ChatEventListener>> chatEventListeners = new ArrayList<>();
    private List<WeakReference<InitializationListener>> initializationListeners = new ArrayList<>();

    private List<Contact> contactList = null;
    private List<MessagesDto> messageList = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("", "Appliation. onCreate");

        DBHelper dbHelper = new DBHelper(this, "homework5DB", 1);
        this.dbService = new DbService(dbHelper.getDB());

        try {
            new ContactListLoader(this).execute(new URL("https://dl.dropboxusercontent.com/u/28030891/FreeUni/Android/assinments/contacts.json"));
            new MessagesLoader(this).execute();

//            new MessagesTaskGlobal(this).execute();
        } catch (MalformedURLException e) {
            Log.e("", "exception while loading contact list", e);
        }
    }

    /**
     *
     * @return true if contact list has already loaded(from DB or Network)
     */
    public boolean isContactListLoaded() {
        return contactList != null;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public boolean isMessageListLoaded() {
        return messageList != null;
    }

    public List<MessagesDto> getMessageList() {
        return messageList;
    }

    public void addInitializationListener(InitializationListener listener) {
        this.initializationListeners.add(new WeakReference<InitializationListener>(listener));
    }

    public boolean removeInitializationListener(InitializationListener listener) {
        return this.initializationListeners.remove(listener);
    }

    public DbService getDbService() {
        return dbService;
    }

    /**
     * returns avatar, asociated with contact, or null , if avatar not yet loaded
     * @param contactId
     * @return
     */
    public byte[] getAvatarByContactId(Long contactId) {
        if (contactList == null || contactList.isEmpty()) return null;

        for (Contact c : contactList) {
            if (c.getId().equals(contactId)) {
                return c.getAvatar();
            }
        }

        return null;
    }

    /**
     * refresh messages list. load new message
     */
    public void refreshMessages() {
        new MessagesLoader(this).execute();
    }

    /**
     * sends message to current user
     * if ChatActivity to sender user is open, then message appears in this chat
     * otherwise Notification will be shonw
     * @param message
     */
    @Override
    public void sendMessage(Message message) {
        Log.d("Application", "sendMEssage");
        //TODO: საჭიროებს იმპლემენტაციას.

        sendMessageToChatActivity(message);

        createNotification(message);
    }

    @Override
    public void addChatEventListener(ChatEventListener chatEventListener) {
        this.chatEventListeners.add(new WeakReference<ChatEventListener>(chatEventListener));
    }

    @Override
    public boolean removeChatEventListener(ChatEventListener chatEventListener) {
        return this.chatEventListeners.remove(chatEventListener);
    }

    @Override
    public void onContactListLoaded(List<Contact> contacts) {
        this.contactList = contacts;

        for (WeakReference<InitializationListener> listener : initializationListeners) {
            if (listener.get() != null)
                listener.get().onContactListLoaded(contactList);
        }
    }

    @Override
    public void onMessageListLoaded(List<MessagesDto> messages) {
        this.messageList = messages;

        for (WeakReference<InitializationListener> listener : initializationListeners) {
            if (listener.get() != null)
                listener.get().onMessageListLoaded(messageList);
        }
    }

    @Override
    public void onAvatarLoaded(Contact avatar) {
        for (WeakReference<InitializationListener> listener : initializationListeners) {
            if (listener.get() != null)
                listener.get().onAvatarLoaded(avatar);
        }

        for (Contact c : contactList) {
            if (c.getId().equals(avatar.getId())) {
                c.setAvatar(avatar.getAvatar());
                break;
            }
        }
    }







    //show notification
    private void createNotification(Message message) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.notication)
                        .setContentTitle(message.getContactName())
                        .setContentText(message.getMessage());

        Intent resultIntent = new Intent(this, ChatActivity.class);

        Bundle extras = new Bundle();
        extras.putString(ContactListFragment.NAME_EXTRA, message.getContactName());
        extras.putLong(ContactListFragment.ID_EXTRA, message.getContactId());
        resultIntent.putExtras(extras);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ChatActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =  stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int nId = message.getContactId().intValue(); //TODO
        mNotificationManager.notify(nId, mBuilder.build());
    }

    /**
     * sends broadcast to ChatActivity
     * message will show , if chat windows belongs to user, with from message is
     * @param message
     */
    private void sendMessageToChatActivity(Message message) {
        Bundle extras = new Bundle();
        extras.putSerializable("message", message);

        Intent intent = new Intent(ChatActivity.class.getName());
        intent.putExtras(extras);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
