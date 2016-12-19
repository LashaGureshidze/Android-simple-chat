package com.example.lasha.homework5.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.lasha.homework5.R;
import com.example.lasha.homework5.adapter.MessagesListViewAdapter;
import com.example.lasha.homework5.application.MyApplication;
import com.example.lasha.homework5.controller.InitializationListener;
import com.example.lasha.homework5.model.Contact;
import com.example.lasha.homework5.model.Message;
import com.example.lasha.homework5.model.MessagesDto;

import java.util.List;

/**
 * Created by lasha on 5/4/2015.
 */
public class MessagesFragment extends Fragment implements InitializationListener, AdapterView.OnItemClickListener {
    private MyApplication application;

    private ProgressBar progressBar;

    private ListView listView;
    private MessagesListViewAdapter adapter;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            application.refreshMessages();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MessagesFragment", "onCreate");
        this.application = (MyApplication)getActivity().getApplication();
        application.addInitializationListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.messages_list_progress_bar);

        listView = (ListView) view.findViewById(R.id.messages_list_view);
        adapter = new MessagesListViewAdapter(inflater, application);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        if (application.isMessageListLoaded()) {
            Log.d("MessagesFragment", "set messagesList to Adapter");
            adapter.setData(application.getMessageList());
            progressBar.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onMessageListLoaded(List<MessagesDto> messages) {
        Log.d("MessagesFragment", "onMessageListLoaded");
        if (adapter == null ) return;

        Log.d("MessagesFragment", "set messageList to Adapter from application");
        adapter.setData(messages);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onContactListLoaded(List<Contact> contacts) {
        //do nothing
    }

    @Override
    public void onAvatarLoaded(Contact avatar) {
        //TODO show avatar
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        MessagesDto messagesDto = (MessagesDto)adapter.getItem(position);

        Intent intent = new Intent(this.getActivity(), ChatActivity.class);

        Bundle extras = new Bundle();
        extras.putString(ContactListFragment.NAME_EXTRA,  messagesDto.getName());
        extras.putLong(ContactListFragment.ID_EXTRA, messagesDto.getId());
        intent.putExtras(extras);

        startActivity(intent);
    }
}
