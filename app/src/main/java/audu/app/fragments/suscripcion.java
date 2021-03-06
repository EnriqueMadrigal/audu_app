package audu.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

import audu.app.R;
import audu.app.models.User_Settings;
import audu.app.util;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link suscripcion.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link suscripcion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class suscripcion extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context myContext;
    private static String TAG = "suscripcion";
    private OnFragmentInteractionListener mListener;

    private TextView miembro_desde;
    private Button entendido;


    public suscripcion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment suscripcion.
     */
    // TODO: Rename and change types and number of parameters
    public static suscripcion newInstance() {
        suscripcion fragment = new suscripcion();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_suscripcion, container, false);


        View _view;
        _view = inflater.inflate( R.layout.fragment_suscripcion, container, false );

        miembro_desde = (TextView) _view.findViewById(R.id.fragment_suscripcion_miembro);
        entendido = (Button) _view.findViewById(R.id.fragment_suscripcion_entendido);

        util Util = new util(myContext);

        User_Settings curUser = Util.getUserSettings();


        Date start_date = curUser.get_start_date();
        miembro_desde.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(start_date));


        entendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Entendido");
                //getActivity().getFragmentManager().popBackStack();
                getActivity().onBackPressed();
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
       myContext= context;
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
