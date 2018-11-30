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
import android.widget.Button;
import android.widget.ImageButton;

import audu.app.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link suscribirse.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link suscribirse#newInstance} factory method to
 * create an instance of this fragment.
 */
public class suscribirse extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context myContext;
    private static String TAG = "suscribirme";

    private OnFragmentInteractionListener mListener;


    private ImageButton btn_close;
    private Button btn_suscribirme;
    private Button btn_entendido;


    public suscribirse() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment suscribirse.
     */
    // TODO: Rename and change types and number of parameters
    public static suscribirse newInstance() {
        suscribirse fragment = new suscribirse();
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
       // return inflater.inflate(R.layout.fragment_suscribirse, container, false);


        View _view;
        _view = inflater.inflate( R.layout.fragment_suscribirse, container, false );

        btn_close = (ImageButton) _view.findViewById(R.id.fragment_suscribirme_close);
        btn_entendido = (Button) _view.findViewById(R.id.fragment_suscribirme_entendido);
        btn_suscribirme = (Button) _view.findViewById(R.id.fragment_suscribirme_suscribirme);



        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Entendido");
                //getActivity().getFragmentManager().popBackStack();
                getActivity().onBackPressed();
            }

        });



        btn_entendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Entendido");
                //getActivity().getFragmentManager().popBackStack();
                getActivity().onBackPressed();
            }

        });


        btn_suscribirme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Suscribirme");
                FragmentManager fragmentManager = getFragmentManager();

                metodospago _metodospago = metodospago.newInstance();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
                fragmentTransaction.replace( R.id.fragment_container,_metodospago, "MetodosPago" );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

        });

        return _view;

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
