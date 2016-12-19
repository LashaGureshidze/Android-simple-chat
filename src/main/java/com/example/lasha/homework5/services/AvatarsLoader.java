package com.example.lasha.homework5.services;

import android.os.AsyncTask;
import android.util.Log;

import com.example.lasha.homework5.application.MyApplication;
import com.example.lasha.homework5.controller.InitializationListener;
import com.example.lasha.homework5.model.Contact;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by lasha on 5/14/2015.
 */
public class AvatarsLoader extends AsyncTask<List, Contact, Void>{

    private InitializationListener listener;

    public AvatarsLoader(InitializationListener listener) {
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(List... contacts) {
        File filesDir = ((MyApplication)listener).getFilesDir();

        byte[] avatar = null;
        File file;
        for (Contact c : (List<Contact>)contacts[0]) {
            file = new File(filesDir, c.getId()+".png");
            if (file.exists()) {
                avatar = getAvatarFromFile(file);
            } else {
                avatar = downloadAvatar(filesDir, c.getId(), c.getAvatarUrl());
            }

            if (avatar != null) {
                c.setAvatar(avatar);
                publishProgress(c);
            }
        }
        return null;
    }



    @Override
    protected void onProgressUpdate(Contact... values) {
        listener.onAvatarLoaded(values[0]);
    }

    private byte[] downloadAvatar(File filesDir, Long id, String avatarUrl) {
        Log.d("AvatarsLoader", "downloading avatar from network: "+avatarUrl);
        FileOutputStream out = null;
        InputStream in = null;
        ByteArrayOutputStream outStream = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(avatarUrl).openConnection();
            connection.setReadTimeout(10000 /* milliseconds */);
            connection.setConnectTimeout(15000 /* milliseconds */);
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() != 200) {
                return null;
            }

            in = connection.getInputStream();
            File fileToWrite = new File(filesDir, id + ".png");
            out = new FileOutputStream(fileToWrite);

            outStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int read = 0;
            while ((read = in.read(bytes)) > 0) {
                outStream.write(bytes, 0, read);
                out.write(bytes, 0, read);
            }

            return outStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private byte[] getAvatarFromFile(File file) {
        Log.d("AvatarsLoader", "loading avatar from file: "+file.getAbsolutePath());
        ByteArrayOutputStream outStream = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);

            outStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int read = 0;
            while ((read = in.read(bytes)) > 0) {
                outStream.write(bytes, 0, read);
            }

            return outStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (outStream != null) {
                    outStream.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }


        return null;
    }
}
