package a2016.soft.ing.unipd.metronomepro.utilities;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.nio.ByteBuffer;

/**
 * Created by feder on 08/11/2016.
 */

public class ByteLongConverter {
    /**
     * per restrocompatibilità altrimenti serve android N
     */
    public static final int LONG_SIZE=8;

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(LONG_SIZE);
        buffer.putLong(x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(LONG_SIZE);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getLong();
    }
}
