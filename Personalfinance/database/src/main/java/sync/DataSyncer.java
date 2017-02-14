package sync;

import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;

/**
 * Created by dagy on 13.02.17..
 */

public class DataSyncer {
    private SyncInterface syncer;

    public boolean syncData(String id, int type){
        if(type == 1){
            syncer = new WebSyncer();
        }
        if(type == 2){
            syncer = new BTSyncer();
        }

        syncer.syncData(id);

        return true;
    }
}
