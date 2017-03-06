package com.fyproject.shrey.ewrittenappclient.activity;

//This is created by SHREY

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fyproject.shrey.ewrittenappclient.R;
import com.fyproject.shrey.ewrittenappclient.fragments.FacultyMain;
import com.fyproject.shrey.ewrittenappclient.fragments.StudentMain;
import com.fyproject.shrey.ewrittenappclient.helper.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public final String TAG = "TAG";
    public String STUDENT;
    public String FACULTY;
    private boolean isFirebaseConnected = false;
    public static boolean persistanceEnabled = false;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference dbref;
    private SessionManager session;

    final FragmentManager fragmentManager = getSupportFragmentManager();

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        STUDENT = getString(R.string.student);
        FACULTY = getString(R.string.faculty);


        if (!persistanceEnabled) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            persistanceEnabled = true;
        }

        //session = new SessionManager(this);
        dbref = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //** CHECK USER STATUS **//
        authStateListener = new FirebaseAuth.AuthStateListener() {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String userType;

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user != null) { // User is signed in

                    final StudentMain studentMainFragment = new StudentMain();
                    final FacultyMain facultyMainFragment = new FacultyMain();

                    //Fetch user type
                    dbref.child("/userType/" + user.getUid() + "/").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            userType = dataSnapshot.getValue(String.class);
                            Log.d(TAG, "Fetch user type: " + userType);
                            if (userType == null) return;

                            //Check type of user
                            if (userType.equals(STUDENT)) {
                                if (getSupportActionBar() != null) {
                                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                                    getSupportActionBar().setTitle(R.string.student_home);
                                }

                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.replace(R.id.container, studentMainFragment);
                                fragmentManager.popBackStack();
                                transaction.commit();
                            } else if (userType.equals(FACULTY)) {
                                if (getSupportActionBar() != null) {
                                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                                    getSupportActionBar().setTitle(R.string.faculty_home);
                                }

                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.replace(R.id.container, facultyMainFragment);
                                fragmentManager.popBackStack();
                                transaction.commit();
                            } else if (userType == null) {  // no session value available
                                Log.d(TAG, "onAuthStateChanged: Unexpected:: no userType fetched");
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d(TAG, "USERTYPE read> onCancelled: " + databaseError);
                        }
                    });
                } else { // User is signed out (User is null)
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    Log.d(TAG, "user is null");
                    finish();
                }

            }
        };

        //Check weather app is online or offline
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                isFirebaseConnected = snapshot.getValue(Boolean.class);
                Log.d(TAG, "MainActivity onDataChange: FireBase Connection status: " + isFirebaseConnected);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d(TAG, "check connection Error: " + error);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.sign_out) {

            if (isFirebaseConnected) {
                //  session.ClearUserType();
                auth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                Log.d(TAG, "user signed out");
            } else Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
        Log.d(TAG, "MainActivity onStart: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "MainActivity onStop: ");
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }

}
