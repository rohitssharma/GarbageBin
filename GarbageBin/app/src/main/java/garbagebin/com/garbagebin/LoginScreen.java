package garbagebin.com.garbagebin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.garbagebin.Utils.AppController;
import com.garbagebin.Utils.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginScreen extends Activity implements View.OnClickListener {

    TextView termsOfService_textview, privacy_textview, header_textview;
    LinearLayout menu_icon_layout, profile_pic_layout, options_layout;
    EditText username_orEmail_et, pwd_et;
    Button login_button;
    boolean isValidate;
    String un_email_address = "", password = "",Device_Token="",
            TAG = LoginScreen.class.getSimpleName(),res="",tag_string_req = "login_req",METHOD_NAME="user/login";

    //................facebook variables...................
    private CallbackManager callbackManager;
    private LoginButton fbLoginButton;
    Context context;
    Activity activity;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = LoginScreen.this;
        activity = LoginScreen.this;

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.prefs_name),Context.MODE_PRIVATE);
        Device_Token = sharedPreferences.getString(getResources().getString(R.string.gcm_id), "");

        //.............Facebook Integartion...............
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        //You need this method to be used only once to configure
        //your key hash in your App Console at
        // developers.facebook.com/apps

        CommonUtils.getFbKeyHash("org.code2care.fbloginwithandroidsdk", LoginScreen.this);

        setContentView(R.layout.activity_login_screen);

        fbLoginButton = (LoginButton)findViewById(R.id.fb_login_button);

        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                Log.v("Facebook User Detail : ", response.toString());
                            }
                        });
                Bundle parameters = new Bundle();
//                @"email,name,first_name,last_name,gender,picture,albums"}];
                parameters.putString("fields", "id,name,first_name,last_name,email,gender, birthday,picture");
                request.setParameters(parameters);
                request.executeAsync();
                System.out.println("Facebook Login Successful!");
                System.out.println("Logged in user Details : ");
                System.out.println("--------------------------");
                System.out.println("User ID  : " + loginResult.getAccessToken().getUserId());
                System.out.println("Authentication Token : " + loginResult.getAccessToken().getToken());
                Toast.makeText(LoginScreen.this, "Login Successful!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginScreen.this, "Login cancelled by user!", Toast.LENGTH_LONG).show();
                System.out.println("Facebook Login failed!!");

            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(LoginScreen.this, "Login unsuccessful!", Toast.LENGTH_LONG).show();
                System.out.println("Facebook Login failed!!");
            }
        });

        //.............initialize views..............
        initializeViews();
    }

    /**
     * Initialize Views
     */
    public void initializeViews() {
        termsOfService_textview = (TextView) (findViewById(R.id.login_termsOfService_textview));
        privacy_textview = (TextView) (findViewById(R.id.login_privacy_textview));
        pwd_et = (EditText) (findViewById(R.id.pwd_et));
        username_orEmail_et = (EditText) (findViewById(R.id.username_orEmail_et));
        login_button = (Button) (findViewById(R.id.login_button));

        Typeface type = Typeface.createFromAsset(getAssets(),"GOTHAM-LIGHT.TTF");
        username_orEmail_et.setTypeface(type);
        pwd_et.setTypeface(type);

        Typeface type2 = Typeface.createFromAsset(getAssets(),"GOTHAM-BOOK.OTF");
        login_button.setTypeface(type2);

        //............OnClickListeners implementation..............
        login_button.setOnClickListener(LoginScreen.this);

        //............Slider layouts..................
        menu_icon_layout = (LinearLayout) (findViewById(R.id.menu_icon_layout));
        header_textview = (TextView) (findViewById(R.id.header_textview));
        profile_pic_layout = (LinearLayout) (findViewById(R.id.profile_pic_layout));
        options_layout = (LinearLayout) (findViewById(R.id.options_layout));

        header_textview.setText(getResources().getString(R.string.login_header_textview));
        menu_icon_layout.setVisibility(View.INVISIBLE);
        profile_pic_layout.setVisibility(View.INVISIBLE);
        options_layout.setVisibility(View.INVISIBLE);

        //............Underline TextView................
        termsOfService_textview.setPaintFlags(termsOfService_textview.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        privacy_textview.setPaintFlags(privacy_textview.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    /**
     * Validating form
     */
    private void submitForm() {

        if (CommonUtils.isNetworkAvailable(context)) {
            makeLoginReq();
        }
        else{
            CommonUtils.showCustomErrorDialog1(context,getResources().getString(R.string.bad_connection));
        }
    }

    //.....................Volley Login Request............................
    public String makeLoginReq() {

        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);

        String url = getResources().getString(R.string.base_url)+METHOD_NAME;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        checkLoginResponse(res);
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
                params.put("email", un_email_address);
                params.put("password", password);
                params.put("device_token", Device_Token);
                params.put("device_type", getResources().getString(R.string.device_type));
                Log.e(TAG,params.toString());
                return params;
            }
            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }

    public void checkLoginResponse(String response)
    {
        try
        {
            Log.e(TAG,response);
            String message="",error="",customer_id="",lname="",fname="",
            email="",username="",profile_image="";
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.has("message"))
            {
                message = jsonObject.getString("message");
            }
            if(jsonObject.has("error"))
            {
                error = jsonObject.getString("error");
            }
            if(jsonObject.has("customerInfo"))
            {
                Log.e("customerInfo",jsonObject.getString("customerInfo").toString());
                JSONObject jsonObject1 = new JSONObject( jsonObject.getString("customerInfo"));
                Log.e("customerInfo sec ", jsonObject1.toString());

                if(jsonObject1.has("customer_id"))
                {
                    customer_id = jsonObject1.getString("customer_id");
                }
                if(jsonObject1.has("email"))
                {
                    email = jsonObject1.getString("email");
                }
                if(jsonObject1.has("username"))
                {
                    username = jsonObject1.getString("username");
                }
                if(jsonObject1.has("profile_image"))
                {
                    profile_image = jsonObject1.getString("profile_image");
                }
                if(jsonObject1.has("fname"))
                {
                    fname = jsonObject1.getString("fname");
                }
                if(jsonObject1.has("lname"))
                {
                    lname = jsonObject1.getString("lname");
                }
            }

            if(message.equalsIgnoreCase("failure"))
            {
                CommonUtils.showCustomErrorDialog1(context,error);
            }
            else {

                Log.e("customer_id  ", customer_id);

                editor = sharedPreferences.edit();
                editor.putString("customer_id",customer_id);
                editor.putString("username",username);
                editor.putString("fname",fname);
                editor.putString("lname",lname);
                editor.putString("email",email);
                editor.putString("profile_image",profile_image);
                editor.commit();

                Intent in = new Intent(LoginScreen.this, Timeline.class);
                startActivity(in);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }
        catch(JSONException e)
        {
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                isValidate = checkValidation();
                if (isValidate) {
                    submitForm();
                }
                break;
        }
    }

    /**
     * Check validations
     */
    public boolean checkValidation() {
        un_email_address = username_orEmail_et.getText().toString().trim();
        password = pwd_et.getText().toString().trim();

        if (un_email_address.isEmpty()) {
            username_orEmail_et.requestFocus();
            CommonUtils.showCustomErrorDialog1(LoginScreen.this, "Please enter email address or Username.");
        } else if (un_email_address.contains("@") && !isValidEmail(un_email_address)) {
            username_orEmail_et.requestFocus();
            CommonUtils.showCustomErrorDialog1(LoginScreen.this,  "Please enter valid email address.");
        } else if (password.isEmpty()) {
            pwd_et.requestFocus();
            CommonUtils.showCustomErrorDialog1(LoginScreen.this, "Please enter password.");
        } else {
            un_email_address = username_orEmail_et.getText().toString().trim();
            password = pwd_et.getText().toString().trim();

            return true;
        }
        return false;
    }

    public boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
                    .getWindowToken(), 0);
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {

        }
        return false;
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent i) {
        callbackManager.onActivityResult(reqCode, resCode, i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in= new Intent(context,MainScreen.class);
        startActivity(in);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}