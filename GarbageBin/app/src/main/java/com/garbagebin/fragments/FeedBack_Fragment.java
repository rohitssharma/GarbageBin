package com.garbagebin.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.garbagebin.Utils.CommonUtils;

import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 26/10/15.
 */
public class FeedBack_Fragment extends Fragment {

    Context context;
    Activity activity;
    Button btn_send_feedback;
    EditText et_feedback_contact;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feedback_layout,container,false);

        context = getActivity();
        activity = getActivity();
        Timeline.bottom.setVisibility(View.VISIBLE);
        Timeline.headerView.setVisibility(View.VISIBLE);
        Timeline.profile_pic_layout.setVisibility(View.INVISIBLE);
        Timeline.options_layout.setVisibility(View.VISIBLE);
        Timeline.header_textview.setGravity(Gravity.CENTER);
        Timeline.header_textview.setText(getResources().getString(R.string.feedback_tv));
        Timeline.back_icon_layout.setVisibility(View.GONE);
        Timeline.menu_icon_layout.setVisibility(View.VISIBLE);

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
        btn_send_feedback = (Button)(v.findViewById(R.id.btn_send_feedback));
        et_feedback_contact = (EditText)(v.findViewById(R.id.et_feedback_contact));

        btn_send_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_feedback_contact.getText().toString().length() >0)
                {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", "abc@gmail.com", null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                    emailIntent.putExtra(Intent.EXTRA_TEXT   , et_feedback_contact.getText().toString());
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
                else
                {
                    CommonUtils.showCustomErrorDialog1(context,"Please enter feedback.");
                }
            }
        });
    }
}
