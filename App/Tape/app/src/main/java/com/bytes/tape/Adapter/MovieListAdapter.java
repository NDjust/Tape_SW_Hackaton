package com.bytes.tape.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import com.bumptech.glide.Glide;
import com.bytes.tape.Activity.ShowActivity;
import com.bytes.tape.App.Data;
import com.bytes.tape.R;

import java.util.ArrayList;

public class MovieListAdapter extends BaseAdapter {

    View View;
    ArrayList<Data.Movie> List = new ArrayList<>();
    Context Context;

    @Override
    public int getCount() {
        return List.size();
    }

    @Override
    public Object getItem(int i) {
        return List.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View = inflater.inflate(R.layout.adp_main_list, viewGroup, false);

        TextView Title = View.findViewById(R.id.adp_main_list_title);
        TextView Name = View.findViewById(R.id.adp_main_list_name);
        TextView Date = View.findViewById(R.id.adp_main_list_date);

        final ImageView Img = View.findViewById(R.id.adp_main_list_img);
        GradientDrawable Drawable= (GradientDrawable) viewGroup.getContext().getDrawable(R.drawable.list_round);
        Img.setBackground(Drawable);
        Img.setClipToOutline(true);
        Glide.with(Context).load(List.get(i).getImgURL()).into(Img);

        Title.setText(List.get(i).getTitle());
        Name.setText(List.get(i).getName());
        Date.setText(List.get(i).getDate());

        View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)Context, Pair.<View, String>create(Img, Img.getTransitionName()));
                Intent Intent = new Intent(Context, ShowActivity.class);
                Intent.putExtra("ListData", List.get(i));
                Context.startActivity(Intent, options.toBundle());
            }
        });
        return View;
    }

    public void addItem(Data.Movie Data) {
        List.add(Data);
    }
    public void Init(Context Context) {
        this.Context = Context;
    }
}