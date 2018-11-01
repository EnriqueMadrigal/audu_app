package audu.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import audu.app.R;
import audu.app.adapters.bookGridAdapter;
import audu.app.models.Categoria_Class;
import audu.app.models.Libro_Class;
import audu.app.utils.BooksDB;
import audu.app.utils.IViewHolderClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link categoria.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link categoria#newInstance} factory method to
 * create an instance of this fragment.
 */
public class categoria extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Context  myContext;
    private int curCategoria=0;

    private TextView txt_label;
    private RecyclerView recycler;

    private bookGridAdapter _adapter;
    private OnFragmentInteractionListener mListener;
    private ArrayList<Libro_Class> _curLibros;

    private static String TAG = "Categoria";

    public categoria() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment categoria.
     */
    // TODO: Rename and change types and number of parameters
    public static categoria newInstance(int CurCategoria) {
        categoria fragment = new categoria();
        Bundle args = new Bundle();
        args.putInt("Categoria", CurCategoria);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.curCategoria = getArguments().getInt("Categoria");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View _view;
        _view = inflater.inflate( R.layout.fragment_categoria, container, false );
        txt_label = (TextView) _view.findViewById(R.id.fragment_categoria_title);
        recycler = (RecyclerView) _view.findViewById(R.id.fragment_categoria_recycler);

        // getLibros por categoria
        BooksDB db = new BooksDB(myContext);
        db.open();
        ArrayList<Libro_Class> libros = db.getLibros();
        final ArrayList<Libro_Class> libros_categoria = new ArrayList<>();

        Categoria_Class categoria = db.getCategoriaById(curCategoria);
        db.close();
        txt_label.setText(categoria.get_name());

        final int columns = 4;
        GridLayoutManager manager = new GridLayoutManager( getActivity(), columns );
        manager.setSpanSizeLookup( new GridLayoutManager.SpanSizeLookup()
        {
            @Override
            public int getSpanSize( int position )
            {
               // if( position == 0 )
                //    return columns;
               // else
                    return  1;
            }
        } );


        String opciones = String.valueOf(curCategoria);

        for(Libro_Class curLibro: libros)
        {
            String categorias = curLibro.get_categorias();

            String[] separated = categorias.split(",");
            for(String curCategoria: separated)
            {
                if(opciones.contains(curCategoria))
                {
                    libros_categoria.add(curLibro);
                    continue;
                }

            }


        }



        _adapter = new bookGridAdapter( myContext, libros_categoria, new IViewHolderClick()
        {
            @Override
            public void onClick( int position )
            {
                Libro_Class curLibro = libros_categoria.get(position);
                Log.d(TAG, "book Selected " + String.valueOf(curLibro.get_idLibro()));
                show_detalle(curLibro);

            }
        } );

        recycler.setHasFixedSize( false );
        recycler.setAdapter( _adapter );
        recycler.setLayoutManager( manager );
        // _recyclerView.addItemDecoration( new SpacesItemDecoration( columns, 12 ) );

        recycler.setItemViewCacheSize(0);



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

    private void show_detalle(Libro_Class curLibro)
    {

        FragmentManager fragmentManager = getFragmentManager();
        detalle _detalle = detalle.newInstance(curLibro);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
        fragmentTransaction.replace( R.id.fragment_container,_detalle, "Detalle" );
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}
