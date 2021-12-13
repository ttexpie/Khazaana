package com.example.khazaana;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.khazaana.main.AssetEntry;
import com.example.khazaana.main.Home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class CallAPI {

    public interface StockListener {
        void OnError(String message);
        void OnResponse(String name, double stockPrice, double stockReturn);
    }

    public interface AUMReturnListener {
        void OnError(String message);
        void OnResponse(AssetEntry a, String name, double stockPrice, double stockReturn, List<Home.PortfolioData> clientData);
    }

    public void calcHomeAumReturn(Context context, List<Map> stocks, List<Home.PortfolioData> clientData, AUMReturnListener listener) {
        String url = "https://finnhub-backend.herokuapp.com/stock/price?symbol=";
        if (stocks != null && stocks.size() > 0) {
            for (int i = 0; i < stocks.size(); i++) {
                AssetEntry a = new AssetEntry();
                a.setStock(stocks.get(i).get("stock").toString());
                a.setPrice(Float.parseFloat(stocks.get(i).get("price").toString()));
                a.setQuantity(Float.parseFloat(stocks.get(i).get("quantity").toString()));
                String complete = url + a.getStock();
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, complete,
                        null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            double stockPrice = response.getDouble("current price");
                            double stockReturn = stockPrice - a.getPrice();

                            listener.OnResponse(a, a.getStock(), stockPrice, stockReturn, clientData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.OnError("Error getting stock price");
                    }
                });
                RequestSingleton.getInstance(context).addToRequestQueue(request);
            }
        }
    }



    public void calcStock(Context context, AssetEntry stock, StockListener listener) {
        String url = "https://finnhub-backend.herokuapp.com/stock/price?symbol=";
        String complete = url + stock.getStock();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, complete,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    double stockPrice = response.getDouble("current price");
                    double stockReturn = stockPrice - stock.getPrice();

                    listener.OnResponse(stock.getStock(), stockPrice, stockReturn);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.OnError("Error getting stock price");
            }
        });
        RequestSingleton.getInstance(context).addToRequestQueue(request);
    }

    public interface CryptoListener {
        void OnError(String message);
        void OnResponse(String name, double cryptoPrice, double cryptoReturn, Double[] prices);
    }

    public void calcCrypto(Context context, AssetEntry crypto, CryptoListener listener) {
        String url = "https://finnhub-backend.herokuapp.com/crypto/ticker?symbol=";
        String complete = url + crypto.getStock();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, complete,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    double cryptoPrice = Double
                            .parseDouble(response.get(response.length() - 1).toString());
                    double cryptoReturn = cryptoPrice - crypto.getPrice();
                    Double [] prices = new Double[response.length()];
                    for (int i = 0; i < prices.length; i++) {
                        prices[i] = Double.parseDouble(response.get(i).toString());
                    }

                    listener.OnResponse(crypto.getStock(), cryptoPrice, cryptoReturn, prices);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //listener.OnError("Error getting crypto price");
            }
        });
        RequestSingleton.getInstance(context).addToRequestQueue(request);
    }
}
