package com.example.babyland;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {
    ArrayList list;
    String id;
    private recyclerVewOnClickListener listener;

    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_history_recycler, parent, false);
        if (id.equals("ilnessInput")) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_history_recycler, parent, false);
            recyclerAdapter.MyViewHolder view = new recyclerAdapter.MyViewHolder(itemView);
        }else if(id.equals("examination")){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_examination_list_items, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {
        if (id.equals("ilnessInput")) {
            List<FamilyHistoryIlnesses> lists = (List<FamilyHistoryIlnesses>) (List<?>) list;
            String name = lists.get(position).getIlness();
            holder.ilnessName.setText(name);
            String details = lists.get(position).getDetails();
            holder.ilnessDetails.setText(details);
            holder.ilnessDetails.setHint("Λεπτομέρειες....");
            Boolean sw = lists.get(position).getSwitches();
            if(sw){
                holder.switches.setChecked(true);
                holder.ilnessDetails.setVisibility(View.VISIBLE);

            }else{
                holder.switches.setChecked(false);
                holder.ilnessDetails.setVisibility(View.GONE);
            }
        }else if(id.equals("examination")){

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public recyclerAdapter(recyclerVewOnClickListener listener, ArrayList list, String id) {
        this.list = list;
        this.id=id;
        this.listener = listener;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView ilnessName, ilnessDetails;
        private Switch switches;


        public MyViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            if (id.equals("ilnessInput")) {
               // list = (ArrayList<FamilyHistoryIlnesses>)(ArrayList<?>) list;
                List<FamilyHistoryIlnesses> lists = (List<FamilyHistoryIlnesses>) (List<?>) list;
                ilnessName = itemView.findViewById(R.id.ilnessName);
                switches = itemView.findViewById(R.id.switches);
                ilnessDetails = itemView.findViewById(R.id.ilnessDetails);
                switches.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int  i = getAdapterPosition();
                        if(switches.isChecked()){
                            ilnessDetails.setVisibility(View.VISIBLE);
                        }else{
                            ilnessDetails.setVisibility(View.GONE);
                        }
                        FamilyHistoryIlnesses f1 = new FamilyHistoryIlnesses(lists.get(i).getIlness(), switches.isChecked(), lists.get(i).getDetails());
                        lists.set(i, f1);
                    }
                });
            }else if(id.equals("examination")){
                List<String> lists = (List<String>) (List<?>) list;
            }
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    public interface recyclerVewOnClickListener {
        void onClick(View view, int position);
    }
}

