package com.example.khazaana.main;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.khazaana.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class specific_crypto extends Fragment {

    TextView currentP = null;
    TextView priceC = null;
    TextView percentC = null;
    GraphView g = null;
    TextView returnP = null;
    double boughtPrice = 0;
    DecimalFormat d = new DecimalFormat("#.###");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_specific_crypto, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        g = view.findViewById(R.id.clineGraph);
        currentP = view.findViewById(R.id.ccurrent_price);
        priceC = view.findViewById(R.id.cpriceChange);
        percentC = view.findViewById(R.id.cpercentChange);
        TextView cryptoOwned = view.findViewById(R.id.cryptoOwned);
        TextView buyingPrice = view.findViewById(R.id.cbuyingPrice);
        returnP = view.findViewById(R.id.creturnPercent);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ifas = db.collection("Authorized IFAs");
        DocumentReference ifa = ifas.document("A5WkIbLiaub1V1bQ9CRwzLdXBSo2");
        CollectionReference clients = ifa.collection("Clients");
        DocumentReference client = clients.document("24pLjJbK43clJtggGDLPk9ALQfZ2");

        client.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<Map> t = (List<Map>) document.get("Crypto");
                        cryptoOwned.setText("Crypto Owned: " + t.get(0).get("quantity"));
                        buyingPrice.setText("Buying Price: " + t.get(0).get("price"));
                        boughtPrice = Double.parseDouble(t.get(0).get("price").toString());
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

        GridLabelRenderer gridLabel = g.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("November 2021 Date");
        gridLabel.setVerticalAxisTitle("Price");
        new tickerTask().execute("https://finnhub-backend.herokuapp.com/crypto_ticker?symbol=ETH-USD");
    }

    private class tickerTask extends AsyncTask<String, String, String> {
        String data = "";

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                String line = "";

                while (line != null) {
                    line = reader.readLine();
                    data = data + line;
                }
                try {
                    JSONArray j = new JSONArray(data);
                    getActivity().runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void run() {
                            try {
                            currentP.setText("$" + d.format(j.get(j.length() - 1)));
                            priceC.setText("$" + d.format(j.get(j.length() - 3)));
                            String cp = j.get(j.length() - 1).toString();
                            String pp = j.get(j.length() - 3).toString();
                            double currP = Double.parseDouble(cp);
                            double prevP = Double.parseDouble(pp);
                            double percent = (currP - prevP)/ prevP;
                            percentC.setText("" + d.format(percent*100) + "%");
                            double returnPrice = ((currP - boughtPrice)/boughtPrice)*100;
                            returnP.setText("Return: "+returnPrice+"%");
                            if (currP > prevP) {
                                currentP.setTextColor(getResources().getColor(R.color.green));
                                priceC.setTextColor(getResources().getColor(R.color.green));
                                percentC.setTextColor(getResources().getColor(R.color.green));
                            } else {
                                currentP.setTextColor(getResources().getColor(R.color.red));
                                priceC.setTextColor(getResources().getColor(R.color.red));
                                percentC.setTextColor(getResources().getColor(R.color.red));
                            }
                            LineGraphSeries<DataPoint> s = null;

                                s = new LineGraphSeries<DataPoint>(new DataPoint[]{
                                        new DataPoint((getLocalDate().getDayOfMonth())-7, Double.parseDouble(j.get(0).toString())),
                                        new DataPoint((getLocalDate().getDayOfMonth())-6, Double.parseDouble(j.get(1).toString())),
                                        new DataPoint((getLocalDate().getDayOfMonth())-5, Double.parseDouble(j.get(2).toString())),
                                        new DataPoint((getLocalDate().getDayOfMonth())-4, Double.parseDouble(j.get(3).toString())),
                                        new DataPoint((getLocalDate().getDayOfMonth())-3, Double.parseDouble(j.get(4).toString())),
                                        new DataPoint((getLocalDate().getDayOfMonth())-2, Double.parseDouble(j.get(5).toString())),
                                        new DataPoint((getLocalDate().getDayOfMonth())-1, Double.parseDouble(j.get(6).toString())),
                                        new DataPoint(getLocalDate().getDayOfMonth(), Double.parseDouble(j.get(8).toString()))
                                });
                                g.addSeries(s);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDate getLocalDate() {
        return LocalDate.now();
    }
}