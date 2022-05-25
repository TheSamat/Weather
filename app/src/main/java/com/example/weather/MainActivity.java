package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    TextView pressure2;
    TextView cloud;
    TextView wind2;
    TextView humidity2;
    TextView date_time;
    TextView city_edit;
    ImageView back;
//    TextView city_logo;
    EditText etCity;
    Calendar calendar;
    TextView gradusnik;
    String key = "f650afb212cd977894f8c1eda48dba5a";
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    DecimalFormat df = new DecimalFormat("#.##");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();



    }
public void init(){
        date_time = findViewById(R.id.data_now);
        city_edit = findViewById(R.id.city);
//        city_logo = city_logo.findViewById(R.id.bottom_sheet_logo);
        etCity = findViewById(R.id.user_field);
        gradusnik = findViewById(R.id.gradus);
        pressure2 = findViewById(R.id.pressure);
        humidity2 = findViewById(R.id.humidity);
        wind2 = findViewById(R.id.wind);
        cloud = findViewById(R.id.cloud);

}
    public void getWeatherDetails(View view) {
        String tempUrl = "";
        String city = etCity.getText().toString().trim();

            tempUrl = url + "?q=" + city + "&appid=" + key;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String output="";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        Integer jsonObjectDate = jsonResponse.getInt("dt");
                        java.util.Date time = new java.util.Date(jsonObjectDate*1000);

                        calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        String datatime = simpleDateFormat.format(calendar.getTime());
                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectClouds.getString("all");
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String cityName = jsonResponse.getString("name");

                        output+=df.format(temp) + "`C";
                        city_edit.setTextColor(Color.rgb(68,45,195));
                        city_edit.setText(city);
                        date_time.setText(datatime);
                        gradusnik.setText(output);
                        String cl = "Обл: "+ clouds + "%";
                        cloud.setText(cl);
                        String pr = "Давл: " + String.valueOf(pressure) + " hPa";
                        pressure2.setText(pr);
                        String hu = "Влаж: " + String.valueOf(humidity) + "%";
                        humidity2.setText(hu);
                        wind2.setText("Ветер: " + wind + "m/s");


//                        tvResult.setTextColor(Color.rgb(68,134,199));
//                        output += " Текущая погода " + cityName + " (" + countryName + ")"
//                                + "\n Температура: " + df.format(temp) + " °C"
//                                + "\n Влажность: " + humidity + "%"
//                                + "\n Время: " + dataTime + ""
//                                + "\n Скорость ветра: " + wind + "м/с (метров в секунду)"
//                                + "\n Облачность: " + clouds + "%"
//                                + "\n Давление: " + pressure + " hPa";
//                        tvResult.setText(output);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }

}
