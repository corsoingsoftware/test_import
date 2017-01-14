package a2016.soft.ing.unipd.metronomepro.data.access.layer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.nio.channels.NotYetConnectedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;

/**
 * Created by Francesco on 09/12/2016.
 */

public class SQLiteDataProvider extends SQLiteOpenHelper implements DataProvider {

    public SQLiteDataProvider(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public List<Song> getSongs() {
        return null;
    }

    @Override
    public List<Song> getSongs(String searchName, Playlist playlist) {
        return null;
    }

    @Override
    public List<Playlist> getPlaylists(String searchName) {
        return null;
    }

    @Override
    public void save(Song song) {

    }

    @Override
    public void deleteSong(Song song) {

    }

    @Override
    public void savePlaylist(Playlist playlist) {

    }

    @Override
    public void deletePlaylist(Playlist playlist) {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
