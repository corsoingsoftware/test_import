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

import a2016.soft.ing.unipd.metronomepro.bluetooth.BluetoothCommunicationService;
import a2016.soft.ing.unipd.metronomepro.bluetooth.Constants;

import static android.R.drawable.ic_media_pause;
import static android.R.drawable.ic_media_play;


public class MetronomeActivity extends AppCompatActivity implements View.OnClickListener {


    public static final int MIN, MAX, INITIAL_VALUE;
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    private static SoundThread clackThread;

    static {
        MIN = 30;
        MAX = 300;
        INITIAL_VALUE = 100;
    }

    /**
     * adapter per il bluetooth
     */
    private BluetoothAdapter mBluetoothAdapter;
    /**
     * Servizio di comunicazione col bluetooth
     */
    private BluetoothCommunicationService mCommService;
    private Button /*fasterButton, slowerButton,*/ fastForwardButton, backForwardButton;
    private FloatingActionButton fab;
    private final Handler mBluetoothHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothCommunicationService.STATE_CONNECTED:
                            break;
                        case BluetoothCommunicationService.STATE_CONNECTING:
                            break;
                        case BluetoothCommunicationService.STATE_LISTEN:
                            break;
                        case BluetoothCommunicationService.STATE_NONE:
                            break;
                        default:
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    /*byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    mConversationArrayAdapter.add("Me:  " + writeMessage);*/
                    break;
                case Constants.MESSAGE_READ:
                    /*byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);*/
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    /*// save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }*/
                    break;
                case Constants.MESSAGE_TOAST:
                    /*if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }*/
                    try {
                        if (msg.obj != null)
                            Snackbar.make(fab, msg.obj.toString(), Snackbar.LENGTH_LONG).show();
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;
                default:
                    //WTF??
                    break;
            }
        }
    };
    private TextView bPMTextView;
    private int actualBPM;
    private Button syncButton;

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
//        fasterButton = (Button) findViewById(R.id.button_faster);
//        slowerButton = (Button) findViewById(R.id.button_slower);
        fastForwardButton = (Button) findViewById(R.id.button_fast_forward);
        backForwardButton = (Button) findViewById(R.id.button_back_forward);
        syncButton = (Button) findViewById(R.id.sync_button);
        bPMTextView = (TextView) findViewById(R.id.number_of_BPM);
//        fasterButton.setOnClickListener(this);
//        slowerButton.setOnClickListener(this);
        fastForwardButton.setOnClickListener(this);
        backForwardButton.setOnClickListener(this);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (clackThread == null) {
            setActualBPM(INITIAL_VALUE);
            clackThread = new SoundThread(this, INITIAL_VALUE);
        }
        final Context c = this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clackThread != null && clackThread.isRun()) {
                    clackThread.setRun(false);
                    clackThread = null;
                    fab.setImageResource(ic_media_play);
                } else {
                    //clackThread.join();
                    clackThread = new SoundThread(c, actualBPM);
                    clackThread.start();
                    fab.setImageResource(ic_media_pause);
                }
            }
        });
//        playButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        inizializeBluetoothServices();
    }

    /**
     * Makes this device discoverable.
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
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

    /**
     * Inizializza il servizio bluetooth
     */
    public void inizializeBluetoothServices() {
        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            } else {
                // Initialize the BluetoothChatService to perform bluetooth connections
                mCommService = new BluetoothCommunicationService(this, mBluetoothHandler);
            }
            final Context c = this;
            syncButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //do something
                    Intent serverIntent = new Intent(c, DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                }
            });
        } catch (Exception ex) {
            syncButton.setEnabled(false);
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    //setupChat();
                    mCommService = new BluetoothCommunicationService(this, mBluetoothHandler);
                } else {

                    // User did not enable Bluetooth or an error occurred
                    //Log.d(TAG, "BT not enabled");
                    Snackbar.make(fab, getString(R.string.no_bt), Snackbar.LENGTH_SHORT);
                    //getActivity().finish();
                }
        }
    }

    /**
     * Establish connection with other divice
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mCommService.connect(device, secure);
    }


}
