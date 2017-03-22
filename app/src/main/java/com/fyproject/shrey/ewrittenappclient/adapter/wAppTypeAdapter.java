package com.fyproject.shrey.ewrittenappclient.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import com.fyproject.shrey.ewrittenappclient.R;
import com.fyproject.shrey.ewrittenappclient.fragments.AppTypeFragments.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shrey on 07/03/17.
 */

public class wAppTypeAdapter {
    private final List<Fragment> AppTypeList= new ArrayList<>();
    private final ArrayList<String>AppType= new ArrayList<>();


    public wAppTypeAdapter(Context x){

        addFragment(x.getString(R.string.leave),new LeaveFragment());
        addFragment(x.getString(R.string.bonafide),new BonafideFragment());
        addFragment(x.getString(R.string.custom),new CustomWAppFragment());
        addFragment(x.getString(R.string.complaint),new ComplaintFragment());
        addFragment(x.getString(R.string.infrastructure),new InfrastructureFragment());
        addFragment(x.getString(R.string.organize_event),new OrganizeEventFragment());

    }

    public void addFragment(String name,Fragment fragment){
        AppType.add(name);
        AppTypeList.add(fragment);
    }

//    public List<String> getAppTypeNameList(){
//        return AppType;
//    }
    public String[] getAppTypeNameList(){
        //Returns String[] of ArrayList AppType, required for initializing spinner
        return AppType.toArray(new String[AppType.size()]);
    }

    public Fragment getAppTypeFragment(int index){
        return AppTypeList.get(index);
    }
    public Fragment getAppTypeFragment(String AppTypeName){
        return AppTypeList.get( AppType.indexOf(AppTypeName) );
    }

    public int getCount(){ return AppType.size(); }


}
