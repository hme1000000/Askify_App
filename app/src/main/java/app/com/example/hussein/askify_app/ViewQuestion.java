package app.com.example.hussein.askify_app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class ViewQuestion extends ActionBarActivity {
    TextView q_name,q,q_date,a,a_date,q_tag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewQuestion.this, NotificationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_id", HomeActivity.userID);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        q_name=(TextView)findViewById(R.id.Ques_name);
        q=(TextView)findViewById(R.id.Ques);
        q_date=(TextView)findViewById(R.id.q_date);
        a=(TextView)findViewById(R.id.Ans);
        a_date=(TextView)findViewById(R.id.a_date);
        q_tag=(TextView)findViewById(R.id.tag);

        Bundle bundle = getIntent().getExtras();
        String ques = bundle.getString("question");
        String ques_tag = bundle.getString("question_tag");
        String name = bundle.getString("questioner_name");
        String ans = bundle.getString("answer");
        String ques_date = bundle.getString("question_date");
        String ans_date = bundle.getString("answer_date");

        q_name.setText(name+ " asks:");
        a.setText(ans);
        q_tag.setText(ques_tag);
        q.setText(ques);
        q_date.setText(ques_date);
        if(ans.equals("No Answer")){
        }else{
            a_date.setText(ans_date);}
    }

}
