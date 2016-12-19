package com.example.lasha.homework5.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.lasha.homework5.activity.ContactListFragment;
import com.example.lasha.homework5.activity.InfoFragment;
import com.example.lasha.homework5.activity.MessagesFragment;

/**
 * Created by lasha on 5/4/2015.
 */
public class FragmentsAdapter extends FragmentPagerAdapter {
    private Fragment messagesFragment, contactsFragment, infoFragment;

    public FragmentsAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 :
                if (contactsFragment == null) {
                    contactsFragment = new ContactListFragment();
                }
                return contactsFragment;
            case 1 :
                if (messagesFragment == null) {
                    messagesFragment = new MessagesFragment();
                }
                return messagesFragment;
            case 2 :
                if (infoFragment == null) {
                    infoFragment = new InfoFragment();
                }
                return infoFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
