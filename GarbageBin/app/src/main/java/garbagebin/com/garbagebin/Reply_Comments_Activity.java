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
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.garbagebin.Utils.AppController;
import com.garbagebin.Utils.CommonUtils;
import com.garbagebin.Utils.Helper;
import com.garbagebin.adapters.ReplyCommentsAdapter;
import com.garbagebin.models.CommentReplyModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Reply_Comments_Activity extends Fragment implements View.OnClickListener {

    Context context;
    Activity activity;
    ListView reply_comments_listView;
    EditText send_reply_et;
    Button send_reply_btn;
    ArrayList<CommentReplyModel> al_reply_comments = new ArrayList<>();
    ReplyCommentsAdapter adapter;
    SharedPreferences sharedPreferences;
    String customer_id="",res="",user_name="",TAG=Reply_Comments_Activity.class.getSimpleName(),
            METHOD_NAME_post="timeline/comment",blog_id="",comment_id="",tag_string_req="reply_comments_req";
    ArrayList<CommentReplyModel> reply_arraylist = new ArrayList<>();
    int position=0,pos=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_reply__comments_,container,false);

        context = getActivity();
        activity = getActivity();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");

        if(sharedPreferences.getString("fname", "").equalsIgnoreCase(""))
        {
            user_name = sharedPreferences.getString("username", "");
        }
        else {
            user_name = sharedPreferences.getString("fname", "")+" "+sharedPreferences.getString("lname", "");
        }

        initializeViews(view);

        Bundle extras = getArguments();
        if (extras != null) {
            al_reply_comments = extras.getParcelableArrayList("replyArraylist");
            blog_id = extras.getString("blog_id");
            comment_id = extras.getString("comment_id");
            position = extras.getInt("position", 0);
            pos = extras.getInt("pos",0);
            Log.e("Test replies_arraylist", al_reply_comments.size() + "");
        }

        if(al_reply_comments.size()<10)
        {
            reply_comments_listView.setStackFromBottom(false);
        }

        adapter = new ReplyCommentsAdapter(context,activity,al_reply_comments);
        reply_comments_listView.setAdapter(adapter);

        return view;
    }

    public void initializeViews(View v)
    {
        reply_comments_listView = (ListView)(v.findViewById(R.id.reply_comments_listView));
        send_reply_et = (EditText)(v.findViewById(R.id.send_reply_et));
        send_reply_btn = (Button)(v.findViewById(R.id.send_reply_btn));
        send_reply_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.send_reply_btn:
                if(CommonUtils.isNetworkAvailable(context)) {
                    if (!send_reply_et.getText().toString().equalsIgnoreCase("")) {
                        CommentReplyModel model = new CommentReplyModel();
                        model.setUser(user_name);
                        model.setComment(send_reply_et.getText().toString().trim());

                        al_reply_comments.add(model);
                        adapter.notifyDataSetChanged();

                        Helper.getListViewSize(reply_comments_listView);

                        postCommentReplyReq(send_reply_et.getText().toString().trim());

                        send_reply_et.setText("");

                    }
                }
                break;

            default:
                break;
        }
    }

    //.....................Post Comment Reply Request..........................
    public String postCommentReplyReq(final String comment) {

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
                        checkPostCommentReplyResponse(res);
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
                params.put("blog_id", blog_id);
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

    public void checkPostCommentReplyResponse(String response)
    {
        Log.e("Response 123 ", response);
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
//              CommentsAdapter.comment_count = String.valueOf(Integer.parseInt(CommentsAdapter.comment_count)+1);
//              TimelineDetailFragment.adapter.notifyDataSetChanged();
                Post_Comment_activity.comments_arraylist.get(position).setTotal_reply(String.valueOf((Integer.parseInt(Post_Comment_activity.comments_arraylist.get(position).getTotal_reply()) + 1)));
                Post_Comment_activity.adapter.notifyDataSetChanged();
                Log.e("Reply response : ",Post_Comment_activity.comments_arraylist.get(position).getTotal_reply());
//              int neew =Integer.parseInt( Home_Fragment.arrayList_timeline.get(pos).getCommentModelArrayList().get(position).getTotal_reply())+1;
//              Home_Fragment.arrayList_timeline.get(pos).getCommentModelArrayList().get(position).setTotal_reply(String.valueOf(neew));
//              TimelineDetailFragment.timeline_comments_tv.setText(String.valueOf(Integer.parseInt(TimelineDetailFragment.comment_count)+1));
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
