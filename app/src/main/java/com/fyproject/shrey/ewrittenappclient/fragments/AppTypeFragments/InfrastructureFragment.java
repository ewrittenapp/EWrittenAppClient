package com.fyproject.shrey.ewrittenappclient.fragments.AppTypeFragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fyproject.shrey.ewrittenappclient.R;
import com.fyproject.shrey.ewrittenappclient.activity.NewApplication;
import com.fyproject.shrey.ewrittenappclient.helper.Converter;
import com.fyproject.shrey.ewrittenappclient.model.FacultyList;
import com.fyproject.shrey.ewrittenappclient.model.StudentProfile;
import com.fyproject.shrey.ewrittenappclient.model.WAppInfrastructure;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class InfrastructureFragment extends Fragment {

    Spinner spSelectFaculty;
    EditText etLocation;
    Spinner spIssueType;
    EditText etMessage;
    TextView tvShowFileName;
    Button btnSubmit;

    private Button btnUploadFile;
    FragmentManager fm;
    private FirebaseStorage fbstorage;
    private StorageReference storageRef;
    private Uri fileUri;
    private String extension;

    private static final int FILE_SELECT_CODE = 0;

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
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        fbRoot= FirebaseDatabase.getInstance().getReference();
        infrastructureApp= new WAppInfrastructure(thisStudent,getActivity());

        btnUploadFile = (Button) view.findViewById(R.id.btnUpload);
        fbstorage = FirebaseStorage.getInstance();
        storageRef = fbstorage.getReference();
        fm = getActivity().getSupportFragmentManager();
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

        btnUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
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


                    if (fileUri != null) {
                        // Save the file at "appId" location
                        StorageReference fbpath = null;
                        if (extension.equals("")) {
                            fbpath = storageRef.child(appId);
                            infrastructureApp.attachedFile = appId;
                        } else {
                            fbpath = storageRef.child(appId + "." + extension);
                            infrastructureApp.attachedFile = appId + "." + extension;
                        }

                        UploadTask upload = fbpath.putFile(fileUri);
                        upload.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e);
                                Toast.makeText(getActivity(), "File Upload fail", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getContext(), "File uploaded successfully!", Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            }
                        });
                    }

                    Map<String, Object> updatePaths = new HashMap<String, Object>();
                    updatePaths.put(to_path + "/" + appId, infrastructureApp);
                    updatePaths.put(from_path + "/" + appId, infrastructureApp);

                    fbRoot.updateChildren(updatePaths, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError error, DatabaseReference databaseReference) {
                            if (error == null){
                                Toast.makeText(getContext(), "application sent!", Toast.LENGTH_SHORT).show();
                                if(fileUri==null) getActivity().finish();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case FILE_SELECT_CODE:  // Uri of selected file is fetched here ...
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    fileUri = data.getData();
                    File mFile = new File(fileUri.toString());
                    tvShowFileName.setText(mFile.getName());
                    extension = FilenameUtils.getExtension(mFile.getPath());
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getActivity(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
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
