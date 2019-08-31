package com.bytes.tape.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bytes.tape.App.BaseActivity;
import com.bytes.tape.R;

public class SettingActivity extends BaseActivity {

    TextView Open;
    ImageView Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_setting);

        Open = findViewById(R.id.setting_open);
        Open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder Builder = new AlertDialog.Builder(SettingActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                Builder.setItems(Manager.OpenSource[0], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse((String)Manager.OpenSource[1][which])));
                    }
                });
                AlertDialog Dialog = Builder.create();
                Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                Dialog.show();
            }
        });

        Back = findViewById(R.id.setting_back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}