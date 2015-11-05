package com.garbagebin.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 27/10/15.
 */
public class Invite_Referral_Fragment extends Fragment {

    Context context;
    Activity activity;
    TextView invite_tv_val;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.invite_referral_layout,container,false);

        context = getActivity();
        activity = getActivity();

        Timeline.headerView.setVisibility(View.VISIBLE);
        Timeline.profile_pic_layout.setVisibility(View.INVISIBLE);
        Timeline.options_layout.setVisibility(View.VISIBLE);
        Timeline.header_textview.setGravity(Gravity.CENTER);
        Timeline.header_textview.setText(getResources().getString(R.string.invite_referral_tv));
        Timeline.back_icon_layout.setVisibility(View.GONE);
        Timeline.menu_icon_layout.setVisibility(View.VISIBLE);
        Timeline.bottom.setVisibility(View.VISIBLE);
        Timeline.hot_gags_imageView.setImageResource(R.mipmap.hot_gags_tab);
        Timeline.videos_imageView.setImageResource(R.mipmap.video_tab);
        Timeline.home_imageView.setImageResource(R.mipmap.home_tab_copy);
        Timeline. search_imageView.setImageResource(R.mipmap.search_tab);
        Timeline. cart_imageView.setImageResource(R.mipmap.kart_tab);

        initializeViews(view);

        return  view;
    }

    public void initializeViews(View v)
    {
        invite_tv_val = (TextView)   v.findViewById(R.id.invite_tv_val);
        invite_tv_val.setText(Html.fromHtml(getResources().getString(R.string.refer_tv)));
    }

}
