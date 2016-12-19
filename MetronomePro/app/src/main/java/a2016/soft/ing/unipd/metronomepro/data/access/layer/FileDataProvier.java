package a2016.soft.ing.unipd.metronomepro.data.access.layer;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;

/**
 * Created by Portatile Lenovo on 18/12/2016.
 */

public class FileDataProvier implements DataProvider {
    private Context context;
    private int song_position = 0;

    public FileDataProvier(Context context) {
        this.context = context;
    }


    @Override
    public List<Song> getSongs() {
        return getSongs(null, null);
    }

    @Override
    public List<Song> getSongs(String searchName, Playlist playlist) {
        try {
            //chiedi tutti i file che sono nella cartella li leggo e ritorno arraylist di song
            File directory = new File(Environment.getExternalStorageDirectory().toString() + "/" + playlist.getName());
            if(!directory.exists()) {
                directory.mkdir();
            }
            File[] files = directory.listFiles();
            ArrayList<Song> arrayToReturn = new ArrayList<>();

            for (int i = 0; i < files.length; i++) {
                FileInputStream fileInputStream = new FileInputStream(searchName);
                Song song = EntitiesBuilder.getSong();
                byte[] byteToConvert = new byte[fileInputStream.available()];
                fileInputStream.read(byteToConvert);
                song.decode(byteToConvert);
                arrayToReturn.add(song);
            }
            return arrayToReturn;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Playlist> getPlaylists(String searchName) {
//        ArrayList<Song> songs = new ArrayList<Song>();
//        try {
//            FileInputStream openFileInput = new FileInputStream(searchName);
//            byte[] arrayToConvert = new byte[openFileInput.available()];
//            openFileInput.read(arrayToConvert);
//            Song s = EntitiesBuilder.getSong();
//            s.decode(arrayToConvert);
//            songs.add(s);
//        }catch(Exception e)
//        {e.printStackTrace();}
        return null;
    }

    @Override
    public void save(Song song) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root +"/"+ song.getName());


        String path = Environment.getExternalStorageDirectory().toString() + "/def-playlist";
        File myDirectory = context.getDir(path, context.MODE_PRIVATE);
        File fileToSave = new File(myDirectory, song_position + "_" + song.getName());
        song_position++;
        try {
            FileOutputStream outputStream = new FileOutputStream(fileToSave);
            outputStream.write(song.encode());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteSong(Song song) {
        File directory = new File(Environment.getExternalStorageDirectory().toString() + "/def-playlist");
        File[] files = directory.listFiles();

        File fileToDelete = new File(context.getFilesDir(), song.getName());
        fileToDelete.delete();
    }

    @Override
    public void savePlaylist(Playlist playlist) {
        File newPlaylist = new File(Environment.getExternalStorageDirectory().toString() + "/" + playlist.getName());
        newPlaylist.mkdir();
    }

    @Override
    public void deletePlaylist(Playlist playlist) {

    }
}
