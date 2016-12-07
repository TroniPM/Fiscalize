package com.troni.fiscalize.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.troni.fiscalize.R;
import com.troni.fiscalize.activities.LibsActivity;
import com.troni.fiscalize.session.Session;

public class SobreFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SobreFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SobreFragment newInstance(String param1, String param2) {
        SobreFragment fragment = new SobreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_libs, container, false);


        Button btn = (Button) v.findViewById(R.id.button_libs);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity2(), LibsActivity.class);
                getActivity2().startActivity(intent);
            }
        });
        try {
            getActivity2().getActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
        }
        TextView textView = (TextView) v.findViewById(R.id.tx_versao);
        PackageInfo pInfo = null;
        String version = "";
        try {
            pInfo = getActivity2().getPackageManager().getPackageInfo(getActivity2().getPackageName(), 0);
        } catch (Exception e) {
        }
        try {
            version = pInfo.versionName;
        } catch (Exception e) {
            version = "...";
        }

        textView.setText(version);
        return v;
    }

    public Activity getActivity2() {
        return Session.currentActivity;
    }
}
