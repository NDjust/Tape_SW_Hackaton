package com.bytes.tape.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bytes.tape.Adapter.MovieListAdapter;
import com.bytes.tape.App.BaseActivity;
import com.bytes.tape.App.Data;
import com.bytes.tape.R;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    ImageView Back, Search, Upload;
    CircleImageView Profile;
    EditText Edit;

    ListView List;
    MovieListAdapter Adapter;
    private final int SELECT_MOVIE = 1000;

    public void Init() {
        Back = findViewById(R.id.search_back);
        Search = findViewById(R.id.search_search);
        Upload = findViewById(R.id.search_upload);
        Profile = findViewById(R.id.search_profile_img);
        Edit = findViewById(R.id.search_edit);

        List = findViewById(R.id.search_list);
        Adapter = new MovieListAdapter();
        Adapter.Init(SearchActivity.this);

        Back.setOnClickListener(this);
        Search.setOnClickListener(this);
        Upload.setOnClickListener(this);
        Profile.setOnClickListener(this);

        Edit.setText(getIntent().getStringExtra("Search"));
        Edit.setOnEditorActionListener(this);
        Search(Edit.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_search);
        Init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_back :
                onBackPressed();
                break;
            case R.id.search_search :
                Search();
                break;
            case R.id.search_upload :
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("video/*");
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                try {
                    startActivityForResult(i, SELECT_MOVIE);
                } catch (android.content.ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.search_profile_img :
                startActivity(new Intent(SearchActivity.this, ProfileActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }

    public void Search() {
        if (Edit.getText().toString().equals(""))
            ShowToast("검색어를 입력해주세요.");
        else Search(Edit.getText().toString());
    }

    public void Search(String Value) {
        setOnConnectionListener(new OnConnectionListener() {
            @Override
            public void onCall(JSONArray Object) {
                try {
                    Adapter = new MovieListAdapter();
                    Adapter.Init(SearchActivity.this);
                    List.setAdapter(Adapter);
                    for (int i = 0; i < Object.length(); i++) {
                        JSONObject Obj = Object.getJSONObject(i);
                        int id = Obj.getInt("id");
                        String title = Obj.getString("title");
                        String name = Obj.getString("user");
                        String date = Obj.getString("pub_date").split("T")[0];

                        String tmp_v[] = Obj.getString("filterpath").split("127.0.0.1:8000");
                        String video_path = tmp_v[0] + "15.164.191.30" + tmp_v[1];

                        String tmp_img[] = Obj.getString("thumbnail").split("127.0.0.1:8000");
                        String img_path = tmp_img[0] + "15.164.191.30" + tmp_img[1];

                        String content = Obj.getString("description");
                        Adapter.addItem(new Data.Movie(id, name, content, title, date, img_path, video_path));
                    }
                    setListViewHeight(List);
                } catch (Exception e) {
                    e.printStackTrace();
                    ShowToast("서버에 접속할 수 없습니다.");
                }
            }
        });
        HttpMultiPart("GET", null, null, null, "main/videovideos/?q=" + Value);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE)
            Search();
        return false;
    }
}