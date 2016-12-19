package com.example.lasha.homework5.adapter;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lasha.homework5.R;
import com.example.lasha.homework5.application.MyApplication;
import com.example.lasha.homework5.model.Contact;
import com.example.lasha.homework5.model.Message;
import com.example.lasha.homework5.model.MessagesDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lasha on 5/5/2015.
 */
public class MessagesListViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<MessagesDto> data;
    private MyApplication application;

    public MessagesListViewAdapter(LayoutInflater inflater, MyApplication application) {
        this.inflater = inflater;
        this.data = new ArrayList<>();
        this.application = application;
    }

    public void setData(List<MessagesDto> data) {
        this.data = data;
        notifyDataSetChanged();
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
            view = inflater.inflate(R.layout.message_list_item, null);

            holder = new Holder();
            holder.name = (TextView) view.findViewById(R.id.message_list_item_name);
            holder.lastMessage = (TextView) view.findViewById(R.id.message_list_item_message);
            holder.avatar = (ImageView) view.findViewById(R.id.message_list_item_avatar);
            view.setTag(holder);

        } else {
            view = convertView;
            holder = (Holder) view.getTag();
        }


        MessagesDto message = (MessagesDto)getItem(position);
        holder.name.setText(message.getName());
        holder.lastMessage.setText(message.getLastMessage() == null ? "" : message.getLastMessage());

        byte[] avatar = application.getAvatarByContactId(message.getId());
        if ( avatar == null) {
            holder.avatar.setImageResource(R.drawable.default_avatar);
        } else {
            holder.avatar.setImageBitmap(BitmapFactory.decodeByteArray(avatar, 0, avatar.length));
        }

        if (message.getIsSeen() == 0) {
            view.setBackgroundColor(Color.GRAY);
        } else {
            view.setBackgroundColor(Color.WHITE);
        }

        return view;
    }


    private static class Holder {
        public ImageView avatar;
        public TextView name;
        public TextView lastMessage;
    }

}
