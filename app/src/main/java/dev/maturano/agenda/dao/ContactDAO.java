package dev.maturano.agenda.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dev.maturano.agenda.constant.DatabaseConstants;
import dev.maturano.agenda.model.Contact;

public class ContactDAO {
    private Context context;
    private DatabaseDAO dao;

    public ContactDAO(Context context) {
        this.context = context;
    }

    public long insertContact(Contact c) {
        this.dao = new DatabaseDAO(this.context);
        ContentValues cv = getContactCV(c);
        SQLiteDatabase db = this.dao.getWritableDatabase();
        long insertResult = db.insert(DatabaseConstants.TABLE_CONTACT, null, cv);
        db.close();
        this.dao.close();
        Log.i("myApp", DatabaseConstants.TABLE_CONTACT.concat(" inserir= ").concat(Long.toString(insertResult)));
        return insertResult;
    }

    public long updateContact(Contact c) {
        this.dao = new DatabaseDAO(this.context);
        ContentValues cv = getContactCV(c);
        SQLiteDatabase db = this.dao.getWritableDatabase();
        String[] params = {Long.toString(c.getId())};
        long updateResult = db.update(DatabaseConstants.TABLE_CONTACT, cv, "id=?", params );
        db.close();
        this.dao.close();
        Log.i("myApp", DatabaseConstants.TABLE_CONTACT.concat(" update= ").concat(Long.toString(updateResult)));
        return updateResult;
    }

    private ContentValues getContactCV(Contact c) {
        ContentValues cv = new ContentValues();
        cv.put("nome", c.getName());
        cv.put("email", c.getEmail());
        cv.put("telefone", c.getPhone());
        return cv;
    }

    public List<Contact> getContactList() {
        List<Contact> contactList = new ArrayList<Contact>();
        String sql = "SELECT * FROM " + DatabaseConstants.TABLE_CONTACT;
        this.dao = new DatabaseDAO(this.context);
        SQLiteDatabase db = this.dao.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Contact contact = new Contact();
            contact.setId(cursor.getInt(cursor.getColumnIndex("id")));
            contact.setName(cursor.getString(cursor.getColumnIndex("nome")));
            contact.setPhone(cursor.getString(cursor.getColumnIndex("telefone")));
            contact.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            contactList.add(contact);
            Log.i("myApp", DatabaseConstants.TABLE_CONTACT.concat(" get contato = ").concat(contact.toString()));
        }
        cursor.close();
        db.close();
        this.dao.close();
        return contactList;
    }
}