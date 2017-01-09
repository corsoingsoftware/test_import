package a2016.soft.ing.unipd.metronomepro.entities;



import java.util.List;

/**
 * Created by feder on 07/12/2016.
 */

public interface Song extends List<TimeSlice>, Streamable {

    String getName();
    void setName(String name);
    long getId();
    void setId(long id);
}
