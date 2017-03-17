package com.fyproject.shrey.ewrittenappclient.fragments;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fyproject.shrey.ewrittenappclient.R;
import com.fyproject.shrey.ewrittenappclient.activity.NewApplication;
import com.fyproject.shrey.ewrittenappclient.adapter.rvStudentAdapter;
import com.fyproject.shrey.ewrittenappclient.helper.SessionManager;
import com.fyproject.shrey.ewrittenappclient.model.StudentProfile;
import com.fyproject.shrey.ewrittenappclient.model.rvStudentRow;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shrey on 02/03/17.
 */

public class StudentMain extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference dbroot;
    private FirebaseAuth auth;
    private StudentProfile thisStudent;
    private SessionManager sessionManager;
    private RecyclerView rv_wAppList;
    private rvStudentAdapter rv_Adapter;
    private RecyclerView.LayoutManager rvLayoutManager;
    private List<rvStudentRow> rv_dataset = new ArrayList<>();
    private String TAG="TAG";

    private void InitVariables(View v){
        auth= FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dbroot=database.getReference();

        rv_wAppList= (RecyclerView) v.findViewById(R.id.rv_wAppList);
        rvLayoutManager=new LinearLayoutManager(getContext());
        rv_wAppList.setLayoutManager(rvLayoutManager);
        //set adapter for recycler view
        rv_Adapter = new rvStudentAdapter(rv_dataset);
        rv_wAppList.setAdapter(rv_Adapter);
        //set rv item animator
        rv_wAppList.setItemAnimator(new DefaultItemAnimator());
        //add divider line decoration
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        rv_wAppList.addItemDecoration(itemDecoration);

        sessionManager=new SessionManager(getContext());
        thisStudent=sessionManager.getCurrentUser();
    }

    public StudentMain() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_student_main, container, false);
        InitVariables(view);
        Log.d(TAG, "onCreateView: StudentMain Frag ");
        dbroot.child("applicationsNode").child(thisStudent.getUid())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot ds, String s) {
                        rvStudentRow rowData=ds.getValue(rvStudentRow.class);
                        rowData.setwAppId(ds.getKey());
                        rv_dataset.add(0,rowData);
                        rv_Adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
/*
        //get previous applications data
        dbroot.child("applicationsNode").child(thisStudent.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot == null){
                    Log.d(TAG, "onDataChange: dataSnapshot is NULL");
                    return;
                }
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    rvStudentRow rowData=ds.getValue(rvStudentRow.class);
                    rowData.setwAppId(ds.getKey());
                    rv_dataset.add(0,rowData);
                }
                rv_Adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"get previous wapp data: onCancelled: "+databaseError);
            }
        });
*/

        //Create new app
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabMain);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), NewApplication.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: StudentMain Frag ");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: StudentMain Frag ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: StudentMain Frag ");
    }
}
