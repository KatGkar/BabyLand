package com.example.babyland;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

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
        if (id.equals("illnessInput")) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_history_recycler, parent, false);
            recyclerAdapter.MyViewHolder view = new recyclerAdapter.MyViewHolder(itemView);
        }else if(id.equals("examination")){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.examination_list_item, parent, false);
        }else if(id.equals("developmental")){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.developmental_list_item, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {
        if (id.equals("illnessInput")) {
            List<FamilyHistoryIllnesses> lists = (List<FamilyHistoryIllnesses>) (List<?>) list;
            String name = lists.get(position).getIllness();
            holder.illnessName.setText(name);
            String details = lists.get(position).getDetails();
            holder.illnessDetails.setText(details);
            holder.illnessDetails.setHint("Λεπτομέρειες....");
            Boolean sw = lists.get(position).getSwitches();
            if(sw){
                holder.switches.setChecked(true);
                holder.illnessDetails.setVisibility(View.VISIBLE);

            }else{
                holder.switches.setChecked(false);
                holder.illnessDetails.setVisibility(View.GONE);
            }
        }else if(id.equals("examination")){
            List<String> lists = (List<String>) (List<?>) list;
            String name = lists.get(position);
            holder.examinationListItemText.setText(name);
        }else if(id.equals("developmental")){
            List<String> lists = (List<String>) (List<?>) list;
            String name = lists.get(position);
            holder.developmentalListItemText.setText(name);
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
        private TextView illnessName, illnessDetails, examinationListItemText, developmentalListItemText;
        private Switch switches;
        private RadioButton radioButton1, radioButton2, radioButton3;
        private RadioGroup radioGroup;
        private EditText developmentalEditText;
        private CardView card;
        boolean i=true;

        public MyViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            if (id.equals("illnessInput")) {
                List<FamilyHistoryIllnesses> lists = (List<FamilyHistoryIllnesses>) (List<?>) list;
                illnessName = itemView.findViewById(R.id.ilnessName);
                switches = itemView.findViewById(R.id.switches);
                illnessDetails = itemView.findViewById(R.id.ilnessDetails);
                switches.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int  i = getAdapterPosition();
                        if(switches.isChecked()){
                            illnessDetails.setVisibility(View.VISIBLE);
                        }else{
                            illnessDetails.setVisibility(View.GONE);
                        }
                        FamilyHistoryIllnesses f1 = new FamilyHistoryIllnesses(lists.get(i).getIllness(), switches.isChecked(), lists.get(i).getDetails());
                        lists.set(i, f1);
                    }
                });
            }else if(id.equals("examination")){
                List<String> lists = (List<String>) (List<?>) list;
                examinationListItemText = itemView.findViewById(R.id.examinationListItemText);
                radioButton1 = itemView.findViewById(R.id.radioButton1);
                radioButton2 = itemView.findViewById(R.id.radioButton2);
                radioButton3 = itemView.findViewById(R.id.radioButton3);
                radioGroup = itemView.findViewById(R.id.radioGroupExaminationList);
                radioGroup.setVisibility(itemView.GONE);
                examinationListItemText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         if(!i){
                            radioGroup.setVisibility(itemView.GONE);
                            i=true;
                        }else{
                            radioGroup.setVisibility(itemView.VISIBLE);
                            i=false;
                        }
                    }
                });
            }else if(id.equals("developmental")){
                List<String> lists = (List<String>) (List<?>) list;
                developmentalListItemText = itemView.findViewById(R.id.developmentalListItemText);
                developmentalEditText = itemView.findViewById(R.id.developmentalEditText);
                card = itemView.findViewById(R.id.cardViewId);
                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!i){
                            developmentalEditText.setVisibility(itemView.GONE);
                            i=true;
                        }else{
                            developmentalEditText.setVisibility(itemView.VISIBLE);
                            i=false;
                        }
                    }
                });
                developmentalListItemText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!i){
                            developmentalEditText.setVisibility(itemView.GONE);
                            i=true;
                        }else{
                            developmentalEditText.setVisibility(itemView.VISIBLE);
                            i=false;
                        }
                    }
                });
            }
        }

        @Override
        public void onClick(View v) {
            try{
                listener.onClick(v, getAdapterPosition());
            }catch (Exception e){
                System.out.println("------------------------------------" + e.getCause());
            }
        }
    }

    public interface recyclerVewOnClickListener {
        void onClick (View view,int position);
    }
}

