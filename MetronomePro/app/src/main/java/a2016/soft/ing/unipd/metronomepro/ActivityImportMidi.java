package a2016.soft.ing.unipd.metronomepro;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.regex.Pattern;

import a2016.soft.ing.unipd.metronomepro.data.access.layer.DataProvider;
import a2016.soft.ing.unipd.metronomepro.data.access.layer.DataProviderBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.MidiSong;

import static android.R.attr.data;

/**
 * Created by Alberto on 25/01/17.
 */

public class ActivityImportMidi extends AppCompatActivity {

    //parameters necessary for MaterialFilePicker
    private static final int PERMISSIONS_REQUEST_CODE = 0;
    private static final int FILE_PICKER_REQUEST_CODE = 1;
    //parameters for save the midi and add it into the database
    private DataProvider db;
    private File midiStorageDir = new File(Environment.getExternalStorageDirectory()+"/midiStorageDir");


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_midi);

        db = DataProviderBuilder.getDefaultDataProvider(this);

        /**
         * Control if the directory for save the midi song already exists, if don't create it
         */
        if(!midiStorageDir.exists()){
            midiStorageDir.mkdir();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Button importButton = (Button) findViewById(R.id.import_midi);
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionsAndOpenFilePicker();
            }
        });

        Button timeSliceButton = (Button) findViewById(R.id.createts);
        timeSliceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditor();
            }
        });


    }

    /**
     * Method for control if the app have the permission for read external storage and if is allowed
     * call the method openFilePicker() in the other case call the method showError()
     */
    private void checkPermissionsAndOpenFilePicker() {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                showError();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            openFilePicker();
        }
    }

    /**
     * Method for show at the user that the app don't have the necessary permissions
     */
    private void showError() {
        Toast.makeText(this, "Allow external storage reading", Toast.LENGTH_SHORT).show();
    }

    /**
     * This method override the method of the material file picker that is the library used for take
     * the absolute path of a chosen file, for other information:
     * https://github.com/nbsp-team/MaterialFilePicker
     * In this case I take the absolute path of the file, try to make a copy of it in the
     * midiStorageDir and if the copy success add the midi file into the database, in the other case
     * show an error
     */
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
                String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                if (filePath != null) {
                    Log.d("Path: ", filePath);

                    String midiTitle = filePath.substring(filePath.lastIndexOf("/")+1);
                    String midiPath = midiStorageDir.getAbsolutePath() + "/" + midiTitle;
                    if(copyFile(filePath,midiPath)){
                        Toast.makeText(this, "File copied in : " + midiPath, Toast.LENGTH_LONG).show();
                        MidiSong md = EntitiesBuilder.getMidiSong();
                        md.setName(midiTitle);
                        md.setPath(midiPath);
                        db.saveSong(md);
                    }
                    else{
                        Toast.makeText(this, "Import failed", Toast.LENGTH_LONG).show();
                    }

                }
            }
        }

    /**
     * This method is used for start the activity of the MaterialFilePicker, for other information
     * see:
     * https://github.com/nbsp-team/MaterialFilePicker
     */
    private void openFilePicker(){
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(FILE_PICKER_REQUEST_CODE)
                .withHiddenFiles(true)
                .withTitle("Device Directory")
                .start();
    }

    /**
     * This method start the activity for create a time slice song
     */
    private void openEditor() {
        Intent intent = new Intent(this, SongCreator.class);
        startActivity(intent);
    }

    /**
     * This method try to make a copy of a file into a chosen directory
     * @param from is the absolute path of the file
     * @param to is the absolute path of the directory where the file have to be copied
     * @return true if the copy succeeds, false if the copy fails
     */
    private static boolean copyFile(String from, String to) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            if (sd.canWrite()) {
                int end = from.toString().lastIndexOf("/");
                String str1 = from.toString().substring(0, end);
                String str2 = from.toString().substring(end+1, from.length());
                File source = new File(str1, str2);
                File destination= new File(to, str2);
                if (source.exists()) {
                    FileChannel src = new FileInputStream(source).getChannel();
                    FileChannel dst = new FileOutputStream(destination).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
