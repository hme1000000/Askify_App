package app.com.example.hussein.askify_app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by hussein on 29/10/15.
 */
public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    private String[] tabs = { "All", "Questions", "Answers","Users","Unsolved" };

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return  new AllFragment();
            case 1:
                // Games fragment activity
                return QuestionFragment.newInstance(index+1);
            case 2:
                // Movies fragment activity
                return new AnswerFragment();
            case 3:
                // Movies fragment activity
                return new UsersFragment();
            case 4:
                // Movies fragment activity
                return new UnsolvedFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
