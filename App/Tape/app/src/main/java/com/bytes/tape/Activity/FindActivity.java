package com.bytes.tape.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bytes.tape.App.BaseActivity;
import com.bytes.tape.R;

public class FindActivity extends BaseActivity implements View.OnClickListener {

    ImageView Back;
    LinearLayout Ok, Fake;
    EditText Phone, Pass, Answer;

    public void Init() {
        Back = findViewById(R.id.find_back);
        Ok = findViewById(R.id.find_ok);
        Phone = findViewById(R.id.find_phone);
        Pass = findViewById(R.id.find_pass);
        Fake = findViewById(R.id.find_fake);
        Answer = findViewById(R.id.find_answer);

        Back.setOnClickListener(this);
        Fake.setOnClickListener(this);
        Ok.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_find);
        Init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.find_back :
                onBackPressed();
                break;
            case R.id.find_fake :
                QuestionDialog(Pass);
                break;
            case R.id.find_ok :
                if (Phone.getText().toString().equals(""))
                    ShowToast("휴대전화 번호를 입력해주세요.");
                else if (Pass.getText().toString().equals(""))
                    ShowToast("질문을 등록해주세요.");
                else if (Answer.getText().toString().equals(""))
                    ShowToast("답변을 입력해주세요.");
                else {

                }
                break;
        }
    }
}