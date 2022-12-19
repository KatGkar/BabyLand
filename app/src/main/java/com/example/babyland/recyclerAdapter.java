package com.example.babyland;

import android.app.Activity;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {
    ArrayList list;
    String id;
    private recyclerVewOnClickListener listener;
    private radioButtonChange radioButtonChange;
    private textChange textChange;
    private sustenanceCheck sustenanceCheck;

    public interface sustenanceCheck{
        void sustenanceChecked(int position, Boolean value);
    }


    public interface textChange {
        void textChanged(int position, String text);
    }

    public void textChange(textChange textChange){
    this.textChange =textChange;
}

    public interface radioButtonChange{
        void rChange(int id, int position);
    }

    public void radioButtonChange(radioButtonChange radioButtonChange){
        this.radioButtonChange = radioButtonChange;
    }

    public void sustenanceCheck(sustenanceCheck sustenanceCheck){this.sustenanceCheck = sustenanceCheck;}


    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_history_recycler, parent, false);
        if (id.equals("illnessInput")) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_history_recycler, parent, false);
            recyclerAdapter.MyViewHolder view = new recyclerAdapter.MyViewHolder(itemView, radioButtonChange, textChange, sustenanceCheck);
        }else if(id.equals("examination")){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.examination_list_item, parent, false);
        }else if(id.equals("developmental")){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.developmental_list_item, parent, false);
        }else if(id.equals("sustenance")){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sustenance_list_item, parent, false);
        }
        else if(id.equals("developments")){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_developments, parent, false);
        }else if(id.equals("developmentalMonitoring")){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_developments_developmental_monitoring, parent, false);
        }else if(id.equals("exam")){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_developments_examination, parent, false);
        }else if(id.equals("sust")){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_developments_sustenance, parent, false);
        }else if(id.equals("deleteChild")){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_delete, parent, false);
        }
        return new MyViewHolder(itemView, radioButtonChange, textChange, sustenanceCheck);
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
            List<examinationItems> lists = (List<examinationItems>) (List<?>) list;
            String name = lists.get(position).getName();
            holder.examinationListItemText.setText(name);
            int rad = lists.get(position).getDetails();
            if(rad==1){
                holder.radioButton1.setChecked(true);
            }else if(rad==2){
                holder.radioButton2.setChecked(true);
            }else if(rad==3){
                holder.radioButton3.setChecked(true);
            }
        }else if(id.equals("developmental")){
            List<developmentalItems> lists = (List<developmentalItems>)  list;
            String name = lists.get(position).getName();
            holder.developmentalListItemText.setText(name);
            String details = lists.get(position).getDetails();
            if(details.length()>0) {
                holder.developmentalEditText.setText(details);
            }
        }else if(id.equals("developments")){
            List<Development> lists = (List<Development>) list;
            String age = lists.get(position).getAge() + " " + lists.get(position).getAgeType();
            String date = lists.get(position).getMeasurementDate();
            holder.ageTextView.setText(age);
            holder.dateTextView.setText(date);
        }else if(id.equals("developmentalMonitoring")){
            List<developmentalItems> lists = (List<developmentalItems>) list;
            String name = lists.get(position).getName();
            String details = lists.get(position).getDetails();
            holder.detailsDevelopmentalMonitoringTextView.setText(details);
            holder.nameDevelopmentalMonitoringTextView.setText(name);
            holder.nameDevelopmentalMonitoringTextView.setPaintFlags(holder.nameDevelopmentalMonitoringTextView.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        }else if(id.equals("exam")){
            List<examinationItems> lists = (List<examinationItems>) list;
            String name = lists.get(position).getName();
            int details = lists.get(position).getDetails();
            if(details==1){
                holder.nameExaminationTextView.setText(name + " : Normal");
            }else if(details==2){
                holder.nameExaminationTextView.setText(name + " : Monitoring");
            }else if(details ==3){
                holder.nameExaminationTextView.setText(name + " : Reference");
            }else {
                holder.nameExaminationTextView.setText(name + " : No information");
            }
        }else if(id.equals("sustenance")) {
            List<sustenanceItems> lists = (List<sustenanceItems>) list;
            String name  = lists.get(position).getName();
            holder.checkedSustenanceMonitoring.setText(name);
            Boolean v = lists.get(position).getChecked();
            holder.checkedSustenanceMonitoring.setChecked(v);
        }else if(id.equals("sust")){
            List<sustenanceItems> lists = (List<sustenanceItems>) list;
            String name = lists.get(position).getName();
            Boolean value = lists.get(position).getChecked();
            holder.checkShowDevelopments.setText(name);
            holder.checkShowDevelopments.setChecked(value);
        }else if(id.equals("deleteChild")){
            List<Baby> lists = (List<Baby>) list;
            String name = lists.get(position).getName();
            String birthDate = lists.get(position).getDateOfBirth();
            String amka = lists.get(position).getAmka();
            String sex = lists.get(position).getSex();
            holder.nameTextViewDelete.setText(name);
            holder.birthDateTextViewDelete.setText(birthDate);
            holder.amkaTextViewDelete.setText(amka);
            if(sex.equals("BOY")){
                Picasso.get().load(R.drawable.boy).into(holder.sexImageViewDelete);
            }else{
                Picasso.get().load(R.drawable.baby_girl).into(holder.sexImageViewDelete);
            }
            int monthsD;
            Calendar cal = Calendar.getInstance();
            int yearNow = cal.get(Calendar.YEAR);
            int monthNow = cal.get(Calendar.MONTH) + 1;
            int dayNow = cal.get(Calendar.DAY_OF_MONTH);
            int i = 0;
            String day = "", month = "", year = "";
            for (Character c : birthDate.toCharArray()) {
                if (i < 2) {
                    day = day + c;
                } else if (i > 2 && i < 5) {
                    month = month + c;
                } else if (i > 5 && i < 11) {
                    year = year + c;
                }
                i++;
            }
            int yearOfBirth = Integer.parseInt(year);
            int monthOfBirth = Integer.parseInt(month);
            int dayOfBirth = Integer.parseInt(day);
            if (yearOfBirth == yearNow && monthOfBirth == monthNow) {
                if (dayNow - dayOfBirth <= 14) {
                    monthsD = 0;
                } else {
                    monthsD = 1;
                }
            } else if (yearOfBirth == yearNow) {
                monthsD = monthNow - monthOfBirth;
            } else {
                int m = 12 - monthOfBirth;
                int m1 = yearNow - (yearOfBirth + 1);
                m1 = m1 * 12;
                monthsD = m + m1 + monthNow;
            }
            String age;
            String ageType;
            if (monthsD == 0) {
                ageType = "weeks";
                age = "1-2";
            } else {
                ageType = "months";
                age = String.valueOf(monthsD);
            }
            holder.ageTextViewDelete.setText(age +" " + ageType);

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
        private TextView illnessName, illnessDetails, examinationListItemText, developmentalListItemText, ageTextView, dateTextView,
                    nameDevelopmentalMonitoringTextView, detailsDevelopmentalMonitoringTextView, nameExaminationTextView,
                nameTextViewDelete, amkaTextViewDelete, birthDateTextViewDelete, ageTextViewDelete;
        private Switch switches;
        private RadioButton radioButton1, radioButton2, radioButton3;
        private RadioGroup radioGroup;
        private EditText developmentalEditText;
        private CardView card;
        private RelativeLayout developmentsRelativeLayout, deleteChildRelativeLayout;
        boolean i=true;
        private ImageView sexImageViewDelete;
        private CheckBox checkedSustenanceMonitoring, checkShowDevelopments;

        public MyViewHolder(final View itemView, radioButtonChange radioButtonChange, textChange textChange, sustenanceCheck sustenanceCheck) {
            super(itemView);
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
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                       if(checkedId == radioButton1.getId()){
                           radioButtonChange.rChange(1, getAdapterPosition());
                       }else if(checkedId== radioButton2.getId()){
                           radioButtonChange.rChange(2, getAdapterPosition());
                       }else if(radioButton3.getId() == checkedId){
                           radioButtonChange.rChange(3, getAdapterPosition());
                       }else {
                           radioButtonChange.rChange(0, getAdapterPosition());
                       }
                    }
                });
            }else if(id.equals("developmental")){
                List<developmentalItems> lists = (List<developmentalItems>) (List<?>) list;
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
                developmentalEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        textChange.textChanged(getAdapterPosition(), developmentalEditText.getText().toString());
                    }
                });
            }else if(id.equals("developments")){
                ageTextView = itemView.findViewById(R.id.ageTextView);
                dateTextView = itemView.findViewById(R.id.dateTextView);
                developmentsRelativeLayout = itemView.findViewById(R.id.developmentsRelativeLayout);
                developmentsRelativeLayout.setOnClickListener(this);
            }else if(id.equals("developmentalMonitoring")){
                nameDevelopmentalMonitoringTextView = itemView.findViewById(R.id.nameShowDevelopmentsDevelopmentalMonitoringTextView);
                detailsDevelopmentalMonitoringTextView = itemView.findViewById(R.id.detailsShowDevelopmentsDevelopmentalMonitoringTextView);
            }else if(id.equals("exam")){
                nameExaminationTextView = itemView.findViewById(R.id.nameAndDetailsShowDevelopmentsExaminationTextView);
            }else if(id.equals("sustenance")){
                checkedSustenanceMonitoring = itemView.findViewById(R.id.checkedSustenanceCheckBox);
                checkedSustenanceMonitoring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        sustenanceCheck.sustenanceChecked(getAdapterPosition(), checkedSustenanceMonitoring.isChecked());
                    }
                });
            }else if(id.equals("sust")){
                checkShowDevelopments = itemView.findViewById(R.id.checkShowDevelopments);
            }else if(id.equals("deleteChild")) {
                nameTextViewDelete = itemView.findViewById(R.id.nameTextViewDeleteChild);
                amkaTextViewDelete = itemView.findViewById(R.id.amkaTextViewDeleteChild);
                birthDateTextViewDelete = itemView.findViewById(R.id.birthDateTextViewDeleteChild);
                ageTextViewDelete = itemView.findViewById(R.id.ageTextViewDeleteChild);
                sexImageViewDelete = itemView.findViewById(R.id.sexImageViewDeleteChild);
                deleteChildRelativeLayout = itemView.findViewById(R.id.deleteChildRelativeLayout);
                deleteChildRelativeLayout.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            try{
                listener.onClick(v, getAdapterPosition());
            }catch (Exception e){
                System.out.println("------------------------------------" + e.getMessage());
            }
        }
    }

    public interface recyclerVewOnClickListener {
        void onClick (View view,int position);
    }
}

