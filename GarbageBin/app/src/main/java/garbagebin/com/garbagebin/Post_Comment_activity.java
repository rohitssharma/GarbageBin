package garbagebin.com.garbagebin;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.garbagebin.Utils.AppController;
import com.garbagebin.Utils.CommonUtils;
import com.garbagebin.Utils.Helper;
import com.garbagebin.adapters.PostCommentAdapter;
import com.garbagebin.fragments.Home_Fragment;
import com.garbagebin.fragments.TimelineDetailFragment;
import com.garbagebin.models.CommentModel;
import com.garbagebin.models.CommonModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Post_Comment_activity extends Fragment implements View.OnClickListener {

    Context context;
    Activity activity;
    SharedPreferences sharedPreferences;
    String customer_id="",user_name="",blog_id="",res="",tag_string_req="postRequest",blogid=""
            ,comment_id="",TAG=Post_Comment_activity.class.getSimpleName(),METHOD_NAME_post="timeline/comment";
    ImageView img_timeline_zoom;
    TextView tv_timelineDetail,timeline_comments_tv;
    Button send_comment_btn;
    LinearLayout lyt_addRelatedData;
    ListView comments_listView;
    FrameLayout comments_layout;
    EditText send_comment_et;
    //    ArrayList<CommentModel> commentModelArrayList = new ArrayList<>();
    public static  PostCommentAdapter adapter;
    CommonModel model;
   public static ArrayList<CommentModel> comments_arraylist = new ArrayList<>();
    int pos = 0;
    // ArrayList<TimelineModel> timelinelist = new ArrayList<>();

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_post_comment_activity);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_post_comment_activity, container, false);

        context = getActivity();
        activity =  getActivity();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Timeline.headerView.setVisibility(View.GONE);
        Timeline.bottom.setVisibility(View.GONE);

        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");

        if(sharedPreferences.getString("fname", "").equalsIgnoreCase(""))
        {
            user_name = sharedPreferences.getString("username", "");
        }
        else {
            user_name = sharedPreferences.getString("fname", "")+" "+sharedPreferences.getString("lname", "");
        }
//        Bundle extras = getIntent().getBundleExtra("list");
//        if (extras != null) {
//            comments_arraylist = extras.getParcelableArrayList("gallerylistimages");
//        //    timelinelist = extras.getParcelableArrayList("timelinelist");
//            Log.e("Test comments_arraylist", comments_arraylist.size()+"");
//            pos = extras.getInt("position",0);
//            blogid = extras.getString("blogid");
//        }

        comments_arraylist = getArguments().getParcelableArrayList("gallerylistimages");
        blogid = getArguments().getString("blog_id");
        pos = getArguments().getInt("position",0);

        for(int i=0;i<comments_arraylist.size();i++)
        {
            Log.e("Comments Model22",comments_arraylist.get(i).getCommentprofile_image()+"//"+comments_arraylist.get(i).getComment());
        }

        comments_listView = (ListView)(view.findViewById(R.id.post_comments_listView));
        send_comment_btn = (Button)(view.findViewById(R.id.send_comment_btn));
        send_comment_et = (EditText)(view.findViewById(R.id.send_comment_et));
        send_comment_btn.setOnClickListener(this);

        if(comments_arraylist.size()<10)
        {
            comments_listView.setStackFromBottom(false);
        }

        //............Comments layout Data................
        adapter = new PostCommentAdapter(context,activity,comments_arraylist,"post",pos);
        comments_listView.setAdapter(adapter);


        return  view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.send_comment_btn:

                if(CommonUtils.isNetworkAvailable(context)) {
                    if (!send_comment_et.getText().toString().equalsIgnoreCase("")) {
                        CommentModel model = new CommentModel();
                        model.setUser(user_name);
                        model.setTotal_reply("0");
                        model.setTotal_likes("0");
                        model.setComment(send_comment_et.getText().toString().trim());

                        comments_arraylist.add(model);
                        adapter.notifyDataSetChanged();

                        Helper.getListViewSize(comments_listView);

                        postCommentReq(send_comment_et.getText().toString().trim());

                        send_comment_et.setText("");

                    }
                }
                break;

            default:
                break;
        }
    }

    //.....................Post Comment Request..........................
    public String postCommentReq(final String comment) {
//        try
//        {
//            PostCommentAdapter.ViewHolder.progress_loading.setVisibility(View.VISIBLE);
//        }
//        catch(Exception e)
//        {
//
//        }

        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);
        String url = getResources().getString(R.string.base_url) + METHOD_NAME_post ;
        Log.e(TAG, url);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        PostCommentAdapter.ViewHolder.progress_loading.setVisibility(View.GONE);
                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.e(TAG, response.toString());
                        res = response.toString();
                        checkPostCommentResponse(res);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  CommentsAdapter.ViewHolder.progress_loading.setVisibility(View.GONE);
                CommonUtils.closeSweetProgressDialog(context, pd);
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                res = error.toString();
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", customer_id);
                params.put("blog_id", blogid);
                params.put("comment", comment);
                params.put("comment_id", comment_id);
                Log.e(TAG, params.toString());
                return params;
            }
            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }

    public void checkPostCommentResponse(String response)
    {
        Log.e("Response 123 ",response);
        try {
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
                Log.e("response cmnt count :","123"+pos);
                TimelineDetailFragment.timeline_comments_tv.setText(String.valueOf(Integer.parseInt(TimelineDetailFragment.comment_count)+1));
                String p = Home_Fragment.arrayList_timeline.get(pos).getComment_count();
                Log.e("response cmnt count :",p+"");
                Home_Fragment.arrayList_timeline.get(pos).setComment_count(String.valueOf((Integer.parseInt(p) + 1)));
                Home_Fragment.timeLineAdapter.notifyDataSetChanged();
                // Home_Fragment.timeline_list.setAdapter(adp);
            }
            else {
                CommonUtils.showCustomErrorDialog1(context,error);
            }
        }
        catch(Exception e)
        {
        }
    }
}
