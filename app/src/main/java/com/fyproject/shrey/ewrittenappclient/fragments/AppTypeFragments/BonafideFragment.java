package com.fyproject.shrey.ewrittenappclient.fragments.AppTypeFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fyproject.shrey.ewrittenappclient.R;
import com.fyproject.shrey.ewrittenappclient.activity.NewApplication;
import com.fyproject.shrey.ewrittenappclient.helper.SessionManager;
import com.fyproject.shrey.ewrittenappclient.model.StudentProfile;

import java.util.zip.Inflater;

/**
 * Created by shrey on 07/03/17.
 */

public class BonafideFragment extends Fragment {

    Spinner spSelectFaculty;
    TextView tvReason;
    TextView tvShowFileName;
    ImageView ivFileUpload;
    Button btnSubmit;

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
        initialization(view);

        return view;
    }

    private void initialization(View view) {
        spSelectFaculty = (Spinner) view.findViewById(R.id.spSelectFaculty);
        tvReason = (TextView) view.findViewById(R.id.tvReason);
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


    @Override
    public void onPause() {
        super.onPause();
    }

}
