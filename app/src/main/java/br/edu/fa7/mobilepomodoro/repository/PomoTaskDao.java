package br.edu.fa7.mobilepomodoro.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import br.edu.fa7.mobilepomodoro.model.PomoTask;

/**
 * Created by erinaldo.souza on 02/06/2016.
 */
public class PomoTaskDao extends GenericDao<PomoTask> {

    private static final String TABLE_NAME = "task";
    public PomoTaskDao(Context context) {
        super(context, TABLE_NAME);
    }

    @Override
    public PomoTask getObjectFromCursor(Cursor cursor) {
        PomoTask task = new PomoTask(
                    cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getInt(cursor.getColumnIndex("quantity")),
                    cursor.getString(cursor.getColumnIndex("detail")));

        return task;
    }

    @Override
    public ContentValues getContenValues(PomoTask obj) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", obj.getId());
        contentValues.put("name", obj.getName());
        contentValues.put("quantity", obj.getQuantity());
        contentValues.put("detail", obj.getDetail());
        return contentValues;
    }
}
