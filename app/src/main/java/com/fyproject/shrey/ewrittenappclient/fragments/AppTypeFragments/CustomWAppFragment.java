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

/**
 * Created by shrey on 08/03/17.
 */

public class CustomWAppFragment extends Fragment{

    Spinner spSelectFaculty;
    EditText etAppSubject;
    TextView tvReason;
    TextView tvShowFileName;
    ImageView ivFileUpload;
    Button btnSubmit;

    public CustomWAppFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wapptype_custom, container, false);
        initialization(view);

        return view;
    }

    private void initialization(View view) {
        spSelectFaculty = (Spinner) view.findViewById(R.id.spSelectFaculty);
        etAppSubject = (EditText) view.findViewById(R.id.etAppSubject);
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
