package com.example.khazaana.main.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.khazaana.R;
import com.example.khazaana.main.client.ClientAccountSettings;
import com.example.khazaana.main.riskprofiling.RiskProfiling_1;
import com.example.khazaana.main.riskprofiling.RiskProfiling_C1;

public class ClientRiskProfilingMain extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.risk_profiling_main);

        Button rp1 = findViewById(R.id.risk_profiling1);
        rp1.setOnClickListener(this::rp1);

        Button rp2 = findViewById(R.id.risk_profiling2);
        rp2.setOnClickListener(this::rp2);

        Button backButton = findViewById(R.id.back_risk_profiling_main);
        backButton.setOnClickListener(this::previousPage);
    }

    public void rp1(View view) {
        Intent intent = new Intent(this, RiskProfiling_1.class);
        startActivity(intent);
    }

    public void previousPage(View view) {
        Intent intent = new Intent(this, ClientAccountSettings.class);
        startActivity(intent);
    }

    public void rp2(View view) {
        Intent intent = new Intent(this, RiskProfiling_C1.class);
        startActivity(intent);
    }
}