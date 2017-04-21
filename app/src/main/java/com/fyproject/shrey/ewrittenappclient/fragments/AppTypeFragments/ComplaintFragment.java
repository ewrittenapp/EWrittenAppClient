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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fyproject.shrey.ewrittenappclient.R;
import com.fyproject.shrey.ewrittenappclient.activity.NewApplication;
import com.fyproject.shrey.ewrittenappclient.adapter.spDataAdapter;
import com.fyproject.shrey.ewrittenappclient.helper.Converter;
import com.fyproject.shrey.ewrittenappclient.model.FacultyList;
import com.fyproject.shrey.ewrittenappclient.model.StudentProfile;
import com.fyproject.shrey.ewrittenappclient.model.WAppBonafide;
import com.fyproject.shrey.ewrittenappclient.model.WAppComplaint;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ComplaintFragment extends Fragment {

    Spinner spSelectFaculty;
    Spinner spComplaintType;
    RadioGroup rgLevel;
    EditText etDescription;
    TextView tvShowFileName;
    ImageView ivFileUpload;
    Button btnSubmit;

    private WAppComplaint complaintApp;
    Converter converter=new Converter();
    StudentProfile thisStudent = NewApplication.thisStudent;
    private DatabaseReference fbRoot;
    final String branch=converter.convertBranch(thisStudent.branch);
    final String sem=converter.convertSem(thisStudent.sem);
    final String Class=converter.convertClass(thisStudent.div);
    final String TAG="TAG";

    public ComplaintFragment() {
    }

    private void initialization(View view) {
        spSelectFaculty = (Spinner) view.findViewById(R.id.spSelectFaculty);
        spComplaintType = (Spinner) view.findViewById(R.id.spComplaintType);
        rgLevel = (RadioGroup) view.findViewById(R.id.rgLevel);
        rgLevel.check(R.id.rbMedium);
        etDescription = (EditText) view.findViewById(R.id.etDescription);
        tvShowFileName = (TextView) view.findViewById(R.id.tvShowFileName);
        ivFileUpload = (ImageView) view.findViewById(R.id.ivFileUpload);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        fbRoot= FirebaseDatabase.getInstance().getReference();
        complaintApp = new WAppComplaint(thisStudent,getActivity());
        complaintApp.level="medium";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wapptype_complaint, container, false);
            initialization(view);

        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(getActivity(),R.array.ComplaintType,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spComplaintType.setAdapter(adapter);

        spComplaintType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedItem = adapterView.getItemAtPosition(position).toString();
                complaintApp.complaintType = selectedItem;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        setSpinner();

        rgLevel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case 0:
                        complaintApp.level="severe";
                        break;

                    case 1:
                        complaintApp.level="medium";
                        break;

                    case 2:
                        complaintApp.level="low";
                        break;
                    default:
                        complaintApp.level="low";
                }
            }
        });

        //Submit wApp
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInputData(view)) {  //send written application
                    complaintApp.message=etDescription.getText().toString(); //set Message content

                    String to_path = "/applicationsNode/" + complaintApp.toUid;
                    String from_path = "/applicationsNode/" + complaintApp.fromUid;
                    String appId = fbRoot.child(from_path).push().getKey();
                    complaintApp.wAppId=appId; //set wApp Id explicitly

                    Map<String, Object> updatePaths = new HashMap<String, Object>();
                    updatePaths.put(to_path + "/" + appId, complaintApp);
                    updatePaths.put(from_path + "/" + appId, complaintApp);

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
        if(complaintApp.toName==null){
            Log.d(TAG, "validateInputData: receiver not selected");
            Snackbar.make(v,"Please select a receiver",Snackbar.LENGTH_SHORT).show();
            return false;
        }

        //get etReason, and check if it is written
        if(TextUtils.isEmpty(etDescription.getText().toString())){
            Snackbar.make(v,"Please describe your complaint",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        Log.d(TAG, "validateInputData: reason: "+etDescription.getText().toString());

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
                complaintApp.toName=selectedItem;
                complaintApp.toUid=officeList.ToUidList.get(position);
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


}
