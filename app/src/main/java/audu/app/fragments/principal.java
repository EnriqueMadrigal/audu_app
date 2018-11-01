package audu.app.fragments;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import audu.app.R;
import audu.app.activities.MainActivity;
import audu.app.common;
import audu.app.models.Capitulo_Class;
import audu.app.models.Libro_Class;
import audu.app.utils.BlurTransformation;
import audu.app.utils.BooksDB;
import audu.app.utils.HttpClient;

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
    public static principal newInstance(int IdBook) {
        principal fragment = new principal();
        Bundle args = new Bundle();
        args.putInt("IdBook",IdBook);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.IdBook = getArguments().getInt("IdBook");
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

        String curLink = common.API_URL_BASE + "getPortada.php?idLibro=" + String.valueOf(this.IdBook);


            Picasso.with(myContext)
                    .load(curLink)
                    .placeholder(R.drawable.placeholder)
                    .into(smallImage);


        Picasso.with(myContext)
                .load(curLink)
                .placeholder(R.drawable.placeholder)
                .transform(new BlurTransformation(myContext))
                .into(bigImage);


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
        if( common.haveInternetPermissions(myContext, "Login") ) // Revisar permisos de internet
        {

            if (common.isOnline(myContext)) // Revisar si tenemos conexión
            {
                _capitulos = new ArrayList<>();

                new loadCapitulos(IdBook).execute();
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



    private void handleSentCapitulos( HttpClient.HttpResponse response )
    {
        if( response.getCode() == 200 )
        {

            //BooksDB db = new BooksDB(context);
            //db.open();
            //db.deleteLibros();

            _capitulos = new ArrayList<>();
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
                }

            }
            catch( Exception e )
            {
                android.util.Log.e( "JSONParser", "Cant parse: " + e.getMessage() );
                // Common.alert( this, "No se ha podido registrar, por favor intenta nuevamente más tarde." );
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


}
