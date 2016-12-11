package a2016.soft.ing.unipd.metronomepro.data.access.layer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;

/**
 * Created by Francesco on 09/12/2016.
 */

public class SQLiteDataProvider extends SQLiteOpenHelper implements DataProvider {

    /**
     * creo il database con SQLopenhelper passandogli il contesto
     * @param c context
     * @Link https://developer.android.com/training/basics/data-storage/databases.html
     * @link http://www.html.it/pag/49180/database-e-sqlite/
     * @link http://blog.michelecorazza.name/android/33-leggere-sqlite-android-studio.html
     *
     */
        public SQLiteDataProvider(Context c) {
            super(c, DataProviderConstants.DBNAME, null, DataProviderConstants.DB_VERSION);
        }

    /**
     * dentro alle stringhe metto il codie sql
     * @param db passo un'istanza del database
     */
        public void onCreate(SQLiteDatabase db) {
            String q = "CREATE TABLE " + DataProviderConstants.TBL_TRACK +
                    " (" +
                    DataProviderConstants.FIELD_TRACK_NAME + " TEXT PRIMARY KEY," +
                    DataProviderConstants.FIELD_TRACK_SONG + " BLOB);";
            db.execSQL(q);

            String w = "CREATE TABLE " + DataProviderConstants.TBL_PLAYLIST +
                    " (" + DataProviderConstants.FIELD_PLAYLIST_NAME + "TEXT PRIMARY KEY);";
            db.execSQL(w);


            String e = "CREATE TABLE " + DataProviderConstants.TBL_ASSOCIATION +
                    "(" +
                    DataProviderConstants.FIELD_ASSOCIATION_SONGS + " BLOB," +
                    DataProviderConstants.FIELD_ASSOCIATION_PLAYLIST + " TEXT," +
                    "FOREIGN KEY(" + DataProviderConstants.FIELD_ASSOCIATION_SONGS + ") REFERENCES " +
                    DataProviderConstants.TBL_TRACK + "(" + DataProviderConstants.FIELD_TRACK_NAME + ")," +
                    "FOREIGN KEY(" + DataProviderConstants.FIELD_ASSOCIATION_PLAYLIST + ") REFERENCES " +
                    DataProviderConstants.TBL_PLAYLIST + "(" + DataProviderConstants.FIELD_PLAYLIST_NAME + "));";
            db.execSQL(e);
        }

    /**
     * non so ancor acome farlo
     * @param db il databse
     * @param oldVersion vecchia versione del db
     * @param newVersion nuova versione del db
     */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

    /**
     * chiama il metodo specifico con parametri null
     * @return la lista di tutte le tracce presenti nel db
     */

    public List<Song> getSongs() {
        List<Song> SongList;
        SongList = getSongs(null,null);
        return SongList;
    }

    /**
     *
     * @param searchName search parameter, ignore if null
     * @return un arraylist di tutte le cazoni con quel nome
     */
    public List<Playlist> getPlaylists(String searchName){

        if(searchName == null)
            throw new SQLException("Null parameter");

        ArrayList<Playlist> Playlist = new ArrayList<>(1);
        String query = "SELECT * FROM " + DataProviderConstants.TBL_PLAYLIST + "WHERE" +
                DataProviderConstants.FIELD_PLAYLIST_NAME + "IS"+ searchName+";";
        Cursor cursor = this.getReadableDatabase().rawQuery(query,null);
        Playlist.set(0,cursor.getString(2));
        return Playlist;
    }

    /**
     * la ricerca like sarà implementata più avanti
     * @param searchName research parameter "like" for name of songs if null=all songs
     * @param playlist to search if null=ignore this parameter
     * @return arraylist delle canzoni con esattamente quel titolo presenti el db
     */
    public List<Song> getSongs(String searchName, Playlist playlist){
        if(searchName == null|| playlist == null)
            throw new SQLException("Null parameter");
        ArrayList<Song> SongList = new ArrayList<>();
        String query = "SELECT * FROM " + DataProviderConstants.TBL_ASSOCIATION
                +"WHERE "+DataProviderConstants.FIELD_ASSOCIATION_SONGS+"IS " + searchName + "AND"
                + DataProviderConstants.FIELD_ASSOCIATION_PLAYLIST +"IS" + playlist+";";
        Cursor cursor = this.getReadableDatabase().rawQuery(query,null);
        int i =0;
        while(cursor.moveToNext()){
            SongList.set(i,cursor.getBlob(1));
            i++;
        }
        return SongList;


    }

    /**
     * memorizza la traccia nel db, il db non mi lascia inserire una traccia con nome null perchè è chiave primaria
     * @param name con cui si vuole salvare la traccia
     * @param song to memorize
     */
    public void save(String name,Song song){
        ContentValues cv = new ContentValues();
        cv.put(DataProviderConstants.TBL_TRACK+"("+DataProviderConstants.FIELD_TRACK_NAME+")", name);
        cv.put(DataProviderConstants.TBL_TRACK+"("+DataProviderConstants.FIELD_TRACK_SONG+")", song);
        this.getWritableDatabase().insertOrThrow(DataProviderConstants.TBL_TRACK,"",cv);
    }

    /**
     * cancella la canzone desiderata
     * @param song to delete
     */
    public void deleteSong(Song song){
        this.getWritableDatabase().delete(DataProviderConstants.TBL_TRACK,DataProviderConstants.FIELD_TRACK_SONG+"='"+song+"'",null);
    }

    /**
     * salva la playlist desidereata nel db, il db no mi lascia salvare una playlist con nome null o già utilizzato
     * perchè è chiave primaria
     * @param playlist to save or update!
     */
    public void savePlaylist(Playlist playlist){
        ContentValues cv = new ContentValues();
        cv.put(DataProviderConstants.TBL_PLAYLIST+"("+DataProviderConstants.FIELD_PLAYLIST_NAME+")", playlist);
        this.getWritableDatabase().insertOrThrow(DataProviderConstants.TBL_PLAYLIST,"",cv);
    }

    /**
     * elimina la playlist desiderata
     * @param playlist to delete
     */
    public void deletePlaylist(Playlist playlist){
        this.getWritableDatabase().delete(DataProviderConstants.TBL_PLAYLIST,DataProviderConstants.FIELD_PLAYLIST_NAME+
                "='"+playlist+"'",null);
    }
}
