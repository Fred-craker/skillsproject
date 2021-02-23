package com.example.xpro;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.xpro.Retrofit.RetrofitBuilder;
import com.example.xpro.Retrofit.RetrofitInterface;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Button convert;
    EditText currencyToBeConverted;
    TextView currencyConverted;
    Spinner fromCurrency;
    Spinner toCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialisation
        drawerLayout = findViewById(R.id.drawer_layout);
        convert = findViewById(R.id.convert_btn);
        currencyToBeConverted = findViewById(R.id.amount_to_be);
        currencyConverted = findViewById(R.id.result_view);
        fromCurrency = findViewById(R.id.from_currency);
        toCurrency = findViewById(R.id.to_currency);

        //functionality
        String[] currencyList = {"USD","EUR","GBP","ZAR","KES","UGX","TZS","CDF","SSP","BIF","RWF","CNY","EGP","INR"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,currencyList);

        fromCurrency.setAdapter(adapter);
        toCurrency.setAdapter(adapter);
        convert.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                RetrofitInterface retrofitInterface = RetrofitBuilder.getRetrofitInstance().create(RetrofitInterface.class);
                Call<JsonObject> call = retrofitInterface.getExchangeCurrency(fromCurrency.getSelectedItem().toString());
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject res = response.body();
                        JsonObject rates = res.getAsJsonObject("conversion_rates");
                        Log.d("response", String.valueOf(response.body()));
                        Log.d("ratis", String.valueOf(rates));
                        double currency = Double.valueOf(currencyToBeConverted.getText().toString());
                        Log.d("currency",String.valueOf(currency));

                        double parti = Double.valueOf(rates.get(currencyList[3]).toString());
                        Log.d("testify", String.valueOf(parti));
                        double multiplier = Double.valueOf(rates.get(toCurrency.getSelectedItem().toString()).toString());
                        Log.d("multi",String.valueOf(multiplier));
                        long ans = (long) ( currency * multiplier);
                        Log.d("answer",String.valueOf(ans));
                        currencyConverted.setText(toCurrency.getSelectedItem().toString() + " "+String.valueOf(ans));
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });




            }
        });

    }

    public void ClickMenu(View view){

        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {

        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view){
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view){
        recreate();
    }
    public void ClickDash(View view){
        redirectActivity(this, Dashboard.class);
    }

    public void ClickAbout(View view){
        redirectActivity(this, AboutUs.class);
    }

    public void ClickLogout(View view){
        logout(this);
    }

    public static void logout(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Logout");

        builder.setMessage("Are you sure you want to Exit ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finishAffinity();
                System.exit(0);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity, aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}