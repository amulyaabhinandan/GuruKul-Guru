package com.example.gurukulguru;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gurukulguru.ModelClasses.Subjects;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Subjects_Adapter extends RecyclerView.Adapter<Subjects_Adapter.Subjects_Holder> {
    Context context;
    ArrayList<Subjects> subs;
    public Subjects_Adapter(Context cnt,ArrayList<Subjects> subjects)
    {
        this.context=cnt;
        this.subs=subjects;
    }

    @NonNull
    @Override
    public Subjects_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.subjects_row,parent,false);
        return new Subjects_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Subjects_Holder holder, int position) {
    holder.subject_name.setText(subs.get(position).getS_NAME());
    Picasso.get().load(subs.get(position).getS_PIC()).into(holder.subject_image);
    holder.subject_layout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(holder.selected_subject.isChecked())
            {
                holder.selected_subject.setChecked(false);
            }
            else
            {
            holder.selected_subject.setChecked(true);
        }}
    });
    }

    @Override
    public int getItemCount() {
        return subs.size();
    }
    public Subjects getitem(int position){
        return subs.get(position);
    }

    public class Subjects_Holder extends RecyclerView.ViewHolder {
        TextView subject_name;
        CheckBox selected_subject;
        ImageView subject_image;
        ConstraintLayout subject_layout;
        public Subjects_Holder(@NonNull View itemView) {
            super(itemView);
            subject_name=itemView.findViewById(R.id.name_of_sub);
            subject_image=itemView.findViewById(R.id.image_of_sub);
            subject_layout=itemView.findViewById(R.id.subjects_raw_linear);
            selected_subject=itemView.findViewById(R.id.box_of_sub);
            selected_subject.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    subs.get(getAdapterPosition()).setIschk(isChecked);
                }
            });
        }
    }
}
