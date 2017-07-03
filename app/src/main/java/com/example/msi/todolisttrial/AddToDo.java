package com.example.msi.todolisttrial;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import io.skygear.skygear.AuthResponseHandler;
import io.skygear.skygear.Container;
import io.skygear.skygear.Database;
import io.skygear.skygear.Error;
import io.skygear.skygear.Query;
import io.skygear.skygear.Record;
import io.skygear.skygear.RecordDeleteResponseHandler;
import io.skygear.skygear.RecordQueryResponseHandler;
import io.skygear.skygear.RecordSaveResponseHandler;
import io.skygear.skygear.User;

public class AddToDo extends AppCompatActivity {

    private String username;
    private String password;
    private Database publicDatabase;
    private ArrayList<String> toDoList;
    private TextView addButton;
    private EditText inputText;
    private ArrayAdapter<String> arrayAdapter;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Add Item To TODO List");
        context = this;

        Intent i = getIntent();
        username = i.getStringExtra("username");
        password = i.getStringExtra("password");

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

        inputText = (EditText) findViewById(R.id.inputText);
        addButton = (TextView) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
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

    public void addItem(){
        String input = inputText.getText().toString();
        Record newRecord = new Record("todoRecord1");
        newRecord.set("title", "titleOftodoRecord");
        newRecord.set("toDoListElement", input);
        saveDatabase(newRecord);

        Intent i = new Intent();
        i.putExtra("username", username);
        i.putExtra("password", password);
        i.setClass(this, ToDoList.class);
        startActivity(i);
    }
}
