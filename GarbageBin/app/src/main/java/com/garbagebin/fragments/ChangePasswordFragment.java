package com.garbagebin.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 26/10/15.
 */
public class ChangePasswordFragment extends Fragment {

    Context context;
    Activity activity;
    EditText old_pwd_et,new_pwd_et,cnfrm_pwd_et;
    Button chnge_pwd_button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_pwd_layout,container,false);

        context = getActivity();
        activity = getActivity();

        Timeline.bottom.setVisibility(View.VISIBLE);
        Timeline.headerView.setVisibility(View.VISIBLE);
        Timeline.profile_pic_layout.setVisibility(View.INVISIBLE);
        Timeline.options_layout.setVisibility(View.VISIBLE);
        Timeline.header_textview.setGravity(Gravity.CENTER);
        Timeline.header_textview.setText(getResources().getString(R.string.change_pwd_tv));
        Timeline.back_icon_layout.setVisibility(View.VISIBLE);
        Timeline.menu_icon_layout.setVisibility(View.GONE);

        Timeline.hot_gags_imageView.setImageResource(R.mipmap.hot_gags_tab);
        Timeline.videos_imageView.setImageResource(R.mipmap.video_tab);
        Timeline.home_imageView.setImageResource(R.mipmap.home_tab_copy);
        Timeline. search_imageView.setImageResource(R.mipmap.search_tab);
        Timeline. cart_imageView.setImageResource(R.mipmap.kart_tab);

        Timeline.back_icon_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        initializeViews(view);

        return  view;
    }

    public void initializeViews(View v)
    {
        old_pwd_et = (EditText)(v.findViewById(R.id.old_pwd_et));
        new_pwd_et = (EditText)(v.findViewById(R.id.new_pwd_et));
        cnfrm_pwd_et = (EditText)(v.findViewById(R.id.cnfrm_pwd_et));
    }
}
