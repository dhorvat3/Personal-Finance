package core;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by dagy on 27.01.17..
 *
 * Class for defining main database object
 * Used to setup database name and version.
 */
@Database(name = MainDatabase.NAME, version = MainDatabase.VERSION)
public class MainDatabase {
    public static final String NAME = "personalfinance";
    public static final int VERSION = 1;
}
