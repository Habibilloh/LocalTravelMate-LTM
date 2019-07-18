package com.example.abdul.ltm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EntryPage extends AppCompatActivity {

    private Button RegisterBtn;
    private Button GoToLogInBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_page);
        RegisterBtn = (Button)findViewById(R.id.registerBtn);
        GoToLogInBtn= (Button)findViewById(R.id.goTologinBtn);

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EntryPage.this, RegisterPage.class);
                startActivity(intent);
            }
        });

        GoToLogInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLog = new Intent(EntryPage.this,LogIn.class);
                startActivity(intentLog);

            }
        });
    }
}
