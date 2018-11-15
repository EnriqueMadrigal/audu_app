package audu.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import audu.app.R;
import audu.app.util;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ajustes.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ajustes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ajustes extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Context myContext;
    private static String TAG = "ajustes";

    private ToggleButton toggleButton;
    private LinearLayout acercade_layout;
    private LinearLayout terminos_layout;

    private TextView version;
    public ajustes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ajustes.
     */
    // TODO: Rename and change types and number of parameters
    public static ajustes newInstance() {
        ajustes fragment = new ajustes();
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
        //return inflater.inflate(R.layout.fragment_ajustes, container, false);

        View _view;
        _view = inflater.inflate( R.layout.fragment_ajustes, container, false );

        toggleButton = (ToggleButton) _view.findViewById(R.id.fragment_ajustes_muestralibros);
        acercade_layout = (LinearLayout) _view.findViewById(R.id.fragment_ajustes_acercade);
        terminos_layout = (LinearLayout) _view.findViewById(R.id.fragment_ajustes_terminos);
        version = (TextView) _view.findViewById(R.id.fragment_ajustes_version);



        acercade_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Acercade");
                //getActivity().getFragmentManag getActivity().onBackPressed();

                FragmentManager fragmentManager = getFragmentManager();
                comentarios _comentarios = comentarios.newInstance();


                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
                fragmentTransaction.replace( R.id.fragment_container,_comentarios, "Comentarios" );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

        });


        terminos_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Terminos");
                //getActivity().getFragmentManag getActivity().onBackPressed();

                FragmentManager fragmentManager = getFragmentManager();
                terminos _terminos = terminos.newInstance();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
                fragmentTransaction.replace( R.id.fragment_container,_terminos, "Ajustes" );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

        });

        util Util = new util(myContext);
        String curVersion = Util.GetCurrentVersion();

        version.setText("Versión " + curVersion +  " Audu S.A.P.I de C.V. - México" );

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
        myContext = context;
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
