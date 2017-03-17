package com.fyproject.shrey.ewrittenappclient.helper;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import com.fyproject.shrey.ewrittenappclient.fragments.AppTypeFragments.LeaveFragment;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by shrey on 08/03/17.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private Calendar cal;

    public DatePickerFragment(){
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(),this, year, month, day);

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        //NOTE: default month input starts with 0 (= Jan)
        Log.d("TAG", "DatePicker - onDateSet(jan=0):y/m/d  "+year+"/"+month+"/"+day);

        cal= Calendar.getInstance();
        cal.set(year,month,day,0,0,0); //calender class month starts with 0

        Log.d("TAG", "onDateSet: "+cal.getTime());
        //Now, return date info to calling activity
        Intent intent = new Intent();
        intent.putExtra("tvDateData" , day+"/"+(month+1)+"/"+year);
        getTargetFragment().onActivityResult(getTargetRequestCode(),0,intent);
    }

    public Object getDate(){
        return cal.clone();
    }



}
