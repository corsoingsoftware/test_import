package a2016.soft.ing.unipd.metronomepro.data.access.layer;


import android.database.sqlite.SQLiteOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.PlayableSong;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;


/**
 * Created by Anthony Ferranti
 * Adapted by Federico Favotto
 *
 * da federico: rivedere la classe, mal strutturata non rispetta le interfacce e non funziona benissimo
 */

public class SQLiteDataProviderGroup extends SQLiteOpenHelper {


    // LogCat Tag
    private static final String LOG = "SQLInstructionExecuted:";

    // Database Version
    private static final int VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "MidiMetroDbManager";

    // Tables Names
    private static final String TABLE_TRACKS = "tracks";
    private static final String TABLE_LISTS = "lists";
    private static final String TABLE_LISTS_TRACKS = "lists_tracks";

    // Common column names
    private static final String KEY_ID = "id";

    // TRACKS Table - column names
    private static final String KEY_NAME_TRACK = "name";
    private static final String KEY_INFO_TRACK = "info";

    // LISTS Table - column names
    private static final String KEY_NAME_LIST = "name";

    // LISTS_TRACKS Table - column names
    private static final String KEY_TRACK_ID = "track_id";
    private static final String KEY_LIST_ID = "list_id";

    // Table Create Statement

    //TRACKS table create statement
    private static final String CREATE_TABLE_TRACK = "CREATE TABLE "
            + TABLE_TRACKS + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME_TRACK
            + " TEXT, " + KEY_INFO_TRACK + " BLOB" + ")";

    // LISTS table create statement
    private static final String CREATE_LIST_TABLE = "CREATE TABLE "
            + TABLE_LISTS + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME_LIST
            + " TEXT" + ")";

    // LISTS_TRACKS table create statement
    private static final String CREATE_TABLE_LISTS_TRACKS = "CREATE TABLE "
            + TABLE_LISTS_TRACKS + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_TRACK_ID
            + " INTEGER, " + KEY_LIST_ID + " INTEGER" + ")";


    public SQLiteDataProviderGroup(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating required tables
        db.execSQL(CREATE_TABLE_TRACK);
        db.execSQL(CREATE_LIST_TABLE);
        db.execSQL(CREATE_TABLE_LISTS_TRACKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // On Upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTS_TRACKS);

        // Create new tables
        onCreate(db);
    }

    /*
    * Creating a track
    */
    public long createTrack(Song track) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME_TRACK, track.getName());

        // Insert row
        long track_id = db.insert(TABLE_TRACKS, null, values);

//        // Assigning playList to track
//        for (long playList_id : playList_ids) {
//            createPlayListTrack(track_id, playList_id);
//        }

        return track_id;
    }

    /*
    * Get single track
    */
    public Song getTrack(long track_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_TRACKS + "WHERE "
                + KEY_ID + " = " + track_id;

        Log.i(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Song track = EntitiesBuilder.getSong();
        track.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        track.setName(c.getString(c.getColumnIndex(KEY_NAME_TRACK)));
        track.decode(c.getBlob(c.getColumnIndex(KEY_INFO_TRACK)));
        return track;
    }

    /*
    * Getting all track
    */
    public List<Song> getAllTrack() {
        List<Song> tracks = new ArrayList<Song>();
        String selectQuery = "SELECT * FROM " + TABLE_TRACKS;

        Log.i(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // Looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Song track = EntitiesBuilder.getSong();
                track.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                track.setName(c.getString(c.getColumnIndex(KEY_NAME_TRACK)));
                track.decode(c.getBlob(c.getColumnIndex(KEY_INFO_TRACK)));
                // Adding to track list
                tracks.add(track);
            } while (c.moveToNext());
        }
        return tracks;
    }

    /*
    * Getting all track under single list
    */
    public List<Song> getAllTrackByList(String tag_list) {
        List<Song> tracks = new ArrayList<Song>();
//da Federico: fare una query decente appena possibile. Orrore
        String selectQuery = "SELECT * FROM " + TABLE_TRACKS + " td, "
                + TABLE_LISTS + " tg, " + TABLE_LISTS_TRACKS + " tt WHERE tg."
                + KEY_NAME_LIST + " = '" + tag_list + "'" + " AND tg." + KEY_ID
                + " = " + "tt. " + KEY_LIST_ID + " AND td." + KEY_ID + " = "
                + "tt. " + KEY_TRACK_ID;

        Log.i(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // Looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Song track = EntitiesBuilder.getSong();
                track.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                track.setName(c.getString(c.getColumnIndex(KEY_NAME_TRACK)));
                track.decode(c.getBlob(c.getColumnIndex(KEY_INFO_TRACK)));

                // Add to track list
                tracks.add(track);
            } while (c.moveToNext());
        }

        return tracks;
    }

    /*
    * UpDating a track
    */

    public int updateTrack(Song track) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME_TRACK, track.getName());
        values.put(KEY_INFO_TRACK, track.encode());

        // UpDating row
        return db.update(TABLE_TRACKS, values, KEY_ID + " = ?", new String[]{String.valueOf(track.getId())});
    }

    /*
    * Deleting a track
    */
    public void deleteTrack(long track_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRACKS, KEY_ID + " = ?", new String[]{String.valueOf(track_id)});
    }

    /*
    * Creating a playList
    */
    public long createList(Playlist playList) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME_LIST, playList.getName());

        // Insert row
        long playList_id = db.insert(TABLE_LISTS, null, values);

        return playList_id;
    }

    /*
    * Getting all PlayList
    */
    public List<Playlist> getAllPlayList() {
        List<Playlist> playLists = new ArrayList<Playlist>();

        String selectQuery = "SELECT * FROM " + TABLE_LISTS;

        Log.i(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // Looping through all row and adding to list
        if (c.moveToFirst()) {
            do {
                /*PlayList playList = new PlayList();
                playList.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                playList.setName(c.getString(c.getColumnIndex(KEY_NAME_LIST)));
                playList.setTotalSize(c.getString(c.getColumnIndex(KEY_TOTAL_SIZE)));
                playList.setTotalDuration(c.getString(c.getColumnIndex(KEY_TOTAL_DURATION)));

                // Looping through all rows and adding to list
                playLists.add(playList);*/
            } while (c.moveToNext());
        }

        return playLists;
    }

    /*
    * Updating a playList
    */
    public int updatePlayList(Playlist playList) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME_LIST, playList.getName());
//todo correct:
        // Updating row
        return db.update(TABLE_LISTS, values, KEY_ID + " = ?", new String[]{String.valueOf(0/*playList.getId()*/)});
    }

    public void deletePlayList(Playlist playList, boolean delete_all_trackc) {
        SQLiteDatabase db = this.getWritableDatabase();
//todo rivedi metodo!
        // Before deleting playList check if there are tracks under playList that should be also deleted
        if (delete_all_trackc) {
            List<Song> allTrackOfList = getAllTrackByList(playList.getName());

            // Delete all tracks
            /*for (Track track : allTrackOfList) {

                // Delete track
                deleteTrack(track.getId());
            }*/

        }

        // Now delete list//// TODO: 09/01/2017  rivedi metodo
        db.delete(TABLE_LISTS, KEY_ID + " = ?", new String[]{String.valueOf(0/*playList.getId()*/)});
    }

    /*
    * Creating track_playList
    */
    public long createPlayListTrack(long track_id, long playList_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TRACK_ID, track_id);
        values.put(KEY_LIST_ID, playList_id);

        long id = db.insert(TABLE_LISTS_TRACKS, null, values);

        return id;
    }

    /*
    * Updating a track_playList
    */
    public int updatePlayListTrack(long id, long track_id, long playList_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LIST_ID, playList_id);
        values.put(KEY_TRACK_ID, track_id);

        // Updating row
        return db.update(TABLE_LISTS_TRACKS, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }

    /*
    *Closing database
    */
    public void closeDb() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
