package com.bytes.tape.Activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bytes.tape.App.BaseActivity;
import com.bytes.tape.App.Data;
import com.bytes.tape.R;

public class ShowActivity extends BaseActivity implements View.OnClickListener {

    TextView Title, Date, Content, Name;
    LinearLayout Back, Bottom;
    ImageView Img;

    Data.Movie Data;
    ProgressBar Bar;

    public void Init() {
        Title = findViewById(R.id.show_title);
        Date = findViewById(R.id.show_date);
        Content = findViewById(R.id.show_content);
        Name = findViewById(R.id.show_name);
        Back = findViewById(R.id.show_back);
        Img = findViewById(R.id.show_img);
        Bottom = findViewById(R.id.show_bottom);
        Bar = findViewById(R.id.show_bar);
        Back.setOnClickListener(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Img.setVisibility(View.GONE);
            }
        }, 1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_show);
        Init();
        setData();
        setVideo();
    }

    public void setData() {
        Data = (Data.Movie) getIntent().getSerializableExtra("ListData");
        Title.setText(Data.getTitle());
        Date.setText(Data.getDate());
        Content.setText(Data.getContent());
        Name.setText(Data.getName());
        Glide.with(ShowActivity.this).load(Data.getImgURL()).into(Img);
    }

    public void setVideo() {
        final VideoView Video = findViewById(R.id.show_video);
        MediaController controller = new MediaController(ShowActivity.this);
        Video.setMediaController(controller);
        Video.requestFocus();
        Video.setVideoURI(Uri.parse(Data.getMovieURL()));

        Video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Bar.setVisibility(View.GONE);
                Video.start();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_back :
                onBackPressed();
                break;
        }
    }
}