package com.garbagebin.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 28/10/15.
 */
public class Comics_Fragment extends Fragment {

    Context context;
    Activity activity;
    SwipeRefreshLayout swipeRefreshLayout_comics;
    RecyclerView comics_list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comics_layout,container,false);
        context = getActivity();
        activity = getActivity();

        Timeline.headerView.setVisibility(View.VISIBLE);
        Timeline.header_textview.setText("Comics");
        Timeline.bottom.setVisibility(View.VISIBLE);
        Timeline.profile_pic_layout.setVisibility(View.VISIBLE);
        Timeline.options_layout.setVisibility(View.VISIBLE);
        Timeline.header_textview.setGravity(Gravity.CENTER);
        Timeline.back_icon_layout.setVisibility(View.GONE);
        Timeline.menu_icon_layout.setVisibility(View.VISIBLE);

        Timeline.hot_gags_imageView.setImageResource(R.mipmap.hot_gags_tab);
        Timeline.videos_imageView.setImageResource(R.mipmap.video_tab);
        Timeline.home_imageView.setImageResource(R.mipmap.home_tab_copy);
        Timeline.search_imageView.setImageResource(R.mipmap.search_tab);
        Timeline.cart_imageView.setImageResource(R.mipmap.kart_tab);

        initializeViews(view);

        return view;
    }

    public void initializeViews(View cView)
    {
        swipeRefreshLayout_comics = (SwipeRefreshLayout)(cView.findViewById(R.id.swipeRefreshLayout_comics));
        comics_list = (RecyclerView)(cView.findViewById(R.id.comics_list));
    }
}
