package garbagebin.com.garbagebin;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.garbacgebin.sliderlibrary.RightSlideMenuFunctions;
import com.garbacgebin.sliderlibrary.SlideMenuFunctions;
import com.garbacgebin.sliderlibrary.SlidingMenu;
import com.garbagebin.Utils.CommonUtils;
import com.garbagebin.fragments.Cart_Fragment;
import com.garbagebin.fragments.Home_Fragment;
import com.garbagebin.fragments.Search_Fragment;
import com.garbagebin.fragments.Videos_Fragment;

public class Timeline extends FragmentActivity implements View.OnClickListener {

    RelativeLayout main_frag_layout;
    SlidingMenu menu, right_menu;
    SlideMenuFunctions menufunctions;
    RightSlideMenuFunctions rightMenuFunctions;
    Activity activity;
    public static  TextView header_textview;
    public static  LinearLayout menu_icon_layout, profile_pic_layout, options_layout,
            cart_layout,search_layout,home_layout,videos_layout,hotgags_layout,back_icon_layout;

    public static  ImageView hot_gags_imageView,videos_imageView,home_imageView,cart_imageView,search_imageView;

    Fragment fragment;
    public static FragmentManager fm;
    FragmentTransaction ft;
    public static View headerView,bottom;
    ImageView timeline_tutorial_imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        activity = Timeline.this;

        headerView = findViewById(R.id.header_lyt);
        headerView.setVisibility(View.GONE);

        bottom = findViewById(R.id.bottom);

        main_frag_layout = (RelativeLayout)(findViewById(R.id.main_frag_layout));

        //........... open Left slider ........................
        menu = CommonUtils.SetLeftSlidingMenu(Timeline.this);
        menufunctions = new SlideMenuFunctions(activity, Timeline.this, menu);
        menu.addIgnoredView(main_frag_layout);

        //........... open Right slider ........................
        right_menu = CommonUtils.SetRightSlidingMenu(Timeline.this);
        rightMenuFunctions = new RightSlideMenuFunctions(activity, Timeline.this, right_menu);
        right_menu.addIgnoredView(main_frag_layout);

        //.............initialize views..............
        initializeViews();
    }

    public void initializeViews() {
        //............Slider layouts..................
        menu_icon_layout = (LinearLayout) (findViewById(R.id.menu_icon_layout));
        header_textview = (TextView) (findViewById(R.id.header_textview));
        profile_pic_layout = (LinearLayout) (findViewById(R.id.profile_pic_layout));
        options_layout = (LinearLayout) (findViewById(R.id.options_layout));
        back_icon_layout = (LinearLayout)(findViewById(R.id.back_icon_layout));

        header_textview.setText(getResources().getString(R.string.timeline_header_textview));
        menu_icon_layout.setOnClickListener(this);
        options_layout.setOnClickListener(this);
        profile_pic_layout.setOnClickListener(this);
        back_icon_layout.setOnClickListener(this);

        timeline_tutorial_imageView = (ImageView)(findViewById(R.id.timeline_tutorial_imageView));
        timeline_tutorial_imageView.setOnClickListener(this);
        if(CommonUtils.tutorial == 1)
        {
            timeline_tutorial_imageView.setVisibility(View.GONE);
        }

        //..............Fragments Layouts.....................
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        hot_gags_imageView = (ImageView)(findViewById(R.id.hot_gags_imageView));
        videos_imageView = (ImageView)(findViewById(R.id.videos_imageView));
        home_imageView = (ImageView)(findViewById(R.id.home_imageView));
        search_imageView = (ImageView)(findViewById(R.id.search_imageView));
        cart_imageView = (ImageView)(findViewById(R.id.cart_imageView));

        hotgags_layout = (LinearLayout) findViewById(R.id.hotgags_layout);
        hotgags_layout.setOnClickListener(this);

        videos_layout = (LinearLayout) findViewById(R.id.videos_layout);
        videos_layout.setOnClickListener(this);

        home_layout = (LinearLayout) findViewById(R.id.home_layout);
        home_layout.setOnClickListener(this);

        search_layout = (LinearLayout) findViewById(R.id.search_layout);
        search_layout.setOnClickListener(this);

        cart_layout = (LinearLayout) findViewById(R.id.cart_layout);
        cart_layout.setOnClickListener(this);

        ft = fm.beginTransaction();

        hot_gags_imageView.setImageResource(R.mipmap.hot_gags_tab);
        videos_imageView.setImageResource(R.mipmap.video_tab);
        home_imageView.setImageResource(R.mipmap.home_tab);
        search_imageView.setImageResource(R.mipmap.search_tab);
        cart_imageView.setImageResource(R.mipmap.kart_tab);

        fragment = new Home_Fragment();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_icon_layout:
                    menu.showMenu();
                break;

            case R.id.timeline_tutorial_imageView:
                CommonUtils.tutorial = 1;
                timeline_tutorial_imageView.setVisibility(View.GONE);
                break;

            case R.id.back_icon_layout:
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                }
                break;

            case R.id.options_layout:
                right_menu.showMenu();
                break;

            case R.id.profile_pic_layout:
//                Intent in = new Intent(activity,EditProfileActivity.class);
//                startActivity(in);
                break;

            case R.id.home_layout:
                hot_gags_imageView.setImageResource(R.mipmap.hot_gags_tab);
                videos_imageView.setImageResource(R.mipmap.video_tab);
                home_imageView.setImageResource(R.mipmap.home_tab);
                search_imageView.setImageResource(R.mipmap.search_tab);
                cart_imageView.setImageResource(R.mipmap.kart_tab);

                ft = fm.beginTransaction();
                fragment = new Home_Fragment();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
                break;
            case R.id.hotgags_layout:
                hot_gags_imageView.setImageResource(R.mipmap.hot_gags_tab_copy);
                videos_imageView.setImageResource(R.mipmap.video_tab);
                home_imageView.setImageResource(R.mipmap.home_tab_copy);
                search_imageView.setImageResource(R.mipmap.search_tab);
                cart_imageView.setImageResource(R.mipmap.kart_tab);

//                ft = fm.beginTransaction();
//                fragment = new HotGagsFragment();
//                ft.replace(R.id.content_frame, fragment);
//                ft.commit();
                break;
            case R.id.videos_layout:
                hot_gags_imageView.setImageResource(R.mipmap.hot_gags_tab);
                videos_imageView.setImageResource(R.mipmap.video_tab_copy);
                home_imageView.setImageResource(R.mipmap.home_tab_copy);
                search_imageView.setImageResource(R.mipmap.search_tab);
                cart_imageView.setImageResource(R.mipmap.kart_tab);

                ft = fm.beginTransaction();
                fragment = new Videos_Fragment();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
                break;
            case R.id.search_layout:
                hot_gags_imageView.setImageResource(R.mipmap.hot_gags_tab);
                videos_imageView.setImageResource(R.mipmap.video_tab);
                home_imageView.setImageResource(R.mipmap.home_tab_copy);
                search_imageView.setImageResource(R.mipmap.search_tab_copy);
                cart_imageView.setImageResource(R.mipmap.kart_tab);

                ft = fm.beginTransaction();
                fragment = new Search_Fragment();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
                break;
            case R.id.cart_layout:
                hot_gags_imageView.setImageResource(R.mipmap.hot_gags_tab);
                videos_imageView.setImageResource(R.mipmap.video_tab);
                home_imageView.setImageResource(R.mipmap.home_tab_copy);
                search_imageView.setImageResource(R.mipmap.search_tab);
                cart_imageView.setImageResource(R.mipmap.kart_tab_copy);

                ft = fm.beginTransaction();
                fragment = new Cart_Fragment();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(menu.isMenuShowing() || right_menu.isMenuShowing())
        {
            menu.showContent();
            right_menu.showContent();
        }
        else
        {
            super.onBackPressed();
        }
    }
}