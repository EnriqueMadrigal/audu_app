package audu.app.fragments;

import android.app.ProgressDialog;
import android.arch.core.executor.TaskExecutor;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import audu.app.R;
import audu.app.activities.register;
import audu.app.common;
import audu.app.models.Libro_Class;
import audu.app.models.SynchronizeResult;
import audu.app.utils.BooksDB;
import audu.app.utils.HttpClient;

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

    private Button btn_trailer;
    private Button btn_cap1;
    private Button btn_downloadBook;


    private Libro_Class _curLibro;
    private OnFragmentInteractionListener mListener;

    MediaPlayer player;
    private boolean isPlaying = false;
    private boolean isTrailerDownload = false;
    private boolean isPaused = false;
    Timer timer;

    public boolean libroYaDescargado = false;

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

        BooksDB db = new BooksDB(myContext);
        db.open();
        Libro_Class curLibro = db.getLibro_Descargado(_curLibro.get_idLibro());
        if (curLibro !=null)
        {
            libroYaDescargado = true;
        }

        db.close();
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
        btn_trailer = (Button) _view.findViewById(R.id.fragment_detalle_btntrailer);
        btn_cap1 = (Button) _view.findViewById(R.id.fragment_detalle_btnCap1);
        btn_downloadBook = (Button) _view.findViewById(R.id.fragment_detalle_btndownload);


        if (libroYaDescargado)
        {
            btn_cap1.setEnabled(false);
            btn_cap1.setVisibility(View.INVISIBLE);
            //Drawable drawable = myContext.getResources().getDrawable(R.drawable.purple_button3);
            //btn_cap1.setBackground(drawable);
        }


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



        btn_trailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Trailer");

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

                if (!isTrailerDownload) {
                    new DownloadTrailer(_curLibro.get_idLibro()).execute();
                }
                else
                {
                    PlayTrailer();
                }

            }

        });


        btn_cap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Cap 1");



                FragmentManager fragmentManager = getFragmentManager();
                principal _principal = principal.newInstance(_curLibro, true);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
                fragmentTransaction.replace( R.id.fragment_container,_principal, "Principal" );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }

        });


        btn_downloadBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "DownloadBook");


                DownloadBook();




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

    private  void DownloadBook()
    {
        if (!libroYaDescargado)
        {
            BooksDB db = new BooksDB(myContext);
            db.open();
            db.insert_libro_descargado(_curLibro);
            db.close();
        }

        img_libro.buildDrawingCache();
        Bitmap bm = img_libro.getDrawingCache();

        //Save caratula

        File root = new File(common.getBaseDirectory());

        String curPath = String.valueOf(_curLibro.get_idLibro());
        File to = new File(root, curPath);
        if (to.exists()) {

            Log.d(TAG, "Directory exists");

        } else {
            to.mkdirs();
        }



        File car = new File(to, "portada.png");
        if (car.exists()) {
            car.delete();
        }


        Uri outputFileUri;
        OutputStream fOut = null;

        try
        {
            fOut = new FileOutputStream(car);
        }
        catch (Exception e)
        {
        Log.d(TAG, "Could not save image");
        }
        try {
            bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e)
        {
            Log.d(TAG, "Could not save image");
        }



        ////
        FragmentManager fragmentManager = getFragmentManager();
        principal _principal = principal.newInstance(_curLibro, false);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
        fragmentTransaction.replace( R.id.fragment_container,_principal, "Principal" );
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


    private void setPaused(boolean paused)
    {

        if (paused)
        {
            Drawable img = getContext().getResources().getDrawable( R.drawable.btn_play );
            btn_trailer.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
            btn_trailer.getLayout();
        }

        else
        {
            Drawable img = getContext().getResources().getDrawable( R.drawable.btn_pause );
            btn_trailer.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
            btn_trailer.getLayout();
        }

    }


    ///////////// Donwload Trailer

    private class DownloadTrailer extends AsyncTask<Void, Void, Integer> {
        ProgressDialog _progressDialog;
        private int _bookId;

        //final int tipoDescarga = tipo_descarga;

        public DownloadTrailer(int IdBook) {
            _bookId = IdBook;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            _progressDialog = new ProgressDialog(myContext);
            _progressDialog.setTitle("Descargando Trailer.");
            _progressDialog.setMessage("Un momento por favor..");
            _progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            _progressDialog.setCancelable(false);
            _progressDialog.setProgressNumberFormat(null);  // No desplegar el numero actual 10/100
            _progressDialog.setProgressPercentFormat(null); // No desplegar el porcentaje
            _progressDialog.show();
        }

        @Override
        protected Integer doInBackground(Void... arg0) {
            ContentValues params = new ContentValues();

            SynchronizeResult result = new SynchronizeResult();

            publishProgress("Descargando..", "Un momento por favor.", 1, 100);


            downloadCommonContent(result);


            return result.getResult();
        }




        private void downloadCommonContent(SynchronizeResult result)
        {

            //String zipUrl = json.getString( "zip" );

            //String zipUrl = "http://icrm.mypiagui.com:7000/FB_descargas/common.zip";
            //String zipUrl = "http://192.168.250.249/sitio/static/common.zip";
            String trailerUrl = common.API_URL_BASE + "getTrailer.php?idLibro=" + String.valueOf(_bookId);



            try
            {

                // publishProgress("3/3 Procesando contenido...", "Descargando  comun", 1, 1);
                File root = new File(common.getBaseDirectory());
               // if (!root.exists()) {
               //     root.mkdirs();
               // }


                File trailer = new File(root, "trailer.mp3");
                if (trailer.exists()) {
                    trailer.delete();
                }



                if (!download(trailer, trailerUrl)) {
                    throw new Exception("Download of trailer failed!");
                }



                //zip.delete();



            }

            catch (Exception e) {
                android.util.Log.e("SYNC", "Comun file error: " + e.getMessage());
                result.setResult(1);
            }


        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            _progressDialog.dismiss();

			/*
            if(((Activity) context).isFinishing()) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
			*/

			PlayTrailer();
			isTrailerDownload = true;
        }


        //////////////////
        private void publishProgress(final String title, final String message, final int progress, final int max) {
           getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _progressDialog.setTitle(title);
                    _progressDialog.setMessage(message);
                    _progressDialog.setProgress(progress);
                    _progressDialog.setMax(max);

                }
            });
        }

        private void publishProgress(final int progress, final int max) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _progressDialog.setProgress(progress);
                    _progressDialog.setMax(max);

                }
            });
        }


        ///////
        private boolean download(File file, String Uurl) {
            boolean result = false;
            try {
                URL url = new URL(Uurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.connect();

                int response = conn.getResponseCode();

                InputStream inputStream  = new BufferedInputStream(url.openStream(), 8192);
                try {
                    if (response == 200) {
                        int fileLength = conn.getContentLength();
                        int total = 0;

                        //inputStream = conn.getInputStream();

                        FileOutputStream fileOutput = new FileOutputStream(file);

                        byte[] buffer = new byte[1024];
                        int bufferLength;

                        while ((bufferLength = inputStream.read(buffer)) > 0) {
                            fileOutput.write(buffer, 0, bufferLength);
                            total += bufferLength;
                            if (fileLength > 0) {
                                 publishProgress(total / 1024, fileLength / 1024);
                            }
                        }
                        //close the output stream when done
                        fileOutput.close();

                        result = true;
                    } else {
                        inputStream = conn.getErrorStream();
                        android.util.Log.d("SYNC", "Cannot download file. ERROR: " + HttpClient.getString(inputStream));
                    }
                } catch (Exception e) {
                    android.util.Log.d("SYNC", "Cannot download file. EXCEPTION: " + e.getMessage());
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            // Ignore.
                        }
                    }
                }

                conn.disconnect();
            } catch (Exception e) {
                android.util.Log.e("HTTPREQ", "Error sending request: " + e.getMessage());
            }

            return result;
        }

/////
    }

private void PlayTrailer()
{

    try
    {
        File root = new File(common.getBaseDirectory());

        File trailer = new File(root, "trailer.mp3");
        if (!trailer.exists()) {
                return;
        }


        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setDataSource(trailer.getAbsolutePath());
        player.prepare();
        player.start();


        Log.d(TAG, "duration" + String.valueOf(player.getDuration()));

        isPlaying = true;

        Drawable img = getContext().getResources().getDrawable( R.drawable.btn_pause );
        btn_trailer.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
        btn_trailer.getLayout();

       timer = new Timer();


        //////////
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (player != null && player.isPlaying()) {
                           Log.d(TAG,"posistion=" + String.valueOf(player.getCurrentPosition()));

                        } else {
                            if (isPaused)
                            {
                                return;
                            }
                            timer.cancel();
                            timer.purge();
                            timer = null;
                            isPlaying=false;
                            Drawable img = getContext().getResources().getDrawable( R.drawable.btn_play );
                            btn_trailer.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                            btn_trailer.getLayout();
                            player.release();
                            player = null;


                        }
                    }
                });
            }
        }, 0, 1000);

        ////////
    }

    catch (Exception e) {
        android.util.Log.e("SYNC", "No se encontre trailer.mp3: " + e.getMessage());
    }




}

    ////////////
}
