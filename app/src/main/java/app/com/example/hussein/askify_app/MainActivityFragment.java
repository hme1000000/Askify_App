package app.com.example.hussein.askify_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A placeholder fragment containing a simple view.
 */
@SuppressLint("NewApi")
public class MainActivityFragment extends Fragment {

    EditText un, pw;
    TextView error;
    Button ok;
    private String resp;
    private String errorMsg;
    String username;
    String password;
    String forecastJsonStr;
    String postDataBytes;
    String Login_URL = "http://askify-app.herokuapp.com/public/api/user/login";
    int success;
    int userid;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        un = (EditText) rootView.findViewById(R.id.et_un);
        pw = (EditText) rootView.findViewById(R.id.et_pw);
        ok = (Button) rootView.findViewById(R.id.btn_login);
        error = (TextView) rootView.findViewById(R.id.tv_error);

        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                username = un.getText().toString();
                password = pw.getText().toString();
                Map<String,Object> params = new LinkedHashMap<>();
                params.put("username", username);
                params.put("password", password );
                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String,Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    try {
                        postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                        postDataBytes = postData.toString();
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    }
                }

                try {
                    JSONParser jP = new JSONParser();
                    forecastJsonStr = jP.excutePost(Login_URL, postDataBytes);
                    if(forecastJsonStr != null) {
                        JSONObject forecast = new JSONObject(forecastJsonStr);
                        success = forecast.getInt("success");
                        userid = forecast.getInt("user_id");
                    }
                    else
                    {
                        success = 0;
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                if (success == 1) {
                    Intent intent = new Intent(getActivity(),HomeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("user_id",userid);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else {
                    Toast.makeText(rootView.getContext(), "username or password is wrong, please try again", Toast.LENGTH_LONG).show();
                }


                /** According with the new StrictGuard policy,  running long tasks on the Main UI thread is not possible
                 So creating new thread to create and execute http operations */
                /*new Thread(new Runnable() {

                    @Override
                    public void run() {
                        ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                        postParameters.add(new BasicNameValuePair("username",un.getText().toString()));
                        postParameters.add(new BasicNameValuePair("password",pw.getText().toString()));

                        String response = null;
                        try {
                            response = SimpleHttpClient.executeHttpPost("http://askify-app.herokuapp.com/public/api/user/login"
                                    , postParameters);
                            String res = response.toString();
                            resp = res.replaceAll("\\s+", "");

                        } catch (Exception e) {
                            e.printStackTrace();
                            errorMsg = e.getMessage();
                        }
                    }

                }).start();
                try {
                    /** wait a second to get response from server /*
                    Thread.sleep(1000);
                    /** Inside the new thread we cannot update the main thread
                     So updating the main thread outside the new thread

                    error.setText(resp);

                    if (null != errorMsg && !errorMsg.isEmpty()) {
                        error.setText(errorMsg);
                    }
                } catch (Exception e) {
                    error.setText(e.getMessage());
                }*/
            }
        });


        /*kmkmkButton btn_home = (Button)rootView.findViewById(R.id.button);
        btn_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),HomeActivity.class);
                startActivity(intent);
            }
        });*/
        return rootView;
    }

}
