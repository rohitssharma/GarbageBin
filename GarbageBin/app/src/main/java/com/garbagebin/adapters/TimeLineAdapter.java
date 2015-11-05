package com.garbagebin.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.garbagebin.Utils.AppController;
import com.garbagebin.Utils.CommonUtils;
import com.garbagebin.fonts_classes.GothamBookTextView;
import com.garbagebin.fragments.TimelineDetailFragment;
import com.garbagebin.models.CommentModel;
import com.garbagebin.models.TimelineModel;
import com.garbagebin.youtube_classes.VideoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import garbagebin.com.garbagebin.Post_Comment_activity;
import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 14/10/15.
 */
public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    public static  List<TimelineModel> data;
    String METHOD_NAME = "timeline/like",res="",tag_string_req="like_a_gag_req",TAG="TimeLineAdapter",
            comment_id="",comment="",customer_id="";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public TimeLineAdapter(Context context, List<TimelineModel> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_timeline, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final TimelineModel current = data.get(position);
        Log.e("timeline adapter",data.get(position).getCommentModelArrayList().size()+"");
        holder.timeline_tv.setText(current.getTitle());

        if(current.getLike_count().equalsIgnoreCase("0") || current.getLike_count().equalsIgnoreCase("1"))
        {
            holder.likes_count_textView.setText(current.getLike_count() + " Like");
        }
        else
        {
            holder.likes_count_textView.setText(current.getLike_count() + " Likes");
        }


        if(current.getComment_count().equalsIgnoreCase("0") || current.getComment_count().equalsIgnoreCase("1"))
        {
            holder.comments_count_textView.setText(current.getComment_count() + " Comment");
        }
        else
        {
            holder.comments_count_textView.setText(current.getComment_count() + " Comments");
        }

        holder.views_tv.setText(current.getHits());
        holder.tym_tv.setText(current.getTime_ago());

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        holder.timeline_imageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, height / 2));
        Log.e("type : ", current.getType());

        if(current.getUserLike().equalsIgnoreCase("0"))
        {
            holder.like_imageView.setImageResource(R.mipmap.like);
        }
        else
        {
            holder.like_imageView.setImageResource(R.mipmap.like_selected);
        }

        if(current.getType().equalsIgnoreCase("comic") )
        {
            Log.e("comic size : ", current.getComic().size()+"" );
            holder.timeline_comic_strip.setVisibility(View.VISIBLE);
            holder.timeline_imageView.setVisibility(View.GONE);
            holder.timeline_play_btn.setVisibility(View.GONE);

            for(int i=0;i<current.getComic().size();i++)
            {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View cv = inflater.inflate(R.layout.custom_comic_layout, null);

                ImageView imageView = (ImageView)(cv.findViewById(R.id.comic_imageView));
                Glide.with(context)
                        .load(Uri.parse(current.getComic().get(i).getImage())).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);

                holder.comic_layout.addView(cv);
            }
        }
        else {
            Log.e("comic size : ", "000" );
            holder.timeline_comic_strip.setVisibility(View.GONE);
            holder.timeline_imageView.setVisibility(View.VISIBLE);

            Log.e("TimeLine Adapter : ", current.getThumb_large());

            if (!current.getThumb_large().equalsIgnoreCase("")) {
                Log.e("TimeLine Adapter : ", "not blank");
                holder.timeline_play_btn.setVisibility(View.GONE);
                Glide.with(context)
                        .load(Uri.parse(current.getThumb_large())).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.timeline_imageView);
            } else {
                Log.e("TimeLine Adapter : ", "blank");
                holder.timeline_play_btn.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Uri.parse(current.getVideo_thumb())).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.timeline_imageView);
            }
        }

        final String blog_id= current.getBlog_id();

        holder.share_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current.getType().equalsIgnoreCase("gag") && !current.getThumb_large().equalsIgnoreCase("")) {
                    Uri imageUri = Uri.parse(current.getThumb_large());
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    shareIntent.setType("image/jpeg");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(Intent.createChooser(shareIntent, "send"));
                } else if (current.getType().equalsIgnoreCase("gag") && current.getThumb_large().equalsIgnoreCase("")) {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    Uri screenshotUri = Uri.parse(current.getVideo());

                    sharingIntent.setType("image/png");
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                    context.startActivity(Intent.createChooser(sharingIntent, "Share image using"));
                } else {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/html");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>This is the text that will be shared.</p>"));
                    context.startActivity(Intent.createChooser(sharingIntent, "Share using"));
                }

            }
        });

        final  ArrayList<CommentModel> aal = current.getCommentModelArrayList();

        for(int i=0;i<aal.size();i++)
        {
            Log.e("Comments Model",aal.get(i).getCommentprofile_image());
        }

        holder.comment_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fr = new Post_Comment_activity();
                FragmentTransaction ft = Timeline.fm.beginTransaction();
//                    Bundle args = new Bundle();
//                    args.putString("blog_id", current.getBlog_id());
                Bundle args = new Bundle();
                args.putString("blog_id", current.getBlog_id());
                args.putInt("position", position);
                args.putParcelableArrayList("gallerylistimages", (ArrayList<? extends Parcelable>) data.get(position).getCommentModelArrayList());
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                fr.setArguments(args);
                ft.replace(R.id.content_frame, fr);
                ft.addToBackStack(null);
                ft.commit();
//                Intent in = new Intent(context, Post_Comment_activity.class);
//                Bundle bun = new Bundle();
//                bun.putInt("position", position);
//                bun.putString("blogid", blog_id);
//                //    bun.putParcelableArrayList("timelinelist", (ArrayList<? extends Parcelable>) data);
//                bun.putParcelableArrayList("gallerylistimages", aal);
//                in.putExtra("list", bun);
//                // in.putParcelableArrayListExtra("gallerylistimages", (ArrayList<? extends Parcelable>) data);
//                context.startActivity(in);

                //  ((Activity)(context)).overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out_noeffect);
            }
        });

        holder.like_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(current.getUserLike().equalsIgnoreCase("0"))
                {
                    makeGagLikeReq(blog_id,position);
                }
            }
        });

        final String thumbLarge = current.getThumb_large();
        holder.timeline_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (thumbLarge.equalsIgnoreCase("")) {

                    if (current != null) {
                        String url = current.getVideo();
                        Log.d("URL : : ", url);
                        if (url != null) {
                            int i = url.lastIndexOf("/") + 1;
                            final String url1 = url.substring(i);
                            int index = url1.lastIndexOf("=") + 1;
                            final String id = url1.substring(index);
                            Log.e("Youtube video ID :", id);
                            Intent in = new Intent(context, VideoActivity.class);
                            in.putExtra("videoid", id);
                            context.startActivity(in);
                        } else {
                        }
                    }
                } else {
                    Fragment fr = new TimelineDetailFragment();
                    FragmentTransaction ft = Timeline.fm.beginTransaction();
//                    Bundle args = new Bundle();
//                    args.putString("blog_id", current.getBlog_id());
                    Bundle args = new Bundle();
                    args.putString("blog_id", current.getBlog_id());
                    args.putString("blog_image", current.getThumb_large());
                    args.putString("comment_count", current.getComment_count());
                    args.putInt("position", position);
                    args.putParcelableArrayList("gallerylistimages", (ArrayList<? extends Parcelable>) data.get(position).getCommentModelArrayList());
                    ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    fr.setArguments(args);
                    ft.replace(R.id.content_frame, fr);
                    ft.addToBackStack(null);
                    ft.commit();
                }
//                I
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends  RecyclerView.ViewHolder
    {
        GothamBookTextView timeline_tv,likes_count_textView,comments_count_textView,tym_tv,views_tv;
        ImageView timeline_imageView,timeline_play_btn,like_imageView,comment_imageView,share_imageView;
        LinearLayout comic_layout;
        FrameLayout timeline_comic_strip;

        public MyViewHolder(View itemView) {
            super(itemView);

            tym_tv = (GothamBookTextView) itemView.findViewById(R.id.tym_tv);
            timeline_tv = (GothamBookTextView) itemView.findViewById(R.id.timeline_tv);
            likes_count_textView = (GothamBookTextView) itemView.findViewById(R.id.likes_count_textView);
            comments_count_textView = (GothamBookTextView) itemView.findViewById(R.id.comments_count_textView);
            views_tv = (GothamBookTextView)(itemView.findViewById(R.id.views_tv));
            comment_imageView = (ImageView)(itemView.findViewById(R.id.comment_imageView));
            share_imageView = (ImageView)(itemView.findViewById(R.id.share_imageView));
            like_imageView = (ImageView)(itemView.findViewById(R.id.like_imageView));
            timeline_comic_strip = (FrameLayout)(itemView.findViewById(R.id.timeline_comic_strip));
            comic_layout = (LinearLayout)(itemView.findViewById(R.id.comic_layout));
            timeline_play_btn  = (ImageView)(itemView.findViewById(R.id.timeline_play_btn));
            timeline_imageView = (ImageView)(itemView.findViewById(R.id.timeline_imageView));
        }
    }

    //..............Like a GAg....................

    /**
     * Implementing Webservice
     */
    //.....................Get Timeline Request..........................
    public String makeGagLikeReq(final String gag_id,final int pos) {

        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);

        String url = context.getResources().getString(R.string.base_url)+METHOD_NAME;
        Log.e(TAG,url);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        checkGagLikeResponse(res,pos);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CommonUtils.closeSweetProgressDialog(context, pd);
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                res = error.toString();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", customer_id);
                params.put("blog_id", gag_id);
                params.put("comment", comment);
                params.put("comment_id",comment_id );
                Log.e(TAG, params.toString());
                return params;
            }
            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }

    public void checkGagLikeResponse(String response,int p)
    {
        Log.e(TAG,response);
        try
        {
            String message="",error="";
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.has("message"))
            {
                message = jsonObject.getString("message");
            }
            if(jsonObject.has("error"))
            {
                error = jsonObject.getString("error");
            }
            if(message.equalsIgnoreCase("success"))
            {
                data.get(p).setUserLike("1");
                int likecount = Integer.parseInt(data.get(p).getLike_count())+1;
                data.get(p).setLike_count(String.valueOf(likecount));
                notifyDataSetChanged();

                //  CommonUtils.showCustomErrorDialog1(context,message);
            }
            else
            {
                CommonUtils.showCustomErrorDialog1(context,error);
            }
        }
        catch(JSONException e)
        {
        }
    }
}

