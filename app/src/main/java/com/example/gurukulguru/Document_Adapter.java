package com.example.gurukulguru;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gurukulguru.ModelClasses.Documents;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Document_Adapter extends RecyclerView.Adapter<Document_Adapter.Document_Holder> {
    Context context;
    String activityname;
    ArrayList<Documents> documents;

    public Document_Adapter(Context context, String activityname, ArrayList<Documents> documents) {
        this.context = context;
        this.activityname = activityname;
        this.documents = documents;
    }

    @NonNull
    @Override
    public Document_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.document_viewer,parent,false);
        return new Document_Holder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Document_Holder holder, final int position) {
        holder.topic.setText(documents.get(position).getTopic());
        holder.date.setText(documents.get(position).getDate());
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child(activityname).child(MyStaticClass.classname).child(MyStaticClass.subjectname);
                databaseReference.child(documents.get(position).getTime()).removeValue();
                Toast.makeText(context, "File Revomed From Database", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context,TeacherHome.class));
            }
        });
        if(activityname.equals("LECTURE"))
        {
            holder.document_iamge.setImageResource(R.drawable.video);
        }
        else if(activityname.equals("NOTES"))
        {
            holder.document_iamge.setImageResource(R.drawable.pdf);
        }
        else if(activityname.equals("EXAMPAPER"))
        {
            holder.document_iamge.setImageResource(R.drawable.pdf);
        }
        else if(activityname.equals("HOMEWORK"))
        {
            holder.document_iamge.setImageResource(R.drawable.pdf);
        }
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }

    public class Document_Holder extends RecyclerView.ViewHolder {
        Button delete_btn;
        TextView topic,date;
        ImageView  document_iamge;
        public Document_Holder(@NonNull View itemView) {
            super(itemView);
            delete_btn=itemView.findViewById(R.id.document_delete_btn);
            topic=itemView.findViewById(R.id.document_topic);
            date=itemView.findViewById(R.id.document_date);
            document_iamge=itemView.findViewById(R.id.document_image);
        }
    }
}
