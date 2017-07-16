package com.example.msi.todolisttrial;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Map;

import io.skygear.skygear.AuthResponseHandler;
import io.skygear.skygear.Container;
import io.skygear.skygear.Database;
import io.skygear.skygear.Error;
import io.skygear.skygear.Record;
import io.skygear.skygear.RecordSaveResponseHandler;
import io.skygear.skygear.User;

public class AddToDo extends AppCompatActivity {

    private Database publicDatabase;
    private TextView addButton;
    private TextView skipButton;
    private EditText inputText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);
        setTitle("Add Item To TODO List");

        // get Skygear Container
        Container skygear = Container.defaultContainer(this);

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

        skipButton = (TextView) findViewById(R.id.skipButton);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage();
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

        nextPage();
    }

    private void nextPage(){
        Intent i = new Intent();
        i.setClass(this, ToDoList.class);
        startActivity(i);
    }
}
