package audu.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import audu.app.R;
import audu.app.activities.MainActivity;
import audu.app.adapters.optionsAdapter;
import audu.app.common;
import audu.app.models.General_class;
import audu.app.models.Libro_Class;
import audu.app.models.User_Settings;
import audu.app.util;
import audu.app.utils.BooksDB;
import audu.app.utils.IViewHolderClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link useroptions.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link useroptions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class useroptions extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context myContext;

    private OnFragmentInteractionListener mListener;


    private optionsAdapter _adapter;
    private RecyclerView _recycler;
    private ArrayList<General_class> _items;
    private LinearLayoutManager _linearLayoutManager;

    private ImageView _iconImage;
    private TextView _nombre;
    private TextView _email;
    private TextView _miembro;
    private  TextView _descargas;
    private TextView _libros;
    private static String TAG = "useroptions";

    public useroptions() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment useroptions.
     */
    // TODO: Rename and change types and number of parameters
    public static useroptions newInstance() {
        useroptions fragment = new useroptions();
        Bundle args = new Bundle();
       // args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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

        View _view;
        _view = inflater.inflate( R.layout.fragment_useroptions, container, false );
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_useroptions, container, false);


        _iconImage = (ImageView) _view.findViewById(R.id.fragment_useroptions_bigImg);
        _nombre = (TextView) _view.findViewById(R.id.fragment_useroptions_name);
        _email = (TextView) _view.findViewById(R.id.fragment_useroptions_email);
        _miembro = (TextView) _view.findViewById(R.id.fragment_useroptions_miembro);
        _descargas = (TextView) _view.findViewById(R.id.fragment_useroptions_downloads);
        _libros = (TextView) _view.findViewById(R.id.fragment_useroptions_num_books);
        _recycler = (RecyclerView) _view.findViewById(R.id.fragment_useroptions_recycler);


        util Util = new util(myContext);

        User_Settings curUser = Util.getUserSettings();

        _nombre.setText(curUser.get_name());
        _email.setText(curUser.get_email());

        Date start_date = curUser.get_start_date();
        _miembro.setText("Miembro desde:" + DateFormat.getDateInstance(DateFormat.SHORT).format(start_date));

        BooksDB db = new BooksDB(myContext);
        db.open();
        ArrayList<Libro_Class> libros = db.getLibros_descargados();
        db.close();

        _descargas.setText(String.valueOf(libros.size()));

        int curAvatar = Util.getAvatar();
        String Avatar = common.avateres[curAvatar];
        int id = myContext.getResources().getIdentifier(Avatar, "drawable", myContext.getPackageName());

        if (id>0)
        {
            _iconImage.setImageResource(id);
        }


        _items = new ArrayList<>();

        _items.add(new General_class(0, R.drawable.ico_accstar, "Mis Logros"));
        _items.add(new General_class(1, R.drawable.ico_accach, "Suscripción"));
        _items.add(new General_class(2, R.drawable.creditcard_icon, "Metódos de Pago"));
        _items.add(new General_class(3, R.drawable.ico_acclock, "Cambiar contraseña"));
        _items.add(new General_class(4, R.drawable.ajustes_icon, "Ajustes"));
        _items.add(new General_class(5, R.drawable.ico_accach, "Preferencias"));
        _items.add(new General_class(6, R.drawable.ico_cerrarsesion, "Cerrar sessión"));



        _adapter = new optionsAdapter(getActivity(), _items, new IViewHolderClick() {
            @Override
            public void onClick(int position) {

                General_class curItem = _items.get(position);
                int curId = curItem.get_id();



                Log.d(TAG, String.valueOf(curId));


                FragmentManager fragmentManager = getFragmentManager();

                if (curId==0)
                {
                    logros _mislogros = logros.newInstance();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
                    fragmentTransaction.replace( R.id.fragment_container,_mislogros, "Mis Logros" );
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
               }

                if (curId==1)
                {
                    suscripcion _suscripcion = suscripcion.newInstance();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
                    fragmentTransaction.replace( R.id.fragment_container,_suscripcion, "Suscripcion" );
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }



                if (curId==3) // Cambiar contraseaña
                {
                    passchange _passchange = passchange.newInstance();

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
                    fragmentTransaction.replace( R.id.fragment_container,_passchange, "PassChange" );
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }



                if (curId==4) // Ajustes
                {

                    ajustes _ajustes = ajustes.newInstance();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
                    fragmentTransaction.replace( R.id.fragment_container,_ajustes, "Ajustes" );
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }


                if (curId==5) // Terminos
                {


                }


                if (curId==6) // Cerrar session
                {
                    ((MainActivity) getActivity()).close_session();

                }

                //playcap _playcap = playcap.newInstance(curLibro, curCap.get_numCapitulo(), curCap);

                //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
                //fragmentTransaction.replace( R.id.fragment_container,_playcap, "PlayCap" );
                //fragmentTransaction.addToBackStack(null);
                //fragmentTransaction.commit();




            }
        });




        _linearLayoutManager = new LinearLayoutManager( getActivity() );

        _recycler.setHasFixedSize( true );
        _recycler.setAdapter( _adapter );
        _recycler.setLayoutManager( _linearLayoutManager );





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
