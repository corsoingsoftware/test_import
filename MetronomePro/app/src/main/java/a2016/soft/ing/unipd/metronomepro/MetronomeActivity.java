package a2016.soft.ing.unipd.metronomepro;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class MetronomeActivity extends AppCompatActivity {

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    private Button syncButton;
    private Button fastForwardButton;
    private Button backForwardButton;
    private TextView bPMTextView;
    private FloatingActionButton fab;
    private Button playButton;

    //   private static SoundThread clackThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metronome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fastForwardButton = (Button) findViewById(R.id.button_fast_forward);
        backForwardButton = (Button) findViewById(R.id.button_back_forward);
        syncButton = (Button) findViewById(R.id.sync_button);
        bPMTextView = (TextView) findViewById(R.id.number_of_BPM);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        playButton = (Button) findViewById(R.id.start_button);
        start();

    }

    public void start() {
        final SoundManager sm = new SoundManager();
        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sm.run();
            }
        });
    }
}
