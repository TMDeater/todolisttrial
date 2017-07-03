package com.example.msi.todolisttrial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.skygear.skygear.AuthContainer;
import io.skygear.skygear.Container;
import io.skygear.skygear.LoginRequest;
import io.skygear.skygear.SignupRequest;

public class MainActivity extends AppCompatActivity {

    private EditText loginname;
    private EditText loginpw;
    private EditText signupname;
    private EditText signuppw;
    private Button loginbutton;
    private Button signupbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Sign Up / Log In");

        // get Skygear Container
        final Container skygear = Container.defaultContainer(this);

        loginname = (EditText) findViewById(R.id.inputloginname);
        loginpw = (EditText) findViewById(R.id.inputloginPW);
        signupname = (EditText) findViewById(R.id.inputsignupname);
        signuppw = (EditText) findViewById(R.id.inputsignupPW);

        loginbutton = (Button) findViewById(R.id.loginbutton);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = loginname.getText().toString();
                String password = loginpw.getText().toString();
                nextPage(username,password);
            }
        });

        signupbutton = (Button) findViewById(R.id.signupbutton);
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = signupname.getText().toString();
                String password = signuppw.getText().toString();
                nextPage(username,password);
            }
        });
    }

    private void nextPage(String username, String password){
        Intent i = new Intent();
        i.setClass(this,ToDoList.class);
        i.putExtra("username",username);
        i.putExtra("password",password);
        startActivity(i);
    }
}
