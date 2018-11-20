package audu.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import audu.app.R;
import audu.app.activities.MainActivity;
import audu.app.adapters.MyPageAdapter;
import audu.app.util;
import audu.app.utils.BoxIndicator;
import audu.app.utils.BoxIndicator2;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link avatar.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link avatar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class avatar extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private static String TAG = "avatar";
    private FragmentActivity myContext;

    private ViewPager mPager;
    private MyPageAdapter myPageAdapter;
    private List<Fragment> fList;
    private Button cambiar_btn;

    private int currentPage=0;

    public avatar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment avatar.
     */
    // TODO: Rename and change types and number of parameters
    public static avatar newInstance() {
        avatar fragment = new avatar();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_avatar, container, false);
        View _view;
        _view = inflater.inflate( R.layout.fragment_avatar, container, false );

        mPager = (ViewPager) _view.findViewById(R.id.fragment_avatar_ViewPager);
        cambiar_btn = (Button) _view.findViewById(R.id.fragment_avatar_cambiar);

        fList = new ArrayList<Fragment>();


        myPageAdapter = new MyPageAdapter(myContext.getSupportFragmentManager(), fList);


        mPager.setAdapter(myPageAdapter);
        mPager.setCurrentItem(0);
        myPageAdapter.notifyDataSetChanged();


        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                // Check if this is the page you want.
                Log.d(TAG,String.valueOf(position));
                currentPage = position;


            }
        });

        BoxIndicator2 indicator = (BoxIndicator2) _view.findViewById( R.id.fragment_avatar_BoxIndicator );
        indicator.setViewPager( mPager );

        fList.add(banner_image_class_resource.newInstance(R.drawable.men));
        fList.add(banner_image_class_resource.newInstance(R.drawable.woman));


        myPageAdapter.notifyDataSetChanged();

cambiar_btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Log.d(TAG, "Enviar");
        //getActivity().getFragmentManag getActivity().onBackPressed();

   util Util = new util(myContext);
   Util.setAvatar(currentPage);

        ((MainActivity)getActivity()).setAvatar(currentPage);
        getActivity().onBackPressed();


    }

});

        return  _view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        myContext = (FragmentActivity) context;
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
