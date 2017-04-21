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
import android.widget.TextView;
import android.widget.Toast;

import com.fyproject.shrey.ewrittenappclient.R;
import com.fyproject.shrey.ewrittenappclient.activity.NewApplication;
import com.fyproject.shrey.ewrittenappclient.helper.Converter;
import com.fyproject.shrey.ewrittenappclient.model.FacultyList;
import com.fyproject.shrey.ewrittenappclient.model.StudentProfile;
import com.fyproject.shrey.ewrittenappclient.model.WAppComplaint;
import com.fyproject.shrey.ewrittenappclient.model.WAppInfrastructure;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfrastructureFragment extends Fragment {

    Spinner spSelectFaculty;
    EditText etLocation;
    Spinner spIssueType;
    EditText etMessage;
    TextView tvShowFileName;
    ImageView ivFileUpload;
    Button btnSubmit;

    private WAppInfrastructure infrastructureApp;
    Converter converter=new Converter();
    StudentProfile thisStudent = NewApplication.thisStudent;
    private DatabaseReference fbRoot;
    final String branch=converter.convertBranch(thisStudent.branch);
    final String sem=converter.convertSem(thisStudent.sem);
    final String Class=converter.convertClass(thisStudent.div);
    final String TAG="TAG";

    private void initialization(View view) {
        spSelectFaculty = (Spinner) view.findViewById(R.id.spSelectFaculty);
        etLocation = (EditText) view.findViewById(R.id.etLocation);
        spIssueType = (Spinner) view.findViewById(R.id.spIssueType);
        etMessage = (EditText) view.findViewById(R.id.etMessage);
        tvShowFileName = (TextView) view.findViewById(R.id.tvShowFileName);
        ivFileUpload = (ImageView) view.findViewById(R.id.ivFileUpload);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        fbRoot= FirebaseDatabase.getInstance().getReference();
        infrastructureApp= new WAppInfrastructure(thisStudent,getActivity());
    }

    public InfrastructureFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wapptype_infrastructure, container, false);
        initialization(view);
        setIssueTypeSpinner();
        setRecipientSpinner();

        //Submit application
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInputData(view)) {  //send written application
                    infrastructureApp.message=etMessage.getText().toString(); //set Message content
                    infrastructureApp.location=etLocation.getText().toString();

                    String to_path = "/applicationsNode/" + infrastructureApp.toUid;
                    String from_path = "/applicationsNode/" + infrastructureApp.fromUid;
                    String appId = fbRoot.child(from_path).push().getKey();
                    infrastructureApp.wAppId=appId; //set wApp Id explicitly

                    Map<String, Object> updatePaths = new HashMap<String, Object>();
                    updatePaths.put(to_path + "/" + appId, infrastructureApp);
                    updatePaths.put(from_path + "/" + appId, infrastructureApp);

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
        if(infrastructureApp.toName==null){
            Log.d(TAG, "validateInputData: receiver not selected");
            Snackbar.make(v,"Please select a receiver",Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if(infrastructureApp.issueType==null){
            Snackbar.make(v,"Please select an issue type",Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(etLocation.getText().toString())){
            Snackbar.make(v,"Location not specified",Snackbar.LENGTH_SHORT).show();
            return false;

        }
        if(TextUtils.isEmpty(etMessage.getText().toString())){
            Snackbar.make(v,"Please describe your complaint",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        Log.d(TAG, "validateInputData: reason: "+etMessage.getText().toString());

        return true;
    }

    private void setIssueTypeSpinner(){
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(getActivity(),R.array.IssueType,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spIssueType.setAdapter(adapter);

        spIssueType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedItem = adapterView.getItemAtPosition(position).toString();
                infrastructureApp.issueType= selectedItem;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setRecipientSpinner(){

        //final List<String> facultyNamesList = new ArrayList<>();
        final List<String> facultyUidList = new ArrayList<>();
        final ArrayAdapter<String> spAdapter= new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item);
        //Fetch faculty
        fbRoot.child("/facultyList/"+branch).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    FacultyList faculty= ds.getValue(FacultyList.class);
                    spAdapter.add(faculty.getName());
                    facultyUidList.add(ds.getKey());
                    Log.d(TAG, "faculty fetched: "+faculty.getName()+" | "+ds.getKey());
                }
                spAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: facultyNamesList Fetch"+databaseError);
            }
        });

        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSelectFaculty.setAdapter(spAdapter);

        //faculty select
        spSelectFaculty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedItem = adapterView.getItemAtPosition(position).toString();
                Log.d(TAG, selectedItem+" | "+facultyUidList.get(position));
                infrastructureApp.toName=selectedItem;
                infrastructureApp.toUid=facultyUidList.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }


}
