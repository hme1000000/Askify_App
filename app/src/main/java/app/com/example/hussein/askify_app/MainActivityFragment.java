package app.com.example.hussein.askify_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
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
    String username;
    String password;
    public static int success = 0;
    public static boolean finish = false;
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
                ok.setEnabled(false);
                username = un.getText().toString();
                password = pw.getText().toString();
                new LoginTask().execute(username,password);
                while (!finish);
                if (success == 1) {
                    Intent intent = new Intent(getActivity(),HomeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("user_id",LoginTask.user_id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else {
                    Toast.makeText(rootView.getContext(), "username or password is wrong, please try again", Toast.LENGTH_LONG).show();
                }


                /*Intent x =new Intent(rootView.getContext(), HomeActivity.class);
                Bundle bun = new Bundle();
                bun.putInt("user_id",LoginTask.user_id);
                x.putExtras(bun);
                startActivity(x);
                //Map<String,Object> params = new LinkedHashMap<>();
                //userid = 5;
                //while (!username.equals("hhh"));
                /*Intent intent = new Intent(rootView.getContext(),HomeActivity.class);
                startActivity(intent);

                /*params.put("username", username);
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
                }*/
                /*Login_URL += "?username="+username+"&password="+password;

                try {
                    HttpURLConnection urlConnection = null;
                    BufferedReader reader = null;
                    try {
                        String urlString = Login_URL;
                        URL url = new URL(urlString);
                        urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.connect();

                        InputStream inputStream = urlConnection.getInputStream();
                        StringBuffer buffer = new StringBuffer();
                        if (inputStream == null) {
                            success = 0;
                        }
                        reader = new BufferedReader(new InputStreamReader(inputStream));

                        String line;
                        while ((line = reader.readLine()) != null) {
                            buffer.append(line + "\n");
                        }

                        if (buffer.length() == 0) {
                            success = 0;
                        }
                        forecastJsonStr = buffer.toString();

                    } catch (IOException e) {
                        success = 0;
                    } finally{
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (final IOException e) {
                                success = 0;
                            }
                        }
                    }
                    //forecastJsonStr = jP.makeHttpRequest(Login_URL);
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
                ok.setEnabled(true);
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
