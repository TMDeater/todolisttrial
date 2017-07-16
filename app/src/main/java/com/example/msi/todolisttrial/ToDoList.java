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

public class ToDoList extends AppCompatActivity {

    private Database publicDatabase;
    private ArrayList<String> toDoList;
    private ListView listViewToDo;
    private TextView addButton;
    private TextView removeButton;
    private ArrayAdapter<String> arrayAdapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        setTitle("ToDoList");
        context = this;

        toDoList = new ArrayList<String>();
        listViewToDo = (ListView) findViewById(R.id.listViewToDo);

        // get Skygear Container
        Container skygear = Container.defaultContainer(this);

        // get public database
        publicDatabase = skygear.getPublicDatabase();

        Query findQuery = new Query("todoRecord1").equalTo("title", "titleOftodoRecord");
        publicDatabase.query(findQuery, new RecordQueryResponseHandler() {
            @Override
            public void onQuerySuccess(Record[] records) {
                if (records.length<=0){}
                else{
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
            }
        });

        addButton = (TextView) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage();
            }
        });

        removeButton = (TextView) findViewById(R.id.removeButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextRemovePage();
            }
        });
    }

    private void loadToDoList(Record[] records){
        for (int i=0;i<records.length;i++){
            toDoList.add((String) records[i].get("toDoListElement"));
        }
    }

    public void removeItem(final int position){
        Query findQuery = new Query("todoRecord1").equalTo("title", "titleOftodoRecord");
        publicDatabase.query(findQuery, new RecordQueryResponseHandler() {
            @Override
            public void onQuerySuccess(Record[] records) {
                RecordDeleteResponseHandler handler = new RecordDeleteResponseHandler() {
                    @Override
                    public void onDeleteSuccess(String[] ids) {}

                    @Override
                    public void onDeletePartialSuccess(String[] ids, Map<String, Error> errors) {}

                    @Override
                    public void onDeleteFail(Error error) {}
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

    private void nextPage() {
        Intent i = new Intent();
        i.setClass(this,AddToDo.class);
        startActivity(i);
    }

    private void nextRemovePage(){
        Intent i = new Intent();
        i.setClass(this,RemoveToDo.class);
        startActivity(i);
    }
}
