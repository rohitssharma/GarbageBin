package garbagebin.com.garbagebin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.garbagebin.fonts_classes.GothamLightTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SignUp extends Activity implements  View.OnClickListener {

    TextView promocode_textView, header_textview;
    LinearLayout menu_icon_layout, profile_pic_layout, options_layout;
    Button signup_button;
    boolean isValidate;
    EditText username_et,emailAddress_et,signup_pwd_et,promocode_et,name_et;
    String username="",emailAddress="",signup_pwd="",name="",Device_Token="",occassional_events="",
            TAG = SignUp.class.getSimpleName(),res="",tag_string_req = "signup_req",METHOD_NAME="user/signup";

    //................facebook variables...................
    private CallbackManager callbackManager;
    private LoginButton fbLoginButton;
    Context context;
    Activity activity;
    CheckBox checkBox_sendOccassional;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    GothamLightTextView alreadymember_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.prefs_name),Context.MODE_PRIVATE);
        Device_Token = sharedPreferences.getString(getResources().getString(R.string.gcm_id),"");
        context = SignUp.this;

        //.............Facebook Integartion...............
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        //You need this method to be used only once to configure
        //your key hash in your App Console at
        // developers.facebook.com/apps

        CommonUtils.getFbKeyHash("org.code2care.fbloginwithandroidsdk", SignUp.this);
        setContentView(R.layout.activity_sign_up);

        fbLoginButton = (LoginButton)findViewById(R.id.fb_signup_button);
        fbLoginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));

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
                                Log.v("LoginActivity", response.toString());
                            }
                        });
                Bundle parameters = new Bundle();
//                @"email,name,first_name,last_name,gender,picture,albums"}];
                parameters.putString("fields", "id,name,first_name,last_name,email,gender, birthday,picture");
                request.setParameters(parameters);
                request.executeAsync();

                System.out.println("Facebook Login Successful!");
                System.out.println("Facebook Login Successful!");
                System.out.println("Logged in user Details : ");
                System.out.println("--------------------------");
                System.out.println("User ID  : " + loginResult.getAccessToken().getUserId());
                System.out.println("Authentication Token : " + loginResult.getAccessToken().getToken());
                Toast.makeText(SignUp.this, "Login Successful!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(SignUp.this, "Login cancelled by user!", Toast.LENGTH_LONG).show();
                System.out.println("Facebook Login failed!!");

            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(SignUp.this, "Login unsuccessful!", Toast.LENGTH_LONG).show();
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
        promocode_textView = (TextView) (findViewById(R.id.promocode_textView));
        String customColorText = getResources().getString(R.string.promocode_textView);
        promocode_textView.setText(Html.fromHtml(customColorText));

        alreadymember_textView = (GothamLightTextView)(findViewById(R.id.alreadymember_textView));
        alreadymember_textView.setOnClickListener(this);

        signup_button = (Button)(findViewById(R.id.signup_button));
        username_et = (EditText)(findViewById(R.id.username_et));
        emailAddress_et = (EditText)(findViewById(R.id.emailAddress_et));
        signup_pwd_et = (EditText)(findViewById(R.id.signup_pwd_et));
        promocode_et = (EditText)(findViewById(R.id.promocode_et));
        name_et = (EditText)(findViewById(R.id.name_et));

        checkBox_sendOccassional = (CheckBox)(findViewById(R.id.checkBox_sendOccassional));
        checkBox_sendOccassional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    //Case 1
                    occassional_events = "yes";
                    Toast.makeText(context,"yes",Toast.LENGTH_SHORT).show();;
                }
                else {
                    //case 2
                    occassional_events = "no";
                    Toast.makeText(context,"uncheckd : No",Toast.LENGTH_SHORT).show();;

                }

            }
        });


        Typeface type = Typeface.createFromAsset(getAssets(),"GOTHAM-LIGHT.TTF");
        username_et.setTypeface(type);
        emailAddress_et.setTypeface(type);
        signup_pwd_et.setTypeface(type);
        name_et.setTypeface(type);
        checkBox_sendOccassional.setTypeface(type);

        Typeface type2 = Typeface.createFromAsset(getAssets(),"GOTHAM-BOOK.OTF");
        signup_button.setTypeface(type);
        promocode_et.setTypeface(type);

        signup_button.setOnClickListener(this);

        //............Slider layouts..................
        menu_icon_layout = (LinearLayout) (findViewById(R.id.menu_icon_layout));
        header_textview = (TextView) (findViewById(R.id.header_textview));
        profile_pic_layout = (LinearLayout) (findViewById(R.id.profile_pic_layout));
        options_layout = (LinearLayout) (findViewById(R.id.options_layout));

        header_textview.setText(getResources().getString(R.string.signup_header_textview));
        menu_icon_layout.setVisibility(View.INVISIBLE);
        profile_pic_layout.setVisibility(View.INVISIBLE);
        options_layout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.signup_button:
                isValidate = checkValidation();
                if (isValidate) {
                    submitForm();
                }
                break;

            case R.id.alreadymember_textView:
                Intent in = new Intent(SignUp.this, LoginScreen.class);
                startActivity(in);
                finish();
                break;

            default:
                break;
        }
    }

    /**
     * Validating form
     */
    private void submitForm() {

        if (CommonUtils.isNetworkAvailable(context)) {
            makeSignUpReq();
        }
        else{
            CommonUtils.showCustomErrorDialog1(context,getResources().getString(R.string.bad_connection));
        }
    }

    //.....................Volley SignUp Request............................
    public String makeSignUpReq() {

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
                        checkSignUpResponse(res);
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
                params.put("email", emailAddress);
                params.put("password", signup_pwd);
                params.put("username", username);
                params.put("name",name);
                params.put("device_token",Device_Token);
                params.put("device_type",getResources().getString(R.string.device_type));
                params.put("notifications",occassional_events);
                params.put("referral_code", promocode_et.getText().toString().trim());
                Log.e(TAG, params.toString());
                return params;
            }

            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }

    public void checkSignUpResponse(String response)
    {
        try {
            if(response.isEmpty())
            {
                CommonUtils.showCustomErrorDialog1(context,getResources().getString(R.string.bad_response));
            }
            else {
                String message="",error="",customer_id="",username="",email="";

                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.has("message"))
                {
                    message = jsonObject.getString("message");
                }
                if(jsonObject.has("error"))
                {
                    error = jsonObject.getString("error");
                }
                if(jsonObject.has("customer_id"))
                {
                    customer_id = jsonObject.getString("customer_id");
                }
                if(jsonObject.has("username"))
                {
                    username = jsonObject.getString("username");
                }
                if(jsonObject.has("email"))
                {
                    email = jsonObject.getString("email");
                }

                if(message.equalsIgnoreCase("failure"))
                {
                    CommonUtils.showCustomErrorDialog1(context,error);
                }
                else
                {
                    editor = sharedPreferences.edit();
                    editor.putString("customer_id",customer_id);
                    editor.putString("username",username);
                    editor.commit();

                    Intent in = new Intent(SignUp.this, Timeline.class);
                    startActivity(in);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        }
        catch(JSONException e)
        {
        }
    }

    /**
     * Check validations
     */
    public boolean checkValidation() {
        username = username_et.getText().toString().trim();
        emailAddress = emailAddress_et.getText().toString().trim();
        signup_pwd = signup_pwd_et.getText().toString().trim();
        name = name_et.getText().toString().trim();

        if (username.isEmpty()) {
            username_et.requestFocus();
            CommonUtils.showCustomErrorDialog1(SignUp.this,  "Please enter username.");
        } else if (!isValidEmail(emailAddress)) {
            emailAddress_et.requestFocus();
            CommonUtils.showCustomErrorDialog1(SignUp.this,  "Please enter valid email address.");
        } else if (signup_pwd.isEmpty()) {
            signup_pwd_et.requestFocus();
            CommonUtils.showCustomErrorDialog1(SignUp.this,  "Please enter password.");
        }
        else if(name.isEmpty()){
            name_et.requestFocus();
            CommonUtils.showCustomErrorDialog1(SignUp.this,  "Please enter name.");
        }
        else {
            username = username_et.getText().toString().trim();
            emailAddress = emailAddress_et.getText().toString().trim();
            signup_pwd = signup_pwd_et.getText().toString().trim();
            name = name_et.getText().toString().trim();

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