package app.com.example.hussein.askify_app;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



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

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        un = (EditText) rootView.findViewById(R.id.et_un);
        pw = (EditText) rootView.findViewById(R.id.et_pw);
        ok = (Button) rootView.findViewById(R.id.btn_login);
        error = (TextView) rootView.findViewById(R.id.tv_error);

        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /** According with the new StrictGuard policy,  running long tasks on the Main UI thread is not possible
                 So creating new thread to create and execute http operations */
                new Thread(new Runnable() {

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
                    /** wait a second to get response from server */
                    Thread.sleep(1000);
                    /** Inside the new thread we cannot update the main thread
                     So updating the main thread outside the new thread */

                    error.setText(resp);

                    if (null != errorMsg && !errorMsg.isEmpty()) {
                        error.setText(errorMsg);
                    }
                } catch (Exception e) {
                    error.setText(e.getMessage());
                }
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
