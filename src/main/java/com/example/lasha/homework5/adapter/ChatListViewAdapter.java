package com.example.lasha.homework5.adapter;

import android.app.Application;
import android.graphics.BitmapFactory;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lasha on 5/23/2015.
 */
public class ChatListViewAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private List<Message> data;
    private MyApplication application;

    public ChatListViewAdapter(LayoutInflater inflater, MyApplication application) {
        this.inflater = inflater;
        this.data = new ArrayList<>();
        this.application = application;
    }

    public void setData(Message[] data) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        } else {
            this.data.clear();
        }

        for (Message message : data) {
            this.data.add(message);
        }
        notifyDataSetChanged();
    }

    public void addData(Message message) {
        this.data.add(message);
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
        return data.get(position).getTime();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        Holder holder;
        if (convertView == null) {
            view = inflater.inflate(R.layout.chat_list_item, null);

            holder = new Holder();
            holder.text = (TextView) view.findViewById(R.id.chat_list_item_name);
            holder.avatar = (ImageView) view.findViewById(R.id.chat_list_item_avatar);

            view.setTag(holder);

        } else {
            view = convertView;
            holder = (Holder) view.getTag();
        }

        Message message = (Message)getItem(position);
        holder.text.setText(message.getMessage());

        byte[] avatar = application.getAvatarByContactId(message.getContactId());
        if (message.getIsIncoming() == 0 || avatar == null) {
            holder.avatar.setImageResource(R.drawable.default_avatar);
        } else {
            holder.avatar.setImageBitmap(BitmapFactory.decodeByteArray(avatar, 0, avatar.length));
        }

        return view;
    }


    private static class Holder {
        public ImageView avatar;
        public TextView text;
    }

}
