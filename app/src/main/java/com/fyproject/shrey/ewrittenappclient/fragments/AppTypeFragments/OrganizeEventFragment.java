package com.fyproject.shrey.ewrittenappclient.fragments.AppTypeFragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fyproject.shrey.ewrittenappclient.R;
import com.fyproject.shrey.ewrittenappclient.activity.NewApplication;
import com.fyproject.shrey.ewrittenappclient.adapter.spDataAdapter;
import com.fyproject.shrey.ewrittenappclient.helper.Converter;
import com.fyproject.shrey.ewrittenappclient.helper.DatePickerFragment;
import com.fyproject.shrey.ewrittenappclient.model.FacultyList;
import com.fyproject.shrey.ewrittenappclient.model.StudentProfile;
import com.fyproject.shrey.ewrittenappclient.model.WAppOrganizeEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

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
    EditText etMessage;
    Button btnSubmit;
    private DatePickerFragment startDateFragment = new DatePickerFragment();
    private DatePickerFragment endDateFragment = new DatePickerFragment();
    FragmentManager fm;

    private WAppOrganizeEvent eventApp;
    Calendar startDate;
    Calendar endDate;
    Converter converter=new Converter();
    StudentProfile thisStudent = NewApplication.thisStudent;
    private DatabaseReference fbRoot;
    final String branch=converter.convertBranch(thisStudent.branch);
    final String sem=converter.convertSem(thisStudent.sem);
    final String Class=converter.convertClass(thisStudent.div);
    final String TAG="TAG";
    private static final int FILE_SELECT_CODE = 0;
    private static final int STARTDATE_FRAG_REQUEST_CODE = 1;
    private static final int ENDDATE_FRAG_REQUEST_CODE = 2;

    private void initialization(View view) {
        spSelectFaculty = (Spinner) view.findViewById(R.id.spSelectFaculty);
        etEventName = (EditText) view.findViewById(R.id.etEventName);
        tvShowStartDate = (TextView) view.findViewById(R.id.tvShowStartDate);
        ivStartDatePicker = (ImageView) view.findViewById(R.id.ivStartDatePicker);
        tvShowEndDate = (TextView) view.findViewById(R.id.tvShowEndDate);
        ivEndDatePicker = (ImageView) view.findViewById(R.id.ivEndDatePicker);
//        tvShowStartTime = (TextView) view.findViewById(R.id.tvShowStartTime);
//        ivStartTimePicker = (ImageView) view.findViewById(R.id.ivStartTimePicker);
//        tvShowEndTime = (TextView) view.findViewById(R.id.tvShowEndTime);
//        ivEndTimePicker = (ImageView) view.findViewById(R.id.ivEndTimePicker);
        etMessage = (EditText) view.findViewById(R.id.etMessage);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        fbRoot= FirebaseDatabase.getInstance().getReference();
        eventApp= new WAppOrganizeEvent(thisStudent,getActivity());

        fm = getActivity().getSupportFragmentManager();
        startDateFragment.setTargetFragment(this,STARTDATE_FRAG_REQUEST_CODE);
        endDateFragment.setTargetFragment(this,ENDDATE_FRAG_REQUEST_CODE);
    }

    public OrganizeEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_wapptype_organize_event, container, false);
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

        //Submit wApp
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInputData(view)) {  //send written application
                    eventApp.eventName=etEventName.getText().toString();//set event title
                    eventApp.message=etMessage.getText().toString(); //set Message content

                    String to_path = "/applicationsNode/" + eventApp.toUid;
                    String from_path = "/applicationsNode/" + eventApp.fromUid;
                    String appId = fbRoot.child(from_path).push().getKey();
                    eventApp.wAppId=appId; //set wApp Id explicitly

                    Map<String, Object> updatePaths = new HashMap<String, Object>();
                    updatePaths.put(to_path + "/" + appId, eventApp);
                    updatePaths.put(from_path + "/" + appId, eventApp);

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
        if(eventApp.toName==null){
            Log.d(TAG, "validateInputData: receiver not selected");
            Snackbar.make(v,"Please select a receiver",Snackbar.LENGTH_SHORT).show();
            return false;
        }

        // validate date input
        if(startDate==null || endDate==null){ // dates not set
            Log.d(TAG, "validateInputData: start or end date input is null");
            Snackbar.make(v,"Please select dates",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        else if(startDate.after(endDate)){ // if start date is after end date startDate.compareTo(endDate)>0
            Log.d(TAG, "validateInputData: startDate is AFTER endDate");
            Snackbar.make(v,"start Date is AFTER end Date!",Snackbar.LENGTH_SHORT).show();
            return false;
        }

        //check etEventName
        if(TextUtils.isEmpty(etEventName.getText().toString())){
            Snackbar.make(v,"Please enter event name",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        //check etReason
        if(TextUtils.isEmpty(etMessage.getText().toString())){
            Snackbar.make(v,"Event description not entered",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        Log.d(TAG, "validateInputData: reason: "+etMessage.getText().toString());

        return true;
    }

    private void setRecipientSpinner(){
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
                eventApp.toName=selectedItem;
                eventApp.toUid=officeList.ToUidList.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case STARTDATE_FRAG_REQUEST_CODE:   // set start date
                tvShowStartDate.setText(data.getStringExtra("tvDateData"));
                startDate = (Calendar) startDateFragment.getDate();
                eventApp.startDate = DateFormat.getDateInstance().format(startDate.getTime());
                Log.d(TAG, "onActivityResult: startDate: "+startDate.getTime());
                break;

            case ENDDATE_FRAG_REQUEST_CODE:     //set end date
                tvShowEndDate.setText(data.getStringExtra("tvDateData"));
                endDate = (Calendar) endDateFragment.getDate();
                eventApp.endDate = DateFormat.getDateInstance().format(endDate.getTime());
                Log.d(TAG, "onActivityResult: endDate: "+endDate.getTime());
                break;

            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d(TAG, "File Uri: " + uri.toString());
                    // Get the path
                    File mFile=new File(uri.toString());
                    //String path = FileUtils.file(uri);
                    Log.d(TAG, "File Path: " + getRealPathFromURI(uri));
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getRealPathFromURI(Uri contentUri){
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



}
