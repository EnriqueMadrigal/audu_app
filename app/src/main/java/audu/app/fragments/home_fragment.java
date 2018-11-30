package audu.app.fragments;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import audu.app.R;
import audu.app.adapters.bookGridAdapter;
import audu.app.models.Categoria_Class;
import audu.app.models.Libro_Class;
import audu.app.util;
import audu.app.utils.BooksDB;
import audu.app.utils.IViewHolderClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link home_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link home_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class home_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final String TAG = "Home Fragment";
    private FragmentActivity myContext;


    private bookGridAdapter _adapter1;
    private bookGridAdapter _adapter2;
    private bookGridAdapter _adapter3;
    private bookGridAdapter _adapter4;

    private ArrayList<Libro_Class> _libros;
    private RelativeLayout imageLayout;

    private LinearLayout parent_layout;

    private Button btn_suscribirme;


    private OnFragmentInteractionListener mListener;

    public home_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment home_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static home_fragment newInstance(String param1, String param2) {
        home_fragment fragment = new home_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.home_fragment, container, false);

        View _view;

        _view = inflater.inflate( R.layout.home_fragment, container, false );

        imageLayout = (RelativeLayout) _view.findViewById(R.id.home_fragment_layout);
        parent_layout = (LinearLayout) _view.findViewById(R.id.parent_linear_layout);
        btn_suscribirme = (Button) _view.findViewById(R.id.fragment_home_suscribirme);



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


        Display display = myContext.getWindowManager().getDefaultDisplay();

        Point size = new Point();
        int height = 0;
        int width = 0;
        try {
            display.getRealSize(size);
            height = size.y;
            width = size.x;

        } catch (NoSuchMethodError e) {
            height = display.getHeight();
            width = display.getWidth();
        }


        double newHeigth = ((double) width) * 0.625;

        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,(int) newHeigth);
        imageLayout.setLayoutParams(parms);
        imageLayout.requestLayout();





        final ArrayList<Libro_Class> nuevos = new ArrayList<>();
        final ArrayList<Libro_Class> recomendados = new ArrayList<>();
        final ArrayList<Libro_Class> favoritos = new ArrayList<>();
        final ArrayList<Libro_Class> mislibros = new ArrayList<>();



        BooksDB db = new BooksDB(myContext);
        db.open();
        _libros = db.getLibros();
        ArrayList<Libro_Class>  _favoritos = db.getLibrosOrderLikes();
        ArrayList<Libro_Class> _mislibros = db.getLibros_descargados();
        db.close();


        //Obtener los últimos

        int lastBook = _libros.size()-1;
        int firstBook = 0;

        if (lastBook>10)
        {
            firstBook = lastBook - 10;
        }

        for (int i = lastBook; i>firstBook; i--)
        {
            Libro_Class curLibro = _libros.get(i);
            nuevos.add(curLibro);

        }

        _adapter1 = new bookGridAdapter( myContext, nuevos, new IViewHolderClick()
        {
            @Override
            public void onClick( int position )
            {
                //onHomeGridItemSelected( position );
                Libro_Class curLibro = nuevos.get(position);
                Log.d(TAG, "book Selected " + String.valueOf(curLibro.get_idLibro()));
                show_detalle(curLibro);
            }
        } );

        // Obtener Recomendados

        util Util = new util(myContext);
        String opciones = Util.getUserPreferences();

        String curSuscription = Util.getSuscripcion();

        if (curSuscription.equals("1"))
        {
            hideMainSuscriptionTitle();
            Log.d(TAG, "Hidding");
        }

        if (opciones.length()==0) opciones = "8,2,5";

        for(Libro_Class curLibro: _libros)
        {
        String categorias = curLibro.get_categorias();

            String[] separated = categorias.split(",");
            for(String curCategoria: separated)
            {
                if(opciones.contains(curCategoria))
                {
                    recomendados.add(curLibro);

                    if (recomendados.size()>15) break;
                    continue;
                }

            }


        }

        _adapter2 = new bookGridAdapter( myContext, recomendados, new IViewHolderClick()
        {
            @Override
            public void onClick( int position )
            {
                //onHomeGridItemSelected( position );
                Libro_Class curLibro = recomendados.get(position);
                Log.d(TAG, "book Selected " + String.valueOf(curLibro.get_idLibro()));
                show_detalle(curLibro);
            }
        } );



        //Obtener Favoritos

        if (_favoritos.size() > 10)
        {

            for(int i=0;i<=10;i++)
            {
                favoritos.add(_favoritos.get(i));
            }
        }
        else
        {

            for(int i=0;i<_favoritos.size();i++)
            {
                favoritos.add(_favoritos.get(i));
            }
        }


        _adapter3 = new bookGridAdapter( myContext, favoritos, new IViewHolderClick()
        {
            @Override
            public void onClick( int position )
            {
                //onHomeGridItemSelected( position );
                Libro_Class curLibro = favoritos.get(position);
                Log.d(TAG, "book Selected " + String.valueOf(curLibro.get_idLibro()));
                show_detalle(curLibro);
            }
        } );

        // Mis libros


        for (Libro_Class curlibro: _mislibros)
        {
        mislibros.add(curlibro);
        }

        _adapter4 = new bookGridAdapter( myContext, mislibros, new IViewHolderClick()
        {
            @Override
            public void onClick( int position )
            {
                //onHomeGridItemSelected( position );
                Libro_Class curLibro = mislibros.get(position);
                Log.d(TAG, "book Selected " + String.valueOf(curLibro.get_idLibro()));

                FragmentManager fragmentManager = getFragmentManager();
                principal _principal = principal.newInstance(curLibro, false);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
                fragmentTransaction.replace( R.id.fragment_container,_principal, "Principal" );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        } );


        // Add LIne
        AddLine(parent_layout,"Nuevos Lanzamientos", _adapter1);
        AddLine(parent_layout,"Recomendados", _adapter2);
        AddLine(parent_layout,"Favoritos", _adapter3);
        AddLine(parent_layout,"Mis Libros", _adapter4);
        //AddLine(parent_layout,"Nuevos Lanzamientos", _items);
        //AddLine(parent_layout,"Nuevos Lanzamientos", _items);
        //AddLine(parent_layout,"Nuevos Lanzamientos", _items);



        /// Adding new line

        return _view;
    }


    public void hideMainSuscriptionTitle()
    {
        imageLayout.setVisibility(View.GONE);

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


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        myContext=(FragmentActivity) context;

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


    private void AddLine(LinearLayout parent, String title, bookGridAdapter _adapter)
    {

        LayoutInflater newinflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = newinflater.inflate(R.layout.row_line, null);

        LinearLayoutManager manager1
                = new LinearLayoutManager(myContext, LinearLayoutManager.HORIZONTAL, false);

        TextView text1 = rowView.findViewById(R.id.rowText);
        text1.setText(title);

        RecyclerView recyclerView1 = rowView.findViewById(R.id.RowRecyclerView);

        /*
        bookGridAdapter _adapter1 = new  bookGridAdapter( myContext, items, new IViewHolderClick()
        {
            @Override
            public void onClick( int position )
            {
                //onHomeGridItemSelected( position );
                Log.d(TAG, "book1 Selected");
            }
        } );
*/
        recyclerView1.setHasFixedSize( false );
        recyclerView1.setAdapter( _adapter );
        recyclerView1.setLayoutManager( manager1 );
        // _recyclerView.addItemDecoration( new SpacesItemDecoration( columns, 12 ) );

        recyclerView1.setItemViewCacheSize(0);

        parent.addView(rowView);



    }

}
