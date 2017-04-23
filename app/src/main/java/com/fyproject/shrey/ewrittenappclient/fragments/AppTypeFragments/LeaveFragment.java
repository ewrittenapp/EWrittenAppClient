package com.fyproject.shrey.ewrittenappclient.fragments.AppTypeFragments;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
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
import com.fyproject.shrey.ewrittenappclient.helper.DatePickerFragment;
import com.fyproject.shrey.ewrittenappclient.model.FacultyList;
import com.fyproject.shrey.ewrittenappclient.model.StudentProfile;
import com.fyproject.shrey.ewrittenappclient.model.WAppLeave;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by shrey on 07/03/17.
 */

public class LeaveFragment extends Fragment {

    Spinner spSelectFaculty;
    private ImageView ivStartDatePicker;
    private ImageView ivEndDatePicker;
    private TextView tvShowStartDate;
    private TextView tvShowEndDate;
    private TextView tvShowFileName;
    private EditText etReason;
    private Button btnUploadFile;
    private Button btnSubmit;
    private DatePickerFragment startDateFragment = new DatePickerFragment();
    private DatePickerFragment endDateFragment = new DatePickerFragment();
    FragmentManager fm;

    private DatabaseReference fbRoot;
    private FirebaseStorage fbstorage;
    private StorageReference storageRef;
    private Uri fileUri;
    private String extension;

    private WAppLeave leaveApp;
    Calendar startDate;
    Calendar endDate;
    Converter converter = new Converter();
    StudentProfile thisStudent = NewApplication.thisStudent;
    final String branch = converter.convertBranch(thisStudent.branch);
    final String sem = converter.convertSem(thisStudent.sem);
    final String Class = converter.convertClass(thisStudent.div);
    final String TAG = "TAG";
    private static final int FILE_SELECT_CODE = 0;
    private static final int STARTDATE_FRAG_REQUEST_CODE = 1;
    private static final int ENDDATE_FRAG_REQUEST_CODE = 2;

    private void initialization(View view) {
        btnUploadFile = (Button) view.findViewById(R.id.btnUpload);
        spSelectFaculty = (Spinner) view.findViewById(R.id.spSelectFaculty);
        ivStartDatePicker = (ImageView) view.findViewById(R.id.ivStartDatePicker);
        ivEndDatePicker = (ImageView) view.findViewById(R.id.ivEndDatePicker);
        tvShowStartDate = (TextView) view.findViewById(R.id.tvShowStartDate);
        tvShowEndDate = (TextView) view.findViewById(R.id.tvShowEndDate);
        tvShowFileName = (TextView) view.findViewById(R.id.tvShowFileName);
        etReason = (EditText) view.findViewById(R.id.etReason);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        fbRoot = FirebaseDatabase.getInstance().getReference();
        fbstorage = FirebaseStorage.getInstance();
        storageRef = fbstorage.getReference();

        leaveApp = new WAppLeave(thisStudent, getActivity());//leave app initialized with student data
        fm = getActivity().getSupportFragmentManager();
        startDateFragment.setTargetFragment(this, STARTDATE_FRAG_REQUEST_CODE);
        endDateFragment.setTargetFragment(this, ENDDATE_FRAG_REQUEST_CODE);

    }

    public LeaveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: LEAVE APP");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wapptype_leave, container, false);

        initialization(view);
        setRecipientSpinner();

        //Date selection button event
        ivStartDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDateFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });
        ivEndDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endDateFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

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
                if (validateInputData(view)) {  //send written application
                    leaveApp.message = etReason.getText().toString(); //set Message content

                    String to_path = "/applicationsNode/" + leaveApp.toUid;
                    String from_path = "/applicationsNode/" + leaveApp.fromUid;
                    String appId = fbRoot.child(from_path).push().getKey();
                    leaveApp.wAppId = appId; //set wApp Id explicitly

                    if (fileUri != null) {
                        // Save the file at "appId" location
                        StorageReference fbpath = null;
                        if (extension.equals("")) {
                            fbpath = storageRef.child(appId);
                            leaveApp.attachedFile = appId;
                        } else {
                            fbpath = storageRef.child(appId + "." + extension);
                            leaveApp.attachedFile = appId + "." + extension;
                        }
                        //leaveApp.attachedFile = fbpath.getDownloadUrl().toString();
                        Log.d(TAG, "cloud file path: " + fbpath.getPath());

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
                    updatePaths.put(to_path + "/" + appId, leaveApp);
                    updatePaths.put(from_path + "/" + appId, leaveApp);


                    fbRoot.updateChildren(updatePaths, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError error, DatabaseReference databaseReference) {
                            if (error == null) {
                                Toast.makeText(getContext(), "application sent!", Toast.LENGTH_SHORT).show();
                                if (fileUri == null) getActivity().finish();
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
            case STARTDATE_FRAG_REQUEST_CODE:   // set start date
                tvShowStartDate.setText(data.getStringExtra("tvDateData"));
                startDate = (Calendar) startDateFragment.getDate();
                leaveApp.startDate = DateFormat.getDateInstance().format(startDate.getTime());
                Log.d(TAG, "onActivityResult: startDate: " + startDate.getTime());
                break;

            case ENDDATE_FRAG_REQUEST_CODE:     //set end date
                tvShowEndDate.setText(data.getStringExtra("tvDateData"));
                endDate = (Calendar) endDateFragment.getDate();
                leaveApp.endDate = DateFormat.getDateInstance().format(endDate.getTime());
                Log.d(TAG, "onActivityResult: endDate: " + endDate.getTime());
                break;

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

    private boolean validateInputData(View v) {

        // validate date input
        if (startDate == null || endDate == null) { // dates not set
            Log.d(TAG, "validateInputData: start or end date input is null");
            Snackbar.make(v, "Please select dates", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (startDate.after(endDate)) { // if start date is after end date startDate.compareTo(endDate)>0
            Log.d(TAG, "validateInputData: startDate is AFTER endDate");
            Snackbar.make(v, "start Date is AFTER end Date!", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        //Check if receiver is selected
        if (leaveApp.toName == null) {
            Log.d(TAG, "validateInputData: receiver not selected");
            Snackbar.make(v, "Please select a receiver", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        //get etReason, and check if it is written
        if (TextUtils.isEmpty(etReason.getText().toString())) {
            Snackbar.make(v, "Please type a reason", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        Log.d(TAG, "validateInputData: reason: " + etReason.getText().toString());

        Log.d(TAG, "**All Dates are set properly**");
        return true;
    }

    private void setRecipientSpinner() {

        //final List<String> facultyNamesList = new ArrayList<>();
        final List<String> facultyUidList = new ArrayList<>();
        final ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        //Fetch faculty
        fbRoot.child("/facultyList/" + branch).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    FacultyList faculty = ds.getValue(FacultyList.class);
                    spAdapter.add(faculty.getName());
                    facultyUidList.add(ds.getKey());
                    Log.d(TAG, "faculty fetched: " + faculty.getName() + " | " + ds.getKey());
                }
                spAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: facultyNamesList Fetch" + databaseError);
            }
        });

        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSelectFaculty.setAdapter(spAdapter);

        //faculty select
        spSelectFaculty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedItem = adapterView.getItemAtPosition(position).toString();
                Log.d(TAG, selectedItem + " | " + facultyUidList.get(position));
                leaveApp.toName = selectedItem;
                leaveApp.toUid = facultyUidList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

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


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: LEAVE APP");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: LEAVE APP ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: LEAVE APP");
    }

}






