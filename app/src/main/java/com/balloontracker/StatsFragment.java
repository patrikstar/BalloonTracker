package com.balloontracker;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by patri_000 on 06.01.2017.
 */

public class StatsFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.stats_fragment, null);

        Button button = (Button) v.findViewById(R.id.btn_close);
        Button stats = (Button) v.findViewById(R.id.btn_stats);
         final TextView alt = (TextView) v.findViewById(R.id.tv_alt);
         final TextView lat = (TextView) v.findViewById(R.id.tv_lat);
         final TextView lng = (TextView) v.findViewById(R.id.tv_lng);
        final Bundle bundle = this.getArguments();
//        final String latitude =  bundle.getString("lat");

        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity().getApplicationContext(),latitude,Toast.LENGTH_SHORT).show();

//                alt.setText((CharSequence) bundle.get("alt"));
//                lat.setText("lol");
//                lng.setText("lol");
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(getTag(), "Button click in Fragment2");

//                Toast.makeText(getActivity().getApplicationContext(),"111111111111",Toast.LENGTH_SHORT).show();
//                getActivity().getFragmentManager().beginTransaction()..commit();
            getActivity().getFragmentManager().popBackStack();
            }

        });

        return v;
    }
}
