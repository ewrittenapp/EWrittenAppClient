package com.fyproject.shrey.ewrittenappclient.fragments.WAppDisplayFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fyproject.shrey.ewrittenappclient.R;
import com.fyproject.shrey.ewrittenappclient.activity.ViewApplicaion;
import com.fyproject.shrey.ewrittenappclient.model.WAppBonafide;


public class BonafideDisplayFragment extends Fragment {

    public TextView tvTry;



    public BonafideDisplayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_display_bonafide, container, false);
        WAppBonafide bonafideApp = (WAppBonafide) ViewApplicaion.info;

        tvTry= (TextView) view.findViewById(R.id.tvTry);

        tvTry.setText(bonafideApp.classInfo+"\n\n"+bonafideApp.message);



        return view;



    }

}
