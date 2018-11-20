package audu.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import audu.app.R;
import audu.app.activities.MainActivity;
import audu.app.adapters.preferenciasAdapter;
import audu.app.adapters.preguntasAdapter;
import audu.app.models.Categoria_Class;
import audu.app.models.Pregunta_class;
import audu.app.util;
import audu.app.utils.BooksDB;
import audu.app.utils.IViewHolderClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link preferencias.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link preferencias#newInstance} factory method to
 * create an instance of this fragment.
 */
public class preferencias extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Context myContext;
    private static String TAG = "preguntas";

    private preferenciasAdapter _adapter;
    private RecyclerView _recycler;
    private ArrayList<Categoria_Class> _items;
    private LinearLayoutManager _linearLayoutManager;
    private Button cambiar_btn;

    public preferencias() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment preferencias.
     */
    // TODO: Rename and change types and number of parameters
    public static preferencias newInstance() {
        preferencias fragment = new preferencias();
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
        //return inflater.inflate(R.layout.fragment_preferencias, container, false);

        View _view;
        _view = inflater.inflate( R.layout.fragment_preferencias, container, false );


        _recycler =(RecyclerView) _view.findViewById(R.id.fragment_preferencias_recycler);
        cambiar_btn = (Button) _view.findViewById(R.id.fragment_preferencias_cambiar);
        _items = new ArrayList<>();

        BooksDB db = new BooksDB(myContext);
        db.open();

        _items = db.getCategorias();
        db.close();

        util Util = new util(myContext);
        String preferences = Util.getUserPreferences();

        String[] separated = preferences.split(",");

        for (int i=0;i<_items.size();i++)
        {

            for (String pre: separated)
            {

                int categoria =  _items.get(i).get_id();
                    if (pre != "" && categoria == Integer.parseInt(pre))
                    {
                        _items.get(i).set_selected(true);
                        continue;
                    }


            }



        }


        _adapter = new preferenciasAdapter(getActivity(), _items, new IViewHolderClick() {
            @Override
            public void onClick(int position) {

                Categoria_Class curItem = _items.get(position);
                int curId = curItem.get_id();
                Log.d(TAG, String.valueOf(curId));

                if (curItem.is_selected())
                {
                    _items.get(position).set_selected(false);
                }

                else
                {
                    _items.get(position).set_selected(true);
                }
                _adapter.notifyDataSetChanged();

            }

        });


       // getCategories();

        _linearLayoutManager = new LinearLayoutManager( getActivity() );

        _recycler.setHasFixedSize( true );
        _recycler.setAdapter( _adapter );
        _recycler.setLayoutManager( _linearLayoutManager );


        cambiar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Enviar");
                 getActivity().onBackPressed();

                util Util = new util(myContext);

                boolean firsTime = false;
                String preferences = "";
                for (int i=0;i<_items.size();i++)
                {

                    if(_items.get(i).is_selected())
                        {

                       if (!firsTime)
                       {
                           preferences = String.valueOf(_items.get(i).get_id());
                           firsTime = true;
                       }

                       else
                       {
                           preferences = preferences + "," + String.valueOf(_items.get(i).get_id());
                       }

                        }


                }


                Util.setUserPreferences(preferences);


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


    private void getCategories()
    {

        BooksDB db = new BooksDB(myContext);
        db.open();

        _items = db.getCategorias();
        db.close();
        _adapter.notifyDataSetChanged();



    }
}
