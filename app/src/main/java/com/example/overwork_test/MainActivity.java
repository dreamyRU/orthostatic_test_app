package com.example.overwork_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DateFormatSymbols;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Spinner birth_year_sp;
    Spinner birth_months_sp;
    Spinner birth_date_sp;
    Spinner gender_sp;
    public static String Van;
    public static String Billy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Отключаем темную тему приложения
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        birth_year_sp = findViewById(R.id.birth_year_spinner);
        birth_months_sp = findViewById(R.id.birth_month_spinner);
        birth_date_sp = findViewById(R.id.birth_date_spinner);
        gender_sp = findViewById(R.id.gender_spinner);



        // Выпадающее меню
        String[] birth_years = new String[100];
        int start_year = 2021;
        for (int i=0; i < 100; i++) {
            birth_years[i] = String.valueOf(start_year - i);
        }
        String[] birth_months = new String[12];
        for (int i=0; i < 12; i++) {
            birth_months[i] = String.valueOf(getMonthForInt(i));
        }
        String[] birth_dates = new String[31];
        for (int i=0; i < 31; i++) {
            birth_dates[i] = String.valueOf(i+1);
        }
        String[] genders = {"М", "Ж"};



        ArrayAdapter<String> spinnerArrayYears = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, birth_years);
        ArrayAdapter<String> spinnerArrayMonths = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, birth_months);
        ArrayAdapter<String> spinnerArrayDates = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, birth_dates);
        ArrayAdapter<String> spinnerArrayGenders = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genders);

        spinnerArrayYears.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArrayMonths.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArrayDates.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArrayGenders.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        birth_year_sp.setAdapter(spinnerArrayYears);
        birth_months_sp.setAdapter(spinnerArrayMonths);
        birth_date_sp.setAdapter(spinnerArrayDates);
        gender_sp.setAdapter(spinnerArrayGenders);

    }

    public void ShowResults (View v) {
        EditText question_3 = findViewById(R.id.heart_rate_lie);
        EditText question_6 = findViewById(R.id.heart_rate_stand);
        Intent intent = new Intent(this, Results.class);
        String[] person_credentials = {birth_date_sp.getSelectedItem().toString(),
                birth_months_sp.getSelectedItem().toString(),
                birth_year_sp.getSelectedItem().toString(),
                gender_sp.getSelectedItem().toString()};
        if (question_3.getText().length() > 0 && question_6.getText().length() > 0) {
            String pulse_lie = question_3.getText().toString();
            String pulse_stand = question_6.getText().toString();
            intent.putExtra("first_value", pulse_lie);
            intent.putExtra("second_value", pulse_stand);
        }
        intent.putExtra("credentials", person_credentials);
        startActivity(intent);
        System.out.println(question_3.getText().toString());
        Van = question_3.getText().toString();
        Billy = question_6.getText().toString();

    }


    public String getMonthForInt(int num) {
        String month = "";
        DateFormatSymbols dfs = new DateFormatSymbols(new Locale("ru"));
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.setGroupVisible(R.id.new_game_group, false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_exit) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}