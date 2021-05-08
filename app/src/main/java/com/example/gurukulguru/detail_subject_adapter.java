package com.example.gurukulguru;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gurukulguru.ModelClasses.Subjects;
import com.example.gurukulguru.sub_activity.Exampaper_Sub;
import com.example.gurukulguru.sub_activity.Homework_Sub_Activity;
import com.example.gurukulguru.sub_activity.Lecture_Sub_Acitivity;
import com.example.gurukulguru.sub_activity.Notes_Sub_Activity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class detail_subject_adapter extends RecyclerView.Adapter<detail_subject_adapter.detail_subject_holder> {
    Context context;
    String actname;
    ArrayList<Subjects> subjects;

    public detail_subject_adapter(Context cnt, ArrayList<Subjects> subs, String act) {
        this.context = cnt;
        this.subjects = subs;
        this.actname = act;
    }

    @NonNull
    @Override
    public detail_subject_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.class_subject_row, parent, false);
        return new detail_subject_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull detail_subject_adapter.detail_subject_holder holder, final int position) {
        Picasso.get().load(subjects.get(position).getS_PIC()).into(holder.subjects_image);
        holder.sub_name.setText(subjects.get(position).getS_NAME());
        holder.sub_code.setText(subjects.get(position).getS_CODE());
        if (actname.equals("Homework")) {
            holder.sub_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyStaticClass.subjectname=subjects.get(position).getS_NAME();
                    context.startActivity(new Intent(context,Homework_Sub_Activity.class));
                }
            });
        }
         if(actname.equals("Notes"))
        {

            holder.sub_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyStaticClass.subjectname=subjects.get(position).getS_NAME();
                    context.startActivity(new Intent(context,Notes_Sub_Activity.class));

                }
            });
        }
         if(actname.equals("Lectures"))
        {
            holder.sub_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyStaticClass.subjectname=subjects.get(position).getS_NAME();
                 context.startActivity(new Intent(context,Lecture_Sub_Acitivity.class));
                }
            });
        }
         if(actname.equals("Exampaper"))
        {
            holder.sub_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyStaticClass.subjectname=subjects.get(position).getS_NAME();
                    context.startActivity(new Intent(context,Exampaper_Sub.class));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public class detail_subject_holder extends RecyclerView.ViewHolder {
        ImageView subjects_image;
        TextView sub_name, sub_code;
        LinearLayout sub_layout;

        public detail_subject_holder(@NonNull View itemView) {
            super(itemView);
            sub_code = itemView.findViewById(R.id.subject_row_subjectcode);
            sub_name = itemView.findViewById(R.id.subject_row_subjectname);
            subjects_image = itemView.findViewById(R.id.subject_row_subjectimage);
            sub_layout = itemView.findViewById(R.id.subjects_holder_layout);
        }
    }
}
