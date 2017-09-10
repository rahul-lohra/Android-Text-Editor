package com.example.rkrde.awesometexteditor.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rkrde.awesometexteditor.R;
import com.example.rkrde.awesometexteditor.activity.EditorActivity;
import com.example.rkrde.awesometexteditor.modal.Notes;

import java.util.ArrayList;

/**
 * Created by rkrde on 10-09-2017.
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Notes> notes;

    public MyAdapter(Context context,ArrayList<Notes> notes) {
        this.context = context;
        this.notes = notes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_notes,null,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder viewHolder = (MyViewHolder) holder;
        Notes notes = this.notes.get(position);
        viewHolder.init(notes);

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tv;
        Notes notes;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (AppCompatTextView) itemView.findViewById(R.id.tv_title);
        }

        public void init(Notes notes){
            this.notes = notes;
            String title = notes.getTitle();
            String text = notes.getText();
            tv.setText(text);

            setClicks();
        }

        void setClicks(){
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EditorActivity.class);
                    intent.putExtra("uId", notes.getuId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
