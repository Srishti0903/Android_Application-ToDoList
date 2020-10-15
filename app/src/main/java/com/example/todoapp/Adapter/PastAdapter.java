package com.example.todoapp.Adapter;

import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class PastAdapter extends FirebaseRecyclerAdapter<ToDoModel,PastAdapter.MyViewHolder> {

    public PastAdapter(@NonNull FirebaseRecyclerOptions<ToDoModel> options) {
        super(options);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.past_task_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position, @NonNull ToDoModel ToDomodel) {
        holder.checkbox.setText(ToDomodel.getTask());
        holder.checkbox.setChecked(toBoolean(ToDomodel.getStatus()));
        holder.date.setText(ToDomodel.getDate());
        holder.time.setText(ToDomodel.getTime());
    }

    private boolean toBoolean(int status) {
        return status!=0;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        //single row things (from task_layout)
        CheckBox checkbox;
        TextView date;
        TextView time;
        MyViewHolder(View view) {
            super(view);
            checkbox = (CheckBox) view.findViewById(R.id.todocheckbox);
            date = (TextView) view.findViewById(R.id.dateEdit);
            time = (TextView) view.findViewById(R.id.timeEdit);
        }
    }
}