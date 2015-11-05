package com.garbacgebin.sliderlibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.garbagebin.Utils.CommonUtils;
import com.garbagebin.fragments.Characters_Fragment;
import com.garbagebin.fragments.Comics_Fragment;
import com.garbagebin.fragments.Home_Fragment;
import com.garbagebin.fragments.Settings_Fragment;

import java.util.ArrayList;
import java.util.List;

import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

@SuppressLint("InlinedApi")
public class SlideMenuFunctions extends RelativeLayout  {

	Context mContext;
	SlidingMenu menu;
	Activity activity;
	ListView products_list;
	ArrayList<SlideMenuModels> al;
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	String username="";
	SliderAdapter adapter;
	Intent in;

	Fragment fragment;
	FragmentManager fm;
	FragmentTransaction ft;

	@SuppressWarnings("static-access")
	public SlideMenuFunctions(final Activity activity, final Context context, final SlidingMenu menu) {
		super(context);
		this.mContext = context;
		this.activity = activity;
		this.menu = menu;

		products_list = (ListView) menu.findViewById(R.id.products_list);

		al = new ArrayList<SlideMenuModels>();

		al.add(new SlideMenuModels("Home", R.mipmap.home_selected_copy));
		al.add(new SlideMenuModels("Archive", R.mipmap.archive));
		al.add(new SlideMenuModels("Video",  R.mipmap.video));
		al.add(new SlideMenuModels("Store",  R.mipmap.store));
		al.add(new SlideMenuModels("Hot Gags", R.mipmap.hot_gags));
		al.add(new SlideMenuModels("Settings", R.mipmap.settings));
		al.add(new SlideMenuModels("Characters", R.mipmap.characters));
		al.add(new SlideMenuModels("Notifications", R.mipmap.notification));
		al.add(new SlideMenuModels("Feature Posts", R.mipmap.feature_post));
		al.add(new SlideMenuModels("Comics", R.mipmap.comic));
		al.add(new SlideMenuModels("Help", R.mipmap.help));
		al.add(new SlideMenuModels("Signout", R.mipmap.signout));

		adapter  = new SliderAdapter(context, activity, al);
		products_list.setAdapter(adapter);

		products_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				if(al.get(position).getMenu_name().equalsIgnoreCase("Home"))
				{

					Fragment f = getVisibleFragment();
					Log.e("Slider",f.getClass().getSimpleName());

					if(f.getClass().getSimpleName().equalsIgnoreCase("Home_Fragment"))
					{
						menu.showContent();
					}
					else
					{
						menu.showContent();
						ft = Timeline.fm.beginTransaction();
						fragment = new Home_Fragment();
						ft.replace(R.id.content_frame, fragment);
						ft.addToBackStack(null);
						ft.commit();
					}
//
//					fragment = new Home_Fragment();
//					if (fragment != null && fragment.isVisible()) {
//						// add your code here
//						menu.showContent();
//					}
//					else
//					{
//						ft = Timeline.fm.beginTransaction();
//						fragment = new Home_Fragment();
//						ft.replace(R.id.content_frame, fragment);
//						ft.addToBackStack(null);
//						ft.commit();
//					}

//					in = new Intent(context,PersonalInjuryActivity.class);
//					context.startActivity(in);
				}
				else if(al.get(position).getMenu_name().equalsIgnoreCase("Feature Posts"))
				{
//					Fragment f = getVisibleFragment();
//					Log.e("Slider",f.getClass().getSimpleName());
//
//					if(f.getClass().getSimpleName().equalsIgnoreCase("Featured_Posts_Fragment"))
//					{
//						menu.showContent();
//					}
//					else {
//						menu.showContent();
//						ft = Timeline.fm.beginTransaction();
//						fragment = new Featured_Posts_Fragment();
//						ft.replace(R.id.content_frame, fragment);
//						ft.addToBackStack(null);
//						ft.commit();
//					}
//					in = new Intent(context,PersonalInjuryActivity.class);
//					context.startActivity(in);
				}
				else if(al.get(position).getMenu_name().equalsIgnoreCase("Archive"))
				{
					menu.showContent();
//					in = new Intent(context,MainActivity.class);
//					context.startActivity(in);
				}
				else if(al.get(position).getMenu_name().equalsIgnoreCase("Video"))
				{
					menu.showContent();
//					in = new Intent(context,MainActivity.class);
//					context.startActivity(in);
				}
				else if(al.get(position).getMenu_name().equalsIgnoreCase("Store"))
				{
					menu.showContent();
//					in = new Intent(context,MainActivity.class);
//					context.startActivity(in);
				}
				else if(al.get(position).getMenu_name().equalsIgnoreCase("Hot Gags"))
				{
//					Fragment f = getVisibleFragment();
//					Log.e("Slider",f.getClass().getSimpleName());
//					if(f.getClass().getSimpleName().equalsIgnoreCase("HotGagsFragment"))
//					{
//						menu.showContent();
//					}
//					else {
//						menu.showContent();
//						ft = Timeline.fm.beginTransaction();
//						fragment = new HotGagsFragment();
//						ft.replace(R.id.content_frame, fragment);
//						ft.addToBackStack(null);
//						ft.commit();
//					}
//					in = new Intent(context,MainActivity.class);
//					context.startActivity(in);
				}
				else if(al.get(position).getMenu_name().equalsIgnoreCase("Settings"))
				{
					Fragment f = getVisibleFragment();
					Log.e("Slider",f.getClass().getSimpleName());

					if(f.getClass().getSimpleName().equalsIgnoreCase("Settings_Fragment"))
					{
						menu.showContent();
					}
					else {
						menu.showContent();
						ft = Timeline.fm.beginTransaction();
						fragment = new Settings_Fragment();
						ft.replace(R.id.content_frame, fragment);
						ft.addToBackStack(null);
						ft.commit();
					}
				}
				else if(al.get(position).getMenu_name().equalsIgnoreCase("Characters"))
				{
					Fragment f = getVisibleFragment();
					Log.e("Slider", f.getClass().getSimpleName());

					if(f.getClass().getSimpleName().equalsIgnoreCase("Characters_Fragment"))
					{
						menu.showContent();
					}
					else {
						menu.showContent();
						ft = Timeline.fm.beginTransaction();
						fragment = new Characters_Fragment();
						ft.replace(R.id.content_frame, fragment);
						ft.addToBackStack(null);
						ft.commit();
					}
				}
				else if(al.get(position).getMenu_name().equalsIgnoreCase("Comics"))
				{
					Fragment f = getVisibleFragment();
					Log.e("Slider", f.getClass().getSimpleName());

					if(f.getClass().getSimpleName().equalsIgnoreCase("Comics_Fragment"))
					{
						menu.showContent();
					}
					else {
						menu.showContent();
						ft = Timeline.fm.beginTransaction();
						fragment = new Comics_Fragment();
						ft.replace(R.id.content_frame, fragment);
						ft.addToBackStack(null);
						ft.commit();
					}
				}
				else if(al.get(position).getMenu_name().equalsIgnoreCase("Signout"))
				{
					Fragment f = getVisibleFragment();
					Log.e("Slider",f.getClass().getSimpleName());

					menu.showContent();
					CommonUtils.showLogoutDialog(context,activity);

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

