package com.bytes.tape.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bytes.tape.Adapter.MovieListAdapter;
import com.bytes.tape.App.BaseActivity;
import com.bytes.tape.App.Data;
import com.bytes.tape.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    MovieListAdapter Adapter;
    ListView ListView;
    ScrollView Scroll;

    EditText Search_Edit;
    ImageView Upload, Search;
    CircleImageView Profile;

    private final int SELECT_MOVIE = 1000;
    public int Page = 0;
    public static Context Context;

    public void Init() {
        Upload = findViewById(R.id.main_upload);
        Profile = findViewById(R.id.main_profile_img);
        Upload.setOnClickListener(this);
        Profile.setOnClickListener(this);

        ListView = findViewById(R.id.main_list);
        Scroll = findViewById(R.id.main_scroll);

        Search = findViewById(R.id.main_search);
        Search_Edit = findViewById(R.id.main_search_edit);
        Search.setOnClickListener(this);

        Search_Edit.setOnEditorActionListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        Context = this;
        Init();
        getData();
    }

    public void getData() {
        setOnConnectionListener(new OnConnectionListener() {
            @Override
            public void onCall(JSONArray Object) {
                try {
                    Adapter = new MovieListAdapter();
                    Adapter.Init(MainActivity.this);
                    ListView.setAdapter(Adapter);
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
                    setListViewHeight(ListView);
                } catch (Exception e) {
                    e.printStackTrace();
                    ShowToast("서버에 접속할 수 없습니다.");
                }
            }
        });
        HttpMultiPart("GET", null, null, null, "main/videovideos/");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_upload :
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("video/*");
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                try {
                    startActivityForResult(i, SELECT_MOVIE);
                } catch (android.content.ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.main_profile_img :
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.main_search :
                Search();
                break;
        }
    }

    public void Search() {
        if (Search_Edit.getText().toString().equals(""))
            ShowToast("검색어를 입력해주세요.");
        else {
            Intent Intent = new Intent(MainActivity.this, SearchActivity.class);
            Intent.putExtra("Search", Search_Edit.getText().toString());
            startActivity(Intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_MOVIE) {
                Uri uri = intent.getData();
                String path = getRealPathFromURI(uri);

                Intent Intent = new Intent(MainActivity.this, UploadActivity.class);
                Intent.putExtra("Path", path);
                startActivity(Intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }
    }
    private String getRealPathFromURI(Uri contentUri) {
        try {
            if (contentUri.getPath().startsWith("/storage")) {
                return contentUri.getPath();
            }
            String id = DocumentsContract.getDocumentId(contentUri).split(":")[1];
            String[] columns = {
                    MediaStore.Files.FileColumns.DATA
            };
            String selection = MediaStore.Files.FileColumns._ID + " = " + id;
            Cursor cursor = getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, null, null);
            try {
                int columnIndex = cursor.getColumnIndex(columns[0]);
                if (cursor.moveToFirst()) {
                    return cursor.getString(columnIndex);
                }
            } finally {
                cursor.close();
            } return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE)
            Search();
        return false;
    }
}