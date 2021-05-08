package com.example.gurukulguru;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Classes_Adapter extends RecyclerView.Adapter<Classes_Adapter.Class_Holder> {
    ArrayList<String> name;
    String activity;
    Context context;
    public  Classes_Adapter(Context cnt,ArrayList<String> nm,String act)
    {
       context=cnt;
        name=nm;
        activity=act;
    }

    @NonNull
    @Override
    public Class_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.my_classes,parent,false);
        return new Class_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Class_Holder holder, final int position) {
                holder.nameofclass.setText(name.get(position));
         holder.classinside.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               if (activity.equals("Lectures")) {
                   Intent intent = new Intent(context, LectureDetailAcitivity.class);
                   MyStaticClass.classname=name.get(position);
                   context.startActivity(intent);
               }
               else if (activity.equals("ClassRoom"))
               {
                   Intent intent = new Intent(context, DetailsClassActivity.class);
                   MyStaticClass.classname=name.get(position);
                   context.startActivity(intent);
               }
               else if (activity.equals("Notes"))
               {
                   Intent intent = new Intent(context, NotesDetailAcitivty.class);
                   MyStaticClass.classname=name.get(position);
                   context.startActivity(intent);
               }
               else if (activity.equals("Exampaper"))
               {
                   Intent intent = new Intent(context, ExampaperDetailActivity.class);
                   MyStaticClass.classname=name.get(position);
                   context.startActivity(intent);
               }
               else if (activity.equals("Homework"))
               {
                   Intent intent = new Intent(context, HomeworkDetailsActivity.class);
                   MyStaticClass.classname=name.get(position);
                   context.startActivity(intent);
               }
           }

        });
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class Class_Holder extends RecyclerView.ViewHolder {
        TextView nameofclass;
        LinearLayout classinside;
        public Class_Holder(@NonNull View itemView) {
            super(itemView);
            nameofclass=itemView.findViewById(R.id.my_classes_container);
            classinside=itemView.findViewById(R.id.class_inside_layout);
        }
    }
}
