package com.example.babyland;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {
    private ArrayList list;
    private String id, userType;
    private recyclerVewOnClickListener listener;
    private radioButtonChange radioButtonChange;
    private textChange textChange;
    private sustenanceCheck sustenanceCheck;
    private addVaccination addVaccination;

    public interface addVaccination{
        void addVaccine(int position);
    }

    public void addVaccination(addVaccination addVaccination){this.addVaccination = addVaccination;}

    public interface sustenanceCheck{
        void sustenanceChecked(int position, Boolean value);
    }

    public void sustenanceCheck(sustenanceCheck sustenanceCheck){this.sustenanceCheck = sustenanceCheck;}

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
        } else if(id.equals("developments")){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_developments, parent, false);
        }else if(id.equals("developmentalMonitoring")){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_developments_developmental_monitoring, parent, false);
        }else if(id.equals("exam")){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_developments_examination, parent, false);
        }else if(id.equals("sust")){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_developments_sustenance, parent, false);
        }else if(id.equals("deleteChild")){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_delete, parent, false);
        }else if(id.equals("availableChildren")){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.available_children, parent, false);
        }else if(id.equals("familyHistoric")){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_historic_view, parent, false);
        }else if(id.equals("viewVaccination")) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vaccination_add_item, parent, false);
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
            if(details!=" "){
                holder.detailsDevelopmentalMonitoringTextView.setText("Nothing");
            }
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
            holder.nameTextViewDelete.setText("Name: "+ name);
            holder.birthDateTextViewDelete.setText("Birth date: "+birthDate);
            holder.amkaTextViewDelete.setText("Amka: "+amka);
            if(sex.equals("BOY")){
                Picasso.get().load(R.drawable._02_baby_boy_1).into(holder.sexImageViewDelete);
            }else{
                Picasso.get().load(R.drawable.baby_girl1).into(holder.sexImageViewDelete);
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
            holder.ageTextViewDelete.setText("Age: "+age +" " + ageType);
        }else if(id.equals("availableChildren")){
            List<Baby> lists = (List<Baby>) list;
            String name = lists.get(position).getName();
            String amka = lists.get(position).getAmka();
            String sex = lists.get(position).getSex();
            holder.amkaAvailableChildrenText.setText(amka);
            holder.nameAvailableChildrenText.setText(name);
            if(sex.equals("BOY")){
                Picasso.get().load(R.drawable._02_baby_boy_1).into(holder.sexAvailableChildren);
            }else{
                Picasso.get().load(R.drawable.baby_girl1).into(holder.sexAvailableChildren);
            }
        }else if(id.equals("familyHistoric")){
            List<FamilyHistoryIllnesses> lists = (List<FamilyHistoryIllnesses>) list;
            String name = lists.get(position).getIllness();
            String details = lists.get(position).getDetails();
            Boolean active = lists.get(position).getSwitches();
            if(active){
                holder.illnessNameTextView.setTextColor(Color.parseColor("#00FF00"));
                holder.illnessDetailsTextView.setText(details);
                holder.illnessDetailsTextView.setVisibility(View.VISIBLE);
            }else{
                holder.illnessNameTextView.setTextColor(Color.parseColor("#FF0000"));
                holder.illnessDetailsTextView.setVisibility(View.GONE);
            }
            holder.illnessNameTextView.setText(name);
        }else if(id.equals("viewVaccination")) {
            List<Vaccination> lists = (List<Vaccination>) list;
            String name = lists.get(position).getName();
            String date = lists.get(position).getDate();
            String doctorsName = lists.get(position).getDoctorName();
            if(date!=null){
                holder.vaccinationDoctorTextView.setText(doctorsName);
                holder.vaccinationDateTextView.setText(date);
            }else{
                holder.vaccinationDateTextView.setText("None");
                holder.vaccinationDoctorTextView.setText("None");
            }
            if(name==null){
                holder.cardViewVaccinations.setClickable(false);
            }
            holder.vaccinationNameTextView.setText(name);
            holder.addVaccineButton.setVisibility(View.GONE);
            holder.vaccinationDateTextView.setVisibility(View.GONE);
            holder.vaccinationDoctorTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();

    }

    public recyclerAdapter(recyclerVewOnClickListener listener, ArrayList list, String id, String userType) {
        this.list = list;
        this.id=id;
        this.listener = listener;
        this.userType = userType;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView illnessName, illnessDetails, examinationListItemText, developmentalListItemText, ageTextView, dateTextView,
                    nameDevelopmentalMonitoringTextView, detailsDevelopmentalMonitoringTextView, nameExaminationTextView,
                    nameTextViewDelete, amkaTextViewDelete, birthDateTextViewDelete, ageTextViewDelete, amkaAvailableChildrenText,
                    nameAvailableChildrenText, illnessNameTextView, illnessDetailsTextView, vaccinationNameTextView,
                    vaccinationDateTextView, vaccinationDoctorTextView;
        private Switch switches;
        private RadioButton radioButton1, radioButton2, radioButton3;
        private RadioGroup radioGroup;
        private EditText developmentalEditText;
        private CardView card, cardViewAdd, cardViewExamination, cardViewVaccinations, cardViewDevelopments, cardViewDeleteChild;
        private boolean i=true;
        private Button addVaccineButton;
        private ImageView sexImageViewDelete, sexAvailableChildren;
        private CheckBox checkedSustenanceMonitoring, checkShowDevelopments;

        public MyViewHolder(final View itemView, radioButtonChange radioButtonChange, textChange textChange, sustenanceCheck sustenanceCheck) {
            super(itemView);
            if (id.equals("illnessInput")) {
                List<FamilyHistoryIllnesses> lists = (List<FamilyHistoryIllnesses>) (List<?>) list;
                illnessName = itemView.findViewById(R.id.illnessName);
                switches = itemView.findViewById(R.id.switches);
                illnessDetails = itemView.findViewById(R.id.illnessDetails);
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
                cardViewExamination = itemView.findViewById(R.id.cardViewExamination);
                radioButton3 = itemView.findViewById(R.id.radioButton3);
                radioGroup = itemView.findViewById(R.id.radioGroupExaminationList);
                radioGroup.setVisibility(itemView.GONE);
                cardViewExamination.setOnClickListener(new View.OnClickListener() {
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
                cardViewDevelopments = itemView.findViewById(R.id.cardViewDevelopments);
                cardViewDevelopments.setOnClickListener(this);
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
                cardViewDeleteChild = itemView.findViewById(R.id.cardViewDeleteChild);
                cardViewDeleteChild.setOnClickListener(this);
            }else if(id.equals("availableChildren")){
                nameAvailableChildrenText = itemView.findViewById(R.id.nameAvailableChildrenText);
                amkaAvailableChildrenText = itemView.findViewById(R.id.amkaAvailableChildrenText);
                sexAvailableChildren = itemView.findViewById(R.id.sexAvailableChildren);
                cardViewAdd = itemView.findViewById(R.id.cardViewAvailableChildren);
                cardViewAdd.setOnClickListener(this);
            }else if(id.equals("familyHistoric")) {
                illnessNameTextView = itemView.findViewById(R.id.illnessNameTextView);
                illnessDetailsTextView = itemView.findViewById(R.id.illnessDetailsTextView);
            }else if(id.equals("viewVaccination")) {
                vaccinationNameTextView = itemView.findViewById(R.id.vaccinationNameTextView);
                vaccinationDateTextView = itemView.findViewById(R.id.vaccinationDateTextView);
                vaccinationDoctorTextView = itemView.findViewById(R.id.vaccinationDoctorTextView);
                addVaccineButton = itemView.findViewById(R.id.addVaccineButton);
                cardViewVaccinations = itemView.findViewById(R.id.cardViewVaccinations);
                cardViewVaccinations.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!i){
                            vaccinationDateTextView.setVisibility(View.GONE);
                            vaccinationDoctorTextView.setVisibility(View.GONE);
                            addVaccineButton.setVisibility(View.GONE);
                            i=true;
                        }else{
                            vaccinationDateTextView.setVisibility(View.VISIBLE);
                            vaccinationDoctorTextView.setVisibility(View.VISIBLE);
                            i=false;
                            if(userType.equals("doctor")) {
                                if (vaccinationDoctorTextView.getText() == "None") {
                                    addVaccineButton.setVisibility(View.VISIBLE);
                                } else {
                                    addVaccineButton.setVisibility(View.GONE);
                                }
                            }
                        }

                    }
                });
                addVaccineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addVaccination.addVaccine(getAdapterPosition());
                    }
                });
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

