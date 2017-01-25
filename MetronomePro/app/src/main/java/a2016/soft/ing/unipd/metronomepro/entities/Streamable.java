package a2016.soft.ing.unipd.metronomepro.entities;

/**
 * Created by Federico Favotto on 07/12/2016.
 */

public interface Streamable {
    byte[] encode();
    void decode(byte[] toDecode);

    /**
     * It provides the size of byte array to decode or the size of byte array to decode
     * @return the stream size in int
     */
    int getStreamedSize();
}
