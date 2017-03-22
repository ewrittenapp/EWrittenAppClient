package com.fyproject.shrey.ewrittenappclient.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.fyproject.shrey.ewrittenappclient.R;
import com.fyproject.shrey.ewrittenappclient.adapter.wAppViewAdapter;
import com.fyproject.shrey.ewrittenappclient.helper.SessionManager;
import com.fyproject.shrey.ewrittenappclient.model.FacultyProfile;
import com.fyproject.shrey.ewrittenappclient.model.StudentProfile;
import com.fyproject.shrey.ewrittenappclient.model.WAppBase;
import com.fyproject.shrey.ewrittenappclient.model.WAppLeave;
import com.fyproject.shrey.ewrittenappclient.model.rvStudentRow;

public class ViewApplicaion extends AppCompatActivity {

    private Toolbar toolbar;

    public static String USERTYPE;
    public static StudentProfile thisStudent;
    public static FacultyProfile thisFaculty;
    //public static WAppBase WAPP_DATA;
    public static WAppBase info;

    public SessionManager sessionManager;

    private wAppViewAdapter appViewAdapter;
    FragmentManager fragManager = getSupportFragmentManager();
    FragmentTransaction fragTransaction;

    final String TAG="TAG";

    private void InitVariables(){
        sessionManager=new SessionManager(this);
        USERTYPE= sessionManager.getUserType();
        if( USERTYPE.equals(getString(R.string.student)) )
            thisStudent= (StudentProfile) sessionManager.getCurrentUser();
        else if( USERTYPE.equals(getString(R.string.faculty)) )
            thisFaculty= (FacultyProfile) sessionManager.getCurrentUser();

        //get info about wApp from calling activity
        info= (WAppBase) getIntent().getSerializableExtra("WAPP_INFO");
        Log.d(TAG, "ViewApp intent Received: "+info);


        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_applicaion);
        InitVariables();

        final wAppViewAdapter appViewAdapter=new wAppViewAdapter(this);

        //check if application view fragment is present
        if ( appViewAdapter.getAppTypeFragment(info.getType()) != null ){

            if (getSupportActionBar()!=null)
                getSupportActionBar().setTitle(info.getType());
            Log.d(TAG, "Choosing wAppView   : "+info.getType());

            fragTransaction = fragManager.beginTransaction();
            fragTransaction.replace(R.id.wAppViewContainer, appViewAdapter.getAppTypeFragment(info.getType()));
            fragManager.popBackStack();
            fragTransaction.commit();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "ViewApplicaion onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "ViewApplicaion onDestroy: ");
    }


}
