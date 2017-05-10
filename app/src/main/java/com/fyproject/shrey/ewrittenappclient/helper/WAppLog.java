package com.fyproject.shrey.ewrittenappclient.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by shrey on 08/05/17.
 */

public class WAppLog extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "WAppLog";
    private static final String TABLE_WAPP_LIST = "WAppList";

    private static final int FALSE = 0;
    private static final int TRUE = 1;

    // Table TABLE_WAPP_LIST Columns
    private static final String K_ID = "_id";
    private static final String K_WAPPID = "WappID";
    private static final String K_VIEWED = "Viewed";
    private static final String TAG = "LOG";


    public WAppLog(Context c){
        super(c,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_WAPP_LIST = "CREATE TABLE "+ TABLE_WAPP_LIST+"("+
                K_WAPPID+ " TEXT,"+K_VIEWED+" INTEGER"+")";

        sqLiteDatabase.execSQL(CREATE_TABLE_WAPP_LIST);
        Log.d(TAG, "onCreate: DATABASE OnCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_WAPP_LIST);
        onCreate(db);
    }

    public void dropTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_WAPP_LIST);
        onCreate(db);
    }

    //***  Public Methods to perform CRUD  *******************//

    public void insertNew (String wAppId,boolean isViewed){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(K_WAPPID,wAppId);
        if(isViewed) values.put(K_VIEWED,TRUE);
        else values.put(K_VIEWED,FALSE);

        db.insert(TABLE_WAPP_LIST,null,values);
        db.close();
    }

    public boolean containsEntry(String wAppId){
        String QUERY = "SELECT * FROM "+TABLE_WAPP_LIST+
                " WHERE "+K_WAPPID+" = \""+wAppId+"\" " ;
        boolean ans=false;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, null);
        if(cursor.getCount() != 0) ans=true;
        Log.d(TAG, "containsEntry: cursor.getCount(): "+cursor.getCount());
        cursor.close();
        db.close();

        return ans;
    }

    // Returns the K_VIEWED value of given wAppId. Returns true of no entry found
    public boolean isNew(String wAppId){
        String QUERY = "SELECT "+ K_VIEWED +" FROM "+TABLE_WAPP_LIST+
                " WHERE "+K_WAPPID+" = \""+wAppId+"\" " ;
        boolean ans=true;
        int viewedValue;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, null);

        if(cursor.getCount() != 0){ // row found (Entry exists)
            cursor.moveToFirst();
            viewedValue = cursor.getInt(0);
            if(viewedValue == FALSE) ans=true; //item not viewed, hence it is New
            else ans=false; // viewedValue=TRUE, item is viewed, hence it is Not New
        }
        else { //else, row not found, i.e. new ewapp; hence New
            //entry doesnot exist, so add it
            insertNew(wAppId,false);
        }

        cursor.close();
        db.close();
        Log.d(TAG, "isNew: "+wAppId+"  -- "+ans);
        return ans;
    }

    public void update(String wAppId,boolean isViewed){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if(isViewed) values.put(K_VIEWED,TRUE);
        else values.put(K_VIEWED,FALSE);

        int i= db.update(TABLE_WAPP_LIST,
                values,
                K_WAPPID+" =?",
                new String[]{String.valueOf(wAppId)});

        Log.d(TAG, TABLE_WAPP_LIST+" updated: "+i+" Rows Affected");
        db.close();
    }

    public void delete(String wAppId){
        SQLiteDatabase db = this.getWritableDatabase();

            int i=db.delete(TABLE_WAPP_LIST,
                    K_WAPPID+"=?",
                    new String[]{String.valueOf(wAppId)});
            Log.d(TAG, "delete: "+i+" rows");
        db.close();
    }


}
