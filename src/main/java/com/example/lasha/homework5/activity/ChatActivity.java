package com.example.lasha.homework5.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.lasha.homework5.R;
import com.example.lasha.homework5.adapter.ChatListViewAdapter;
import com.example.lasha.homework5.application.MyApplication;
import com.example.lasha.homework5.model.Message;
import com.example.lasha.homework5.services.MessageByContactLoader;
import com.example.lasha.homework5.services.MessageTask;

import java.lang.ref.WeakReference;
import java.util.Date;

/**
 * Created by lasha on 5/5/2015.
 */
public class ChatActivity extends ActionBarActivity {
    private boolean isForeground;

    private IntentFilter receiveFilter;


    private Long contactId;
    private String contactName;

    private EditText textField;

    private ListView listView;
    private ChatListViewAdapter adapter;
    private ProgressBar progressBar;

    private BroadcastReceiver handler = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("BroadCastReceiver", "onReceive()");
            Bundle extras = intent.getExtras();
            Message message = (Message) extras.getSerializable("message");
            if (contactId.equals(message.getContactId()) && isForeground) {
                adapter.addData(message);
                markMessagesAsReadByContactId(message.getContactId());
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ChatActivity", "OnCreate");

        setContentView(R.layout.activity_chat);

        //register broadcast receiver, to handle incoming messages
        receiveFilter = new IntentFilter(getClass().getName());
        LocalBroadcastManager.getInstance(this).registerReceiver(handler, receiveFilter);

        //initialize view elements
        textField = (EditText) findViewById(R.id.chat_text_area);

        Bundle extras = getIntent().getExtras();
        contactName = extras.getString(ContactListFragment.NAME_EXTRA);
        contactId = extras.getLong(ContactListFragment.ID_EXTRA);

        getSupportActionBar().setTitle(contactName);

        progressBar = (ProgressBar)findViewById(R.id.chat_list_progress_bar);

        listView = (ListView)findViewById(R.id.chat_list_view);
        adapter = new ChatListViewAdapter(getLayoutInflater(), (MyApplication) getApplication());
        listView.setAdapter(adapter);

        //load messages
        new MessageByContactLoader(
                new WeakReference<ChatListViewAdapter>(adapter),
                ((MyApplication)getApplication()).getDbService())
                .execute(contactId);
    }

    @Override
    protected void onResume() {
        super.onResume();

        isForeground = true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        isForeground = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSendClick(View v) {
        if (v.getId() == R.id.chat_send_button) {
            String text = textField.getText().toString();
            if ( text.isEmpty()) return;

            Log.d("ChatActivity", "onSendMessage");
            Message message = new Message();
            message.setTime(new Date().getTime());
            message.setContactId(contactId);
            message.setIsIncoming(0);
            message.setIsSeen(1);
            message.setMessage(text);
            message.setContactName(contactName);

            adapter.addData(message);
            new MessageTask(getApplication()).execute(message);

            //
            textField.setText("");
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(textField.getWindowToken(), 0);
        };
}

    private void markMessagesAsReadByContactId(final Long contactId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ((MyApplication)getApplication()).getDbService().markMessagesAsReadByContactId(contactId);
            }
        }).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("ChatActivity", "OnStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ChatActivity", "onDestroy");
    }
}
