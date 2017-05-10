package com.fyproject.shrey.ewrittenappclient.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fyproject.shrey.ewrittenappclient.R;
import com.fyproject.shrey.ewrittenappclient.activity.ViewApplicaion;
import com.fyproject.shrey.ewrittenappclient.adapter.rvFacultyAdapter;
import com.fyproject.shrey.ewrittenappclient.helper.SessionManager;
import com.fyproject.shrey.ewrittenappclient.helper.WAppLog;
import com.fyproject.shrey.ewrittenappclient.model.FacultyProfile;
import com.fyproject.shrey.ewrittenappclient.model.WAppBase;
import com.fyproject.shrey.ewrittenappclient.model.WAppBonafide;
import com.fyproject.shrey.ewrittenappclient.model.WAppComplaint;
import com.fyproject.shrey.ewrittenappclient.model.WAppCustom;
import com.fyproject.shrey.ewrittenappclient.model.WAppInfrastructure;
import com.fyproject.shrey.ewrittenappclient.model.WAppLeave;
import com.fyproject.shrey.ewrittenappclient.model.WAppOrganizeEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class FacultyMain extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference dbroot;
    private FirebaseAuth auth;
    private FacultyProfile thisFaculty;
    private SessionManager sessionManager;
    private WAppLog wAppLog; //local log of wApp for new notification purpose
    private RecyclerView rv_wAppList;
    private rvFacultyAdapter rv_Adapter;
    private RecyclerView.LayoutManager rvLayoutManager;
    private List<WAppBase> rv_dataset = new ArrayList<>();
    private List<Boolean> rv_newIndicator = new ArrayList<>();
    private String TAG="TAG";
    private String no_support="written application type not supported";

    private void InitVariables(View v){
        auth= FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dbroot=database.getReference();
        wAppLog=new WAppLog(getContext());

        rv_wAppList= (RecyclerView) v.findViewById(R.id.rv_wAppList);
        rvLayoutManager=new LinearLayoutManager(getContext());
        rv_wAppList.setLayoutManager(rvLayoutManager);
        //set adapter for recycler view
        rv_Adapter = new rvFacultyAdapter(rv_dataset,rv_newIndicator,getContext());
        rv_wAppList.setAdapter(rv_Adapter);
        //set rv item animator
        rv_wAppList.setItemAnimator(new DefaultItemAnimator());
        //add divider line decoration
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        rv_wAppList.addItemDecoration(itemDecoration);

        sessionManager=new SessionManager(getContext());
        thisFaculty= (FacultyProfile) sessionManager.getCurrentUser();

//        try {
//            rootPath = new File(Environment.getExternalStorageDirectory(), "EWAPP/wAppLog");
//            if (!rootPath.exists()) {
//                rootPath.mkdirs();
//            }
//            wAppLocalList = new File(rootPath,"wAppListLog.json");
//            if(!wAppLocalList.exists()){
//                wAppLocalList.createNewFile();
//            }
//
//            FileWriter fWrite = new FileWriter(wAppLocalList);
//
////            JSONObject o = new JSONObject()
//
//
//        }catch (Exception e){
//            Log.d(TAG, "InitVariables Exception: "+e);
//        }

    }

    public FacultyMain() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_faculty_main, container, false);
        InitVariables(view);
        Log.d(TAG, "onCreateView: FacultyMain Frag ");

        //Add data to rv
        ChildEventListener childEventListener = dbroot.child("applicationsNode").child(thisFaculty.getUid())
                .addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot ds, String s) {
                        if(ds.getValue() == null) return;

                        WAppBase rowData;
                        String wAppType = ds.child("type").getValue(String.class);
                        Log.d(TAG, ">>>> App Type Fetch: "+wAppType);
                        switch (wAppType){ //fetch appropriate wApp and store it to rowData
                            case "LEAVE APPLICATION":
                                rowData = ds.getValue(WAppLeave.class);
                                Log.d(TAG, "LEAVE APPL : Added");
                                break;

                            case "BONAFIDE CERTIFICATE REQUEST":
                                rowData = ds.getValue(WAppBonafide.class);
                                break;

                            case "CUSTOM APPLICATION":
                                rowData = ds.getValue(WAppCustom.class);
                                break;

                            case "COMPLAINT APPLICATION":
                                rowData = ds.getValue(WAppComplaint.class);
                                break;

                            case "ORGANIZE EVENT APPLICATION":
                                rowData = ds.getValue(WAppOrganizeEvent.class);
                                break;

                            case "INFRASTRUCTURE APPLICATION":
                                rowData = ds.getValue(WAppInfrastructure.class);
                                break;

                            default:
                                rowData = ds.getValue(WAppBase.class);
                                Toast.makeText(getContext(),no_support, Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "listening for filling rv_row: "+no_support);
                        }
                        rowData.setwAppId(ds.getKey());

                        rv_dataset.add(0, rowData);
                        setRv_newIndicator(0, rowData);
                        rv_Adapter.notifyItemInserted(0);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot ds, String s) {
                        if(ds.getValue() == null) return;
                        WAppBase rowData;
                        String wAppType = ds.child("type").getValue(String.class);
                        switch (wAppType){ //fetch appropriate wApp and store it to rowData
                            case "LEAVE APPLICATION":
                                rowData = ds.getValue(WAppLeave.class);
                                Log.d(TAG, "LEAVE APPL : Added");
                                break;

                            case "BONAFIDE CERTIFICATE REQUEST":
                                rowData = ds.getValue(WAppBonafide.class);
                                break;

                            case "CUSTOM APPLICATION":
                                rowData = ds.getValue(WAppCustom.class);
                                break;

                            case "COMPLAINT APPLICATION":
                                rowData = ds.getValue(WAppComplaint.class);
                                break;

                            case "ORGANIZE EVENT APPLICATION":
                                rowData = ds.getValue(WAppOrganizeEvent.class);
                                break;

                            case "INFRASTRUCTURE APPLICATION":
                                rowData = ds.getValue(WAppInfrastructure.class);
                                break;

                            default:
                                rowData = ds.getValue(WAppBase.class);
                                Toast.makeText(getContext(),no_support, Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "listening for filling rv_row: "+no_support);
                        }
                        rowData.setwAppId(ds.getKey());

                        String k = ds.getKey();
                        int i;
                        for (i = 0; i < rv_dataset.size(); i++) {
                            if(rv_dataset.get(i).wAppId.equals(k)) break;
                        }//i contains index of key
                        rv_dataset.set(i,rowData);
                        rv_Adapter.notifyItemChanged(i);

                        Log.d(TAG, "WAPP CHANGED -- > "+rv_dataset.get(i));

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot ds) {
                        String k = ds.getKey();
                        int i;
                        for (i = 0; i < rv_dataset.size(); i++) {
                            if(rv_dataset.get(i).wAppId.equals(k)) break;
                        }//i contains index of key

                        WAppBase wapp = rv_dataset.remove(i);
                        rv_newIndicator.remove(i);
                        Log.d(TAG, "onChildRemoved: "+wapp);

                        rv_Adapter.notifyItemRemoved(i);
                        wAppLog.delete(k);
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: "+databaseError);
                    }

                });

        rv_Adapter.setOnItemClickListener(new rvFacultyAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View itemView, WAppBase rowData, int position) {
                Log.d(TAG, "onItemClick: "+position+") "+rowData);

                Intent info = new Intent(getActivity(), ViewApplicaion.class);
                info.putExtra("WAPP_INFO", rowData);
                startActivity(info);
                setNewIndicatorAsVisited(position,rowData);
            }

            @Override
            public void onItemLongClick(View itemView, WAppBase rowData, int position) {
                //Delete Item on Long click
                deleteItemFromList(rowData);

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


        return view;
    }

    private void setRv_newIndicator(int index,WAppBase wApp){
        String wAppId = wApp.getwAppId();
        String status = wApp.getStatus();
        if(status.equals(getResources().getString(R.string.status_pending))){
            if( wAppLog.isNew(wAppId) ){
                rv_newIndicator.add(index,Boolean.TRUE);
                rv_Adapter.setNewIndicatorRing();
            }
            else rv_newIndicator.add(index,Boolean.FALSE);
        }
        else { // Status is not pending
            wAppLog.isNew(wAppId);
            rv_newIndicator.add(index,Boolean.FALSE);
        }

    }

    private void setNewIndicatorAsVisited(int index,WAppBase rowData){
        //if rowData at index is New, then mark it as visited
        if(rv_newIndicator.get(index)){
            wAppLog.update(rowData.getwAppId(),true);
            rv_newIndicator.set(index,Boolean.FALSE);
            rv_Adapter.notifyItemChanged(index);
            Log.d(TAG, "setNewIndicatorAsVisited: item at index "+index+" visited");
        }
    }

    private void deleteItemFromList(final WAppBase rowData){

        String msg = "Are you sure you want to delete "+rowData.type+" from "+rowData.fromName+" ?";
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm delete?");
        builder.setMessage(msg);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //DELETE SELECTED Item
                String path = "/applicationsNode/" + thisFaculty.getUid() + "/" + rowData.getwAppId();
                dbroot.child(path).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError error, DatabaseReference databaseReference) {
                        if(error == null){
                            Toast.makeText(getContext(), "Item Removed", Toast.LENGTH_SHORT).show();
                        }else{
                            Log.d(TAG, "onComplete: ERROR:: "+error);
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel",null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }



}





