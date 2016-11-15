package a2016.soft.ing.unipd.metronomepro;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import a2016.soft.ing.unipd.metronomepro.bluetooth.BluetoothChatService;
import a2016.soft.ing.unipd.metronomepro.bluetooth.Constants;
import a2016.soft.ing.unipd.metronomepro.sound.management.SoundManagerService;
import a2016.soft.ing.unipd.metronomepro.sound.management.SoundManagerServiceCaller;
import a2016.soft.ing.unipd.metronomepro.utilities.ByteLongConverter;

import static a2016.soft.ing.unipd.metronomepro.sound.management.SoundServiceConstants.MAX;
import static a2016.soft.ing.unipd.metronomepro.sound.management.SoundServiceConstants.MIN;
import static android.R.drawable.ic_media_pause;
import static android.R.drawable.ic_media_play;

/**
 * Qua dentro ci sarà un SoundManagerServiceCaller che si occuperà di chiamare il servizio
 */
public class MetronomeActivity extends AppCompatActivity implements View.OnClickListener {




    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    private static final byte ASK_NANOTIME=1;
    private static final byte REPLY_NANOTIME=2;
//    private static SoundThread clackThread;

    private boolean shouldInitializeTalking=false;
    private SoundManagerServiceCaller soundManagerServiceCaller;



   /* *//**
     * Wifi direct
     *//*
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;*/

    /**
     * adapter per il bluetooth
     */
    private BluetoothAdapter mBluetoothAdapter;

    /**
     * Servizio di comunicazione col bluetooth
     */
    private BluetoothChatService mCommService;
    private long firstNanoTime;

    private Button /*fasterButton, slowerButton,*/ fastForwardButton, backForwardButton;
    private FloatingActionButton fab;
    private final Handler mBluetoothHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            if (shouldInitializeTalking) {
                                firstNanoTime=System.nanoTime();
                                byte[] bytes=ByteLongConverter.longToBytes(firstNanoTime);
                                byte[] messageToSend=new byte[bytes.length+1];
                                messageToSend[0]=MetronomeActivity.ASK_NANOTIME;
                                System.arraycopy(bytes,0,messageToSend,1,bytes.length);
                                Snackbar.make(fab, "ask nanotime sent", Snackbar.LENGTH_INDEFINITE).show();
                                mCommService.write(messageToSend);
                            }
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                            break;
                        case BluetoothChatService.STATE_NONE:
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
                    //Chiamo il metodo in base alla risposta
                    byte[] buffer= (byte[])msg.obj;
                    byte[] message=new byte[msg.arg1];
                    System.arraycopy(buffer,0,message,0,message.length);
                    if(message.length>0){
                        byte action= message[0];
                        byte[] datas=new byte[message.length-1];
                        System.arraycopy(message,1,datas,0,datas.length);
                        switch (action) {
                            case MetronomeActivity.ASK_NANOTIME:

                                Snackbar.make(fab, "ask nanotime received", Snackbar.LENGTH_INDEFINITE).show();
                                long otherNanoTime=ByteLongConverter.bytesToLong(datas);
                                long nanoTime= System.nanoTime();
                                byte[] myNanoTimeInBytes=ByteLongConverter.longToBytes(nanoTime);
                                //byte[] nanoDifference=ByteLongConverter.longToBytes(otherNanoTime-nanoTime);
                                byte[] messageToSend= new byte[/*nanoDifference.length+*/myNanoTimeInBytes.length+1];
                                messageToSend[0]=MetronomeActivity.REPLY_NANOTIME;
                                System.arraycopy(myNanoTimeInBytes,0,messageToSend,1,myNanoTimeInBytes.length);
                                Snackbar.make(fab, "reply nanotime sent", Snackbar.LENGTH_INDEFINITE).show();
                                //System.arraycopy(nanoDifference,0,messageToSend,1+myNanoTimeInBytes.length,nanoDifference.length);
                                mCommService.write(messageToSend);
                                break;
                            case MetronomeActivity.REPLY_NANOTIME:
                                Snackbar.make(fab, "reply nanotime received", Snackbar.LENGTH_INDEFINITE).show();
                                //qua ho nei primi 8 byte di datas otherNanoTimeInBytes, negli altri ho la differenza!!
                                long actualTime=System.nanoTime();
                                long ping=actualTime-firstNanoTime;
                                byte[] otherNanoTimeInBytes=new byte[datas.length];
                                long otherNanoTimeLong=ByteLongConverter.bytesToLong(otherNanoTimeInBytes);
                                break;
                            default:
                                break;

                        }
                    }
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
        soundManagerServiceCaller.setBPM(actualBPM);
//        if (clackThread != null)
//            clackThread.setStepMillis(SoundThread.millisIntervalFromBPM(actualBPM));
        //Ricalcola ora il delay tra un clack e l'altro
        //controlla se il thread è in esecuzione
        //lo mette in pausa e cambia i millis e riparte
    }

    public void onServiceInitialized() {
        int bpm= soundManagerServiceCaller.getBPM();
        //Aggiorno la view
        this.actualBPM = bpm;
        this.bPMTextView.setText(Integer.toString(actualBPM));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metronome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        soundManagerServiceCaller= new SoundManagerServiceCaller(this);

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
//        if (clackThread == null) {
//            setActualBPM(INITIAL_VALUE);
//            clackThread = new SoundThread(this, INITIAL_VALUE);
//        }
        final Context c = this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soundManagerServiceCaller.getState()==1)
                    soundManagerServiceCaller.stop();
                else
                    soundManagerServiceCaller.play();
            }
        });

//        playButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        /*mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                System.out.println("success");
            }

            @Override
            public void onFailure(int reason) {
                System.out.println("fails");

            }
        });
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);*/
       // inizializeBluetoothServices();
    }

    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(mReceiver);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCommService != null) {
            mCommService.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mCommService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mCommService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mCommService.start();
            }
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
                mCommService = new BluetoothChatService(this, mBluetoothHandler);
            }
            final Context c = this;
            syncButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //do something
                    //Dico che deve essere questo device ad iniziare a parlare
                    shouldInitializeTalking=true;
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
                    mCommService = new BluetoothChatService(this, mBluetoothHandler);
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
