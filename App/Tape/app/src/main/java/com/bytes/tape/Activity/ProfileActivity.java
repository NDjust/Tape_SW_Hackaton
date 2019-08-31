package com.bytes.tape.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bytes.tape.Adapter.MovieListAdapter;
import com.bytes.tape.App.BaseActivity;
import com.bytes.tape.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    ImageView Back, Setting;
    TextView Name;
    CircleImageView Img;

    ListView List;
    MovieListAdapter Adapter;

    public void Init() {
        Back = findViewById(R.id.profile_back);
        Setting = findViewById(R.id.profile_setting);
        Name = findViewById(R.id.profile_name);
        Img = findViewById(R.id.profile_img);

        Back.setOnClickListener(this);
        Setting.setOnClickListener(this);
        Img.setOnClickListener(this);

        List = findViewById(R.id.profile_list);
        Adapter = new MovieListAdapter();
        Adapter.Init(ProfileActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_profile);
        Init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_back :
                onBackPressed();
                break;
            case R.id.profile_setting :
                startActivity(new Intent(ProfileActivity.this, SettingActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.profile_img :
                break;
        }
    }
}