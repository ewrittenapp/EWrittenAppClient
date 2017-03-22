package com.fyproject.shrey.ewrittenappclient.model;

import com.google.firebase.database.Exclude;

/**
 * Created by shrey on 20/03/17.
 */

public class UserProfile {
    public String email;
    @Exclude
    public String Uid;


}
