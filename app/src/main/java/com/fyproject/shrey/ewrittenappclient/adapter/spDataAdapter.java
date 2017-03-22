package com.fyproject.shrey.ewrittenappclient.adapter;

import android.util.Log;

import com.fyproject.shrey.ewrittenappclient.model.FacultyList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shrey on 22/03/17.
 */

public class spDataAdapter {
    public List<String> ToUidList = new ArrayList<>();
    public List<String> ToNameList = new ArrayList<>();

    public spDataAdapter(){
    }

    public void add(FacultyList fl){
        ToUidList.add(fl.Uid);
        ToNameList.add(fl.name);
    }

    public String getUid(String name){
        int index = ToNameList.indexOf(name);
        if(index == -1 ){
            Log.d("TAG", ">>>>>ERROR : spDataAdapter.getUid: no 'name' found");
            return null;
        }
        return ToUidList.get(index);
    }

    public boolean setHOD(String Uid){
        int index= ToUidList.indexOf(Uid);
        if(index == -1) return false; //HoD Uid Not available

        String hodName = ToNameList.get(index);
        ToNameList.set(index,hodName + "(HoD)");
        return true;
    }

    public List<String> getNameList(){
        return ToNameList;
    }


}
