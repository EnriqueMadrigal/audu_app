package audu.app.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;

import audu.app.R;
import audu.app.common;
import audu.app.models.Capitulo_Class;
import audu.app.models.Libro_Class;
import audu.app.utils.BlurTransformation;
import audu.app.utils.BooksDB;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link playcap.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link playcap#newInstance} factory method to
 * create an instance of this fragment.
 */
public class playcap extends Fragment implements SeekBar.OnSeekBarChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Context myContext;


    private ImageView bigImage;
    private ImageView smallImage;


    private Libro_Class curLibro;
    private int curCapitulo;
    ArrayList<Capitulo_Class> capitulos;

    private SeekBar seekbar;

    private ImageButton favoriteBtn;
    private ImageButton shareBtn;

    private ImageButton playBtn;
    private ImageButton fwBtn;
    private ImageButton previousBtn;
    private ImageButton tempoBtn;
    private ImageButton nextBtn;

    private ImageButton bookmarkBtn;
    private ImageButton capsBtn;


    MediaPlayer player;
    private boolean isPlaying = false;
    private boolean isPaused = false;
    Timer timer;

    public boolean libroYaDescargado = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public playcap() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment playcap.
     */
    // TODO: Rename and change types and number of parameters
    public static playcap newInstance(Libro_Class _curlibro, int _curCapitulo) {
        playcap fragment = new playcap();
        Bundle args = new Bundle();
       args.putSerializable("libro", _curlibro);
       args.putSerializable("capitulo", _curCapitulo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           curLibro = (Libro_Class) getArguments().getSerializable("libro");
           curCapitulo = getArguments().getInt("capitulo");
        }

        BooksDB db = new BooksDB(myContext);
        db.open();
        Libro_Class _curLibro = db.getLibro_Descargado(curLibro.get_idLibro());
        if (_curLibro !=null)
        {
            libroYaDescargado = true;
        }

        db.close();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_playcap, container, false);


        View _view;
        _view = inflater.inflate( R.layout.fragment_playcap, container, false );

        bigImage = (ImageView) _view.findViewById(R.id.fragment_playcap_bigImg);
        smallImage = (ImageView) _view.findViewById(R.id.fragment_playcap_smallImg);

        favoriteBtn = (ImageButton) _view.findViewById(R.id.fragment_playcap_addfavorito);
        shareBtn = (ImageButton) _view.findViewById(R.id.fragment_playcap_share);

        previousBtn = (ImageButton) _view.findViewById(R.id.fragment_playcap_previous);
        fwBtn = (ImageButton) _view.findViewById(R.id.fragment_playcap_fw);
        playBtn = (ImageButton) _view.findViewById(R.id.fragment_playcap_play);
        tempoBtn = (ImageButton) _view.findViewById(R.id.fragment_playcap_tempo);
        nextBtn = (ImageButton) _view.findViewById(R.id.fragment_playcap_next);
        bookmarkBtn = (ImageButton) _view.findViewById(R.id.fragment_playcap_bookmark);
        capsBtn = (ImageButton) _view.findViewById(R.id.fragment_playcap_caps);




        //Cargar las imagenes
        String curLink = common.API_URL_BASE + "getPortada.php?idLibro=" + String.valueOf(this.curLibro.get_idLibro());


        Picasso.with(myContext)
                .load(curLink)
                .placeholder(R.drawable.placeholder)
                .into(smallImage);


        Picasso.with(myContext)
                .load(curLink)
                .placeholder(R.drawable.placeholder)
                .transform(new BlurTransformation(myContext))
                .into(bigImage);




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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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
