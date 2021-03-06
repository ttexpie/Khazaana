package com.example.khazaana.main.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.khazaana.R;
import com.example.khazaana.main.ClientBasic;
import com.example.khazaana.main.NavView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class client_registration extends AppCompatActivity {

    EditText fName, lName, email, pass, repass,ifa_key;
    Button nextScreen;
    FirebaseAuth fAuthorization;
    FirebaseFirestore fStore;
    String uID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_registration);

        fName = findViewById(R.id.firstName3);
        lName = findViewById(R.id.lastName2);
        email = findViewById(R.id.email2);
        pass = findViewById(R.id.passText2);
        repass = findViewById(R.id.reEnterPass2);
        ifa_key = findViewById(R.id.ifaKey);
        nextScreen = findViewById(R.id.next3);
        fAuthorization = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if(fAuthorization.getCurrentUser() != null) {
            fAuthorization.signOut();
            //startActivity(new Intent(getApplicationContext(), userDetails.class));
            //finish();
        }



        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = fName.getText().toString();
                String lastName = lName.getText().toString();
                String emailAdd = email.getText().toString();
                String password = pass.getText().toString();
                String passMatch = repass.getText().toString();
                String ifaKey = ifa_key.getText().toString();

                if (TextUtils.isEmpty(firstName)) {
                    fName.setError("Please enter a first name!!");
                    return;
                }

                if (TextUtils.isEmpty(lastName)) {
                    lName.setError("Please enter a last name!!");
                    return;
                }

                if (TextUtils.isEmpty(emailAdd)){
                    email.setError("Please enter a valid email address!!");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    pass.setError("Please enter a valid password!!");
                    return;
                }

                if (password.length() < 6) {
                    pass.setError("Password can only be >= 6 characters");
                    return;
                }

                if (!TextUtils.equals(password, passMatch)) {
                    repass.setError("Password does not match!!");
                    return;
                }

                if (TextUtils.isEmpty(ifaKey)){
                    ifa_key.setError("Please enter a valid ifa Key!!");
                    return;
                }


                fAuthorization.createUserWithEmailAndPassword(emailAdd, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(client_registration.this,"User Created", Toast.LENGTH_SHORT).show();

                            uID = fAuthorization.getCurrentUser().getUid();
                            /*
                            DocumentReference d = fStore.collection("Authorized IFAs").document(ifaKey).collection("Clients").document(uID);
                            Map<String, Object> new_Client = new HashMap<>();
                            ArrayList<Integer> eq = new ArrayList<>();
                            eq.add(25);
                            eq.add(75);
                            ArrayList<Triplet> stocks = new ArrayList<>();
                            ArrayList<Triplet> crypto = new ArrayList<>();
                            Triplet t1 = new Triplet("AAPL", 100.0, 50);
                            Triplet t2 = new Triplet("TSLA", 500.0, 5);
                            Triplet t3 = new Triplet("AMZN",1245.76, 3.78);
                            stocks.add(t1);
                            stocks.add(t2);
                            stocks.add(t3);
                            Triplet t4 = new Triplet("DOGE-USD", 1.456, 500);
                            Triplet t5 = new Triplet("BTC-USD", 25234.67, 2);
                            Triplet t6 = new Triplet("ETH-USD", 4789, 4.98);
                            crypto.add(t4);
                            crypto.add(t5);
                            crypto.add(t6);
                            ArrayList<String> risks = new ArrayList<>();
                            String totalRiskScore = null;
                            new_Client.put("First Name", firstName);
                            new_Client.put("Last Name", lastName);
                            new_Client.put("Equity", eq);
                            new_Client.put("Stocks", stocks);
                            new_Client.put("Crypto", crypto);
                            new_Client.put("Risk Profiling Answers", risks);
                            new_Client.put("Risks total", totalRiskScore);
                            */

                            DocumentReference dref = fStore.collection("Client List").document(uID);
                            Map<String, Object> client = new HashMap<>();
                            client.put("First Name", firstName);
                            client.put("Last Name", lastName);
                            client.put("Email", emailAdd);
                            client.put("Associated IFA", ifaKey);
                            //d.set(new_Client);
                            dref.set(client);
                            launchClient();

                        } else {
                            Toast.makeText(client_registration.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

    }

    private void launchClient() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference client = db.collection("Client List").document((String) fAuthorization.getUid());
        Log.d("LOGIN", fAuthorization.getUid());
        client.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        Intent intent = new Intent(getApplicationContext(),
                                ClientBasic.class);
                        String ifa = (String) doc.get("Associated IFA");
                        String first = (String) doc.get("First Name");
                        String last = (String) doc.get("Last Name");
                        intent.putExtra("IFA", ifa);
                        intent.putExtra("First", first);
                        intent.putExtra("Last", last);
                        Log.d("LOGIN", "Starting client activity");
                        startActivity(intent);
                    }
                    else {
                        Log.d("LOGIN", "Doc doesn't exist");
                    }
                }
                else {

                    Log.d("LOGIN", "Couldn't get documents");
                }
            }
        });
    }

    public class Triplet<T, U, V> {

        private final String stock;
        private final double price;
        private final double quantity;

        public Triplet(String stock, double price, double quantity) {
            this.stock = stock;
            this.price = price;
            this.quantity = quantity;
        }

        public String getStock() { return stock; }
        public double getPrice() { return price; }
        public double getQuantity() { return quantity; }
    }

}