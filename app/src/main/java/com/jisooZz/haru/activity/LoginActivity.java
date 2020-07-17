package com.jisooZz.haru.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jisooZz.haru.BaseApplication;
import com.jisooZz.haru.R;
import com.jisooZz.haru.db.DatabaseSource;
import com.jisooZz.haru.db.model.UserInfo;
import com.jisooZz.haru.util.PreferenceManager;

public class LoginActivity extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnClickListener{

    private EditText et_id,et_pw;
    private Button loginbt;
    private TextView joinbt;
    private CheckBox checkBox;
    private DatabaseSource databaseSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        init();

    }

    private void init(){
        et_id = (EditText)findViewById(R.id.et_id);
        et_pw = (EditText)findViewById(R.id.et_pw);
        loginbt = (Button)findViewById(R.id.loginbt);
        loginbt.setOnClickListener(this);
        joinbt = (TextView)findViewById(R.id.joinbt);
        joinbt.setOnClickListener(this);
        checkBox = (CheckBox)findViewById(R.id.checkBox);

    // -- EditText에 입력되는 값을 비밀번호 입력처럼 동그라미 표시로 바꿈 --
    PasswordTransformationMethod passwdtm = new PasswordTransformationMethod();
        et_pw.setTransformationMethod(passwdtm);
        et_pw.setOnEditorActionListener(this); // EditText와 onEditorActionListener 연결

    boolean rememberId = PreferenceManager.getBoolean(getApplicationContext(), "rememberId"); //id 기억하기
        if (rememberId) {
        // -- SharedPreferences 를 이용한 아이디 기억 구현
        checkBox.setChecked(true);
        String prefId = PreferenceManager.getString(getApplicationContext(), "inputId");
        if (prefId != null) et_id.setText(prefId);
    } else {
        checkBox.setChecked(false);
        et_id.setText("");
    }

    databaseSource = new DatabaseSource(LoginActivity.this);
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
            case R.id.loginbt:

                String id = et_id.getText().toString();
                String pw = et_pw.getText().toString();

                if (id.equals("") || pw.equals("")) {
                   //Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();

                   setCustomToast(this,"아이디 또는 비밀번호를 입력해주세요.");
                    return;
                }
                else {
                    UserInfo info = new UserInfo();
                    info.setId(id);
                    info.setPassword(pw);

                    // 등록된 회원일 경우 로그인 성공
                    if (databaseSource.checkUserExists(info)){
                        BaseApplication.getInstance(this).setUserId(id); //application단에 user id 저장

                        boolean isRemember = checkBox.isChecked();
                        if (isRemember) {
                            PreferenceManager.setBoolean(getApplicationContext(), "rememberId", true);
                            PreferenceManager.setString(getApplicationContext(), "inputId", id);
                        } else {
                            PreferenceManager.setBoolean(getApplicationContext(), "rememberId", false);
                            PreferenceManager.setString(getApplicationContext(), "inputId", null);
                        }

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                       // Toast.makeText(getApplicationContext(),"로그인에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                        setCustomToast(this,"로그인에 실패하였습니다.");
                    }
                }

                break;

            case R.id.joinbt:
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
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








