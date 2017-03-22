package com.fyproject.shrey.ewrittenappclient.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fyproject.shrey.ewrittenappclient.R;
import com.fyproject.shrey.ewrittenappclient.adapter.rvStudentAdapter;
import com.fyproject.shrey.ewrittenappclient.helper.SessionManager;
import com.fyproject.shrey.ewrittenappclient.model.FacultyProfile;
import com.fyproject.shrey.ewrittenappclient.model.StudentProfile;
import com.fyproject.shrey.ewrittenappclient.model.WAppBase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FacultyMain extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference dbroot;
    private FirebaseAuth auth;
    private FacultyProfile thisFaculty;
    private SessionManager sessionManager;
    private RecyclerView rv_wAppList;
    private rvStudentAdapter rv_Adapter;
    private RecyclerView.LayoutManager rvLayoutManager;
    private List<WAppBase> rv_dataset = new ArrayList<>();
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
        thisFaculty= (FacultyProfile) sessionManager.getCurrentUser();
    }

    public FacultyMain() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_faculty_main, container, false);
        InitVariables(view);










        return view;
    }




}
