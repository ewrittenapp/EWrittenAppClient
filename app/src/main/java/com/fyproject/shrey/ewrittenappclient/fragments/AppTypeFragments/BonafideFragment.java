package com.fyproject.shrey.ewrittenappclient.fragments.AppTypeFragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.fyproject.shrey.ewrittenappclient.R;
import com.fyproject.shrey.ewrittenappclient.activity.NewApplication;
import com.fyproject.shrey.ewrittenappclient.activity.ViewApplicaion;
import com.fyproject.shrey.ewrittenappclient.adapter.spDataAdapter;
import com.fyproject.shrey.ewrittenappclient.helper.Converter;
import com.fyproject.shrey.ewrittenappclient.helper.SessionManager;
import com.fyproject.shrey.ewrittenappclient.model.FacultyList;
import com.fyproject.shrey.ewrittenappclient.model.StudentProfile;
import com.fyproject.shrey.ewrittenappclient.model.WAppBonafide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by shrey on 07/03/17.
 */

public class BonafideFragment extends Fragment {

    Spinner spSelectFaculty;
    EditText etReason;
    TextView tvShowFileName;
    ImageView ivFileUpload;
    Button btnSubmit;

    private WAppBonafide bonafideApp;
    Converter converter=new Converter();
    StudentProfile thisStudent = NewApplication.thisStudent;
    private DatabaseReference fbRoot;
    final String branch=converter.convertBranch(thisStudent.branch);
    final String sem=converter.convertSem(thisStudent.sem);
    final String Class=converter.convertClass(thisStudent.div);
    final String TAG="TAG";

    public BonafideFragment(){
        // Required empty public constructor
    }

    private void initialization(View view) {
        spSelectFaculty = (Spinner) view.findViewById(R.id.spSelectFaculty);
        etReason = (EditText) view.findViewById(R.id.etReason);
        tvShowFileName = (TextView) view.findViewById(R.id.tvShowFileName);
        ivFileUpload = (ImageView) view.findViewById(R.id.ivFileUpload);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        bonafideApp = new WAppBonafide(thisStudent,getActivity());//bonafide app initialized with student data
        fbRoot= FirebaseDatabase.getInstance().getReference();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_wapptype_bonafide, container, false);
        initialization(view);

        setSpinner();


        //Submit wApp
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInputData(view)) {  //send written application
                    bonafideApp.message=etReason.getText().toString(); //set Message content

                    String to_path = "/applicationsNode/" + bonafideApp.toUid;
                    String from_path = "/applicationsNode/" + bonafideApp.fromUid;
                    String appId = fbRoot.child(from_path).push().getKey();
                    bonafideApp.wAppId=appId; //set wApp Id explicitly

                    Map<String, Object> updatePaths = new HashMap<String, Object>();
                    updatePaths.put(to_path + "/" + appId, bonafideApp);
                    updatePaths.put(from_path + "/" + appId, bonafideApp);

                    fbRoot.updateChildren(updatePaths, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError error, DatabaseReference databaseReference) {
                            if (error == null){
                                Toast.makeText(getContext(), "application sent!", Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                                return; //Success
                            }
                            Log.d(TAG, "onComplete: ERRoR: " + error); //write failure
                            Toast.makeText(getContext(), "failed to send application", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });



        return view;
    }

    private boolean validateInputData(View v){

        //Check if receiver is selected
        if(bonafideApp.toName==null){
            Log.d(TAG, "validateInputData: receiver not selected");
            Snackbar.make(v,"Please select a receiver",Snackbar.LENGTH_SHORT).show();
            return false;
        }

        //get etReason, and check if it is written
        if(TextUtils.isEmpty(etReason.getText().toString())){
            Snackbar.make(v,"Please type a reason",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        Log.d(TAG, "validateInputData: reason: "+etReason.getText().toString());

        return true;
    }

    private void setSpinner(){
        final spDataAdapter officeList = new spDataAdapter();
        final ArrayAdapter<String> spAdapter= new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,officeList.getNameList());

        //Fetch principal
        String principal="/staffNode/principal";
        fbRoot.child(principal).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                if( ds == null ) return;
                FacultyList principal= ds.getValue(FacultyList.class);
                principal.setUid( ds.child("Uid").getValue(String.class) );
                principal.name = principal.name + " (principal)";
                officeList.add(principal);
                ////  spAdapter.add(principal.getName());
                spAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "fetch staff/principal onCancelled: "+databaseError);
                Toast.makeText(getContext(), "Unable to fetch all recipients", Toast.LENGTH_SHORT).show();

            }
        });

        //Fetch faculty
        fbRoot.child("/facultyList/"+branch).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    FacultyList faculty= ds.getValue(FacultyList.class);
                    faculty.setUid(ds.getKey());
                    officeList.add(faculty);
                    ////spAdapter.add(faculty.getName());
                    //ToUidList.add(ds.getKey());
                    Log.d(TAG, "faculty fetched: "+faculty.getName()+" | "+ds.getKey());
                }
                spAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: facultyNamesList Fetch"+databaseError);
            }
        });

        //set HOD
        String HOD="/facultyCoordinators/"+branch+"/HOD/";
        fbRoot.child(HOD).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                String HODKey= ds.getValue(String.class);
                if( officeList.setHOD(HODKey) ) {
                    Log.d(TAG, "HOD set successfully");
                    spAdapter.notifyDataSetChanged();
                }
                else Log.d(TAG, "HOD NOT SET ");


//                fbRoot.child("/facultyNode/"+HODKey).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        FacultyList Hod=dataSnapshot.getValue()
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Fetch office
        final String officePath="/staffNode/office";
        fbRoot.child(officePath).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if( dataSnapshot == null) return;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    FacultyList officeMember = ds.getValue(FacultyList.class);
                    officeMember.setUid(ds.getKey());
                    officeList.add(officeMember);
                    ////spAdapter.add(officeMember.getName());
                }
                spAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "fetch staff/office onCancelled: "+databaseError);
                Toast.makeText(getContext(), "Unable to fetch all recipients", Toast.LENGTH_SHORT).show();

            }
        });

        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSelectFaculty.setAdapter(spAdapter);

        //recipient select
        spSelectFaculty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedItem = adapterView.getItemAtPosition(position).toString();
                Log.d(TAG, selectedItem+" | "+officeList.ToUidList.get(position));
                bonafideApp.toName=selectedItem;
                bonafideApp.toUid=officeList.ToUidList.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

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
