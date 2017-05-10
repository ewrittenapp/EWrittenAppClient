package com.fyproject.shrey.ewrittenappclient.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.fyproject.shrey.ewrittenappclient.R;
import com.fyproject.shrey.ewrittenappclient.fragments.WAppDisplayFragments.BonafideDisplayFragment;
import com.fyproject.shrey.ewrittenappclient.fragments.WAppDisplayFragments.ComplaintDisplayFragment;
import com.fyproject.shrey.ewrittenappclient.fragments.WAppDisplayFragments.CustomDisplayFragment;
import com.fyproject.shrey.ewrittenappclient.fragments.WAppDisplayFragments.InfrastructureDisplayFragment;
import com.fyproject.shrey.ewrittenappclient.fragments.WAppDisplayFragments.LeaveDisplayFragment;
import com.fyproject.shrey.ewrittenappclient.fragments.WAppDisplayFragments.OrganizeEventDisplayFragment;

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
        addFragment(x.getString(R.string.bonafide),new BonafideDisplayFragment());
        addFragment(x.getString(R.string.complaint),new ComplaintDisplayFragment());
        addFragment(x.getString(R.string.infrastructure),new InfrastructureDisplayFragment());
        addFragment(x.getString(R.string.organize_event),new OrganizeEventDisplayFragment());
        addFragment(x.getString(R.string.custom),new CustomDisplayFragment());

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
