package com.pixelite.covidtracker;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements View.OnClickListener {

    private Button settingsButton;
    private Button donateButton;
    private Button aboutButton;

    private ImageView imageView;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view =  inflater.inflate(R.layout.fragment_menu_, container, false);

       settingsButton = view.findViewById(R.id.button_Settings);
       donateButton = view.findViewById(R.id.button_Donate);
       aboutButton = view.findViewById(R.id.button_About);

       settingsButton.setOnClickListener(this);
       donateButton.setOnClickListener(this);
       aboutButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.button_Settings:
                Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(settingsIntent);
                break;

            case R.id.button_Donate:
                Intent donateIntent = new Intent(getActivity(), DonateActivity.class);
                startActivity(donateIntent);
                break;

            case R.id.button_About:
                Intent aboutIntent = new Intent(getActivity(), AboutActivity.class);
                startActivity(aboutIntent);
                break;

        }
    }

}
