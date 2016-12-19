package com.example.lasha.homework5.services;

import android.os.AsyncTask;
import android.util.Log;

import com.example.lasha.homework5.application.MyApplication;
import com.example.lasha.homework5.controller.InitializationListener;
import com.example.lasha.homework5.db.DbService;
import com.example.lasha.homework5.model.Contact;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lasha on 5/4/2015.
 */
public class ContactListLoader extends AsyncTask<URL, List, List> {
    private InitializationListener listener;
    private DbService dbService;

    public ContactListLoader(InitializationListener listener) {
        this.listener = listener;

        this.dbService = ((MyApplication) listener).getDbService();
    }

    @Override
    protected List doInBackground(URL... urls) {
        try {
            dbService.initScheme();
        } catch (SQLException e) {
            Log.e("ContactListLoader", "error initializing database", e);
        }

        boolean isContactFromDb = false;
        List<Contact> contacts = new ArrayList<>();
        if (isContactFromDb = isContactsDownloaded()) {
            contacts = getContactFromDB();
        } else {
            try {
                contacts = downloadContacts(urls[0]);
            } catch (Exception e) {
                Log.e("", "error while downloading contact list from internet", e);
            }
        }

        publishProgress(contacts);

        //run avatar loader AsyncTask
        new AvatarsLoader(listener).execute(contacts);

        if (!isContactFromDb) {
              saveContactsInDB(contacts);
        }

        return contacts;
    }

    @Override
    protected void onPostExecute(List result) {
        Log.d("AsyncTask", "onPostExecute");
    }

    @Override
    protected void onProgressUpdate(List... values) {
        Log.d("AsyncTask", "progress update");
        listener.onContactListLoaded(values[0]);
    }

    private List<Contact> downloadContacts(URL url) throws Exception {
        List<Contact> contacts = new ArrayList<>();

        JSONObject root = new JSONObject(downloadUrl(url));
        JSONArray arr = root.getJSONArray("contactList");
        for(int i = 0; i < arr.length(); i++){
            JSONObject cont = arr.getJSONObject(i);
            Contact c = new Contact();
            c.setId(cont.getLong("id"));
            c.setDisplayName(cont.getString("displayName"));
            c.setPhoneNumber(cont.getString("phoneNumber"));
            c.setAvatarUrl(cont.getString("avatarImg"));
            contacts.add(c);
        }

        return contacts;
    }

    private String downloadUrl(URL url) throws IOException {
        InputStream is = null;
        try {
            long start = System.currentTimeMillis();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("NETW", "The response is: " + response);
            is = conn.getInputStream();

            Log.d("", "downlaod contact in :" + (System.currentTimeMillis() - start));
            // Convert the InputStream into a string
            String contentAsString = new String(readIt(is));

            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public byte[] readIt(InputStream stream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int size = 0;
        while ((size = stream.read(buffer)) > 0) {
            bos.write(buffer, 0, size);
        }

        return bos.toByteArray();
    }


    private boolean isContactsDownloaded() {
        return dbService.isContactListInDB();
    }

    public List<Contact> getContactFromDB() {
        Log.d("ContactListLoader","getContactFromDB");
        return dbService.getContacts();
    }

    private void saveContactsInDB(List<Contact> contacts) {
        Log.d("ContactListLoader", "saveContactsInDB");
        dbService.saveContacts(contacts);
    }
}
