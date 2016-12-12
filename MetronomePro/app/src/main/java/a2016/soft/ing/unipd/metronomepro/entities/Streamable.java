package a2016.soft.ing.unipd.metronomepro.entities;

/**
 * Created by feder on 07/12/2016.
 */

public interface Streamable {
    byte[] encode();
    void decode(byte[] toDecode);
}
