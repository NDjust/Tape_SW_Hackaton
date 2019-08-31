package com.bytes.tape.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.bytes.tape.App.BaseActivity;
import com.bytes.tape.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class SignActivity extends BaseActivity implements View.OnClickListener {

    TextView Info;
    ImageView Back;
    EditText Phone, Name, Pass, PassOk, Question, Answer;
    AppCompatCheckBox Check;
    LinearLayout Ok, Fake;

    public void Init() {
        Back = findViewById(R.id.sign_back);
        Info = findViewById(R.id.sign_info);
        Phone = findViewById(R.id.sign_phone);
        Name = findViewById(R.id.sign_name);
        Pass = findViewById(R.id.sign_pass);
        PassOk = findViewById(R.id.sign_pass_ok);
        Question = findViewById(R.id.sign_pass_find_question);
        Answer = findViewById(R.id.sign_pass_find_answer);
        Check = findViewById(R.id.sign_check);
        Ok = findViewById(R.id.sign_ok);
        Fake = findViewById(R.id.sign_fake);

        Info.setText(Manager.Info);
        Ok.setOnClickListener(this);
        Back.setOnClickListener(this);
        Fake.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_sign);
        Init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_back :
                onBackPressed();
                break;
            case R.id.sign_fake:
                QuestionDialog(Question);
                break;
            case R.id.sign_ok :
                if (!Check.isChecked())
                    ShowToast("위 내용에 동의해주세요.");
                else if (Phone.getText().toString().equals(""))
                    ShowToast("휴대전화 번호를 입력해주세요.");
                else if (Name.getText().toString().equals(""))
                    ShowToast("이름을 입력해주세요.");
                else if (Pass.getText().toString().equals(""))
                    ShowToast("비밀번호를 입력해주세요.");
                else if (PassOk.getText().toString().equals(""))
                    ShowToast("비밀번호를 다시 입력해주세요.");
                else if (!PassOk.getText().toString().equals(Pass.getText().toString()))
                    ShowToast("비밀번호가 다릅니다. 다시 입력해주세요.");
                else if (Question.getText().toString().equals(""))
                    ShowToast("비밀번호 찾기 질문을 등록해주세요.");
                else if (Answer.getText().toString().equals(""))
                    ShowToast("답변을 등록해주세요.");
                else {
                    setOnConnectionListener(new OnConnectionListener() {
                        @Override
                        public void onCall(JSONArray Object) {
                            JSONObject obj = null;
                            try {
                                obj = Object.getJSONObject(0);
                                if (obj.getString("error") != null)
                                    ShowToast("중복된 번호입니다.");
                            } catch (Exception e) {
                                if (e.getMessage().equals("No value for error"))
                                    finish();
                                else
                                    ShowToast("서버에 접속할 수 없습니다.");
                            }
                        }
                    });
                    String Data[][] = {
                            {"phone", Phone.getText().toString()},
                            {"password", Pass.getText().toString()},
                            {"name", Name.getText().toString()},
                            {"password_question", Question.getText().toString()},
                            {"password_question_answer", Answer.getText().toString()},
                    };
                    HttpMultiPart("POST", Data, null, null, "signup");
                }
                break;
        }
    }
}
