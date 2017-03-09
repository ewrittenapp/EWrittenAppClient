package com.fyproject.shrey.ewrittenappclient.fragments.AppTypeFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fyproject.shrey.ewrittenappclient.R;

import java.util.zip.Inflater;

/**
 * Created by shrey on 07/03/17.
 */

public class BonafideFragment extends Fragment {
    public BonafideFragment(){
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_wapptype_bonafide, container, false);


        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
