package com.example.xpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.xpro.Retrofit.RetrofitBuilder;
import com.example.xpro.Retrofit.RetrofitInterface;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Spinner select_currency;
    Button show;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        listView = findViewById(R.id.list_to_view);
        ArrayList<String> list = new ArrayList<>();
        drawerLayout = findViewById(R.id.drawer_layout);
        show = findViewById(R.id.view_btn);
        select_currency = findViewById(R.id.currency_select);
        String[] currencyList = {"USD","EUR","GBP","ZAR","KES","UGX","TZS","CDF","SSP","BIF","RWF","CNY","EGP","INR"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,currencyList);
        select_currency.setAdapter(adapter);
        double ratis[] = new double[currencyList.length];



        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RetrofitInterface retrofitInterface = RetrofitBuilder.getRetrofitInstance().create(RetrofitInterface.class);
                Call<JsonObject> call = retrofitInterface.getExchangeCurrency(select_currency.getSelectedItem().toString());

                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject rese =response.body();
                        JsonObject rates = rese.getAsJsonObject("conversion_rates");

                        for(int i=0;i<currencyList.length;i++){
                            double parti = Double.valueOf(rates.get(currencyList[i]).toString());
                            ratis[i]=parti;
                            list.add(currencyList[i] +" "+ ratis[i]);
                            Log.d("result ", String.valueOf(list));
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });

            }
        });

    }

    public void ClickMenu(View view){
        MainActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        MainActivity.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view){
        MainActivity.redirectActivity(this, MainActivity.class);
    }

    public void ClickDash(View view){
        recreate();
    }

    public void ClickAbout(View view){
        MainActivity.redirectActivity(this ,AboutUs.class);
    }
    public void ClickLogout(View view){
        MainActivity.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.closeDrawer(drawerLayout);
    }
}