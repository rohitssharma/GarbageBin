package com.garbagebin.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.garbagebin.Utils.CircleImageView;
import com.garbagebin.models.CommentReplyModel;

import java.util.ArrayList;

import garbagebin.com.garbagebin.R;

/**
 * Created by sharanjeet on 3/11/15.
 */
public class ReplyCommentsAdapter extends BaseAdapter {

    Context context;
    Activity activity;
    ArrayList<CommentReplyModel> al;

    public ReplyCommentsAdapter(Context context, Activity activity, ArrayList<CommentReplyModel> al) {
        this.context = context;
        this.activity = activity;
        this.al = al;
    }

    @Override
    public int getCount() {
        return al.size();
    }

    @Override
    public Object getItem(int i) {
        return al.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

      //  final View view = convertView;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_reply_layout, null);
            holder.comment_user_tv = (TextView) (convertView.findViewById(R.id.reply_user_tv));
            holder.comment_tv = (TextView) convertView.findViewById(R.id.reply_tv);
            holder.sub_comment_twolike_tv = (TextView)(convertView.findViewById(R.id.sub_comment_twolike_tv));
            holder.progress_loading = (ProgressBar)(convertView.findViewById(R.id.replyprogress_loading));
            holder.comments_profile_pic = (CircleImageView)(convertView.findViewById(R.id.reply_profile_pic));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Log.e("hello",al.get(i).getUser());

        holder.comment_user_tv.setText(al.get(i).getUser());
        holder.comment_tv.setText(al.get(i).getComment());

        if (al.get(i).getProfile_image().equalsIgnoreCase("")) {
            holder. comments_profile_pic.setImageResource(R.mipmap.profile_pic_sec);
        } else {
            Glide.with(context)
                    .load(Uri.parse(al.get(i).getProfile_image())).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.comments_profile_pic);
        }
        return  convertView;
    }

    public static class ViewHolder {
        TextView comment_user_tv,comment_tv,sub_comment_twolike_tv;
        CircleImageView comments_profile_pic;
        public static ProgressBar progress_loading;
    }
}
