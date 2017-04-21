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
import com.fyproject.shrey.ewrittenappclient.model.WAppCustom;
import com.fyproject.shrey.ewrittenappclient.model.WAppLeave;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shrey on 08/03/17.
 */

public class CustomWAppFragment extends Fragment{

    Spinner spSelectFaculty;
    EditText etAppSubject;
    EditText etReason;
    TextView tvShowFileName;
    ImageView ivFileUpload;
    Button btnSubmit;

    private DatabaseReference fbRoot;
    private WAppCustom customApp;
    Converter converter=new Converter();
    StudentProfile thisStudent = NewApplication.thisStudent;
    final String branch=converter.convertBranch(thisStudent.branch);
    final String sem=converter.convertSem(thisStudent.sem);
    final String Class=converter.convertClass(thisStudent.div);
    final String TAG="TAG";
    private static final int FILE_SELECT_CODE = 0;

    private void initialization(View view) {
        spSelectFaculty = (Spinner) view.findViewById(R.id.spSelectFaculty);
        etAppSubject = (EditText) view.findViewById(R.id.etAppSubject);
        etReason = (EditText) view.findViewById(R.id.etReason);
        tvShowFileName = (TextView) view.findViewById(R.id.tvShowFileName);
        ivFileUpload = (ImageView) view.findViewById(R.id.ivFileUpload);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        fbRoot= FirebaseDatabase.getInstance().getReference();
        customApp=new WAppCustom(thisStudent,getActivity());//leave app initialized with student data
    }

    public CustomWAppFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wapptype_custom, container, false);
        initialization(view);
        setRecipientSpinner();

        //Submit application
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInputData(view)) {  //send written application
                    customApp.message=etReason.getText().toString(); //set Message content
                    customApp.subject=etAppSubject.getText().toString();

                    String to_path = "/applicationsNode/" + customApp.toUid;
                    String from_path = "/applicationsNode/" + customApp.fromUid;
                    String appId = fbRoot.child(from_path).push().getKey();
                    customApp.wAppId=appId; //set wApp Id explicitly

                    Map<String, Object> updatePaths = new HashMap<String, Object>();
                    updatePaths.put(to_path + "/" + appId, customApp);
                    updatePaths.put(from_path + "/" + appId, customApp);

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
        if(customApp.toName==null){
            Log.d(TAG, "validateInputData: receiver not selected");
            Snackbar.make(v,"Please select a receiver",Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(etAppSubject.getText().toString())){
            Snackbar.make(v,"subject empty",Snackbar.LENGTH_SHORT).show();
            return false;
        }

        //get etReason, and check if it is written
        if(TextUtils.isEmpty(etReason.getText().toString())){
            Snackbar.make(v,"description empty",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        Log.d(TAG, "validateInputData: reason: "+etReason.getText().toString());

        return true;
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
                customApp.toName=selectedItem;
                customApp.toUid=facultyUidList.get(position);
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

    @Override
    public void onPause() {
        super.onPause();
    }


}
