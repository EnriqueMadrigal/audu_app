package audu.app.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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
public class playcap extends Fragment implements MediaPlayer.OnCompletionListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "playcap";

    private Context myContext;


    private ImageView bigImage;
    private ImageView smallImage;


    private Libro_Class curLibro;
    private int curCapitulo;
    ArrayList<Capitulo_Class> capitulos;
    private Capitulo_Class curChap;

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

    private TextView counterTxt;
    private TextView autorTxt;
    private TextView capNameTxt;
    private TextView subtituloTxt;
    private TextView progressTxt;

    MediaPlayer player;
    private boolean isPlaying = false;
    private boolean isPaused = false;
    private boolean wasPreviouslyPaused = false;
    Timer timer;
    private int curPos = 1;
    public boolean libroYaDescargado = false;
    private boolean canPlayNextChapter = true;

    private int curCapituloPos = 0;
    private int curMaxCapitulos = 0;
    public boolean nextplay=false;

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
    public static playcap newInstance(Libro_Class _curlibro, int _curCapitulo, Capitulo_Class _curChap) {
        playcap fragment = new playcap();
        Bundle args = new Bundle();
       args.putSerializable("libro", _curlibro);
       args.putSerializable("capitulo", _curChap);
       args.putInt("curcap", _curCapitulo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           curLibro = (Libro_Class) getArguments().getSerializable("libro");
           curCapitulo = getArguments().getInt("curcap");
           curChap = (Capitulo_Class) getArguments().getSerializable("capitulo");
        }

        BooksDB db = new BooksDB(myContext);
        db.open();
        Libro_Class _curLibro = db.getLibro_Descargado(curLibro.get_idLibro());
        if (_curLibro !=null)
        {
            libroYaDescargado = true;
        }

        capitulos = db.getCapitulosByIdLibro(curLibro.get_idLibro());
        db.close();

        curCapituloPos = curCapitulo -1;
        curMaxCapitulos = capitulos.size();

        // Create MediaPlayer
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
       // player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);

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
        seekbar = (SeekBar) _view.findViewById(R.id.fragment_playcap_seekbar);

        capNameTxt = (TextView) _view.findViewById(R.id.fragment_playcap_text_capitulo);
        autorTxt = (TextView) _view.findViewById(R.id.fragment_playcap_text_autor);
        subtituloTxt = (TextView) _view.findViewById(R.id.fragment_playcap_txt_subtitulo);
        counterTxt = (TextView) _view.findViewById(R.id.fragment_playcap_text_counter);
        progressTxt = (TextView) _view.findViewById(R.id.fragment_playcap_txt_progress);

        autorTxt.setText(curLibro.get_autor());

        setChapterValues();

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

// Boton de play
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "PLay");

                if (isPlaying) {
                    if (!isPaused)
                    {
                        player.pause();
                        isPaused = true;
                        setPaused(isPaused);
                        Log.d(TAG, "Paused");
                    }
                    else{
                        player.start();
                        isPaused = false;
                        setPaused(isPaused);
                        Log.d(TAG, "Continue");
                    }
                    return;
                }

              playCapitulo();

            }

        });


        //Button previous
        previousBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "Previous chapter");
            playPreviousCap();
        }

        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "Next chapter");
            playNextCap();
        }

        });


////// Button Fw

      fwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Fw");

                if (!isPlaying) return;
                if (isPaused) return;

                if (curPos >30)
                {
                    curPos = curPos -30;
                    player.pause();

                    player.seekTo(curPos * 1000);
                    player.start();
                    seekbar.setProgress(curPos);
                }

            }



            });

/// Seek bar

seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        curPos = progress;

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.d(TAG,"startTracking");
        if(isPlaying)
        {

            if (!isPaused)
            {
                isPaused = true;
                player.pause();
                setPaused(true);
            }

            else
            {
                wasPreviouslyPaused = true;
            }

        }


    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        Log.d(TAG, "stop tracking");
        if (isPlaying)
        {

            if (wasPreviouslyPaused)
            {
                wasPreviouslyPaused = false;
                player.seekTo(curPos * 1000);


            }

            else
            {
                isPaused = false;
                player.seekTo(curPos * 1000);
                player.start();
                setPaused(false);
            }


        }

    }

    ////

});

     // Button Capitulos
        capsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Capitulos");
                //getActivity().getFragmentManager().popBackStack();
                getActivity().onBackPressed();
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


        Log.d(TAG, "on detach");
        if (timer!= null)
        {
            timer.cancel();
            timer.purge();
            timer = null;

        }

        if (player != null)
        {
            player.stop();
            player.release();
            player = null;
        }


    }

    private void playPreviousCap()
    {

        if (!libroYaDescargado) return; // Solo es un capitulo ignorar

        if (curCapitulo == 1) return; // Primer capitulo ignorar

        curCapitulo = curCapitulo -1;
        curCapituloPos = curCapituloPos -1;
        setChapterValues();

        if (timer !=null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }


        if (isPlaying)
        {
            player.stop();
            player.reset();
            playCapitulo();
        }

        if (isPaused)
        {
            //wasPreviouslyPaused=true;
            player.stop();
            player.reset();
           playCapitulo();
        }


    }


    private void playNextCap()
    {
        if (!libroYaDescargado) return; // Solo es un capitulo ignorar

        if (curCapitulo == curMaxCapitulos) return; // Estamos al final de los capitulos

        curCapitulo ++;
        curCapituloPos ++;
        setChapterValues();

        if (timer !=null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }



        if (isPlaying)
        {
            player.stop();
            player.reset();
            playCapitulo();
        }

        if (isPaused)
        {
           // wasPreviouslyPaused = true;
            player.stop();
            player.reset();
            playCapitulo();
        }



    }


private void setChapterValues()
{

    Capitulo_Class curCap;


    if (!libroYaDescargado) // No es libro descargado signfica que solo es el capitulo 1
    {
     curCap = curChap;
    }

    else  // Buscar de la memoria
    {
        curCap =  capitulos.get(curCapituloPos);
     }


    // Obtener datos del capitulo
    capNameTxt.setText(curCap.get_nombreCapitulo());
    subtituloTxt.setText(curCap.get_subtitulo());




}




    private void setPaused(boolean paused)
    {

        if (paused)
        {
            Drawable img = getContext().getResources().getDrawable( R.drawable.ico_purpleplaybig );
            playBtn.setImageDrawable(img);
        }

        else
        {
            Drawable img = getContext().getResources().getDrawable( R.drawable.ico_purplepause );
            playBtn.setImageDrawable(img);
        }

    }

    private void playCapitulo()
    {

        final File chapter;

        if (!libroYaDescargado) // No es libro descargado signfica que solo es el capitulo 1
        {
            File root = new File(common.getBaseDirectory());
            File to = new File(root, "0");

            chapter = new File(to, "1.mp3");
            if (!chapter.exists()) {
                return; // No existe regresar y no tocar nada
            }


        }

        else  // Buscar de la memoria
        {
            Capitulo_Class curCap =  capitulos.get(curCapituloPos);
            if (curCap.get_downloaded() == 0) return;  // Si no esta descargado no tocar

            File root = new File(common.getBaseDirectory());
            File to = new File(root, String.valueOf(curLibro.get_idLibro()));

            chapter = new File(to, String.valueOf(curCapitulo) + ".mp3");
            if (!chapter.exists()) {
                return; // No existe regresar y no tocar nada
            }


        }



            // Reproducir el audio



            if (player == null)
            {
                player = new MediaPlayer();
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);



            }

            // Play chapter First Time

        try
        {


                    player.setDataSource(chapter.getAbsolutePath());
                    player.prepare();

                        player.start();
                        isPaused = false;

                    isPlaying = true;

                    setPaused(false);
                    Log.d(TAG, "duration" + String.valueOf(player.getDuration()));

                    int fileMax = player.getDuration() / 1000;

                    Log.d(TAG, "duration:" + String.valueOf(fileMax));
                    // Prepare seekbar

                    seekbar.setMax(fileMax);
                    //seekbar.setMin(1);
                    seekbar.setProgress(curPos);


                    // Tiempo
                    int minutes = (fileMax % 3600) / 60;
                    int seconds = fileMax % 60;

                    String timeString = String.format("%02d:%02d",  minutes, seconds);
                    counterTxt.setText(timeString);
                    /// Timer
                    timer = new Timer();


                    //////////
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (player != null && player.isPlaying()) {
                                        curPos = player.getCurrentPosition() / 1000;
                                        Log.d(TAG,"posistion=" + String.valueOf(curPos));
                                        seekbar.setProgress(curPos);
                                        int minutes = (curPos % 3600) / 60;
                                        int seconds = curPos % 60;

                                        String timeString = String.format("%02d:%02d",  minutes, seconds);
                                        progressTxt.setText(timeString);


                                    }
                                    else
                                        {
                                        if (isPaused)
                                        {
                                            return;
                                        }

                                        Log.d(TAG, "finished playing");

                                        if (canPlayNextChapter)
                                        {
                                            playNextCap();
                                            return;
                                        }

                                        timer.cancel();
                                        timer.purge();
                                        timer = null;
                                        isPlaying=false;
                                       setPaused(true);
                                        player.release();
                                        player = null;


                                    }
                                }
                            });
                        }
                    }, 0, 1000);



                    //// Timer
                }


                catch (Exception e) {
                    android.util.Log.e("SYNC", "No se encontro capitulo.mp3: " + e.getMessage());
                }















    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "Stopped");


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
