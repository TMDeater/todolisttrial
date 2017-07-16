package com.example.msi.todolisttrial;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class RemoveToDo extends AppCompatActivity {

    //class members needed
    private Database publicDatabase;
    private ArrayList<String> toDoList;
    private ListView listViewToDo;
    private TextView backButton;
    private ArrayAdapter<String> arrayAdapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_to_do);
        setTitle("Removing a TODO");
        context = this;

        //initialize the arraylist for TODO list
        toDoList = new ArrayList<String>();
        listViewToDo = (ListView) findViewById(R.id.listViewToDo);

        // get Skygear Container
        Container skygear = Container.defaultContainer(this);

        // get public database
        publicDatabase = skygear.getPublicDatabase();

        //Query for title and we can get all TODOs
        Query findQuery = new Query("todoRecord1").equalTo("title", "titleOftodoRecord");
        publicDatabase.query(findQuery, new RecordQueryResponseHandler() {
            @Override
            public void onQuerySuccess(final Record[] records) {
                loadToDoList(records);
                // Create The Adapter with passing ArrayList as 3rd parameter
                arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, toDoList);
                // Set The Adapter so the TODO list could be populated in UI
                listViewToDo.setAdapter(arrayAdapter);

                //This is for removing TODO items,
                //when it is clicked, it will be removed
                listViewToDo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //initialize a record delete response handler
                        RecordDeleteResponseHandler handler = new RecordDeleteResponseHandler() {
                            @Override
                            public void onDeleteSuccess(String[] ids) {}
                            @Override
                            public void onDeletePartialSuccess(String[] ids, Map<String, Error> errors) {}
                            @Override
                            public void onDeleteFail(Error error) {}
                        };
                        //delete the records in corresponding position
                        publicDatabase.delete(records[position], handler);
                        nextPage();
                    }
                });
            }

            @Override
            public void onQueryError(Error error) {}
        });

        //Initialize button for going to add ToDoList page
        backButton = (TextView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage();
            }
        });
    }

    //copying the records in arraylist so it can be used to shown in UI
    private void loadToDoList(Record[] records){
        for (int i=0;i<records.length;i++){
            toDoList.add((String) records[i].get("toDoListElement"));
        }
    }

    //function for going to the ToDoList page
    private void nextPage() {
        Intent i = new Intent();
        i.setClass(this,ToDoList.class);
        startActivity(i);
    }
}


