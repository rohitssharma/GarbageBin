package com.garbagebin.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.garbagebin.Utils.AppController;
import com.garbagebin.Utils.CommonUtils;
import com.garbagebin.adapters.TimeLineAdapter;
import com.garbagebin.models.TimelineModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 1/10/15.
 */
public class HotGagsFragment extends Fragment {

    Context context;
    Activity activity;
    SharedPreferences sharedPreferences;
    SwipeRefreshLayout hotgags_swipeRefreshLayout;
    RecyclerView hotgags_list;
    String res="",TAG=HotGagsFragment.class.getSimpleName(),METHOD_NAME="timeline/hotgags",
            tag_string_req="hotgags_request",customer_id="";
    int page_number=1;
    TimeLineAdapter timeLineAdapter;
    List<TimelineModel> hotgagsTimelineArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hot_gags_layout,container,false);

        context = getActivity();
        activity = getActivity();

        Timeline.headerView.setVisibility(View.VISIBLE);
        Timeline.bottom.setVisibility(View.VISIBLE);
        Timeline.hot_gags_imageView.setImageResource(R.mipmap.hot_gags_tab_copy);
        Timeline.videos_imageView.setImageResource(R.mipmap.video_tab);
        Timeline.home_imageView.setImageResource(R.mipmap.home_tab_copy);
        Timeline. search_imageView.setImageResource(R.mipmap.search_tab);
        Timeline. cart_imageView.setImageResource(R.mipmap.kart_tab);

        Timeline.profile_pic_layout.setVisibility(View.VISIBLE);
        Timeline.options_layout.setVisibility(View.VISIBLE);
        Timeline.header_textview.setGravity(Gravity.CENTER);
        Timeline.back_icon_layout.setVisibility(View.GONE);
        Timeline.menu_icon_layout.setVisibility(View.VISIBLE);

        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");

        initializeViews(view);

        timeLineAdapter = new TimeLineAdapter(context,hotgagsTimelineArrayList);
        hotgags_list.setAdapter(timeLineAdapter);

        //it is necessary to write this line without this recycleview will not reflect the data.
        hotgags_list.setLayoutManager(new LinearLayoutManager(getActivity()));

        hotgags_list.addOnItemTouchListener(new CommonUtils.RecyclerTouchListener(context, hotgags_list, new CommonUtils.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Fragment fr=new TimelineDetailFragment();
                FragmentManager fm=getFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                Bundle args = new Bundle();
                args.putString("blog_id", hotgagsTimelineArrayList.get(position).getBlog_id());
                fr.setArguments(args);
                ft.replace(R.id.content_frame, fr);
                ft.addToBackStack(null);
                ft.commit();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        //..............Get TimeLine Data...................
        if (CommonUtils.isNetworkAvailable(context)) {
            getHotGagsReq(page_number, "first");
        }
        else{
            CommonUtils.showCustomErrorDialog1(context,getResources().getString(R.string.bad_connection));
        }

        return view;
    }

    /**
     * Finding Components
     */
    public void initializeViews(View cView)
    {
        hotgags_list = (RecyclerView)(cView.findViewById(R.id.hotgags_list));

        Timeline.header_textview.setText(context.getResources().getString(R.string.hotgags_title));

        //..............Implemeting Pull to refresh......................
        hotgags_swipeRefreshLayout = (SwipeRefreshLayout)(cView.findViewById(R.id.hotgags_swipeRefreshLayout));
        hotgags_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });
    }


    /**
     * Implementing Pull to refresh
     */
    void refreshItems() {
        // Load items
        // ...
        page_number = page_number+1;
        if (CommonUtils.isNetworkAvailable(context)) {
            getHotGagsReq(page_number,"second");
        }
        else{
            CommonUtils.showCustomErrorDialog1(context,getResources().getString(R.string.bad_connection));
        }
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        timeLineAdapter.notifyDataSetChanged();

        // Stop refresh animation
        hotgags_swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Implementing Webservice
     */
    //.....................Get HotGags Request..........................
    public String getHotGagsReq(int page_counter,String when) {

        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);
        if(when.equalsIgnoreCase("second"))
        {
            CommonUtils.closeSweetProgressDialog(context, pd);
        }

        String url = getResources().getString(R.string.base_url)+METHOD_NAME+"&p="+page_counter;
        Log.e(TAG, url);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        checkHotGagsResponse(res);
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
                return null;
            }
            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }

    public void checkHotGagsResponse(String response)
    {
        try
        {
            Log.e(TAG,response);
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
            if(jsonObject.has("hotGags"))
            {
                JSONArray jsonArray = new JSONArray(jsonObject.getString("hotGags"));
                for(int i=0;i<jsonArray.length();i++)
                {
                    TimelineModel timelineModel = new TimelineModel();
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    if(jsonObject1.has("blog_id"))
                    {
                        timelineModel.setBlog_id(jsonObject1.getString("blog_id"));
                    }
                    if(jsonObject1.has("category_id"))
                    {
                        timelineModel.setCategory_id(jsonObject1.getString("category_id"));
                    }
                    if(jsonObject1.has("comic_id"))
                    {
                        timelineModel.setComic_id(jsonObject1.getString("comic_id"));
                    }
                    if(jsonObject1.has("created"))
                    {
                        timelineModel.setCreated(jsonObject1.getString("created"));
                    }
                    if(jsonObject1.has("start_date"))
                    {
                        timelineModel.setStart_date(jsonObject1.getString("start_date"));
                    }
                    if(jsonObject1.has("hits"))
                    {
                        timelineModel.setHits(jsonObject1.getString("hits"));
                    }
                    if(jsonObject1.has("image"))
                    {
                        timelineModel.setImage(jsonObject1.getString("image"));
                    }
                    if(jsonObject1.has("video"))
                    {
                        timelineModel.setVideo(jsonObject1.getString("video"));
                    }
                    if(jsonObject1.has("title"))
                    {
                        timelineModel.setTitle(jsonObject1.getString("title"));
                    }
                    if(jsonObject1.has("thumb_xsmall"))
                    {
                        timelineModel.setThumb_large(jsonObject1.getString("thumb_xsmall"));
                    }
                    if(jsonObject1.has("thumb"))
                    {
                        timelineModel.setThumb(jsonObject1.getString("thumb"));
                    }
                    if(jsonObject1.has("comment_count"))
                    {
                        timelineModel.setComment_count(jsonObject1.getString("comment_count"));
                    }
                    if(jsonObject1.has("likecount"))
                    {
                        timelineModel.setLike_count(jsonObject1.getString("likecount"));
                    }
                    if(jsonObject1.has("time_ago"))
                    {
                        timelineModel.setTime_ago(jsonObject1.getString("time_ago"));
                    }
                    if(jsonObject1.has("video_thumb"))
                    {
                        timelineModel.setVideo_thumb(jsonObject1.getString("video_thumb"));
                    }
                    if(jsonObject1.has("hits"))
                    {
                        timelineModel.setHits(jsonObject1.getString("hits"));
                    }
                    hotgagsTimelineArrayList.add(timelineModel);
                }
            }

            Collections.reverse(hotgagsTimelineArrayList);
            if(message.equalsIgnoreCase("failure"))
            {
                CommonUtils.showCustomErrorDialog1(context,error);
            }
            else
            {
                timeLineAdapter.notifyDataSetChanged();
            }
            // Load complete
            onItemsLoadComplete();
        }
        catch(JSONException ex)
        {
        }
    }
}
