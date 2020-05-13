package com.pixelite.covidtracker;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class CovidCountryDetail extends AppCompatActivity {

    private TextView tvDetailCountryName, tvDetailTotalDeaths, tvDetailTodayDeaths, tvDetailTotalCases, tvDetailTodayCases,
            tvDetailTotalRecovered, tvDetailTotalActive, tvDetailTotalCritical;

    private MaterialToolbar materialToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_country_detail);

        materialToolbar = findViewById(R.id.toolbarCountry);
        setSupportActionBar(materialToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvDetailCountryName = findViewById(R.id.tvDetailCountryName);
        tvDetailTotalDeaths = findViewById(R.id.number_totalDeathsDetails);
        tvDetailTodayDeaths = findViewById(R.id.number_todayDeathsDetails);
        tvDetailTotalCases = findViewById(R.id.number_totalCasesDetails);
        tvDetailTodayCases = findViewById(R.id.number_todayCasesDetails);
        tvDetailTotalRecovered = findViewById(R.id.number_totalRecoveredDetails);
        tvDetailTotalActive = findViewById(R.id.number_totalActive);
        tvDetailTotalCritical = findViewById(R.id.number_totalCriticalDetails);

        CovidCountry covidCountry = getIntent().getParcelableExtra("EXTRA_COVID");

        tvDetailCountryName.setText(covidCountry.getmCovidCountry());
        tvDetailTotalDeaths.setText(covidCountry.getmDeaths());
        tvDetailTodayDeaths.setText(covidCountry.getmTodayDeaths());
        tvDetailTotalCases.setText(covidCountry.getmCases());
        tvDetailTodayCases.setText(covidCountry.getmTodayCases());
        tvDetailTotalRecovered.setText(covidCountry.getmRecovered());
        tvDetailTotalActive.setText(covidCountry.getmActive());
        tvDetailTotalCritical.setText(covidCountry.getmCritical());

    }
}
