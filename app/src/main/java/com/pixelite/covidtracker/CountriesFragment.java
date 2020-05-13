package com.pixelite.covidtracker;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

;


/**
 * A simple {@link Fragment} subclass.
 */
public class CountriesFragment extends Fragment {

    RecyclerView rvCountries;
    ProgressBar countryBar;
    CovidCountryAdapter covidCountryAdapter;

    private static final String TAG = CountriesFragment.class.getSimpleName();
    List<CovidCountry> covidCountries;

    public CountriesFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_countries, container, false);

        setHasOptionsMenu(true);

        rvCountries = root.findViewById(R.id.recycler_countries);
        countryBar = root.findViewById(R.id.progress_circular_countries);
        rvCountries.setLayoutManager(new LinearLayoutManager(getActivity()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvCountries.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.line_divider));
        rvCountries.addItemDecoration(dividerItemDecoration);

        covidCountries = new ArrayList<>();

        getDataFromServer();
        
        return root;
    }

    private void showRecyclerView()
    {
        covidCountryAdapter = new CovidCountryAdapter(covidCountries, getActivity());
        rvCountries.setAdapter(covidCountryAdapter);
        countryBar.setVisibility(View.GONE);

        ItemClickSupport.addTo(rvCountries).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedCovidCountry(covidCountries.get(position));
            }
        });
    }

    private void showSelectedCovidCountry(CovidCountry covidCountry)
    {
        Intent covidCountryDetail = new Intent(getActivity(), CovidCountryDetail.class);
        covidCountryDetail.putExtra("EXTRA_COVID", covidCountry);
        startActivity(covidCountryDetail);
    }

    private void getDataFromServer()
    {
        String url = "https://disease.sh/v2/countries";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                if (response != null) {
                    Log.e(TAG, "onResponse: " + response);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            JSONObject countryInfo = data.getJSONObject("countryInfo");

                            covidCountries.add(new CovidCountry(data.getString("country"), data.getString("cases"),
                                    data.getString("todayCases"), data.getString("deaths"), data.getString("todayDeaths"),
                                    data.getString("recovered"), data.getString("active"),data.getString("critical"),
                                    countryInfo.getString("flag")
                                    ));
                        }
                        showRecyclerView();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
              }
            },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    countryBar.setVisibility(View.GONE);
                    Log.e(TAG, "onResponse:" + error);
                }

            });
        Volley.newRequestQueue(getActivity()).add(stringRequest);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(getActivity());
        searchView.setQueryHint("Search for a country");
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(covidCountryAdapter!=null){
                    covidCountryAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });

        searchItem.setActionView(searchView);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
