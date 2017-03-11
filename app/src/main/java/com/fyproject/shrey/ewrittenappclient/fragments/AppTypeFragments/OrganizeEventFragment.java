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

public class OrganizeEventFragment extends Fragment {

    Spinner spSelectFaculty;
    EditText etEventName;
    TextView tvShowStartDate;
    ImageView ivStartDatePicker;
    TextView tvShowEndDate;
    ImageView ivEndDatePicker;
    TextView tvShowStartTime;
    ImageView ivStartTimePicker;
    TextView tvShowEndTime;
    ImageView ivEndTimePicker;
    TextView tvDescription;
    Button btnSubmit;


    public OrganizeEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_wapptype_organize_event, container, false);
        initialization(view);
        return view;
    }

    private void initialization(View view) {
        spSelectFaculty = (Spinner) view.findViewById(R.id.spSelectFaculty);
        etEventName = (EditText) view.findViewById(R.id.etEventName);
        tvShowStartDate = (TextView) view.findViewById(R.id.tvShowStartDate);
        ivStartDatePicker = (ImageView) view.findViewById(R.id.ivStartDatePicker);
        tvShowEndDate = (TextView) view.findViewById(R.id.tvShowEndDate);
        ivEndDatePicker = (ImageView) view.findViewById(R.id.ivEndDatePicker);
        tvShowStartTime = (TextView) view.findViewById(R.id.tvShowStartTime);
        ivStartTimePicker = (ImageView) view.findViewById(R.id.ivStartTimePicker);
        tvShowEndTime = (TextView) view.findViewById(R.id.tvShowEndTime);
        ivEndTimePicker = (ImageView) view.findViewById(R.id.ivEndTimePicker);
        tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
    }

}
