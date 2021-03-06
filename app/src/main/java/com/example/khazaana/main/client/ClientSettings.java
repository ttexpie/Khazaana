package com.example.khazaana.main.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.khazaana.R;

public class ClientSettings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_settings);

        Button addifa = findViewById(R.id.add_ifa);
        addifa.setOnClickListener(this::goAddIFA);

        Button removeifa = findViewById(R.id.remove_ifa);
        removeifa.setOnClickListener(this::goRemoveIFA);

        Button updatePassword = findViewById(R.id.update_password);
        updatePassword.setOnClickListener(this::goUpdatePassword);

        Button backButton = findViewById(R.id.back_client_settings);
        backButton.setOnClickListener(this::previousPage);
    }

    public void goAddIFA(View view) {
        Intent intent = new Intent(this, AddNewIFA.class);
        startActivity(intent);
    }

    public void goRemoveIFA(View view) {
        Intent intent = new Intent(this, RemoveIFA.class);
        startActivity(intent);
    }

    public void goUpdatePassword(View view) {
        Intent intent = new Intent(this, ClientUpdatePassword.class);
        startActivity(intent);
    }

    public void previousPage(View view) {
        Intent intent = new Intent(this, ClientAccountSettings.class);
        startActivity(intent);
    }
}