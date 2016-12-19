package com.example.lasha.homework5.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.lasha.homework5.model.Contact;
import com.example.lasha.homework5.model.Message;
import com.example.lasha.homework5.model.MessagesDto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by lasha on 5/18/2015.
 */
public class DbService {
    private SQLiteDatabase db;

    public DbService(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * create tables for first time
     */
    public void initScheme() throws SQLException{
        Log.d("DbService", "initializing database");

        db.execSQL("CREATE TABLE IF NOT EXISTS CONTACTS("
                + "id text primary key, "
                + "displayName text, "
                + "phoneNumber text, "
                + "avatarUrl text);");

        db.execSQL("CREATE TABLE IF NOT EXISTS MESSAGES("
                + "time numeric primary key, "
                + "contactId integer, "
                + "message text, "
                + "is_incoming integer, "
                + "is_seen integer);");
    }

    public void saveMessage(Message message) {
        db.beginTransaction();
        try {
            db.execSQL("insert into MESSAGES(time, contactId, message, is_incoming, is_seen) "
                        + "values ('" + message.getTime() + "', '" + message.getContactId() + "', '" + message.getMessage() + "', '" + message.getIsIncoming() + "', '" + message.getIsSeen() + " ');");
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            Log.e("", "error saving message into database ", e);
        } finally {
            db.endTransaction();
        }
    }

    public boolean isContactListInDB() {
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM CONTACTS", null);
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        return count > 0;
    }

    public void saveContacts(List<Contact> contacts) {
        db.beginTransaction();
        try {
            for (Contact c : contacts) {
                db.execSQL("insert into CONTACTS(id, displayName, phoneNumber, avatarUrl) "
                        + "values ('" + c.getId() + "', '" + c.getDisplayName() + "', '" + c.getPhoneNumber() + "', '" + c.getAvatarUrl() + " ');");
            }
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            Log.e("", "error saving contacts into database ", e);
        } finally {
            db.endTransaction();
        }
    }

    public List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();

        String[] columns = {"id", "displayName" , "phoneNumber", "avatarUrl"};
        Cursor cur = db.query("CONTACTS", columns, null, null, null, null, "id");
        while (cur.moveToNext()) {
            Contact c = new Contact();
            c.setId(cur.getLong(0));
            c.setDisplayName(cur.getString(1));
            c.setPhoneNumber(cur.getString(2));
            c.setAvatarUrl(cur.getString(3));
            contacts.add(c);
        }
        cur.close();
        return contacts;
    }

    public String getContactNameById(Long contactIdFrom) {
        Cursor c = db.rawQuery("SELECT displayName FROM CONTACTS WHERE id = " + contactIdFrom, null);
        c.moveToFirst();
        String name = c.getString(0);
        c.close();
        return name;
    }

    public Message[] getMessagesByContactId(Long contactId) {
        List<Message> messages = new ArrayList<>();

        String[] columns = {"time", "contactId" , "message", "is_incoming", "is_seen"};
        String whereClause = "contactId = ?";
        String[] whereArgs = {contactId.toString()};
        Cursor cur = db.query("MESSAGES", columns, whereClause, whereArgs, null, null, "time");
        while (cur.moveToNext()) {
            Message c = new Message();
            c.setTime(cur.getLong(0));
            c.setContactId(cur.getLong(1));
            c.setMessage(cur.getString(2));
            c.setIsIncoming(cur.getInt(3));
            c.setIsSeen(cur.getInt(4));
            messages.add(c);
        }
        cur.close();

        Message[] result = new Message[messages.size()];
        for (int i = 0; i < messages.size(); i++) {
            result[i] = messages.get(i);
        }
        return result;
    }

    public List<MessagesDto> getMessages() {
        List<MessagesDto> messages = new ArrayList<>();

        String qr = "SELECT c.id, c.displayName, m.message, m.is_seen, m.time FROM MESSAGES m INNER JOIN CONTACTS c ON m.contactId = c.id " +
                "WHERE m.time = " +
                "(SELECT MAX(mm.time) FROM MESSAGES mm WHERE mm.contactId = c.id) " +
                "ORDER BY m.time DESC";
        Cursor cur = db.rawQuery(qr, null);
        while (cur.moveToNext()) {
            MessagesDto c = new MessagesDto();
            c.setId(cur.getLong(0));
            c.setName(cur.getString(1));
            c.setLastMessage(cur.getString(2));
            c.setIsSeen(cur.getInt(3));
            messages.add(c);
        }
        cur.close();

        return messages;
    }

    public void markMessagesAsReadByContactId(Long contactId) {
        Log.d("DBService", "markMessagesAsReadByContactId");

        String qr = "UPDATE MESSAGES SET is_seen = 1 WHERE contactId = " + contactId;
        db.beginTransaction();
        try {
            db.execSQL(qr);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            Log.e("", "error saving contacts into database ", e);
        } finally {
            db.endTransaction();
        }
    }
}
