package audu.app.fragments;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import audu.app.R;
import audu.app.common;
import audu.app.models.User_Settings;
import audu.app.util;
import audu.app.utils.HttpClient;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link comentarios.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link comentarios#newInstance} factory method to
 * create an instance of this fragment.
 */
public class comentarios extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context myContext;
    private static String TAG = "comentarios";

    private EditText comentario;
    private Button enviar;

    private OnFragmentInteractionListener mListener;

    public comentarios() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment comentarios.
     */
    // TODO: Rename and change types and number of parameters
    public static comentarios newInstance() {
        comentarios fragment = new comentarios();
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
        //return inflater.inflate(R.layout.fragment_comentarios, container, false);

        View _view;
        _view = inflater.inflate( R.layout.fragment_comentarios, container, false );

        comentario = (EditText) _view.findViewById(R.id.fragment_comentarios_comentario);
        enviar = (Button)_view.findViewById(R.id.fragment_comentarios_enviar);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Enviar");
                //getActivity().getFragmentManag getActivity().onBackPressed();

                enviarComentario();



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


    private void enviarComentario()
    {

        util Util = new util(myContext);

        User_Settings curUser = Util.getUserSettings();
        int idSuscripcion = curUser.get_userid();

        String cur_comentario = comentario.getText().toString();

        ///////

        if (cur_comentario.length() > 8)
        {

        }

        else
        {
                common.showWarningDialog("! Advertencia ¡", "Por favor escriba un comentario válido, gracias.", myContext);
                return;
        }


            ///////



        new EnviarComentario(cur_comentario, idSuscripcion).execute();

    }

    private void handleSent( HttpClient.HttpResponse response )
    {
        if( response.getCode() == 200 )
        {
            try
            {
                JSONObject json = new JSONObject( response.getResponse() );
                Log.d(TAG, "resultado");



                if (json.getInt("error") == 1)
                {
                    common.showWarningDialog("! Error ¡", "No se pudo enviar su comentario", myContext);
                    return;
                }

                //    if (json.getInt("activo") == 1)// Suscripcion Activa
                //     {
                //     }

                if (json.getInt("error") == 0) // No hay errores continuar
                {
                    // Si se actualizo contraseña


                    AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
                    builder.setMessage("Su comentario se envio correctament, gracias.")
                            .setTitle("! Información ¡")
                            .setCancelable(false)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    getActivity().onBackPressed();

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();


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






    private class EnviarComentario extends AsyncTask<Void, Void, HttpClient.HttpResponse>
    {
        ProgressDialog _progressDialog;
        String _comentario;
        int _idSuscripcion;


        public EnviarComentario( String comentario, int IdSuscripcion)
        {
            _comentario = comentario;
            _idSuscripcion = IdSuscripcion;

        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            _progressDialog = ProgressDialog.show( myContext, "Espera un momento..", "Enviando comentario..", true );


        }

        @Override
        protected HttpClient.HttpResponse doInBackground( Void... arg0 )
        {

            ContentValues params = new ContentValues();

            params.put("newComentario", _comentario);
            params.put("idSuscripcion", _idSuscripcion);


            HttpClient.HttpResponse response = HttpClient.post( common.API_URL_BASE + "registerComentario.php", params );
            android.util.Log.d( TAG, String.format( "HTTP CODE: %d RESPONSE: %s", response.getCode(), response.getResponse() ) );

            return response;
        }

        @Override
        protected void onPostExecute( HttpClient.HttpResponse result )
        {
            super.onPostExecute( result );
            _progressDialog.dismiss();
            handleSent( result );


        }
    }


}
