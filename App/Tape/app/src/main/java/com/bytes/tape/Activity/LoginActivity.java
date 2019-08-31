package com.bytes.tape.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bytes.tape.App.BaseActivity;
import com.bytes.tape.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    LinearLayout Ui, Login, Sign;
    EditText Phone, Pass;
    TextView Find;
    ImageView Img;

    public void Init() {
        Ui = findViewById(R.id.login_ui);
        Login = findViewById(R.id.login_ok);
        Phone = findViewById(R.id.login_phone);
        Pass = findViewById(R.id.login_pass);
        Sign = findViewById(R.id.login_sign);
        Find = findViewById(R.id.login_find);
        Img = findViewById(R.id.login_img);
        Login.setOnClickListener(this);
        Sign.setOnClickListener(this);
        Find.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        Init();
        SharedPreferences SP = getSharedPreferences("Tape", MODE_PRIVATE);
        if (!SP.getString("Phone", "").equals("")) {
            Login(SP.getString("Phone", ""), SP.getString("Pass", ""));
        } else {
            requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Splash();
                }
                else finish();
            }
        }
    }

    public void Splash() {
        Img.setAnimation(getAnimation(R.anim.login_img));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Ui.setVisibility(View.VISIBLE);
                Ui.setAnimation(getAnimation(R.anim.login_alpha));
            }
        }, 1200);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_ok : Login(Phone.getText().toString(), Pass.getText().toString()); break;
            case R.id.login_sign : Sign(); break;
            case R.id.login_find : Find(); break;
        }
    }

    public void Login(final String Phone, final String Pass) {
        if (Phone.equals(""))
            ShowToast("휴대전화 번호를 입력해주세요.");
        else if (Pass.equals(""))
            ShowToast("비밀번호를 입력해주세요.");
        else {
            setOnConnectionListener(new OnConnectionListener() {
                @Override
                public void onCall(JSONArray Object) {
                    JSONObject obj = null;
                    try {
                        obj = Object.getJSONObject(0);
                        if (obj.getString("error") != null)
                            ShowToast("계정을 찾을 수 없습니다.");
                    } catch (Exception e) {
                        if (e.getMessage().equals("No value for error")) {
                            try {
                                SharedPreferences SP = getSharedPreferences("Tape", MODE_PRIVATE);
                                SharedPreferences.Editor Editor = SP.edit();
                                Editor.putString("Phone", Phone);
                                Editor.putString("Pass", Pass);
                                Editor.commit();
                                Manager.User = String.valueOf(obj.getInt("id"));
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                finish();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else ShowToast("서버에 접속할 수 없습니다.");
                    }
                }
            });
            String Data[][] = {
                    {"phone", Phone},
                    {"password", Pass},
            };
            HttpMultiPart("POST", Data, null, null, "login");
        }
    }

    public void Sign() {
        startActivity(new Intent(LoginActivity.this, SignActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void Find() {
        startActivity(new Intent(LoginActivity.this, FindActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}