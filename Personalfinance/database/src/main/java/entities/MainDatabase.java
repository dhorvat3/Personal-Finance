package entities;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Filip on 8.11.2016..
 */
@Database(name = MainDatabase.NAME, version = MainDatabase.VERSION)
public class MainDatabase{
    public static final String NAME = "personalFinance";
    public static final int VERSION = 1;
}
