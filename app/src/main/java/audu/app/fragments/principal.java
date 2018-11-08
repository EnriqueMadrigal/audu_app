package audu.app.fragments;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import audu.app.R;
import audu.app.activities.MainActivity;
import audu.app.adapters.capituloAdapter;
import audu.app.common;
import audu.app.models.Capitulo_Class;
import audu.app.models.Libro_Class;
import audu.app.models.SynchronizeResult;
import audu.app.utils.BlurTransformation;
import audu.app.utils.BooksDB;
import audu.app.utils.HttpClient;
import audu.app.utils.IViewHolderClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link principal.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link principal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class principal extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int IdBook;
    private Context myContext;


    private ImageView bigImage;
    private ImageView smallImage;


    private Libro_Class curLibro;
    private OnFragmentInteractionListener mListener;

    private ArrayList<Capitulo_Class> _capitulos;
    private capituloAdapter _adapter;
    private RecyclerView _recycler;
    private LinearLayoutManager _linearLayoutManager;
    private boolean isOnlyCap1 = false;

    private static String TAG = "principal";

    public principal() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment principal.
     */
    // TODO: Rename and change types and number of parameters
    public static principal newInstance(Libro_Class _curLibro, boolean only1) {
        principal fragment = new principal();
        Bundle args = new Bundle();
        args.putSerializable("Libro", _curLibro);
        args.putBoolean("only1", only1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           // this.IdBook = getArguments().getInt("IdBook");
            this.curLibro = (Libro_Class) getArguments().getSerializable("Libro");
            this.isOnlyCap1 = (boolean) getArguments().getBoolean("only1");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View _view;
        _view = inflater.inflate( R.layout.fragment_principal, container, false );

        bigImage = (ImageView) _view.findViewById(R.id.fragment_principal_bigImg);
        smallImage = (ImageView) _view.findViewById(R.id.fragment_principal_smallImg);
        _recycler = (RecyclerView) _view.findViewById(R.id.fragment_principal_recycler);



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


        _capitulos = new ArrayList<>();


        _adapter = new capituloAdapter(getActivity(), _capitulos, new IViewHolderClick() {
            @Override
            public void onClick(int position) {

                Capitulo_Class curCap = _capitulos.get(position);
                Log.d(TAG, String.valueOf(curCap.get_idCapitulo()));

                FragmentManager fragmentManager = getFragmentManager();
                playcap _playcap = playcap.newInstance(curLibro, curCap.get_numCapitulo());

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
                fragmentTransaction.replace( R.id.fragment_container,_playcap, "PlayCap" );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


                //myContext.getSupportFragmentManager().beginTransaction().setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right ).replace( R.id.fragment_container,_subCategory, "Sub Categoria" ).commit();




            }
        });



        //////////

        _linearLayoutManager = new LinearLayoutManager( getActivity() );

        _recycler.setHasFixedSize( true );
        _recycler.setAdapter( _adapter );
        _recycler.setLayoutManager( _linearLayoutManager );




        getCapitulos();



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

    // Capitulos


    private void getCapitulos()
    {

        //Revisar si los capitulos ya existen para un libro descargado

        BooksDB db = new BooksDB(myContext);
        db.open();
        ArrayList<Capitulo_Class> capitulos = db.getCapitulosByIdLibro(curLibro.get_idLibro());
        db.close();

        if (capitulos.size()>=1)
        {

            //Ya existen ya no descargar
           // _capitulos = new ArrayList<>();
            for (Capitulo_Class curCap: capitulos)
            {
                _capitulos.add(curCap);

            }

            _adapter.notifyDataSetChanged();
            downloadCapitulos();
            return;
        }

        if( common.haveInternetPermissions(myContext, "Login") ) // Revisar permisos de internet
        {

            if (common.isOnline(myContext)) // Revisar si tenemos conexión
            {


                new loadCapitulos(curLibro.get_idLibro()).execute();
                //Intent intent = new Intent();
                //intent.setClass(context, presentacion.class);
                //finish();
                //startActivity(intent);

            }

            else
            {
                common.showWarningDialog("! Sin conexión ¡", "Favor de revisar su conexión de Datos..", myContext);
                //alertDialog.dismiss();
            }

        }

    }

private void downloadCapitulos()
{

    int totCapitulos = _capitulos.size();
    if (isOnlyCap1) totCapitulos = 1;

    for (int i=0;i<totCapitulos;i++) {

        Capitulo_Class curCap = _capitulos.get(i);

        if (curCap.get_downloaded() == 0) { // SI no se ha descargado, descargar,
            new DownloadCap(curCap, curLibro.get_idLibro(), i).execute();
        }

    }

}


private void setDownloadCap(int curCap)
{
    _capitulos.get(curCap).set_downloaded(1);

    _adapter.notifyItemChanged(curCap);

    Capitulo_Class curCapitulo = _capitulos.get(curCap);
    BooksDB db = new BooksDB(myContext);
    db.open();
    db.updateCapituloDownload(1, curCapitulo.get_idCapitulo() );
    db.close();

}



    private void handleSentCapitulos( HttpClient.HttpResponse response )
    {
        if( response.getCode() == 200 )
        {

            BooksDB db = new BooksDB(myContext);
            db.open();
            //db.deleteLibros();


            try
            {
                JSONObject json = new JSONObject( response.getResponse() );
                JSONArray capitulos = json.getJSONArray("capitulos");

                for (int i = 0; i < capitulos.length(); i++)
                {
                    JSONObject row = capitulos.getJSONObject(i);
                    int id = row.getInt("idCapitulo");
                    int idAudioLibro = row.getInt("idAudioLibro");
                    String nombreCapitulo = row.getString("nombreCapitulo");
                    String subtitulo = row.getString("subtitulo");
                    int numCapitulo = row.getInt("numCapitulo");


                    Capitulo_Class curCap = new Capitulo_Class();
                    curCap.set_idCapitulo(id);
                    curCap.set_idAudioLibro(idAudioLibro);
                    curCap.set_nombreCapitulo(nombreCapitulo);
                    curCap.set_numCapitulo(numCapitulo);
                    curCap.set_subtitulo(subtitulo);
                    _capitulos.add(curCap);
                    if (!isOnlyCap1)  db.insert(curCap);
                }





            }
            catch( Exception e )
            {
                android.util.Log.e( "JSONParser", "Cant parse: " + e.getMessage() );
                // Common.alert( this, "No se ha podido registrar, por favor intenta nuevamente más tarde." );
            }

            finally {
                db.close();
                _adapter.notifyDataSetChanged();
                downloadCapitulos();
            }

        }
        else {
            //Common.alert( this, "No se ha podido registrar, por favor intenta nuevamente más tarde." );

        }
    }
    private class loadCapitulos extends AsyncTask<Void, Void, HttpClient.HttpResponse>
    {
        ProgressDialog _progressDialog;
        int _idAudioLibro;

        public loadCapitulos(int IdAudioLibro)
        {
        _idAudioLibro = IdAudioLibro;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            _progressDialog = ProgressDialog.show( myContext, "Espera un momento..", "Obteniendo Capitulos..", true );


        }

        @Override
        protected HttpClient.HttpResponse doInBackground( Void... arg0 )
        {

            ContentValues params = new ContentValues();

            HttpClient.HttpResponse response = HttpClient.post( common.API_URL_BASE + "getCapitulos.php?idAudioLibro=" + String.valueOf(_idAudioLibro), params );
            android.util.Log.d( "TEST", String.format( "HTTP CODE: %d RESPONSE: %s", response.getCode(), response.getResponse() ) );

            return response;
        }

        @Override
        protected void onPostExecute( HttpClient.HttpResponse result )
        {
            super.onPostExecute( result );
            _progressDialog.dismiss();
            handleSentCapitulos( result );


        }
    }


    ///

    private class DownloadCap extends AsyncTask<Void, Void, Integer> {
        ProgressDialog _progressDialog;
        private int _CapId;
        private int _LibroId;
        private int _numRegis;

        private Capitulo_Class curCapitulo;


        //final int tipoDescarga = tipo_descarga;

        public DownloadCap(Capitulo_Class _curCap, int IdLibro, int NumRegis) {
            curCapitulo = _curCap;
            _LibroId = IdLibro;
            _numRegis = NumRegis;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           // _progressDialog = new ProgressDialog(myContext);
          //  _progressDialog.setTitle("Descargando Capitulo.");
          //  _progressDialog.setMessage("Un momento por favor.." + String.valueOf(_CapId));
         //   _progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
          //  _progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
          //  Drawable drawable = myContext.getResources().getDrawable(R.drawable.progress_circle);


          //  _progressDialog.setProgressDrawable(drawable);
          //  _progressDialog.setSecondaryProgress(100);

          //  _progressDialog.setCancelable(false);
          //  _progressDialog.setProgressNumberFormat(null);  // No desplegar el numero actual 10/100
          //  _progressDialog.setProgressPercentFormat(null); // No desplegar el porcentaje
           // _progressDialog.show();

        }

        @Override
        protected Integer doInBackground(Void... arg0) {
            ContentValues params = new ContentValues();

            SynchronizeResult result = new SynchronizeResult();

          //  publishProgress("Descargando..", "Un momento por favor.", 1, 100);


            downloadCommonContent(result);


            return result.getResult();
        }




        private void downloadCommonContent(SynchronizeResult result)
        {

            //String zipUrl = json.getString( "zip" );

            //String zipUrl = "http://icrm.mypiagui.com:7000/FB_descargas/common.zip";
            //String zipUrl = "http://192.168.250.249/sitio/static/common.zip";
            String trailerUrl = common.API_URL_BASE + "getCapitulo.php?idCapitulo=" + String.valueOf(curCapitulo.get_idCapitulo()) +"&idLibro=" + String.valueOf(_LibroId);



            try
            {

                // publishProgress("3/3 Procesando contenido...", "Descargando  comun", 1, 1);
                File root = new File(common.getBaseDirectory());
                // if (!root.exists()) {
                //     root.mkdirs();
                // }

                String curPath = String.valueOf(curLibro.get_idLibro());
                if (isOnlyCap1)
                {
                    curPath = "0";
                }

                File to = new File(root, curPath);
                if (to.exists()) {
                    //if (!common.deleteDirectory(to)) {
                    //    throw new Exception("Cannot prepare assets folder!");
                    //}

                    Log.d(TAG, "Directory exists");

                } else {
                    to.mkdirs();
                }




                File cap = new File(to, String.valueOf(curCapitulo.get_numCapitulo()) + ".mp3");
                if (cap.exists()) {
                    cap.delete();
                }



                if (!download(cap, trailerUrl)) {
                    throw new Exception("Download of chapter failed!");
                }



                //zip.delete();



            }

            catch (Exception e) {
                Log.d("SYNC", "Chapter file error: " + e.getMessage());
                result.setResult(1);
            }


        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            //_progressDialog.dismiss();

			/*
            if(((Activity) context).isFinishing()) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
			*/

            //_capitulos.get(_numRegis).set_downloaded(1);

           //_adapter.notifyItemChanged(_numCap);
           // _adapter.notifyDataSetChanged();
            Log.d("Capitulo descargado", String.valueOf(curCapitulo.get_numCapitulo()));
            setDownloadCap(_numRegis);
        }


        //////////////////
        private void publishProgress(final String title, final String message, final int progress, final int max) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                 //   _progressDialog.setTitle(title);
                 //   _progressDialog.setMessage(message);
                 //   _progressDialog.setProgress(progress);
                  //  _progressDialog.setMax(max);

                }
            });
        }

        private void publishProgress(final int progress, final int max) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //_progressDialog.setProgress(progress);
                    //_progressDialog.setMax(max);
                    int curPercent = (progress * 100) / max;
                    //Log.d(TAG, "porcentaje=" + String.valueOf(curPercent));


                    _capitulos.get(_numRegis).set_progress(curPercent);


                    //_adapter.notifyDataSetChanged();
                    _adapter.notifyItemChanged(_numRegis);

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
                        conn.disconnect();
                        inputStream.close();
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


}
