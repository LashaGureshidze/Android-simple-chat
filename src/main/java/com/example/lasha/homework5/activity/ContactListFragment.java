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
import com.example.lasha.homework5.adapter.ContactsListViewAdapter;
import com.example.lasha.homework5.application.MyApplication;
import com.example.lasha.homework5.controller.InitializationListener;
import com.example.lasha.homework5.model.Contact;
import com.example.lasha.homework5.model.Message;
import com.example.lasha.homework5.model.MessagesDto;

import java.util.List;

/**
 * Created by lasha on 5/4/2015.
 */
public class ContactListFragment extends Fragment implements InitializationListener, AdapterView.OnItemClickListener {
    public static final String NAME_EXTRA = "display_name_extra";
    public static final String ID_EXTRA = "contact_id_extra";

    private MyApplication application;

    private ListView listView;
    private ContactsListViewAdapter adapter;

    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ContactListFragment", "onCreate");
        this.application = (MyApplication)getActivity().getApplication();
        application.addInitializationListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.contact_list_progress_bar);

        listView = (ListView) view.findViewById(R.id.contacts_list_view);
        adapter = new ContactsListViewAdapter(inflater);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        if (application.isContactListLoaded()) {
            Log.d("ContactListFragment", "set ContactList to Adapter");
            adapter.setData(application.getContactList());
            progressBar.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this.getActivity(), ChatActivity.class);

        Bundle extras = new Bundle();
        extras.putString(NAME_EXTRA, ((Contact) adapter.getItem(position)).getDisplayName());
        extras.putLong(ID_EXTRA, ((Contact) adapter.getItem(position)).getId());
        intent.putExtras(extras);

        startActivity(intent);
    }

    @Override
    public void onContactListLoaded(List<Contact> contacts) {
        Log.d("ContactListFragment", "onContactListLoaded");
        if (adapter == null || adapter.getCount() > 0) return;

        Log.d("ContactListFragment", "set ContactList to Adapter with from application");
        adapter.setData(contacts);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onMessageListLoaded(List<MessagesDto> messages) {
        //do nothing
    }

    @Override
    public void onAvatarLoaded(Contact avatar) {
        Log.d("ContactListFragment", "onAvatarLoaded()");
        adapter.updateContact(avatar);
    }
}
