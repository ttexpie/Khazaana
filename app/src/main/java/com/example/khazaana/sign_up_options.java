package com.example.khazaana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class sign_up_options extends AppCompatActivity{
    Button google_signup;
    Button email_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_options);
        google_signup = findViewById(R.id.google_signup);
        email_signup = findViewById(R.id.email_signup);

        google_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        email_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sign_up_options.this, IFARegistration.class);
                startActivity(intent);
            }
        });


    }
}