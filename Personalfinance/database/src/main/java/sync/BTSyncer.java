package sync;

import android.app.Activity;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.devpaul.bluetoothutillib.SimpleBluetooth;
import com.devpaul.bluetoothutillib.dialogs.DeviceDialog;
import com.devpaul.bluetoothutillib.utils.BluetoothUtility;
import com.devpaul.bluetoothutillib.utils.SimpleBluetoothListener;
import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import hr.foi.air.database.R;
import pojo.Categories;
import pojo.Category;
import pojo.Category_Table;
import pojo.Record;
import pojo.Record_Table;
import pojo.Records;
import pojo.Task;
import pojo.Task_Table;
import pojo.Tasks;

/**
 * Created by dagy on 13.02.17..
 */

public class BTSyncer extends Activity implements SyncInterface {
    private String id;
    private Context context;
    private SimpleBluetooth simpleBluetooth;
    private static final int CHOOSE_SERVER_REQUEST = 120;
    private String curMacAddress;
    private String localEditedCategory;
    private String localEditedTask;
    private String localEditedRecord;
    private String deviceEditedCategory;
    private String deviceEditedTask;
    private String deviceEditedRecord;
    private Gson gson = new Gson();
    private Button server, client, cancel;
    private Category category;

    public BTSyncer(){}

    public void startActivity(){
        Intent intent = new Intent(context, BTSyncer.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void onResume(){
        super.onResume();

        if(simpleBluetooth == null){
            simpleBluetooth = new SimpleBluetooth(this, new SimpleBluetoothListener() {
                @Override
                public void onBluetoothDataReceived(byte[] bytes, String data) {
                    String recieved = data;
                    System.out.println("Data recieved: " + recieved);

                    if(recieved.toLowerCase().contains("SyncData;".toLowerCase())) {
                        System.out.println("SYNC DATA");
                        String[] parts = recieved.split(";");
                        deviceEditedCategory = gson.fromJson(parts[1], String.class);
                        deviceEditedRecord = gson.fromJson(parts[2], String.class);
                        deviceEditedTask = gson.fromJson(parts[3], String.class);

                        localEditedCategory = getLastEditedCategory();
                        localEditedRecord = getLastEditedRecord();
                        localEditedTask = getLastEditedTask();

                        //Server is ahead with category
                        if (localEditedCategory.compareTo(deviceEditedCategory) > 0) {
                            Categories categories = new Categories();
                            categories.setCategory(SQLite.select().from(Category.class).where(Category_Table.lastEdited.greaterThan(localEditedCategory)).queryList());
                            categories.setCategory(SQLite.select().from(Category.class).where(Category_Table.lastEdited.is(localEditedCategory)).queryList());
                            String json = gson.toJson(categories);
                            simpleBluetooth.sendData(json);
                        } else if (localEditedCategory.compareTo(deviceEditedCategory) < 0) {
                            //client is ahead send request
                            String request = "REQUEST_CATEGORIES;"+localEditedCategory;
                            simpleBluetooth.sendData(request);
                        } else if(localEditedTask.compareTo(deviceEditedTask) > 0){
                            Tasks tasks = new Tasks();
                            tasks.setTasks(SQLite.select().from(Task.class).where(Task_Table.lastEdited.greaterThan(localEditedTask)).queryList());
                            tasks.setTasks(SQLite.select().from(Task.class).where(Task_Table.lastEdited.is(localEditedTask)).queryList());
                            simpleBluetooth.sendData(gson.toJson(tasks));
                        } else if(localEditedTask.compareTo(deviceEditedTask) < 0){
                            String request = "REQUEST_TASKS;"+localEditedTask;
                            simpleBluetooth.sendData(request);
                        } else if(localEditedRecord.compareTo(deviceEditedRecord) > 0){
                            Records records = new Records();
                            records.setRecord(SQLite.select().from(Record.class).where(Record_Table.lastEdited.greaterThan(localEditedRecord)).queryList());
                            records.setRecord(SQLite.select().from(Record.class).where(Record_Table.lastEdited.is(localEditedRecord)).queryList());
                            simpleBluetooth.sendData(gson.toJson(records));
                        } else if(localEditedRecord.compareTo(deviceEditedRecord) < 0){
                            String request = "REQUEST_RECORDS;"+localEditedRecord;
                            simpleBluetooth.sendData(request);
                        }
                    } else if(recieved.toLowerCase().contains("REQUEST_CATEGORIES".toLowerCase())) {
                        //send categories
                        String[] parts = recieved.split(";");
                        Categories categories = new Categories();
                        categories.setCategory(SQLite.select().from(Category.class).where(Category_Table.lastEdited.greaterThan(parts[1])).queryList());
                        categories.setCategory(SQLite.select().from(Category.class).where(Category_Table.lastEdited.is(parts[1])).queryList());
                        simpleBluetooth.sendData(gson.toJson(categories));
                    } else if(recieved.toLowerCase().contains("REQUEST_TASKS".toLowerCase())) {
                        String[] parts = recieved.split(";");
                        Tasks tasks = new Tasks();
                        tasks.setTasks(SQLite.select().from(Task.class).where(Task_Table.lastEdited.greaterThan(parts[1])).queryList());
                        tasks.setTasks(SQLite.select().from(Task.class).where(Task_Table.lastEdited.is(parts[1])).queryList());
                        simpleBluetooth.sendData(gson.toJson(tasks));
                    } else if(recieved.toLowerCase().contains("REQUEST_RECORDS".toLowerCase())){
                        String[] parts = recieved.split(";");
                        Records records = new Records();
                        records.setRecord(SQLite.select().from(Record.class).where(Record_Table.lastEdited.greaterThan(parts[1])).queryList());
                        records.setRecord(SQLite.select().from(Record.class).where(Record_Table.lastEdited.is(parts[1])).queryList());
                        simpleBluetooth.sendData(gson.toJson(records));
                    } else {
                        System.out.println("DATA");
                        System.out.println(recieved);
                        //Categories categories = gson.fromJson(recieved, Categories.class);
                        if(recieved.toLowerCase().contains("category".toLowerCase())){
                            Categories categories = gson.fromJson(recieved, Categories.class);
                            for(Category category : categories.getCategory()){
                                category.save();
                                System.out.println("CATEGORY SAVE");
                            }
                        } else if(recieved.toLowerCase().contains("record".toLowerCase())){
                            Records records = gson.fromJson(recieved, Records.class);
                            for(Record record : records.getRecord()){
                                record.save();
                                System.out.println("RECORD SAVE");
                            }
                        } else if (recieved.toLowerCase().contains("tasks".toLowerCase())){
                            Tasks tasks = gson.fromJson(recieved, Tasks.class);
                            for(Task task : tasks.getTasks()){
                                task.save();
                                System.out.println("TASK SAVE");
                            }
                        }

                        localEditedCategory = getLastEditedCategory();
                        localEditedRecord = getLastEditedRecord();
                        localEditedTask = getLastEditedTask();
                        id = getIntent().getStringExtra("id");
                        String jsonCategory = gson.toJson(localEditedCategory);
                        String jsonRecord = gson.toJson(localEditedRecord);
                        String jsonTask = gson.toJson(localEditedTask);
                        String jsonID = gson.toJson(id);
                        String json = "SyncData;" + jsonCategory + ";" + jsonRecord + ";" + jsonTask + ";" + jsonID + ";";
                        simpleBluetooth.sendData(json);
                    }
                }

                @Override
                public void onDeviceConnected(BluetoothDevice device) {
                    Toast.makeText(getApplication(), "Spojen", Toast.LENGTH_SHORT).show();
                    cancel.setVisibility(View.VISIBLE);
                }

                @Override
                public void onDeviceDisconnected(BluetoothDevice device) {
                    Toast.makeText(getApplication(), "Prekid veze", Toast.LENGTH_SHORT).show();
                    cancel.setVisibility(View.INVISIBLE);
                }
            });
        }
        simpleBluetooth.initializeSimpleBluetooth();
        simpleBluetooth.setInputStreamType(BluetoothUtility.InputStreamType.BUFFERED);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.type_selection);
        super.onCreate(savedInstanceState);
        server = (Button) findViewById(R.id.server);
        client = (Button) findViewById(R.id.client);
        cancel = (Button) findViewById(R.id.cancel);
        localEditedCategory = getLastEditedCategory();
        localEditedRecord = getLastEditedRecord();
        localEditedTask = getLastEditedTask();
        id = getIntent().getStringExtra("id");

        server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                host();
            }
        });

        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jsonCategory = gson.toJson(localEditedCategory);
                String jsonRecord = gson.toJson(localEditedRecord);
                String jsonTask = gson.toJson(localEditedTask);
                String jsonID = gson.toJson(id);
                String json = "SyncData;" + jsonCategory + ";" + jsonRecord + ";" + jsonTask + ";" + jsonID + ";";
                simpleBluetooth.sendData(json);
            }
        });
    }

    @Override
    public boolean syncData(final Context context, final String id) {
        this.id = id;
        this.context = context;


        startActivity();

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHOOSE_SERVER_REQUEST){
            if(resultCode == RESULT_OK){
                curMacAddress = data.getStringExtra(DeviceDialog.DEVICE_DIALOG_DEVICE_ADDRESS_EXTRA);
                if(requestCode == CHOOSE_SERVER_REQUEST){
                    simpleBluetooth.connectToBluetoothServer(curMacAddress);


                } else {
                    simpleBluetooth.connectToBluetoothDevice(curMacAddress);
                }
            }
        }
    }

    public void connect(){
        if(curMacAddress != null){
            simpleBluetooth.connectToBluetoothServer(curMacAddress);

        } else {
            simpleBluetooth.scan(CHOOSE_SERVER_REQUEST);
        }
    }

    public void host(){
        simpleBluetooth.createBluetoothServerConnection();
    }

    public String getLastEditedCategory(){
        String lastEdited;
        Categories categories = new Categories();

        if(SQLite.select().from(Category.class).orderBy(Category_Table.lastEdited, false).queryList().size() > 0) {
            categories.setCategory(SQLite.select().from(Category.class).orderBy(Category_Table.lastEdited, false).queryList());
            lastEdited = categories.getCategory().get(0).getLastEdited();
            System.out.println("Last edited category date: " + lastEdited);
        } else {
            lastEdited = "0";
        }

        return lastEdited;
    }

    public String getLastEditedRecord(){
        String lastEdited;
        Records records = new Records();

        if(SQLite.select().from(Record.class).orderBy(Record_Table.lastEdited, false).queryList().size() > 0) {
            records.setRecord(SQLite.select().from(Record.class).orderBy(Record_Table.lastEdited, false).queryList());
            lastEdited = records.getRecord().get(0).getLastEdited();
            System.out.println("Last edited record date: " + lastEdited);
        } else {
            lastEdited = "0";
        }

        return  lastEdited;
    }

    public String getLastEditedTask(){
        String lastEdited;
        Tasks tasks = new Tasks();

        if(SQLite.select().from(Task.class).orderBy(Task_Table.lastEdited, false).queryList().size() > 0) {
            tasks.setTasks(SQLite.select().from(Task.class).orderBy(Task_Table.lastEdited, false).queryList());
            lastEdited = tasks.getTasks().get(0).getLastEdited();
            System.out.println("Last edited task date: " + lastEdited);
        } else {
            lastEdited = "0";
        }

        return lastEdited;
    }
    private void mockData(){
        this.category = new Category();
        category.setId("1");
        category.setUserId("1");
        category.setActive("1");
        category.setCategoryId("1");
        category.setDescription("dsfdf");
        category.setEdited(1);
        category.setLastEdited("1");
        category.setTitle("test");
    }

}
