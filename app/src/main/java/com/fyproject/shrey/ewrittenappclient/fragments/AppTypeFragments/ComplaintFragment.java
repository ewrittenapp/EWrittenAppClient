package com.fyproject.shrey.ewrittenappclient.fragments.AppTypeFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.fyproject.shrey.ewrittenappclient.R;

public class ComplaintFragment extends Fragment {


    Spinner spSelectFaculty;
    Spinner spComplaintType;
    RadioGroup rgLevel;
    TextView tvDescription;
    TextView tvShowFileName;
    ImageView ivFileUpload;
    Button btnSubmit;



    public ComplaintFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wapptype_complaint, container, false);
            initialization(view);





        return view;
    }

    private void initialization(View view) {
        spSelectFaculty = (Spinner) view.findViewById(R.id.spSelectFaculty);
        spComplaintType = (Spinner) view.findViewById(R.id.spComplaintType);
        rgLevel = (RadioGroup) view.findViewById(R.id.rgLevel);
        tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        tvShowFileName = (TextView) view.findViewById(R.id.tvShowFileName);
        ivFileUpload = (ImageView) view.findViewById(R.id.ivFileUpload);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
    }

    //call these method according to your requirements
    private void fileUploadChooser(){
        ivFileUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //choose file and show the file name into the tvShowFileName
            }
        });
    }

    private void btnSubmitClickEvent(){
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //upload data to firebase
            }
        });
    }
}
