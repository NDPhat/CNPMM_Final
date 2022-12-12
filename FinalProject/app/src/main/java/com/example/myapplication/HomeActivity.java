package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    Button buttonLogout;
    TextView textWelcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Init();
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    void Init()
    {
        buttonLogout=findViewById(R.id.buttonLogout);
        textWelcome=findViewById(R.id.textViewWelcome);
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("userName");
        textWelcome.setText("Welcome "+message);
    }


}