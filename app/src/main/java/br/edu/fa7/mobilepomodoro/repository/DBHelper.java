package br.edu.fa7.mobilepomodoro.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by erinaldo.souza on 02/06/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "MobilePomodoro.db";
    private Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }


    /**
     * Cria ou atualiza o banco de dados dependente do nome do aarquivo passado por parâmetro
     *
     * @since  02-06-2016
     * @param fileName
     * @param db
     */
    private void createDB(String fileName, SQLiteDatabase db) {
        try {
            InputStream is = context.getAssets().open(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while((line = br.readLine()) != null) {
                db.execSQL(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cria o banco de dados
     *
     *@since 02-06-2016
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        createDB("db/CREATE_DB.sql", db);
    }

    /**
     * Destroi e recria o banco de dados
     *
     * @since 02-06-2016
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        createDB("db/UPDATE_DB.sql", db);
        onCreate(db);
    }
}