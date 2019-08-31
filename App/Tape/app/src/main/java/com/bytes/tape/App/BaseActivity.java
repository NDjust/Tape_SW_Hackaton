package com.bytes.tape.App;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class BaseActivity extends AppCompatActivity {

    public Manager Manager = com.bytes.tape.App.Manager.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Animation getAnimation(Integer File) {
        return AnimationUtils.loadAnimation(getApplicationContext(), File);
    }

    public void ShowToast(String Value) {
        Toast.makeText(getApplicationContext(), Value, Toast.LENGTH_SHORT).show();
    }

    public void QuestionDialog(final EditText Edit) {
        AlertDialog.Builder Builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        Builder.setItems(Manager.Questions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Edit.setText(Manager.Questions[which]);
            }
        });
        AlertDialog Dialog = Builder.create();
        Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Dialog.show();
    }

    public static void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public interface OnConnectionListener {
        void onCall(JSONArray Object);
    }
    OnConnectionListener OnConnect;
    public void setOnConnectionListener(OnConnectionListener OnConnect) {
        this.OnConnect = OnConnect;
    }

    public void HttpMultiPart(final String V, final String Data[][], final File File, final String FileName, final String URL) {
        new AsyncTask<Void, Void, JSONArray>(){
            @Override
            protected JSONArray doInBackground(Void... voids) {
                String boundary = "^-----^";
                String LINE_FEED = "\r\n";
                String charset = "UTF-8";
                OutputStream outputStream;
                PrintWriter writer;

                JSONArray result = null;

                try {
                    URL url = new URL("http://15.164.191.30/" + URL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod(V);
                    if (!V.equals("GET")) {
                        connection.setRequestProperty("Content-Type", "multipart/form-data;charset=utf-8;boundary=" + boundary);
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.setUseCaches(false);
                        connection.setConnectTimeout(15000);

                        outputStream = connection.getOutputStream();
                        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);

                        if (Data != null)
                            for (int i = 0; i < Data.length; i++) {
                                writer.append("--" + boundary).append(LINE_FEED);
                                writer.append("Content-Disposition: form-data; name=\"" + Data[i][0] + "\"").append(LINE_FEED);
                                writer.append("Content-Type: text/plain; charset=" + charset).append(LINE_FEED);
                                writer.append(LINE_FEED);
                                writer.append(Data[i][1]).append(LINE_FEED);
                                writer.flush();
                            }
                        if (File != null) {
                            writer.append("--" + boundary).append(LINE_FEED);
                            writer.append("Content-Disposition: form-data; name=\"" + FileName + "\"; filename=\"" + File.getName() + "\"").append(LINE_FEED);
                            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(File.getName())).append(LINE_FEED);
                            writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
                            writer.append(LINE_FEED);
                            writer.flush();

                            FileInputStream inputStream = new FileInputStream(File);
                            byte[] buffer = new byte[(int)File.length()];
                            int bytesRead = -1;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }
                            outputStream.flush();
                            inputStream.close();
                            writer.append(LINE_FEED);
                            writer.flush();
                        }
                        writer.append("--" + boundary + "--").append(LINE_FEED);
                        writer.close();
                    }
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        try {
                            result = new JSONArray(response.toString());
                        } catch (JSONException e) {
                            result = new JSONArray("["+response.toString()+"]");
                        }
                    } else {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        try {
                            result = new JSONArray(response.toString());
                        } catch (JSONException e) {
                            result = new JSONArray("["+response.toString()+"]");
                        }
                    }
                } catch (ConnectException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
                return result;
            }
            @Override
            protected void onPostExecute(JSONArray jsonObject) {
                super.onPostExecute(jsonObject);
                if (OnConnect != null)
                    OnConnect.onCall(jsonObject);
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}