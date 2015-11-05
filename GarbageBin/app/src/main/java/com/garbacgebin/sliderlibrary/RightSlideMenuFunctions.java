package com.garbacgebin.sliderlibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.garbagebin.fragments.ChangePasswordFragment;
import com.garbagebin.fragments.FeedBack_Fragment;
import com.garbagebin.fragments.Invite_Referral_Fragment;
import com.garbagebin.fragments.Points_Badges_Fragment;
import com.garbagebin.fragments.Terms_Conditions_Fragment;
import com.garbagebin.fragments.WalletFragment;

import java.util.ArrayList;
import java.util.List;

import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

@SuppressLint("InlinedApi")
public class RightSlideMenuFunctions extends RelativeLayout {

    Context mContext;
    SlidingMenu menu;
    Activity activity;
    ListView products_list;
    ArrayList<SlideMenuModels> al;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String username = "";
    SliderAdapter adapter;
    Intent in;
    TextView slider_profile_name;
    Fragment fragment;
    FragmentManager fm;
    FragmentTransaction ft;

    @SuppressWarnings("static-access")
    public RightSlideMenuFunctions(final Activity activity, final Context context, final SlidingMenu menu) {
        super(context);
        this.mContext = context;
        this.activity = activity;
        this.menu = menu;

        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        slider_profile_name = (TextView)(menu.findViewById(R.id.slider_profile_name));
        slider_profile_name.setText(username);

        products_list = (ListView) menu.findViewById(R.id.sec_products_list);
        al = new ArrayList<SlideMenuModels>();

        al.add(new SlideMenuModels("Wallet", R.mipmap.wallet));
        al.add(new SlideMenuModels("Order Queries", R.mipmap.query));
        al.add(new SlideMenuModels("History", R.mipmap.history));
        al.add(new SlideMenuModels("Feedback", R.mipmap.feedback));
        al.add(new SlideMenuModels("Rate us", R.mipmap.rate_us));
        al.add(new SlideMenuModels("Terms & Conditions", R.mipmap.t_c));
        al.add(new SlideMenuModels("Points/Badges", R.mipmap.points_badges));
        al.add(new SlideMenuModels("Change Password", R.mipmap.points_badges));
        al.add(new SlideMenuModels("Invite/Referrals", R.mipmap.invite_referrals));

        adapter = new SliderAdapter(context, activity, al);
        products_list.setAdapter(adapter);

        products_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (al.get(position).getMenu_name().equalsIgnoreCase("Wallet")) {
                    Fragment f = getVisibleFragment();
                    Log.e("Slider", f.getClass().getSimpleName());

                    if(f.getClass().getSimpleName().equalsIgnoreCase("WalletFragment"))
                    {
                        menu.showContent();
                    }
                    else
                    {
                        menu.showContent();
                        ft = Timeline.fm.beginTransaction();
                        fragment = new WalletFragment();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                } else if (al.get(position).getMenu_name().equalsIgnoreCase("Order Queries")) {
                    menu.showContent();
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + "123456789"));
                    activity.startActivity(callIntent);
                } else if (al.get(position).getMenu_name().equalsIgnoreCase("History")) {
                    menu.showContent();
                } else if (al.get(position).getMenu_name().equalsIgnoreCase("Feedback")) {
                    Fragment f = getVisibleFragment();
                    Log.e("Slider", f.getClass().getSimpleName());

                    if(f.getClass().getSimpleName().equalsIgnoreCase("FeedBack_Fragment"))
                    {
                        menu.showContent();
                    }
                    else
                    {
                        menu.showContent();
                        ft = Timeline.fm.beginTransaction();
                        fragment = new FeedBack_Fragment();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }

                } else if (al.get(position).getMenu_name().equalsIgnoreCase("Rate us")) {
                    menu.showContent();
                } else if (al.get(position).getMenu_name().equalsIgnoreCase("Terms & Conditions")) {

                    Fragment f = getVisibleFragment();
                    Log.e("Slider", f.getClass().getSimpleName());

                    if(f.getClass().getSimpleName().equalsIgnoreCase("Terms_Conditions_Fragment"))
                    {
                        menu.showContent();
                    }
                    else
                    {
                        menu.showContent();
                        ft = Timeline.fm.beginTransaction();
                        fragment = new Terms_Conditions_Fragment();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                }
                else if (al.get(position).getMenu_name().equalsIgnoreCase("Points/Badges")) {
                    Fragment f = getVisibleFragment();
                    Log.e("Slider", f.getClass().getSimpleName());

                    if(f.getClass().getSimpleName().equalsIgnoreCase("Points_Badges_Fragment"))
                    {
                        menu.showContent();
                    }
                    else
                    {
                        menu.showContent();
                        ft = Timeline.fm.beginTransaction();
                        fragment = new Points_Badges_Fragment();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                }
                else if (al.get(position).getMenu_name().equalsIgnoreCase("Change Password")) {
                    Fragment f = getVisibleFragment();
                    Log.e("Slider", f.getClass().getSimpleName());

                    if(f.getClass().getSimpleName().equalsIgnoreCase("ChangePasswordFragment"))
                    {
                        menu.showContent();
                    }
                    else
                    {
                        menu.showContent();
                        ft = Timeline.fm.beginTransaction();
                        fragment = new ChangePasswordFragment();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                }
                else if (al.get(position).getMenu_name().equalsIgnoreCase("Invite/Referrals")) {
                    Fragment f = getVisibleFragment();
                    Log.e("Slider", f.getClass().getSimpleName());

                    if(f.getClass().getSimpleName().equalsIgnoreCase("Invite_Referral_Fragment"))
                    {
                        menu.showContent();
                    }
                    else
                    {
                        menu.showContent();
                        ft = Timeline.fm.beginTransaction();
                        fragment = new Invite_Referral_Fragment();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                }

            }
        });

    }
    public Fragment getVisibleFragment(){
//		FragmentManager fragmentManager = activity.getSupportFragmentManager();
        List<Fragment> fragments = Timeline.fm.getFragments();
        for(Fragment fragment : fragments){
            if(fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }
}


