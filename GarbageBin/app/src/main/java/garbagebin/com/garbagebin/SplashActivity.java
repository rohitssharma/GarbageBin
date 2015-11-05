package garbagebin.com.garbagebin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

    private boolean mIsBackButtonPressed;
    private static final int SPLASH_DURATION = 2000; // 2 seconds
    Intent intent;
    String customer_id="";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id","");

        Handler handler = new Handler();
        // run a thread after 2 seconds to start the home screen
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // make sure we close the splash screen so the user won't come back when it presses back key
                finish();

                if (!mIsBackButtonPressed) {
                    if (!customer_id.equalsIgnoreCase("")) {
                        intent = new Intent(SplashActivity.this, Timeline.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        intent = new Intent(SplashActivity.this, MainScreen.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }
            }
        }, SPLASH_DURATION); // time in milliseconds (1 second = 1000 milliseconds) until the run() method will be called
    }


}
