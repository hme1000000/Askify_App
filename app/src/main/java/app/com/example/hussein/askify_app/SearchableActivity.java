package app.com.example.hussein.askify_app;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchableActivity extends AppCompatActivity {

    public static ArrayAdapter<String> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        List<String> myArray = new ArrayList<String>();




        myAdapter = new ArrayAdapter<String>(this,
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textView,
                myArray);

        ListView list = (ListView)findViewById(R.id.listView_forecast);
        list.setAdapter(myAdapter);
        onSearchRequested();
        handleIntent(getIntent());

    }




    @Override
    protected void onResume() {
        onSearchRequested();
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
        super.onNewIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            myTask mytask = new myTask();
            mytask.execute("Cairo","metric",query);

            //ArrayList<String> resultArray = new ArrayList<String>();

            /*for (String element:myArray
                 ) {
                if(query.equals(element))
                    resultArray.add(element);
            }*/


        }
    }


}
