package audu.app.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import audu.app.utils.IViewHolderClick;

public class MyPageAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;
    private Context _context;
    private IViewHolderClick _listener;

    public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {

        super(fm);

        this.fragments = fragments;

    }

    @Override

    public Fragment getItem(int position) {

        return this.fragments.get(position);

    }

    @Override

    public int getCount() {

        return this.fragments.size();

    }

}
