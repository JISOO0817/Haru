package com.jisooZz.haru.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jisooZz.haru.R;
import com.jisooZz.haru.activity.adapter.DiaryAdapter;
import com.jisooZz.haru.activity.adapter.OnDiaryItemClickListener;
import com.jisooZz.haru.db.DatabaseSource;
import com.jisooZz.haru.db.model.DiaryInfo;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDiaryItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName().toString();

    private DatabaseSource databaseSource;

    private LinearLayout noDataLayout;
    private RecyclerView lvDiary;
    private ImageView btnAdd;
    private DiaryAdapter mAdapter;

    private List<DiaryInfo> items= null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
        noDataLayout = (LinearLayout) findViewById(R.id.noDataLayout);
        lvDiary = (RecyclerView)findViewById(R.id.lvDiary);
        btnAdd = (ImageView) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WriteActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
            }
        });

        databaseSource = new DatabaseSource(MainActivity.this);
        databaseSource.open();
    }


    @Override
    protected void onResume() {
        super.onResume();
        databaseSource.open();

        checkList();
    }

    private void checkList(){
        if(databaseSource.getMyDiary()!=null && databaseSource.getMyDiary().size()>0){
            noDataLayout.setVisibility(View.GONE);
            lvDiary.setVisibility(View.VISIBLE);

            if(mAdapter == null){
                items = databaseSource.getMyDiary();
                LinearLayoutManager lm = new LinearLayoutManager(this);
                lvDiary.setLayoutManager(lm);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(lvDiary.getContext(),lm.getOrientation());
                lvDiary.addItemDecoration(dividerItemDecoration);

                mAdapter = new DiaryAdapter(items);
                lvDiary.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(this);

            }
            else{
                items.clear();
                items.addAll(databaseSource.getMyDiary());
                mAdapter.notifyDataSetChanged();
            }
        }else{
            noDataLayout.setVisibility(View.VISIBLE);
            lvDiary.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        databaseSource.close();
    }

    @Override
    public void onItemClick(DiaryAdapter.CustomViewHolder holder, View view, int position) {
        int no = items.get(position).getNo();
        Log.d(TAG, "no : " + no);

        Intent intent = new Intent(MainActivity.this, WriteActivity.class);
        intent.putExtra("type", 2);
        intent.putExtra("no", no);
        startActivity(intent);
    }
}
