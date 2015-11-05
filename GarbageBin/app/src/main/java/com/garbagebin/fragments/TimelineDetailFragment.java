package com.garbagebin.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.garbagebin.Utils.TouchImageView;
import com.garbagebin.adapters.CommentsAdapter;
import com.garbagebin.models.CommentModel;
import com.pulltozoom_scroll.PullToZoomListView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import garbagebin.com.garbagebin.Post_Comment_activity;
import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 23/10/15.
 */
public class TimelineDetailFragment extends Fragment  {

    Context context;
    Activity activity;
    String  blog_id = "",user_name="";
    public static String customer_id="",thumb_large = "",blog_image="",comment_count="";
    public static  TextView timeline_comments_tv;
    ArrayList<CommentModel> commentModelArrayList = new ArrayList<>();
    PullToZoomListView comments_listView;
    public static CommentsAdapter adapter;
    SharedPreferences sharedPreferences;
    LayoutInflater inflater;
    int pos=0;
    ImageView back_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_time_line_detail,container,false);
        context = getActivity();
        activity = getActivity();

        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");

        Timeline.headerView.setVisibility(View.GONE);
        Timeline.bottom.setVisibility(View.VISIBLE);
        if(sharedPreferences.getString("fname", "").equalsIgnoreCase(""))
        {
            user_name = sharedPreferences.getString("username", "");
        }
        else {
            user_name = sharedPreferences.getString("fname", "")+" "+sharedPreferences.getString("lname", "");
        }

        commentModelArrayList = getArguments().getParcelableArrayList("gallerylistimages");
        blog_image = getArguments().getString("blog_image");
        comment_count = getArguments().getString("comment_count");
        blog_id = getArguments().getString("blog_id");
        pos = getArguments().getInt("position");
        for(int i=0;i<commentModelArrayList.size();i++)
        {
            Log.e("123Comments",commentModelArrayList.get(i).getCommentprofile_image());
        }


        initializeViews(view);

//        timeline_comments_tv.setText(comment_count + " comments ");

        //............Comments layout Data................
        Collections.reverse(commentModelArrayList);
        adapter = new CommentsAdapter(context,activity,commentModelArrayList,"detail",comment_count);
        comments_listView.setAdapter(adapter);
      //  Helper.getListViewSize(comments_listView);

//        commonAdapter = new CommonAdapter(activity, android.R.layout.simple_spinner_dropdown_item, languageModelArrayList,language_spinner);
//        language_spinner.setAdapter(commonAdapter);

        comments_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fragment fr = new Post_Comment_activity();
                FragmentTransaction ft = Timeline.fm.beginTransaction();
//                    Bundle args = new Bundle();
//                    args.putString("blog_id", current.getBlog_id());
                Bundle args = new Bundle();
                args.putString("blog_id", blog_id);
                args.putInt("position", pos);

                args.putParcelableArrayList("gallerylistimages", commentModelArrayList);
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                fr.setArguments(args);
                ft.replace(R.id.content_frame, fr);
                ft.addToBackStack(null);
                ft.commit();
//                Intent in = new Intent(context, Post_Comment_activity.class);
//                in.putExtra("blog_id", blog_id);
//                startActivity(in);
            }
        });


        return view;
    }

    private void initializeViews(View v) {
        back_button = (ImageView)(v.findViewById(R.id.back_button));
        comments_listView = (PullToZoomListView)(v.findViewById(R.id.comments_listView));


        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        comments_listView.getHeaderView().setImageBitmap(getBitmapFromURL(blog_image));
        comments_listView.getHeaderView().setScaleType(ImageView.ScaleType.CENTER_CROP);

        comments_listView.getHeaderView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageDialog();
            }
        });

//        inflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View header = inflater.inflate(R.layout.profile_content_view, null);
//
//
//        imv_timeline_detail = (ImageView)(header.findViewById(R.id.imv_timeline_detail));
//        timeline_comments_tv = (TextView)(header.findViewById(R.id.timeline_comments_tv));
//        comments_listView.addHeaderView(header);
//
//        Glide.with(context)
//                .load(Uri.parse(blog_image)).diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imv_timeline_detail);



//        add_comment_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Fragment fr = new Post_Comment_activity();
//                FragmentTransaction ft = Timeline.fm.beginTransaction();
////                    Bundle args = new Bundle();
////                    args.putString("blog_id", current.getBlog_id());
//                Bundle args = new Bundle();
//                args.putString("blog_id", blog_id);
//                args.putInt("position", pos);
//                args.putParcelableArrayList("gallerylistimages", commentModelArrayList);
//                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
//                fr.setArguments(args);
//                ft.replace(R.id.content_frame, fr);
//                ft.addToBackStack(null);
//                ft.commit();
////                Intent in = new Intent(context, Post_Comment_activity.class);
////                in.putExtra("blog_id", blog_id);
////                startActivity(in);
//            }
//        });



        //  send_comment_btn.setOnClickListener((View.OnClickListener) context);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                }
            }
        });
    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    //............FullView imageView..............
    public void showImageDialog()
    {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        final Dialog dialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.custom_image_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,height);
        final TouchImageView custom_image = (TouchImageView)(dialog.findViewById(R.id.custom_image));
        custom_image.setLayoutParams(params);
        Glide.with(context)
                .load(Uri.parse(blog_image)).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(custom_image);
        custom_image.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
            @Override
            public void onMove() {
                PointF point = custom_image.getScrollPosition();
                RectF rect = custom_image.getZoomedRect();
                float currentZoom = custom_image.getCurrentZoom();
                boolean isZoomed = custom_image.isZoomed();
            }
        });
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
       // lyt_addRelatedData.removeAllViews();

        // initializeViews();
        Log.e("onResume", "onResume");
    }


}