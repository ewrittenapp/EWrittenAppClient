package com.fyproject.shrey.ewrittenappclient.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.fyproject.shrey.ewrittenappclient.R;
import com.fyproject.shrey.ewrittenappclient.fragments.WAppDisplayFragments.LeaveDisplayFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shrey on 19/03/17.
 */

public class wAppViewAdapter {
    private final List<Fragment> AppTypeList= new ArrayList<>();
    private final ArrayList<String>AppType= new ArrayList<>();
    private Context context;
    private final String TAG="TAG";

    public wAppViewAdapter(Context x){
        context=x;

        addFragment(x.getString(R.string.leave),new LeaveDisplayFragment());

    }

    public void addFragment(String name,Fragment fragment){
        AppType.add(name);
        AppTypeList.add(fragment);
    }

    public Fragment getAppTypeFragment(int index){
        return AppTypeList.get(index);
    }
    public Fragment getAppTypeFragment(String AppTypeName){
        int index=AppType.indexOf(AppTypeName);
        if(index == -1){ //App Type not found
            Log.d(TAG, "getAppTypeFragment: AppType NOT FOUND");
            Toast.makeText(context, "application type not supported yet", Toast.LENGTH_LONG).show();
            return null;
        }
        return AppTypeList.get(index);
    }

    public int getCount(){ return AppType.size(); }



}
