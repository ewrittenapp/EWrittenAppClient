package com.fyproject.shrey.ewrittenappclient.fragments.WAppDisplayFragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fyproject.shrey.ewrittenappclient.R;
import com.fyproject.shrey.ewrittenappclient.activity.ViewApplicaion;
import com.fyproject.shrey.ewrittenappclient.model.WAppOrganizeEvent;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrganizeEventDisplayFragment extends Fragment {

    private TextView tvToName;
    private TextView tvFromName;
    private TextView tvFromInfo;
    private TextView tvEventName;
    private TextView tvFromDate;
    private TextView tvThrugh;
    private TextView tvTime;
    private TextView tvDescription;
    private TextView tvStatus;
    private TextView tvResponse;

    private Button btnFile;
    private Button btnAccept; //Faculty
    private Button btnReject; //Faculty

    private DatabaseReference fbRoot;

    private WAppOrganizeEvent eventApp;

    public String STUDENT;
    public String FACULTY;
    final String TAG="TAG";
    final String ACCEPT = "accepted";
    final String REJECT = "rejected";

    private void initialization(View v) {
        tvToName= (TextView) v.findViewById(R.id.tvToName);
        tvFromName= (TextView) v.findViewById(R.id.tvFromName);
        tvFromInfo= (TextView) v.findViewById(R.id.tvFromInfo);
        tvEventName= (TextView) v.findViewById(R.id.tvEventName);
        tvFromDate= (TextView) v.findViewById(R.id.tvFromDate);
        tvThrugh= (TextView) v.findViewById(R.id.tvThrugh);
        tvTime= (TextView) v.findViewById(R.id.tvTime);
        tvDescription= (TextView) v.findViewById(R.id.tvDescription);
        tvStatus= (TextView) v.findViewById(R.id.tvStatus);
        btnFile= (Button) v.findViewById(R.id.btnFile);
        btnAccept= (Button) v.findViewById(R.id.btnAccept);
        btnReject= (Button) v.findViewById(R.id.btnReject);
        tvResponse = (TextView) v.findViewById(R.id.tvResponse);
        STUDENT = getString(R.string.student);
        FACULTY = getString(R.string.faculty);
        eventApp = (WAppOrganizeEvent) ViewApplicaion.info;

        FirebaseDatabase.getInstance().getReference();

        tvToName.append(eventApp.toName);
        tvFromName.setText(eventApp.fromName);
        tvFromInfo.setText(eventApp.classInfo);
        tvStatus.setText(eventApp.status.toUpperCase());
        tvEventName.append(eventApp.eventName);
        tvFromDate.append(eventApp.startDate);
        tvThrugh.append(eventApp.endDate);
        tvTime.append(eventApp.time);
        tvDescription.append(eventApp.message);

        //check user type and set UI accordingly
        if( ViewApplicaion.USERTYPE.equals(STUDENT) ){
            setUpStudentGUI(v);
        } else if( ViewApplicaion.USERTYPE.equals(FACULTY) ){
            setUpFacultyGUI();
        }
    }

    private void setUpStudentGUI(View v){
        btnAccept.setVisibility(View.GONE);
        btnReject.setVisibility(View.GONE);
    }

    private void setUpFacultyGUI() {  //FACULTY Display code
        tvToName.setVisibility(View.GONE);
        //Faculty responded
        if(eventApp.status.equals(ACCEPT) || eventApp.status.equals(REJECT)){
            btnReject.setVisibility(View.GONE);
            btnAccept.setVisibility(View.GONE);
        } else {
            //Faculty Not yet responded
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UpdateStatus(ACCEPT);
                }
            });

            btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    View dialogView =  LayoutInflater.from(getContext()).inflate(R.layout.dialog_response,null,false);
                    final EditText etResponseInput = (EditText) dialogView.findViewById(R.id.etResponseInput);

                    builder.setTitle("Confirm reject?");
                    builder.setPositiveButton("Reject", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if( !TextUtils.isEmpty(etResponseInput.getText()) )
                                eventApp.response = etResponseInput.getText().toString();

                            UpdateStatus(REJECT);
                        }
                    });
                    builder.setNegativeButton("Cancel",null);
                    builder.setView(dialogView);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
    }

    public OrganizeEventDisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_display_organize_event, container, false);
        initialization(view);

        return view;
    }

    public void UpdateStatus(String status){
        String wAppPath1="/applicationsNode/"+eventApp.toUid+"/"+eventApp.getwAppId();
        String wAppPath2="/applicationsNode/"+eventApp.fromUid+"/"+eventApp.getwAppId();

        Map<String, Object> updateStatus = new HashMap<String, Object>();
        eventApp.setStatus(status);
        updateStatus.put(wAppPath1+"/", eventApp);
        updateStatus.put(wAppPath2+"/", eventApp);

        fbRoot.updateChildren(updateStatus, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference databaseReference) {
                if (error == null){ //Success
                    Toast.makeText(getContext(), "application sent!", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    return;
                }
                Log.d(TAG, "updateStatus: ERRoR: " + error); //write failure
                Toast.makeText(getContext(), "failed to update status", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
