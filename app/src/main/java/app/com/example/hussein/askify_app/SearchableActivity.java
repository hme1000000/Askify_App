package app.com.example.hussein.askify_app;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SearchableActivity extends AppCompatActivity {



    private ViewPager viewPager;
    //private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    public static String inputQuery;
    public static ArrayList<String> allSearch = new ArrayList<>();

    public static ArrayList<String> allResults = new ArrayList<>();
    public static ArrayList<Map<String, String>> allData = new ArrayList<Map<String, String>>();
    public static ArrayList<String> questionResults = new ArrayList<>();
    public static ArrayList<Map<String, String>> questionData = new ArrayList<Map<String, String>>();
    public static ArrayList<String> answerResults = new ArrayList<>();
    public static ArrayList<Map<String, String>> answerData = new ArrayList<Map<String, String>>();
    public static ArrayList<String> usersResults = new ArrayList<>();
    public static ArrayList<Map<String, String>> usersData = new ArrayList<Map<String, String>>();
    public static ArrayList<String> unsolvedResults = new ArrayList<>();
    public static ArrayList<Map<String, String>> unsolvedData = new ArrayList<Map<String, String>>();
    public static boolean finished = false;
    // Tab titles






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
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

        viewPager = (ViewPager) findViewById(R.id.pager);
        //actionBar = getSupportActionBar();
        //mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        //viewPager.setAdapter(mAdapter);
        //actionBar.setHomeButtonEnabled(false);
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        /*for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }*/

        viewPager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //onSearchRequested();

                Toast.makeText(SearchableActivity.this, "Filtered by: " + TabsPagerAdapter.tabs[position], Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });















        //onSearchRequested();
        handleIntent(getIntent());

        /*viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });*/


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        //SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        //SearchView searchView = (SearchView)menu.findItem(R.id.action_searchView).getActionView();
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setIconifiedByDefault(false);
        //inputQuery = searchView.getQuery().toString();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_searchView) {
            //startActivity(new Intent(this,SearchableActivity.class));
            onSearchRequested();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    protected void onResume() {
        onSearchRequested();
        super.onResume();
    }

    @Override
    protected void onPause() {
        onSearchRequested();
        super.onPause();
    }

    @Override
    public void setFinishOnTouchOutside(boolean finish) {
        onSearchRequested();
        super.setFinishOnTouchOutside(finish);
    }*/

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
        super.onNewIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            inputQuery = query;
            SearchTask task = new SearchTask();
            task.execute(query);
            if(viewPager.getCurrentItem() == 0){
            myTask task2 = new myTask();
            task2.execute(query);}
            else if(viewPager.getCurrentItem() == 2){
            AnswerTask answerTask = new AnswerTask();
            answerTask.execute(query);}
            else if(viewPager.getCurrentItem() == 1){
            QuestionsTask questionsTask = new QuestionsTask();
            questionsTask.execute(query);}
            else if(viewPager.getCurrentItem() == 4){
            UnsolvedTask unsolvedTask = new UnsolvedTask();
            unsolvedTask.execute(query);}
            else if(viewPager.getCurrentItem() == 3){
            UserTask userTask = new UserTask();
            userTask.execute(query);}

            //startActivity(new Intent(this,SearchableActivity.class));
            //AllFragment.getAll();
            //UserTask u = new UserTask();
            //u.execute("Cairo","metric",query);
            //myTask mytask = new myTask();
            //mytask.execute("Cairo", "metric", query);

            //QuestionTask task = new QuestionTask();
            //task.execute("Cairo", "metric", query);

            //ArrayList<String> resultArray = new ArrayList<String>();

            /*for (String element:myArray
                 ) {
                if(query.equals(element))
                    resultArray.add(element);
            }*/


        }
    }


}
