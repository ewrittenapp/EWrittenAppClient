package com.fyproject.shrey.ewrittenappclient.fragments.AppTypeFragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.fyproject.shrey.ewrittenappclient.R;
import com.fyproject.shrey.ewrittenappclient.helper.DatePickerFragment;
import com.fyproject.shrey.ewrittenappclient.helper.SessionManager;
import com.fyproject.shrey.ewrittenappclient.model.FacultyList;
import com.fyproject.shrey.ewrittenappclient.model.StudentProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shrey on 07/03/17.
 */

public class LeaveFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    Spinner spSelectFaculty;
    private Button btnStartDatePicker;
    private Button btnEndDatePicker;
    DatePickerDialog datePickerDialog;

    private DatabaseReference fbRoot;
    private SessionManager session;
    StudentProfile thisStudent;
    private final List<String> facultyNamesList = new ArrayList<>();

    final String TAG="TAG";

    private void initialization(View view){
        spSelectFaculty= (Spinner) view.findViewById(R.id.spSelectFaculty);
        btnStartDatePicker= (Button) view.findViewById(R.id.btnStartDatePicker);
        btnEndDatePicker= (Button) view.findViewById(R.id.btnEndDatePicker);
        session=new SessionManager(getContext());
        thisStudent = session.getCurrentUser();

        fbRoot= FirebaseDatabase.getInstance().getReference();


    }

    public LeaveFragment(){
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_wapptype_leave, container, false);

        initialization(view);

        //Fetch faculty
        final String branch=thisStudent.branch.replaceAll("\\s",""); ;
        //branch=branch.replaceAll("\\s",""); //remove spaces
        Log.d(TAG, "onCreateView: Branch:"+branch);

        spSelectFaculty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fbRoot.child("/facultyNamesList/"+branch).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren()) {
                            FacultyList faculty= ds.getValue(FacultyList.class);
                            facultyNamesList.add(faculty.getName());
                            Log.d(TAG, "faculty fetched: "+faculty.getName());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: facultyNamesList Fetch"+databaseError);

                    }
                });
                String [] s=new String[facultyNamesList.size()];
                s= facultyNamesList.toArray(s);

                final ArrayAdapter<String> spAdapter= new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,facultyNamesList);
                spAdapter.notifyDataSetChanged();
                spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spSelectFaculty.setAdapter(spAdapter);




            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


//        fbRoot.child("/facultyNamesList/"+branch).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                FacultyList faculty= dataSnapshot.getValue(FacultyList.class);
//                facultyNamesList.add(faculty);
//                Log.d(TAG, "onChildAdded: Faculty:");
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });






        //Date selection button event
        btnStartDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                //datePickerDialog = (DatePickerDialog) newFragment.getDialog();

            }
        });
        btnEndDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(facultyNamesList.isEmpty()) Log.d(TAG, "onStart: facultyNamesList is Empty");
        else Log.d(TAG, "onStart: facultyNamesList is NOT Empty");

        for (int i = 0; i < facultyNamesList.size(); i++) {
            Log.d(TAG, "FacultyList: "+ facultyNamesList.get(i));

        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Log.d(TAG, "Leave frag: DatePicker - onDateSet: y/m/d  "+year+"/"+month+"/"+day);


    }


    @Override
    public void onPause() {
        super.onPause();
    }



}






