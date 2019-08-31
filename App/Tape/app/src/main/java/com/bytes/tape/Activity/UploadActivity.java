package com.bytes.tape.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bytes.tape.App.BaseActivity;
import com.bytes.tape.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class UploadActivity extends BaseActivity implements View.OnClickListener {

    ImageView Back;
    LinearLayout Ok;
    EditText Title, Content;

    ProgressBar Bar;

    public void Init() {
        Back = findViewById(R.id.upload_back);
        Ok = findViewById(R.id.upload_ok);
        Title = findViewById(R.id.upload_title);
        Content = findViewById(R.id.upload_content);
        Bar = findViewById(R.id.upload_bar);

        Back.setOnClickListener(this);
        Ok.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_upload);
        Init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upload_back :
                onBackPressed();
                break;
            case R.id.upload_ok :
                Bar.setVisibility(View.VISIBLE);
                ShowToast("잠시만 기다려주세요...");
                String Path = getIntent().getStringExtra("Path");
                setOnConnectionListener(new OnConnectionListener() {
                    @Override
                    public void onCall(JSONArray Object) {
                        JSONObject obj;
                        try {
                            obj = Object.getJSONObject(0);
                            if (obj.getString("error") != null)
                                ShowToast(obj.getString("error"));
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (e.getMessage().equals("No value for error")) {
                                ((MainActivity)MainActivity.Context).getData();
                                ShowToast("영상이 등록되었습니다.");
                                finish();
                            }
                            else
                                ShowToast("서버에 접속할 수 없습니다.");
                        }
                    }
                });
                String Data[][] = {
                        {"title", Title.getText().toString()},
                        {"description", Content.getText().toString()},
                        {"user", Manager.User},
                };
                HttpMultiPart("POST", Data, new File(Path), "filepath", "main/videovideos/");
                break;
        }
    }
}