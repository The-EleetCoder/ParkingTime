package com.project.parkingtime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.MyViewHolder> {

    ArrayList<Inst_Data> list;
    public AdapterClass(ArrayList<Inst_Data> list)
    {
        this.list=list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_holder,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.id.setText(list.get(i).name);
        myViewHolder.desc.setText("Address: "+list.get(i).city+", "+list.get(i).state);
        myViewHolder.status.setText("No of slots open: "+(list.get(i).slots - list.get(i).count));
        myViewHolder.contact_info.setText("Contact info: "+list.get(i).email);
        myViewHolder.paisa.setText("Price/hour for one slot: ₹"+list.get(i).price);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id, desc,status,contact_info,paisa;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id=itemView.findViewById(R.id.inst_name_id);
            desc = itemView.findViewById(R.id.address);
            status = itemView.findViewById(R.id.status);
            contact_info = itemView.findViewById(R.id.contact_info);
            paisa = itemView.findViewById(R.id.paisa);
        }
    }
}

