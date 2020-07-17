package com.jisooZz.haru.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.jisooZz.haru.BaseApplication;
import com.jisooZz.haru.R;
import com.jisooZz.haru.db.DatabaseSource;
import com.jisooZz.haru.db.model.DiaryInfo;
import com.jisooZz.haru.util.ImageManager;
import com.jisooZz.haru.util.Util;

import java.io.File;
import java.util.ArrayList;

public class WriteActivity extends AppCompatActivity implements View.OnClickListener{


    private static final String TAG = WriteActivity.class.getSimpleName().toString();
    private static final int GALLERY = 1000; //requestCode 반환 값

    private DatabaseSource databaseSource;

    private EditText tvtitle, etcontent;
    private ConstraintLayout layout_photo;
    private ImageView photo;
    private TextView tvNo;

    private String id = null; //user Id
    private int type = 1; // 1: 작성, 2: 수정
    private int no = 0;

    private String filePath;
    private File tempFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        if(getIntent() != null){
            type = getIntent().getIntExtra("type",1);
            no = getIntent().getIntExtra("no",0);
        }


        init();
    }


    private void init(){
        ImageView btnClose = (ImageView)findViewById(R.id.close);
        btnClose.setOnClickListener(this);
        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(this);
        TextView tvDate = (TextView)findViewById(R.id.tvdate);

        Button delete = (Button)findViewById(R.id.delete);
        delete.setOnClickListener(this);

        tvNo = (TextView)findViewById(R.id.tvNo);
        tvtitle = (EditText)findViewById(R.id.tvtitle);
        photo = (ImageView)findViewById(R.id.photo);
        layout_photo = (ConstraintLayout)findViewById(R.id.layout_photo);
        layout_photo.setOnClickListener(this);
        etcontent = (EditText)findViewById(R.id.etcontent);

        databaseSource = new DatabaseSource(WriteActivity.this);
        databaseSource.open();

        if(BaseApplication.getInstance(this).getUserId() != null){
            id = BaseApplication.getInstance(this).getUserId();
        }


        //작성

        if(type == 1){
             save.setText("저장");
             delete.setText("닫기");

             String today = Util.getDateTime("MM월 dd일");
             tvDate.setText(today);
             filePath=""; //초기화
        }
        //수정화면
        else{

            save.setText("수정");
            delete.setText("삭제");

            if(databaseSource.getTheDiary(no)!=null){
                DiaryInfo info = databaseSource.getTheDiary(no);

                tvtitle.setText(info.getDtitle());
                tvDate.setText(info.getDdate());
                etcontent.setText(info.getDcontent());
                filePath=info.getDimg();
                photo.setImageBitmap(ImageManager.setBitmap(filePath));
            }

        }
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
        switch (v.getId()){
            case R.id.layout_photo:
                //갤러리 이용에 대한 permission check
                checkPermission();
                break;


            case R.id.close:
                finish();
                overridePendingTransition(0,android.R.anim.fade_out);
                break;

            case R.id.save:
                String title = tvtitle.getText().toString();
               // if( title.isEmpty()){
               //     Toast.makeText(getApplicationContext(),"제목 또는 내용을 입력해주세요.",Toast.LENGTH_SHORT).show();
               //     return;
               // }

                DiaryInfo diaryInfo = new DiaryInfo();
                diaryInfo.setDtitle(title);
                diaryInfo.setDcontent(etcontent.getText().toString());
                diaryInfo.setDdate(Util.getDateTime("yyyy년 MM월dd일 HH시mm분"));
                diaryInfo.setDimg(filePath);
                diaryInfo.setId(id);


                //작성

                if(type == 1){
                    databaseSource.inserDiary(diaryInfo);

                   finish();

                }

                // 수정
                else {
                    databaseSource.updateDiary(diaryInfo, no);
                    finish();
                   // finish("다이어리가 수정되었습니다.");
                }
                break;

            case R.id.delete:
                if (type == 1) {

                    databaseSource.deleteDiary(no);
                    finish();
                   // finish("다이어리가 삭제되었습니다.");
                    overridePendingTransition(0, android.R.anim.fade_out);
                }
                else { //type 2 :
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("안내");
                    builder.setMessage("일기를 삭제할까요?");
                    builder.setIcon(R.drawable.ministar);
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            databaseSource.deleteDiary(no);
                            finish();
                           // finish("삭제되었습니다.");


                        }
                    });

                    builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
                break;

        }
    }

    private void checkPermission() {

        PermissionListener listener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                getImageFromGallery();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {

            }
        };
        TedPermission.with(this)
                .setPermissionListener(listener)
                .setRationaleTitle("권한 설정 안내")
                .setRationaleMessage("카메라 및 갤러리 기능을 사용을 위해 권한 설정을 허용해 주세요.")
                .setDeniedMessage("권한 설정을 하지 않으면 카메라 및 갤러리 기능을 사용할 수 없습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    //Intent를 통해 앨범화면으로 이동
    private void getImageFromGallery() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, GALLERY);
    }



    private void finish(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(0, android.R.anim.fade_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            if (tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        tempFile = null;
                        filePath = "";
                    }
                }
            }
            return;
        }

        if (requestCode == GALLERY) {
            if (resultCode == RESULT_CANCELED) {
                filePath = "";
                return;
            }
            if (data == null) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, GALLERY);
            } else {
                if (data.getData() != null) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap returnBitmp = ImageManager.setBitmap(picturePath);
                    if (returnBitmp == null) {
                        filePath = "";
                        Toast.makeText(getApplicationContext(), "이미지를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        filePath = picturePath;
                        photo.setImageBitmap(ImageManager.setBitmap(filePath));
                    }
                }
            }
        }
    }
}
