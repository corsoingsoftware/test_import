package a2016.soft.ing.unipd.metronomepro.utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Francesco on 08/12/2016.
 * Questa classe crea ed inizializza il database in SQLite
 *
 * @link http://www.html.it/pag/49180/database-e-sqlite/
 * @link http://mobilesiri.com/android-sqlite-database-tutorial-using-android-studio/
 */

 class Dbhelper extends SQLiteOpenHelper {


    public Dbhelper(Context c) {
        super(c, DatabaseStrings.DBNAME, null, DatabaseStrings.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String q = "CREATE TABLE " + DatabaseStrings.TBL_TRACCE +
                " ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                DatabaseStrings.FIELD_T_ID + " TEXT," +
                DatabaseStrings.FIELD_T_NAME + " TEXT,";
        db.execSQL(q);

        String w = "CREATE TABLE " + DatabaseStrings.TBL_PLAYLIST +
                " (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                DatabaseStrings.FIELD_P_NAME + "TEXT," +
                DatabaseStrings.FIELD_P_ID + " TEXT,";
        db.execSQL(w);
    }

    /**
     * @param db         istanza del database
     * @param oldVersion vecchia versione del database
     * @param newVersion nuova versione del database
     *                   non so ancora come realizzare questa classe ma quando chiamata deve restituire il database aggiornato
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
