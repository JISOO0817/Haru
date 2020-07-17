package com.jisooZz.haru.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jisooZz.haru.R;
import com.jisooZz.haru.db.DatabaseSource;
import com.jisooZz.haru.db.model.UserInfo;
import com.jisooZz.haru.util.PreferenceManager;

public class JoinActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_id,et_pw,check_pw;
    private DatabaseSource databaseSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        init();
    }


    private void init(){

        et_id = (EditText)findViewById(R.id.et_id);
        et_pw = (EditText)findViewById(R.id.et_pw);
        check_pw = (EditText)findViewById(R.id.check_pw);

        Button savebt = (Button)findViewById(R.id.savebt);
        savebt.setOnClickListener(this);
        Button closebt = (Button)findViewById(R.id.closebt);
        closebt.setOnClickListener(this);

        databaseSource = new DatabaseSource(JoinActivity.this);
        databaseSource.open();
    }


    @Override
    protected void onResume() {
        super.onResume();
        databaseSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseSource.close();
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.savebt:
                // id와 pw값을 모두 제대로 입력했을 경우
                if (!checkEmptyField()) {
                    String id = et_id.getText().toString();
                    String pw = et_pw.getText().toString();
                    String checkPw = check_pw.getText().toString();

                    // 비밀번호가 일치할 경우 회원가입 진행
                    if(pw.equals(checkPw)){
                        UserInfo info = new UserInfo();
                        info.setId(id);
                        info.setPassword(pw);

                        // preference에 저장
                        PreferenceManager.setString(getApplicationContext(), "inputId", id);

                        // database에 user 등록
                        if (databaseSource.insertUser(info)) {
                            //Toast.makeText(getApplicationContext(),"등록 되었습니다.",Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            setCustomToast(this,"이미 존재하는 아이디입니다.");
                            //Toast.makeText(getApplicationContext(),"이미 존재하는 아이디입니다.",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        setCustomToast(this,"비밀번호가 일치하지 않습니다.");
                        //Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    setCustomToast(this,"빈칸을 빠짐없이 입력해주세요.");
                    //Toast.makeText(getApplicationContext(),"빈칸을 빠짐없이 입력해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }

                break;

            case R.id.closebt:
                finish();
                break;
        }
    }


    /**
     * edit text field가 빈값인지 체크
     * */
    private boolean checkEmptyField() {
        String id = et_id.getText().toString();
        String pw = et_pw.getText().toString();
        String checkPw = check_pw.getText().toString();

        if (id.trim().isEmpty() || pw.trim().isEmpty() || checkPw.trim().isEmpty()) {
            return true;
        }
        return false;
    }

    public static void setCustomToast(Context context, String msg) {
        TextView tvToastMsg = new TextView(context);
        tvToastMsg.setText(msg);
        tvToastMsg.setBackgroundResource(R.drawable.toast);
        tvToastMsg.setTextColor(Color.BLACK);
        tvToastMsg.setTextSize(8);

        final Toast toastMsg = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        toastMsg.setView(tvToastMsg);

        toastMsg.show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                toastMsg.cancel();
            }
        }, 1000);
    }
}
