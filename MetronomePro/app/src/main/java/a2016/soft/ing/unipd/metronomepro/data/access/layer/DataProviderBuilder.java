package a2016.soft.ing.unipd.metronomepro.data.access.layer;

import android.content.Context;

/**
 * Created by feder on 09/12/2016.
 */

public class DataProviderBuilder {
    /**
     * get the default class that implements dataproviders methods
     * @return an instance for the class
     */
    public static DataProvider getDefaultDataProvider(Context c) {

        DataProvider dp = new FileDataProvier(c);
        return dp;
    }
}
