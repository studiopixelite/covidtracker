package com.pixelite.covidtracker;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private TextView totalDeaths, totalCases, totalRecovered, totalActive, todayDeaths, todayCases, totalCountries;
    private ProgressBar homeBar;
    private View homeView;
    private PieChart homePieChart;
    private com.github.mikephil.charting.charts.PieChart pieChart;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        homeBar = root.findViewById(R.id.progress_circular_home);
        totalDeaths = root.findViewById(R.id.number_Deaths);
        totalCases = root.findViewById(R.id.number_Cases);
        totalRecovered = root.findViewById(R.id.number_Recovered);
        totalActive = root.findViewById(R.id.number_Active);
        todayDeaths = root.findViewById(R.id.number_todayDeaths);
        todayCases = root.findViewById(R.id.number_todayCases);
        totalCountries = root.findViewById(R.id.number_Countries);
        homeView = root.findViewById(R.id.view_Home);
        homePieChart = root.findViewById(R.id.home_Chart);
        pieChart = root.findViewById(R.id.mphome_Chart);

        getData();

        return root;
    }

    private void getData()
    {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = "https://disease.sh/v2/all";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                homeBar.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    totalDeaths.setText(jsonObject.getString("deaths"));
                    totalCases.setText(jsonObject.getString("cases"));
                    totalRecovered.setText(jsonObject.getString("recovered"));
                    totalActive.setText(jsonObject.getString("active"));
                    todayDeaths.setText(jsonObject.getString("todayDeaths"));
                    todayCases.setText(jsonObject.getString("todayCases"));
                    totalCountries.setText(jsonObject.getString("affectedCountries"));

                    showChart();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showError();
                Log.d("Error Response", error.toString());
            }
        });

        queue.add(stringRequest);
    }

    public void showError()
    {
        homeBar.setVisibility(View.GONE);
        Snackbar snackbar = Snackbar.make(homeView, "Check your internet connection then restart the application", Snackbar.LENGTH_LONG);
        snackbar.setDuration(15000);
        snackbar.setActionTextColor(getResources().getColor(R.color.colorMain));
        snackbar.setAction("DISMISS", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        snackbar.show();
    }

    public void showChart()
    {
        homePieChart.addPieSlice(new PieModel("Deaths", Integer.parseInt(totalDeaths.getText().toString())
                ,Color.parseColor("#F02C2C")));

        homePieChart.addPieSlice(new PieModel("Cases", Integer.parseInt(totalCases.getText().toString())
                ,Color.parseColor("#F5D503")));

        homePieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(totalRecovered.getText().toString())
                ,Color.parseColor("#4CAF4E")));

        homePieChart.addPieSlice(new PieModel("Active", Integer.parseInt(totalActive.getText().toString())
                ,Color.parseColor("#864CAF")));

        homePieChart.setContentDescription("Stats");
        homePieChart.setAnimationTime(3000);
        homePieChart.startAnimation();
    }

    public void showmpChart()
    {
        ArrayList<PieEntry> stats = new ArrayList<>();

        stats.add(new PieEntry(Integer.parseInt(totalDeaths.getText().toString()), "Deaths"));
        stats.add(new PieEntry(Integer.parseInt(totalCases.getText().toString()), "Cases"));
        stats.add(new PieEntry(Integer.parseInt(totalRecovered.getText().toString()), "Recovered"));

        PieDataSet pieDataSet = new PieDataSet(stats, "Stats");
        pieDataSet.setColors(Color.parseColor("#F02C2C"),
                Color.parseColor("#F5D503"), Color.parseColor("#4CAF4E"));
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(12.0f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText(null);
        pieDataSet.setVisible(true);
        pieChart.invalidate();
        pieChart.animate();
    }
}

