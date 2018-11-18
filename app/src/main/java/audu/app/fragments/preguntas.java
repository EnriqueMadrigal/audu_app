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

import java.util.ArrayList;

import audu.app.R;
import audu.app.adapters.optionsAdapter;
import audu.app.adapters.preguntasAdapter;
import audu.app.models.General_class;
import audu.app.models.Pregunta_class;
import audu.app.utils.IViewHolderClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link preguntas.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link preguntas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class preguntas extends Fragment {
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

    private preguntasAdapter _adapter;
    private RecyclerView _recycler;
    private ArrayList<Pregunta_class> _items;
    private LinearLayoutManager _linearLayoutManager;


    public preguntas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment preguntas.
     */
    // TODO: Rename and change types and number of parameters
    public static preguntas newInstance() {
        preguntas fragment = new preguntas();
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
        //return inflater.inflate(R.layout.fragment_preguntas, container, false);
        View _view;
        _view = inflater.inflate( R.layout.fragment_preguntas, container, false );

        _recycler =(RecyclerView) _view.findViewById(R.id.fragment_preguntas_recycler);

        _items = new ArrayList<>();

        String pregunta1 = myContext.getString(R.string.pregunta1);
        String pregunta2 = myContext.getString(R.string.pregunta2);
        String pregunta3 = myContext.getString(R.string.pregunta3);
        String pregunta4 = myContext.getString(R.string.pregunta4);
        String pregunta5 = myContext.getString(R.string.pregunta5);
        String pregunta6 = myContext.getString(R.string.pregunta6);
        String pregunta7 = myContext.getString(R.string.pregunta7);
        String pregunta8 = myContext.getString(R.string.pregunta8);

        String respuesta1 = myContext.getString(R.string.respuesta1);
        String respuesta2 = myContext.getString(R.string.respuesta2);
        String respuesta3 = myContext.getString(R.string.respuesta3);
        String respuesta4 = myContext.getString(R.string.respuesta4);
        String respuesta5 = myContext.getString(R.string.respuesta5);
        String respuesta6 = myContext.getString(R.string.respuesta6);
        String respuesta7 = myContext.getString(R.string.respuesta7);
        String respuesta8 = myContext.getString(R.string.respuesta8);


        _items.add(new Pregunta_class(1,pregunta1, respuesta1));
        _items.add(new Pregunta_class(2,pregunta2, respuesta2));
        _items.add(new Pregunta_class(3,pregunta3, respuesta3));
        _items.add(new Pregunta_class(4,pregunta4, respuesta4));
        _items.add(new Pregunta_class(5,pregunta5, respuesta5));
        _items.add(new Pregunta_class(6,pregunta6, respuesta6));
        _items.add(new Pregunta_class(7,pregunta7, respuesta7));
        _items.add(new Pregunta_class(8,pregunta8, respuesta8));


        _adapter = new preguntasAdapter(getActivity(), _items, new IViewHolderClick() {
            @Override
            public void onClick(int position) {

                Pregunta_class curItem = _items.get(position);
                int curId = curItem.get_id();
                Log.d(TAG, String.valueOf(curId));

                for (int i=0;i<_items.size();i++)
                {
                _items.get(i).set_expanded(false);
                }

                _items.get(position).set_expanded(true);
                _adapter.notifyDataSetChanged();

            }

            });

                _linearLayoutManager = new LinearLayoutManager( getActivity() );

        _recycler.setHasFixedSize( true );
        _recycler.setAdapter( _adapter );
        _recycler.setLayoutManager( _linearLayoutManager );




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
