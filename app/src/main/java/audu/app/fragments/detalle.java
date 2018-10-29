package audu.app.fragments;

import android.arch.core.executor.TaskExecutor;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import audu.app.R;
import audu.app.common;
import audu.app.models.Libro_Class;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link detalle.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link detalle#newInstance} factory method to
 * create an instance of this fragment.
 */
public class detalle extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context myContext;
    public static final String TAG = "detalle";


    private TextView txt_titulo;
    private TextView txt_autor;
    private TextView txt_ano;
    private TextView txt_editorial;
    private TextView txt_narrador;
    private TextView txt_sinopsis;
    private ImageView img_libro;

    private Libro_Class _curLibro;
    private OnFragmentInteractionListener mListener;

    public detalle() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment detalle.
     */
    // TODO: Rename and change types and number of parameters
    public static detalle newInstance(Libro_Class curLibro) {
        detalle fragment = new detalle();
        Bundle args = new Bundle();
       args.putSerializable("Libro", curLibro);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           // mParam1 = getArguments().getString(ARG_PARAM1);
           // mParam2 = getArguments().getString(ARG_PARAM2);
            this._curLibro = (Libro_Class) getArguments().getSerializable("Libro");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View _view;
        _view = inflater.inflate( R.layout.fragment_detalle, container, false );
        img_libro = (ImageView) _view.findViewById(R.id.fragment_detalle_image);
        txt_titulo = (TextView) _view.findViewById(R.id.fragment_detalle_titulo);
        txt_autor = (TextView) _view.findViewById(R.id.fragment_detalle_autor);
        txt_ano = (TextView) _view.findViewById(R.id.fragment_detalle_ano);
        txt_editorial = (TextView) _view.findViewById(R.id.fragment_detalle_editorial);
        txt_narrador = (TextView) _view.findViewById(R.id.fragment_detalle_narrador);
        txt_sinopsis = (TextView) _view.findViewById(R.id.fragment_detalle_sinopsis);


        if (this._curLibro != null)
        {
            txt_titulo.setText(_curLibro.get_titulo());
            txt_autor.setText("Por: " + _curLibro.get_autor());
            txt_ano.setText("Año: " + _curLibro.get_ano());
            txt_editorial.setText("Editorial: " +_curLibro.get_editorial());
            txt_narrador.setText("Narrador: " + _curLibro.get_narrador());
            txt_sinopsis.setText(_curLibro.get_sinopsis());


        }


        String curLink = common.API_URL_BASE + "getPortada.php?idLibro=" + String.valueOf(this._curLibro.get_idLibro());

        if (curLink.length()>8) {
            Picasso.with(myContext)
                    .load(curLink)
                    .placeholder(R.drawable.placeholder)
                    .into(img_libro);
        }


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
