package a2016.soft.ing.unipd.metronomepro.bluetooth;

import android.os.Handler;
import android.os.Message;

/**
 * Created by feder on 08/11/2016.
 */

public class BluetoothServiceHandler extends Handler {

    Handler[] mHandlers;
    Handler exceptionHandler;

    public BluetoothServiceHandler(Handler[] handlers, Handler exceptionHandler)
    {
        mHandlers=handlers;
        this.exceptionHandler=exceptionHandler;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(mHandlers!=null) {
            if (msg.what < mHandlers.length && msg.what > 0) {
                mHandlers[msg.what].obtainMessage(msg.arg1, -1, -1, msg.obj)
                        .sendToTarget();
            } else {
                exceptionHandler.obtainMessage(msg.arg1, -1, -1, msg.obj)
                        .sendToTarget();
            }
        }
    }
}
