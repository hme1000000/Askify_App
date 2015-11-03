package app.com.example.hussein.askify_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeActivityFragment extends Fragment implements View.OnClickListener {


    private static String TAG = MainActivity.class.getSimpleName();
    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String[] navMenuTitles;
    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    /////////////////////*For the main content*////////////////////////

    /***************For questions********************/
    JSONArray jsonMainNode=null;
    JSONArray jsonTag =null;
    private ProgressDialog pDialog;
    ArrayList<Map<String, String>> qList;
    String Questions_URL = "http://askify-app.herokuapp.com/public/api/questionlist/all";
    String name ;
    String q ;
    String ans;
    String Tag;
    String ans_date;
    String ques_date;
    /***************For Ask*************************/
    private Button AskButton;
    String user_id="4"; //user_id from login

    public HomeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);



            /////////////////////*For the main content*////////////////////////
            /***************For questions********************/
            qList = new ArrayList<Map<String, String>>();
            new QuesAsyn().execute();

            /***************For Ask*************************/
            AskButton=(Button)rootView.findViewById(R.id.Ask_button);
            AskButton.setOnClickListener(this);

            ////////////////////*For the navMenu*////////////////////////////
            // load slide menu items
            navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
            //To display the up button

            mNavItems.add(new NavItem(navMenuTitles[0]));
            mNavItems.add(new NavItem(navMenuTitles[1]));
            mNavItems.add(new NavItem(navMenuTitles[2]));
            mNavItems.add(new NavItem(navMenuTitles[3]));
            mNavItems.add(new NavItem(navMenuTitles[4]));

            // DrawerLayout
            mDrawerLayout = (DrawerLayout) rootView.findViewById(R.id.drawerLayout);
            // Populate the Navigtion Drawer with options
            mDrawerPane = (RelativeLayout) rootView.findViewById(R.id.drawerPane);
            mDrawerList = (ListView) rootView.findViewById(R.id.navList);
            DrawerListAdapter adapter = new DrawerListAdapter(rootView.getContext(), mNavItems);
            mDrawerList.setAdapter(adapter);
            // Drawer Item click listeners
            mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectItemFromDrawer(position);
                }
            });
            mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    getActivity().invalidateOptionsMenu();
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    Log.d(TAG, "onDrawerClosed: " + getActivity().getTitle());
                    getActivity().invalidateOptionsMenu();
                }
            };
            mDrawerLayout.setDrawerListener(mDrawerToggle);
            mDrawerList.setOnItemClickListener(new SlideMenuClickListener());


        return rootView;
    }


    /////////////////////////////////////** Slide menu item click listener**///////////////////////////////////////////
        private class SlideMenuClickListener implements
                ListView.OnItemClickListener {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // display view for selected nav drawer item
                selectItemFromDrawer(position);
            }
        }


        /////////////** Called when a particular item from the navigation drawer is selected.**////////////////////
        private void selectItemFromDrawer(int position) {
            switch (position) {
                case 0:
                    Intent y =new Intent(getActivity(), HomeActivity.class);
                    Bundle b = new Bundle();
                    b.putString("user_id",user_id);
                    y.putExtras(b);
                    startActivity(y);
                    break;
                case 1:


                    break;
                case 2:
                    Intent x =new Intent(getActivity(), My_Questions.class);
                    Bundle bun = new Bundle();
                    bun.putString("user_id",user_id);
                    x.putExtras(bun);
                    startActivity(x);
                    break;
                case 3:

                    break;
                case 4:

                    break;


                default:
                    break;
            }

        }//end of selectItemFromDrawer
        ////////////////////////////////////////////*onClick*/////////////////////////////////////////
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.Ask_button){
                //new AskAsync().execute();
                Intent i = new Intent(getActivity(),AskQuestion.class);
                Bundle qbundle = new Bundle();
                qbundle.putString("user_id",user_id);
                i.putExtras(qbundle);
                startActivity(i);
            }

        }//end of onClick

//////////////////////////////////////////////////////////////////

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDrawerToggle.syncState();
    }
    ///////////////////////////////////////////////*MainContent */////////////////////////////////////////////////////

private class QuesAsyn extends AsyncTask<String,Void,ArrayList<Map<String, String>>> {

    private final String LOG_TAG = QuesAsyn.class.getSimpleName();
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading Questions...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }
    @Override
    protected ArrayList<Map<String, String>> doInBackground(String... args) {
        JSONParser jP = new JSONParser();
        String forecastJsonStr = jP.makeHttpRequest(Questions_URL);
        try {

            JSONObject forcastJson = new JSONObject(forecastJsonStr);
            jsonMainNode = forcastJson.getJSONArray("question_List");
            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                name = jsonChildNode.getString("questioner_name");
                q = jsonChildNode.getString("question");
                ans = jsonChildNode.getString("Answer");
                ques_date=jsonChildNode.getString("question_date");
                ans_date=jsonChildNode.getString("Answer_date");
                HashMap<String, String> contact = new HashMap<String, String>();
                contact.put("questioner_name", name);
                contact.put("question", q);
                contact.put("Answer",ans);
                contact.put("Answer_date",ans_date);
                contact.put("question_date",ques_date);
                jsonTag = jsonChildNode.getJSONArray("question_tag");
                Tag = "";
                for (int j = 0; j < jsonTag.length(); j++) {
                    Tag+= "#"+jsonTag.getString(j);
                }

                contact.put("question_tag",Tag);
                qList.add(contact);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return qList;
    }//end of doInBackground

    @Override
    protected void onPostExecute(ArrayList<Map<String, String>> x){
        pDialog.dismiss();
        ListAdapter simpleAdapter = new SimpleAdapter(getActivity(), x, R.layout.list_item_homequestion, new String[]{"question",
                "questioner_name", "Answer"}, new int[]{R.id.question,
                R.id.name,R.id.answer});
        ListView listView1 =(ListView) getActivity().findViewById(R.id.list);
        listView1.setAdapter(simpleAdapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String item = parent.getItemAtPosition(position).toString();
                Map<String,String> mq = qList.get(position);
                Intent i = new Intent(getActivity(),ViewQuestion.class);
                Bundle bundle = new Bundle();
                bundle.putString("questioner_name",mq.get("questioner_name"));
                bundle.putString("question",mq.get("question"));
                bundle.putString("question_tag",mq.get("question_tag"));
                bundle.putString("answer",mq.get("Answer"));
                bundle.putString("user_id",user_id);
                bundle.putString("question_date",mq.get("question_date"));
                bundle.putString("answer_date",mq.get("Answer_date"));
                i.putExtras(bundle);
                startActivity(i);


            }
        });
    }//end on OnPostExecute


}//end of QuesAsyn


}
