package garbagebin.com.garbagebin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.garbagebin.Utils.CommonUtils;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class MainScreen extends Activity implements View.OnClickListener {

    TextView termsOfService_textview, privacy_textview, login_textview, signup_textview;
    Intent in;
    GoogleCloudMessaging gcm;
    String DeviceToken;
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        context = MainScreen.this;
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.prefs_name),Context.MODE_PRIVATE);
        registerDevice();

        //.............initialize views..............
        initializeViews();
    }

    public void initializeViews() {

        termsOfService_textview = (TextView) (findViewById(R.id.termsOfService_textview));
        privacy_textview = (TextView) (findViewById(R.id.privacy_textview));
        login_textview = (TextView) (findViewById(R.id.login_textview));
        signup_textview = (TextView) (findViewById(R.id.signup_textview));
        login_textview.setOnClickListener(this);
        signup_textview.setOnClickListener(this);

        //............Underline TextView................
        termsOfService_textview.setPaintFlags(termsOfService_textview.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        privacy_textview.setPaintFlags(privacy_textview.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signup_textview:
                in = new Intent(MainScreen.this, SignUp.class);
                startActivity(in);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.login_textview:
                in = new Intent(MainScreen.this, LoginScreen.class);
                startActivity(in);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            default:
                break;
        }
    }


    private void registerDevice() {
        gcm = GoogleCloudMessaging.getInstance(this);
        DeviceToken = getRegistrationId(context);
        new Thread(null, registeronserver, "").start();
    }

    public Runnable registeronserver = new Runnable() {
        @SuppressLint("NewApi")
        @Override
        public void run() {
            try {
                if (DeviceToken.isEmpty()) {
                    registerInBackground();
                }
                sendRegistrationIdToBackend();
            } catch (Exception e) {
                e.printStackTrace();
            }
            reg_serverHandler.sendMessage(new Message());
        }
    };
    Handler reg_serverHandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.e("registered on server", "true");
        }
    };


    private void registerInBackground() {
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            DeviceToken = gcm.register(CommonUtils.GCM_SENDER_ID);
            Log.e("DeviceToken ", DeviceToken);

            storeRegistrationId(context, DeviceToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String sendRegistrationIdToBackend() {
        String res = "";
        return res;
    }

    protected void storeRegistrationId(Context context, String regid) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i("", "Saving regId on app version " + appVersion);

        editor = sharedPreferences.edit();
        editor.putString(getResources().getString(R.string.gcm_id),DeviceToken);
        editor.commit();


        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(CommonUtils.GCM_APPLICATION_DEVICEID, regid);
        editor.putInt(CommonUtils.APP_VERSION, appVersion);
        editor.commit();
    }

    @SuppressLint("NewApi")
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(
                CommonUtils.GCM_APPLICATION_DEVICEID, "");
        if (registrationId.isEmpty()) {
            Log.i("", "Registration not found.");
            return "";
        }

        int registeredVersion = prefs.getInt(CommonUtils.APP_VERSION,
                Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i("", "App version changed.");
            return "";
        }
        return registrationId;
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(MainScreen.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }
}