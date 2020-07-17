package com.jisooZz.haru.activity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.jisooZz.haru.R;
import com.jisooZz.haru.db.model.DiaryInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.CustomViewHolder> implements OnDiaryItemClickListener{

    List<DiaryInfo> items = null;
    OnDiaryItemClickListener listener;
    private SimpleDateFormat mFormat = new SimpleDateFormat("MM\ndd");
    ImageView moodImg;

    // -- 레이아웃에 해당하는 뷰를 담아둘 객체. 필요하면 재사용하는 뷰홀더
    public class CustomViewHolder extends RecyclerView.ViewHolder{

        protected TextView title;
        protected TextView date;
        protected ImageView img;
        protected  ImageView moodImg;

        public CustomViewHolder(View view){
            super(view);
            this.title = (TextView) view.findViewById(R.id.dtitle);
            this.date = (TextView)view.findViewById(R.id.ddate);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(CustomViewHolder.this,v,position);
                    }
                }
            });
        }

    }

    public DiaryAdapter(List<DiaryInfo> diaryInfos){
        this.items = diaryInfos;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_item,parent,false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    // -- 뷰홀더가 재사용될 때 호출됨
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.title.setText(items.get(position).getDtitle());


        Date date1 = new Date();
        String time = mFormat.format(date1);
        holder.date.setText(time);
        // String[] date = items.get(position).getDdate().split(" ");
        //  viewHolder.date.setText(date[0]);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void setOnItemClickListener(OnDiaryItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(CustomViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder,view,position);
        }
    }



}

