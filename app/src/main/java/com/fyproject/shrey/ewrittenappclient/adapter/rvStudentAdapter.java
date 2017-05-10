package com.fyproject.shrey.ewrittenappclient.adapter;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fyproject.shrey.ewrittenappclient.R;
import com.fyproject.shrey.ewrittenappclient.model.WAppBase;

import java.util.List;


/**
 * Created by shrey
 */

public class rvStudentAdapter extends RecyclerView.Adapter<rvStudentAdapter.MyViewHolder> {
    static { //For enabling usage of vector drawable images
        if(!AppCompatDelegate.isCompatVectorFromResourcesEnabled())
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    //WAppBase
    private List<WAppBase> listData;
    private List<Boolean> newIndicator;
    private Context context;
    public static boolean newIndicatorRing=true;
    private OnItemClickListener listener;

    /***** Creating OnItemClickListener *****/

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView,WAppBase rowData, int position);
        void onItemLongClick(View itemView,WAppBase rowData, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    /***** ***************************** *****/

    // Provide a reference to the views(in rv_row.xml) for each data item
    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView toName,appType,date,status;
        public AppCompatImageView ivNewMessage;

        public MyViewHolder(final View itemView){
            super(itemView);
            toName= (TextView) itemView.findViewById(R.id.tvToName);
            appType=(TextView) itemView.findViewById(R.id.tvAppType);
            date=(TextView) itemView.findViewById(R.id.tvDate);
            status=(TextView) itemView.findViewById(R.id.tvStatus);
            ivNewMessage= (AppCompatImageView) itemView.findViewById(R.id.ivNewMessage);
            ivNewMessage.setVisibility(View.GONE);

            // Setup the click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView,listData.get(position), position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.d("TAG", "onLongClick: ");
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemLongClick(itemView,listData.get(position), position);
                        }
                    }
                    return true;
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public rvStudentAdapter(List<WAppBase> listing,List<Boolean> newIndicatorList,Context c){
        this.listData=listing;
        this.newIndicator=newIndicatorList;
        this.context=c;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_student_row,parent,false);
        return new MyViewHolder(itemView);
    }

    // Replace the contents of a view(a row), bind data (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // get data from List<rvStudentRow> declared in this class
        WAppBase data=listData.get(position);
        holder.toName.setText(data.getToName());
        holder.appType.setText(data.getType());
        holder.date.setText(data.getDate_submitted());
        holder.status.setText(data.getStatus());  //Can check status here

        if(newIndicator.get(position)){
            holder.ivNewMessage.setVisibility(View.VISIBLE);
            ringUp();
        }else holder.ivNewMessage.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void setNewIndicatorRing(){
        newIndicatorRing=true;
    }
    public boolean getNewIndicatorRing(){
        return newIndicatorRing;
    }
    private void ringUp(){
        if(newIndicatorRing){
            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(context, notification);
                if(r.isPlaying())r.stop();
                r.play();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        newIndicatorRing=false;
    }
}
