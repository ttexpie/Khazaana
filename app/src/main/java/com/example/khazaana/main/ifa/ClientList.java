package com.example.khazaana.main.ifa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.khazaana.AddData;
import com.example.khazaana.Portfolio;
import com.example.khazaana.R;

public class ClientList extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.client_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.add_stock_button) {
            goAddData(item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout layout = findViewById(R.id.clientScroll);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        String[] clients = getResources().getStringArray(R.array.clients);

        for (String client: clients) {
            TextView textView = new TextView(this);
            textView.setText(client);
            textView.setLayoutParams(params);
            textView.setOnClickListener(this::goClientPortfolio);
            layout.addView(textView);
        }
    }

    public void goAddData(MenuItem item) {
        Intent intent = new Intent(this, AddData.class);
        startActivity(intent);
    }

    public void goClientPortfolio(View view) {
        Intent intent = new Intent(this, Portfolio.class);
        startActivity(intent);
    }
}