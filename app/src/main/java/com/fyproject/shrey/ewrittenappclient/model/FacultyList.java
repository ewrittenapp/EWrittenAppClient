package com.fyproject.shrey.ewrittenappclient.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by shrey on 09/03/17.
 */

@IgnoreExtraProperties
public class FacultyList {
    public String name;
    public String email;
    @Exclude public String Uid;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    @Exclude
    public String getUid() {
        return Uid;
    }

    @Exclude
    public void setUid(String uid) {
        Uid = uid;
    }
}
