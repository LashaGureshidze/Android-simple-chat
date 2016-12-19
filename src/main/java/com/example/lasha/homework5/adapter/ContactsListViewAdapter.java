package com.example.lasha.homework5.adapter;

import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lasha.homework5.R;
import com.example.lasha.homework5.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lasha on 5/4/2015.
 */
public class ContactsListViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Contact> data;

    public ContactsListViewAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
        this.data = new ArrayList<>();
    }

    public void setData(List<Contact> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void updateContact(Contact avatar) {
        for (Contact c : data) {
            if (c.getId().equals(avatar.getId())) {
                c.setAvatar(avatar.getAvatar());
                notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        Holder holder;
        if (convertView == null) {
            view = inflater.inflate(R.layout.contact_list_item, null);

            holder = new Holder();
            holder.name = (TextView) view.findViewById(R.id.contact_list_item_name);
            holder.avatar = (ImageView) view.findViewById(R.id.contact_list_item_avatar);

            view.setTag(holder);

        } else {
            view = convertView;
            holder = (Holder) view.getTag();
        }

        Contact contact = (Contact)getItem(position);
        holder.name.setText(contact.getDisplayName());
        if (contact.getAvatar() == null) {
            holder.avatar.setImageResource(R.drawable.default_avatar);
        } else {
            holder.avatar.setImageBitmap(BitmapFactory.decodeByteArray(contact.getAvatar(), 0, contact.getAvatar().length));
        }

        return view;
    }


    private static class Holder {
        public ImageView avatar;
        public TextView name;
    }

}
