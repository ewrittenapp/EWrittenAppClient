package com.fyproject.shrey.ewrittenappclient.fragments.AppTypeFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fyproject.shrey.ewrittenappclient.R;

public class InfrastructureFragment extends Fragment {

    Spinner spSelectFaculty;
    EditText etLocation;
    Spinner spLocationType;
    TextView tvDescription;
    TextView tvShowFileName;
    ImageView ivFileUpload;
    Button btnSubmit;

    public InfrastructureFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wapptype_infrastructure, container, false);

        initialization(view);
        return view;
    }

    private void initialization(View view) {
        spSelectFaculty = (Spinner) view.findViewById(R.id.spSelectFaculty);
        etLocation = (EditText) view.findViewById(R.id.etLocation);
        spLocationType = (Spinner) view.findViewById(R.id.spLocationType);
        tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        tvShowFileName = (TextView) view.findViewById(R.id.tvShowFileName);
        ivFileUpload = (ImageView) view.findViewById(R.id.ivFileUpload);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
    }

}
