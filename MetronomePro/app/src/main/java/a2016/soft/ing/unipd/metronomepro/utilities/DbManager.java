package a2016.soft.ing.unipd.metronomepro.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Francesco on 08/12/2016.
 * Questa classe serve a gestire il database con le sue funzioni base quali: save, delete(?) e query
 */

public class DbManager {

    private Dbhelper myDbhelper;

    public DbManager(Context c) {
        myDbhelper = new Dbhelper(c);
    }

    /**
     * @param TrackName nome della traccia da aggiungere
     *                  questo metodo aggiunge una traccia al database, non fa controlli sull'unicità del nome
     */

    public void saveTrack(String TrackName) {
        SQLiteDatabase db = myDbhelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DatabaseStrings.FIELD_T_NAME, TrackName);
/**
 * gestione eccezioni
 */
        try {
            db.insert(DatabaseStrings.TBL_TRACCE, null, cv);
        } catch (SQLException sqle) {
        }
    }

    /**
     * @param Playlistname nome della playlist da aggiungere
     *                     questo metodo aggiunge una playlist al database, non fa controlli sull'unicità del nome
     */

    public void savePlaylist(String Playlistname) {
        SQLiteDatabase db = myDbhelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DatabaseStrings.FIELD_P_NAME, Playlistname);
/**
 * gestione eccezioni
 */
        try {
            db.insert(DatabaseStrings.TBL_PLAYLIST, null, cv);
        } catch (SQLException sqle) {
        }
    }

    /**
     * @param ID id dellatraccia che voglio eliminare
     * @return false se non viene eliminata a cause di un errore
     * <p>
     * non ho ancora deciso se ha del tutto senso mettere un metodo che cancelli dati da un database, dal momento che questo va contro la definizione
     * stessa di DB, tuttavia essendo un'app per cellulare così facendo garantiremo un utilizzo pi ottimizzato della memoria
     */
    public boolean delete(int ID) {
        SQLiteDatabase db = myDbhelper.getWritableDatabase();
        try {
            if (db.delete(DatabaseStrings.TBL_TRACCE, DatabaseStrings.FIELD_T_NAME + "=?", new String[]{Long.toString(ID)}) > 0)
                return true;
            return false;
        } catch (SQLException sqle) {
            return false;
        }
    }
}
