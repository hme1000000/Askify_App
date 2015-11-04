package app.com.example.hussein.askify_app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UnsolvedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UnsolvedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnsolvedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListAdapter myAdapter;

    private OnFragmentInteractionListener mListener;
    public static TextView unsolvedText;
    public static View rootView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UnsolvedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UnsolvedFragment newInstance(String param1, String param2) {
        UnsolvedFragment fragment = new UnsolvedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UnsolvedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_unsolved, container, false);

        ArrayList<Map<String, String>> myArray = new ArrayList<Map<String, String>>();
        myAdapter = new SimpleAdapter(rootView.getContext(), myArray, R.layout.list_item_unsolved, new String[]{"question",
                "questioner_name", "Answer"}, new int[]{R.id.questionUnsolved,
                R.id.nameUnsolved,R.id.answerUnsolved});
        ListView listView1 =(ListView) rootView.findViewById(R.id.listView_unsolved);
        //listView1.setAdapter(myAdapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String item = parent.getItemAtPosition(position).toString();
                Map<String, String> mq = SearchableActivity.unsolvedData.get(position);
                Intent i = new Intent(getActivity(), ViewQuestion.class);
                Bundle bundle = new Bundle();
                bundle.putString("questioner_name", mq.get("questioner_name"));
                bundle.putString("question", mq.get("question"));
                bundle.putString("question_tag", mq.get("question_tag"));
                bundle.putString("answer", mq.get("Answer"));
                bundle.putString("user_id", HomeActivity.userID);
                bundle.putString("question_date", mq.get("question_date"));
                bundle.putString("answer_date", mq.get("Answer_date"));
                i.putExtras(bundle);
                startActivity(i);


            }
        });
        unsolvedText = (TextView)rootView.findViewById(R.id.unsolved_textView);

        return rootView;
    }

    @Override
    public void onStart() {
        getUnsolved();
        super.onStart();

    }

    @Override
    public void onResume() {
        getUnsolved();
        super.onResume();
    }

    private void getUnsolved() {
        UnsolvedTask task = new UnsolvedTask();
        task.execute(SearchableActivity.inputQuery);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
