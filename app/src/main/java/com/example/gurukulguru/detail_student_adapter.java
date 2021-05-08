package com.example.gurukulguru;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gurukulguru.ModelClasses.Student;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class detail_student_adapter extends RecyclerView.Adapter<detail_student_adapter.detail_student_holder> {

    Context context;
    ArrayList<Student> students;
    public detail_student_adapter(Context cnt,ArrayList<Student> std) {
        this.context=cnt;
        this.students=std;
    }

    @NonNull
    @Override
    public detail_student_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.class_student_row,parent,false);
       return new detail_student_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull detail_student_holder holder, int position) {
        Picasso.get().load(students.get(position).getPICURL()).into(holder.stud_pic);
        holder.stud_phone.setText(students.get(position).getPHONE());
        holder.stud_roll.setText(students.get(position).getROLLNO());
        holder.stud_name.setText(students.get(position).getNAME());
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public class detail_student_holder extends RecyclerView.ViewHolder {
        ImageView stud_pic;
        TextView stud_name,stud_roll,stud_phone;
        public detail_student_holder(@NonNull View itemView) {
            super(itemView);
            stud_pic=itemView.findViewById(R.id.details_student_iamge);
            stud_name=itemView.findViewById(R.id.details_student_name);
            stud_phone=itemView.findViewById(R.id.details_student_phone);
            stud_roll=itemView.findViewById(R.id.details_student_rollno);
        }
    }
}
