package com.fyproject.shrey.ewrittenappclient.activity;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.fyproject.shrey.ewrittenappclient.R;
import com.fyproject.shrey.ewrittenappclient.adapter.wAppTypeAdapter;
import com.fyproject.shrey.ewrittenappclient.helper.DatePickerFragment;
import com.fyproject.shrey.ewrittenappclient.helper.SessionManager;
import com.fyproject.shrey.ewrittenappclient.model.StudentProfile;

import java.util.List;

public class NewApplication extends AppCompatActivity {

    public static StudentProfile thisStudent;
    public SessionManager sessionManager;

    FragmentManager fragManager = getSupportFragmentManager();
    FragmentTransaction fragTransaction;

    final String TAG="TAG";

    private void InitVariables(){
        sessionManager=new SessionManager(this);
        thisStudent=sessionManager.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_application);

        InitVariables();


        //Main written type selection spinner
        Spinner spWAppType=(Spinner) findViewById(R.id.spWAppType);
        final wAppTypeAdapter appTypeAdapter=new wAppTypeAdapter(this);

        final String [] wAppTypeList= appTypeAdapter.getAppTypeNameList();
        final ArrayAdapter<String> spAdapter= new ArrayAdapter<String>(NewApplication.this,android.R.layout.simple_spinner_item,wAppTypeList);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWAppType.setAdapter(spAdapter);

        spWAppType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //Select written application type format here..
                String selectedItem= adapterView.getItemAtPosition(position).toString();

                fragTransaction = fragManager.beginTransaction();
                fragTransaction.replace(R.id.wAppTypeContainer, appTypeAdapter.getAppTypeFragment(selectedItem));
                fragManager.popBackStack();
                fragTransaction.commit();

                Toast.makeText(NewApplication.this, selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.application_format_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.sign_out) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
