package a2016.soft.ing.unipd.metronomepro;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Set;
import java.util.UUID;

import a2016.soft.ing.unipd.metronomepro.bluetooth.CommunicationThread;


public class MetronomeActivity extends AppCompatActivity implements View.OnClickListener {


    public static final int MIN, MAX, INITIAL_VALUE;
    private static SoundThread clackThread;

    static {
        MIN = 30;
        MAX = 300;
        INITIAL_VALUE = 100;
    }

    private Button fasterButton, slowerButton, fastForwardButton, backForwardButton;
    private TextView bPMTextView;
    private int actualBPM;

    public int getActualBPM() {
        return actualBPM;
    }

    public void setActualBPM(int actualBPM) {
        this.actualBPM = actualBPM;
        this.bPMTextView.setText(Integer.toString(actualBPM));
        if (clackThread != null)
            clackThread.setStepMillis(SoundThread.millisIntervalFromBPM(actualBPM));
        //Ricalcola ora il delay tra un clack e l'altro
        //controlla se il thread è in esecuzione
        //lo mette in pausa e cambia i millis e riparte
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metronome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fasterButton = (Button) findViewById(R.id.button_faster);
        slowerButton = (Button) findViewById(R.id.button_slower);
        fastForwardButton = (Button) findViewById(R.id.button_fast_forward);
        backForwardButton = (Button) findViewById(R.id.button_back_forward);
        bPMTextView = (TextView) findViewById(R.id.number_of_BPM);
        fasterButton.setOnClickListener(this);
        slowerButton.setOnClickListener(this);
        fastForwardButton.setOnClickListener(this);
        backForwardButton.setOnClickListener(this);
        FloatingActionButton playButton = (FloatingActionButton) findViewById(R.id.fab);
        if (clackThread == null) {
            setActualBPM(INITIAL_VALUE);
            clackThread = new SoundThread(this, INITIAL_VALUE);
        }
        playButton.setOnClickListener(clackThread);
        BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = bt.getBondedDevices();
        UUID sessionUUID = UUID.randomUUID();
        BluetoothSocket bs = null;
        for (BluetoothDevice bd : pairedDevices) {
            try {
                bs = bd.createInsecureRfcommSocketToServiceRecord(sessionUUID);
            } catch (Exception ex) {

            }
        }
        CommunicationThread ct = new CommunicationThread(bs);
        ct.start();
        try {
            ct.write(new byte[]{2, 4});

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
//        playButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            Button b = (Button) v;
            int toAdd = 0;
            switch (b.getId()) {

                case R.id.button_faster:
                    toAdd = 1;
                    break;

                case R.id.button_slower:
                    toAdd = -1;
                    break;

                case R.id.button_fast_forward:
                    toAdd = 10;
                    break;

                case R.id.button_back_forward:
                    toAdd = -10;
                    break;

            }
            setActualBPM(Math.max(Math.min(getActualBPM() + toAdd, MAX), MIN));
        }
    }
    public void checkBT() {
        BluetoothAdapter myBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!myBtAdapter.isEnabled())
            System.err.print("BT non attivato");
    }
    public void visibile(){
        Intent discoverability = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivity(discoverability);
    }

}
