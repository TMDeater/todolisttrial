package com.example.msi.todolisttrial;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import io.skygear.skygear.Container;
import io.skygear.skygear.Database;
import io.skygear.skygear.Error;
import io.skygear.skygear.Query;
import io.skygear.skygear.Record;
import io.skygear.skygear.RecordDeleteResponseHandler;
import io.skygear.skygear.RecordQueryResponseHandler;
import io.skygear.skygear.RecordSaveResponseHandler;

public class ToDoList extends AppCompatActivity {

    private String username;
    private String password;
    private Database publicDatabase;
    private ArrayList<String> toDoList;
    private ListView listViewToDo;
    private TextView addButton;
    private ArrayAdapter<String> arrayAdapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        setTitle("ToDoList");
        context = this;

        Intent i = getIntent();
        username = i.getStringExtra("username");
        password = i.getStringExtra("password");

        toDoList = new ArrayList<String>();
        listViewToDo = (ListView) findViewById(R.id.listViewToDo);

        // get Skygear Container
        Container skygear = Container.defaultContainer(this);

//        skygear.loginWithUsername(username,password,new AuthResponseHandler() {
//            @Override
//            public void onAuthSuccess(User user) {
//                Log.i("MyApplication", "Signup successfully");
//            }
//
//            @Override
//            public void onAuthFail(Error error) {
//                Log.w("MyApplication", "Failed to signup: " + error.getMessage(), error);
//            }
//        });

        // get public database
        publicDatabase = skygear.getPublicDatabase();

        Query findQuery = new Query("todoRecord1").equalTo("title", "titleOftodoRecord");
        publicDatabase.query(findQuery, new RecordQueryResponseHandler() {
            @Override
            public void onQuerySuccess(Record[] records) {
                Log.i("Record Query", String.format("Successfully got %d records", records.length));
                if (records.length<=0){
                    initializeRecord();
                }else{
                    loadToDoList(records);
                }
                // Create The Adapter with passing ArrayList as 3rd parameter
                arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, toDoList);
                // Set The Adapter
                listViewToDo.setAdapter(arrayAdapter);

                listViewToDo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        removeItem(position);
                    }
                });
            }

            @Override
            public void onQueryError(Error error) {
                Log.i("Record Query", String.format("Fail with reason: %s", error));
            }
        });

        addButton = (TextView) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(ToDoList.this, AddToDo.class);
                i.putExtra("username", username);
                i.putExtra("password", password);
                startActivity(i);
            }
        });
    }

    private void initializeRecord() {
        toDoList.add("Buy apple");

        Record first = new Record("todoRecord1");
        first.set("title", "titleOftodoRecord");
        first.set("toDoListElement", "Buy Apple");
        saveDatabase(first);
    }

    private void loadToDoList(Record[] records){
        for (int i=0;i<records.length;i++){
            toDoList.add((String) records[i].get("toDoListElement"));
        }
    }

    private void saveDatabase(Record record){
        RecordSaveResponseHandler handler = new RecordSaveResponseHandler() {
            @Override
            public void onSaveSuccess(Record[] records) {
                Log.i(
                        "Skygear Record Save",
                        "Successfully saved " + records.length + " records"
                );

            }

            @Override
            public void onPartiallySaveSuccess(Map<String, Record> successRecords, Map<String, Error> errors) {
                Log.i(
                        "Skygear Record Save",
                        "Successfully saved " + successRecords.size() + " records"
                );
                Log.i(
                        "Skygear Record Save",
                        errors.size() + " records are fail to save"
                );
            }

            @Override
            public void onSaveFail(Error error) {
                Log.i(
                        "Skygear Record Save",
                        "Fail to save: " + error
                );
            }
        };
        publicDatabase.save(record,handler);
    }

    public void removeItem(final int position){
        Query findQuery = new Query("todoRecord1").equalTo("title", "titleOftodoRecord");
        publicDatabase.query(findQuery, new RecordQueryResponseHandler() {
            @Override
            public void onQuerySuccess(Record[] records) {
                RecordDeleteResponseHandler handler = new RecordDeleteResponseHandler() {
                    @Override
                    public void onDeleteSuccess(String[] ids) {
                        Log.i(
                                "Skygear Record Delete",
                                "Successfully deleted " + ids.length + " records"
                        );
                    }

                    @Override
                    public void onDeletePartialSuccess(String[] ids, Map<String, Error> errors) {
                        Log.i(
                                "Skygear Record Delete",
                                "Successfully deleted " + ids.length + " records"
                        );
                        Log.i(
                                "Skygear Record Delete",
                                errors.size() + " records are fail to delete"
                        );
                    }

                    @Override
                    public void onDeleteFail(Error error) {
                        Log.i(
                                "Skygear Record Delete",
                                "Fail to delete: " + error
                        );
                    }
                };
                publicDatabase.delete(records[position], handler);
            }

            @Override
            public void onQueryError(Error error) {
            }
        });

        ArrayList<String> temp = new ArrayList<>();
        temp.addAll(toDoList);
        temp.remove(position);
        toDoList=new ArrayList<>();
        toDoList.addAll(temp);
        arrayAdapter.clear();
        arrayAdapter.addAll(toDoList);
        arrayAdapter.notifyDataSetChanged();

    }
}
