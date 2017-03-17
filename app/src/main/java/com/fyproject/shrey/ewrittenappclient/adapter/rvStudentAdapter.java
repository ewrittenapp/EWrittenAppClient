package com.fyproject.shrey.ewrittenappclient.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fyproject.shrey.ewrittenappclient.R;
import com.fyproject.shrey.ewrittenappclient.model.rvStudentRow;

import java.util.List;


/**
 * Created by shrey
 */

public class rvStudentAdapter extends RecyclerView.Adapter<rvStudentAdapter.MyViewHolder> {

    private List<rvStudentRow> listData;

    // Provide a reference to the views(in rv_row.xml) for each data item
    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView toName,appType,date,status;

        public MyViewHolder(View v){
            super(v);
            toName= (TextView) v.findViewById(R.id.tvToName);
            appType=(TextView) v.findViewById(R.id.tvAppType);
            date=(TextView) v.findViewById(R.id.tvDate);
            status=(TextView) v.findViewById(R.id.tvStatus);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public rvStudentAdapter(List<rvStudentRow> listing){
        this.listData=listing;
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
        rvStudentRow data=listData.get(position);
        holder.toName.setText(data.getToName());
        holder.appType.setText(data.getType());
        holder.date.setText(data.getDate_submitted());
        holder.status.setText(data.getStatus());  //Can check status here
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
