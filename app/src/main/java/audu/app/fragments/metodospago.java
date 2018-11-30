package audu.app.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import audu.app.OpenPayApp;
import audu.app.R;
import audu.app.activities.MainActivity;
import audu.app.adapters.cardAdapter;
import audu.app.common;
import audu.app.models.Pregunta_class;
import audu.app.models.card_info;
import audu.app.util;
import audu.app.utils.HttpClient;
import audu.app.utils.IViewHolderClick;
import mx.openpay.android.Openpay;
import mx.openpay.android.OperationCallBack;
import mx.openpay.android.OperationResult;
import mx.openpay.android.exceptions.OpenpayServiceException;
import mx.openpay.android.exceptions.ServiceUnavailableException;
import mx.openpay.android.model.Card;
import mx.openpay.android.model.Token;
import mx.openpay.android.validation.CardValidator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link metodospago.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link metodospago#newInstance} factory method to
 * create an instance of this fragment.
 */
public class metodospago extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters

    private Context myContext;

    private static String TAG = "metodospago";

    private cardAdapter _adapter;
    private RecyclerView _recycler;
    private ArrayList<card_info> _items;
    private LinearLayoutManager _linearLayoutManager;
    private LinearLayout _addCard;

    private DeviceIdFragment deviceIdFragment;


    Openpay openpay;
    String sessionID;
    String tokenID;






    private OnFragmentInteractionListener mListener;

    public metodospago() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment metodospago.
     */
    // TODO: Rename and change types and number of parameters
    public static metodospago newInstance() {
        metodospago fragment = new metodospago();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }


        //Openpay openpay = ((OpenPayApp) myContext.getApplication()).getOpenpay();
        openpay = new Openpay(common.MERCHANT_ID, common.API_KEY, common.productionMode);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     //   return inflater.inflate(R.layout.fragment_metodospago, container, false);

        View _view;
        _view = inflater.inflate( R.layout.fragment_metodospago, container, false );


        _recycler =(RecyclerView) _view.findViewById(R.id.fragment_metodos_recycler);
        _addCard = (LinearLayout) _view.findViewById(R.id.fragment_metodospago_agregar);

        _items = new ArrayList<>();



        _adapter = new cardAdapter(getActivity(), _items, new IViewHolderClick() {
            @Override
            public void onClick(int position) {

                card_info curItem = _items.get(position);
                String card_id = curItem.get_car_id();
                Log.d(TAG, card_id);
                DeleteCard(card_id);

            }

        });

        _linearLayoutManager = new LinearLayoutManager( getActivity() );

        _recycler.setHasFixedSize( true );
        _recycler.setAdapter( _adapter );
        _recycler.setLayoutManager( _linearLayoutManager );

        _addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Add Card");

                if (_items.size()>0)  // Si ya existe una tajerta no continuar
                {
                    return;
                }


                ShowAddCardPopUp();

            }

        });


        LoadCards();


        FragmentManager fm = getFragmentManager();

        this.deviceIdFragment = (DeviceIdFragment) fm.findFragmentByTag("DeviceCollector");
        // If not retained (or first time running), we need to create it.
        if (this.deviceIdFragment == null) {
            this.deviceIdFragment = new DeviceIdFragment();
            fm.beginTransaction().add(this.deviceIdFragment, "DeviceCollector").commit();
        }




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


    private void DeleteCard(final String car_id)
    {
        new AlertDialog.Builder( myContext )
                .setTitle("Advertencia")
                .setMessage( "¿Está seguro de eliminar esta tarjeta?" )
                .setCancelable( false )
                .setPositiveButton( "Si", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id )
                    {
                        util _util = new util(myContext);
                        String curSuscription = String.valueOf(_util.getUserId());

                        new deleteCard(curSuscription, car_id).execute();


                    }
                } )
                .setNegativeButton( "No", null )
                .show();



    }


    /// OpenPay

    public String getDeviceId() {
        String deviceIdString = this.deviceIdFragment.getDeviceId();
        return deviceIdString;
    }


    private void ShowAddCardPopUp()
    {



        final Dialog alertDialog = new Dialog(myContext);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        final LayoutInflater factory = LayoutInflater.from(myContext);
        final View view2 = factory.inflate(R.layout.add_card, null);

       final EditText holderNameEt = ((EditText) view2.findViewById(R.id.holder_name));
       final EditText cardNumberEt = ((EditText) view2.findViewById(R.id.card_number));
       final EditText cvv2Et = ((EditText) view2.findViewById(R.id.cvv2));

       final TextView holderNameEt_message = ((TextView) view2.findViewById(R.id.holder_name_message));
       final TextView cardNumberEt_message = ((TextView) view2.findViewById(R.id.card_number_message));
       final TextView cvv2Et_message = ((TextView) view2.findViewById(R.id.cvv2_message));

       final Spinner year_spinner = ((Spinner) view2.findViewById(R.id.year_spinner));
       final Spinner month_spinner = ((Spinner) view2.findViewById(R.id.month_spinner));

       final Button saveCard = ((Button) view2.findViewById(R.id.saveCard));
       final Button cancelCard = ((Button) view2.findViewById(R.id.cancelCard));

        holderNameEt.setText("");
        cardNumberEt.setText("");
        cvv2Et.setText("");

        holderNameEt_message.setText("");
        cardNumberEt_message.setText("");
        cvv2Et_message.setText("");

        year_spinner.setSelection(0);
        month_spinner.setSelection(0);


        alertDialog.setContentView(view2);
        //alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);





        alertDialog.show();

        cancelCard.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {

            alertDialog.dismiss();
            }

        });


        saveCard.setOnClickListener(new View.OnClickListener() {
        //@Override
        public void onClick(View v) {


            boolean isValid = true;




            final String holderName = holderNameEt.getText().toString();
            final String cardNumber = cardNumberEt.getText().toString();
            final String cvv = cvv2Et.getText().toString();

            Integer year = getInteger((year_spinner.getSelectedItem().toString()));

            Integer month = getInteger((month_spinner.getSelectedItem().toString()));


            if (!CardValidator.validateHolderName(holderName)) {
                holderNameEt.setError(myContext.getString(R.string.invalid_holder_name));
                isValid = false;

            }

            if (!CardValidator.validateNumber(cardNumber)) {
                cardNumberEt.setError(myContext.getString(R.string.invalid_card_number));
                isValid = false;
            }


            if (!CardValidator.validateCVV(cvv, cardNumber)) {
                cvv2Et.setError(myContext.getString(R.string.invalid_cvv));
                isValid = false;
            }


            if (!CardValidator.validateExpiryDate(month, year)) {
                common.showWarningDialog(myContext.getString(R.string.error), myContext.getString(R.string.invalid_expire_date), myContext);
                 isValid = false;
            }


            if (isValid) {
                alertDialog.dismiss();

                Card card = new Card();
                card.holderName(holderName);
                card.cardNumber(cardNumber);
                card.cvv2(cvv);
                card.expirationMonth(month);
                card.expirationYear(year);
                SaveCard(card);

            }


        }
    });


    }


    private void SaveCard(Card card)
    {

        final ProgressDialog _progressDialog;
        _progressDialog = ProgressDialog.show( myContext, "Espera un momento..", "Registrando Tarjetas..", true );

        openpay.createToken(card, new OperationCallBack<Token>() {


            @Override
            public void onError(OpenpayServiceException error) {
                ////

                error.printStackTrace();
                _progressDialog.dismiss();

                int desc = 0;
                String msg = null;
                switch (error.getErrorCode()) {
                    case 3001:
                        desc = R.string.declined;
                        msg = myContext.getString(desc);
                        break;
                    case 3002:
                        desc = R.string.expired;
                        msg = myContext.getString(desc);
                        break;
                    case 3003:
                        desc = R.string.insufficient_funds;
                        msg = myContext.getString(desc);
                        break;
                    case 3004:
                        desc = R.string.stolen_card;
                        msg = myContext.getString(desc);
                        break;
                    case 3005:
                        desc = R.string.suspected_fraud;
                        msg = myContext.getString(desc);
                        break;

                    case 2002:
                        desc = R.string.already_exists;
                        msg = myContext.getString(desc);
                        break;
                    default:
                        //desc = R.string.error_creating_card;
                        msg = error.getDescription();
                }


                common.showWarningDialog(myContext.getString(R.string.error), msg, myContext);


                ////
            }

            @Override
            public void onCommunicationError(ServiceUnavailableException error) {
                _progressDialog.dismiss();

                error.printStackTrace();
                common.showWarningDialog(myContext.getString(R.string.error), myContext.getString(R.string.communication_error), myContext);


            }

            @Override
            public void onSuccess(OperationResult<Token> operationResult) {
                _progressDialog.dismiss();
                Token token = operationResult.getResult();
                tokenID = token.getId();




                util _util = new util(myContext);
                _util.setSessionDeviceID(sessionID);

                String _sessionId = _util.getSessionDeviceID();

                if (_sessionId.equals(""))
                {
                    sessionID = deviceIdFragment.getDeviceId();
                }

                else
                {
                    sessionID = _sessionId;
                }


                Log.d(TAG,"tokenid" + tokenID);
                Log.d(TAG,"sessionid:" + sessionID);

                RegisterCard();

            }


        });


    }


    private void LoadCards()
    {
        util _util = new util(myContext);

        String curSuscription = String.valueOf(_util.getUserId());
        Log.d(TAG,"idsuscription:" + curSuscription);

        new loadCards(curSuscription).execute();

    }


    private void handleSentCards( HttpClient.HttpResponse response )
    {
        if( response.getCode() == 200 )
        {




            try
            {
                JSONObject json = new JSONObject( response.getResponse() );


                if (json.getInt("error") == 1)
                {
                    //common.showWarningDialog("! Invalid Data ¡", "Datos Invalidos", myContext);
                    Log.d(TAG, "No hay tarjetas asignadas");
                    return;
                }


                if (json.getInt("error") == 0) // No hay errores continuar
                {
                    JSONObject card = json.getJSONObject("message");

                    String card_number = card.getString("card_number");
                    String card_type = card.getString("brand");
                    String card_id = card.getString("car_id");

                    card_info newCard = new card_info();
                    newCard.set_brand("");
                    newCard.set_car_id(card_id);
                    newCard.set_card_number(card_number);
                    _items.add(newCard);
                    _adapter.notifyDataSetChanged();

                }








                }
            catch( Exception e )
            {
                android.util.Log.e( "JSONParser", "Cant parse: " + e.getMessage() );
                // Common.alert( this, "No se ha podido registrar, por favor intenta nuevamente más tarde." );
            }

            finally {


            }

        }
        else {
            //Common.alert( this, "No se ha podido registrar, por favor intenta nuevamente más tarde." );

        }
    }


    private class loadCards extends AsyncTask<Void, Void, HttpClient.HttpResponse>
    {
        ProgressDialog _progressDialog;
        String _idSuscripcion;

        public loadCards(String suscription)
        {
            _idSuscripcion = suscription;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            _progressDialog = ProgressDialog.show( myContext, "Espera un momento..", "Obteniendo Tarjetas..", true );


        }

        @Override
        protected HttpClient.HttpResponse doInBackground( Void... arg0 )
        {

            ContentValues params = new ContentValues();

            params.put("idSuscripcion", _idSuscripcion);



            HttpClient.HttpResponse response = HttpClient.post( common.API_URL_BASE + "getCard.php", params );
            android.util.Log.d( "TEST", String.format( "HTTP CODE: %d RESPONSE: %s", response.getCode(), response.getResponse() ) );

            return response;
        }

        @Override
        protected void onPostExecute( HttpClient.HttpResponse result )
        {
            super.onPostExecute( result );
            _progressDialog.dismiss();
            handleSentCards( result );


        }
    }

////////Register Card ///////

    private void RegisterCard()
    {
        util _util = new util(myContext);

        String curSuscription = String.valueOf(_util.getUserId());
        Log.d(TAG,"idsuscription:" + curSuscription);

       // new loadCards(curSuscription).execute();
        new registerCard(curSuscription).execute();

    }


    private void handleSentRegisterCard( HttpClient.HttpResponse response )
    {
        if( response.getCode() == 200 )
        {


            try
            {
                JSONObject json = new JSONObject( response.getResponse() );


                if (json.getInt("error") == 1)
                {
                    //common.showWarningDialog("! Invalid Data ¡", "Datos Invalidos", myContext);
                    Log.d(TAG, "No fue posible registar tarjeta");
                    return;
                }


                if (json.getInt("error") == 0) // No hay errores continuar
                {
                    Log.d(TAG, "Tarjeta registrada");

                    RequestPago();

                }








            }
            catch( Exception e )
            {
                android.util.Log.e( "JSONParser", "Cant parse: " + e.getMessage() );
                // Common.alert( this, "No se ha podido registrar, por favor intenta nuevamente más tarde." );
            }

            finally {


            }

        }
        else {
            //Common.alert( this, "No se ha podido registrar, por favor intenta nuevamente más tarde." );

        }
    }


    private class registerCard extends AsyncTask<Void, Void, HttpClient.HttpResponse>
    {
        ProgressDialog _progressDialog;
        String _idSuscripcion;

        public registerCard(String suscription)
        {
            _idSuscripcion = suscription;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            _progressDialog = ProgressDialog.show( myContext, "Espera un momento..", "Registrando Tarjeta..", true );


        }

        @Override
        protected HttpClient.HttpResponse doInBackground( Void... arg0 )
        {

            ContentValues params = new ContentValues();

            params.put("idSuscripcion", _idSuscripcion);
            params.put("tokenId", tokenID);
            params.put("sessionId", sessionID);



            HttpClient.HttpResponse response = HttpClient.post( common.API_URL_BASE + "registerCard.php", params );
            android.util.Log.d( "TEST", String.format( "HTTP CODE: %d RESPONSE: %s", response.getCode(), response.getResponse() ) );

            return response;
        }

        @Override
        protected void onPostExecute( HttpClient.HttpResponse result )
        {
            super.onPostExecute( result );
            _progressDialog.dismiss();
            handleSentRegisterCard( result );


        }
    }

////////Register Card ///////

    /////////// Payment /////

    private void RequestPago()
    {
        util _util = new util(myContext);

        String curSuscription = String.valueOf(_util.getUserId());
        Log.d(TAG,"idsuscription:" + curSuscription);

        // new loadCards(curSuscription).execute();
        new requestPago(curSuscription).execute();

    }


    private void handleSentRequestPago( HttpClient.HttpResponse response )
    {
        if( response.getCode() == 200 )
        {


            try
            {
                JSONObject json = new JSONObject( response.getResponse() );


                if (json.getInt("error") == 1)
                {
                    //common.showWarningDialog("! Invalid Data ¡", "Datos Invalidos", myContext);
                    Log.d(TAG, "No fue posible realizar el pago");
                    LoadCards();
                    String message = json.getString("message");

                    if (message.equals("SUSCRIPCION activa"))
                    {
                        common.showWarningDialog("Mensaje", "No realizo pago, su sucripción se encuentra activa", myContext);
                        util _util = new util(myContext);
                        _util.setSuscripcion("1");



                    }

                    return;
                }


                if (json.getInt("error") == 0) // No hay errores continuar
                {
                    Log.d(TAG, "Pago Registrado");
                    util _util = new util(myContext);
                    _util.setSuscripcion("1");


                }








            }
            catch( Exception e )
            {
                android.util.Log.e( "JSONParser", "Cant parse: " + e.getMessage() );
                // Common.alert( this, "No se ha podido registrar, por favor intenta nuevamente más tarde." );
            }

            finally {

            }

        }
        else {
            //Common.alert( this, "No se ha podido registrar, por favor intenta nuevamente más tarde." );

        }
    }


    private class requestPago extends AsyncTask<Void, Void, HttpClient.HttpResponse>
    {
        ProgressDialog _progressDialog;
        String _idSuscripcion;

        public requestPago(String suscription)
        {
            _idSuscripcion = suscription;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            _progressDialog = ProgressDialog.show( myContext, "Espera un momento..", "Generando Pago..", true );


        }

        @Override
        protected HttpClient.HttpResponse doInBackground( Void... arg0 )
        {

            ContentValues params = new ContentValues();

            params.put("idSuscripcion", _idSuscripcion);



            HttpClient.HttpResponse response = HttpClient.post( common.API_URL_BASE + "requestPago.php", params );
            android.util.Log.d( "TEST", String.format( "HTTP CODE: %d RESPONSE: %s", response.getCode(), response.getResponse() ) );

            return response;
        }

        @Override
        protected void onPostExecute( HttpClient.HttpResponse result )
        {
            super.onPostExecute( result );
            _progressDialog.dismiss();
            handleSentRequestPago( result );

        }
    }

    /////////// Payment /////


////////// Eliminar tarjeta

    private void eliminarTarjeta(String car_id)
    {
        util _util = new util(myContext);

        String curSuscription = String.valueOf(_util.getUserId());
        Log.d(TAG,"idsuscription:" + curSuscription);

        // new loadCards(curSuscription).execute();
        new requestPago(curSuscription).execute();

    }


    private void handleSentDeleteCard( HttpClient.HttpResponse response )
    {
        if( response.getCode() == 200 )
        {


            try
            {
                JSONObject json = new JSONObject( response.getResponse() );


                if (json.getInt("error") == 1)
                {
                    //common.showWarningDialog("! Invalid Data ¡", "Datos Invalidos", myContext);
                    Log.d(TAG, "No fue posible Eliminar tarjeta");
                    return;
                }


                if (json.getInt("error") == 0) // No hay errores continuar
                {
                    Log.d(TAG, "Tarjeta Eliminada");
                    _items.clear();
                    _adapter.notifyDataSetChanged();

                    //LoadCards();

                }








            }
            catch( Exception e )
            {
                android.util.Log.e( "JSONParser", "Cant parse: " + e.getMessage() );
                // Common.alert( this, "No se ha podido registrar, por favor intenta nuevamente más tarde." );
            }

            finally {


            }

        }
        else {
            //Common.alert( this, "No se ha podido registrar, por favor intenta nuevamente más tarde." );

        }
    }


    private class deleteCard extends AsyncTask<Void, Void, HttpClient.HttpResponse>
    {
        ProgressDialog _progressDialog;
        String _idSuscripcion;
        String _carid;

        public deleteCard(String suscription, String car_id)
        {
            _idSuscripcion = suscription;
            _carid = car_id;

        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            _progressDialog = ProgressDialog.show( myContext, "Espera un momento..", "Eliminando tarjeta..", true );


        }

        @Override
        protected HttpClient.HttpResponse doInBackground( Void... arg0 )
        {

            ContentValues params = new ContentValues();

            params.put("idSuscripcion", _idSuscripcion);
            params.put("car_id", _carid);



            HttpClient.HttpResponse response = HttpClient.post( common.API_URL_BASE + "deleteCard.php", params );
            android.util.Log.d( "TEST", String.format( "HTTP CODE: %d RESPONSE: %s", response.getCode(), response.getResponse() ) );

            return response;
        }

        @Override
        protected void onPostExecute( HttpClient.HttpResponse result )
        {
            super.onPostExecute( result );
            _progressDialog.dismiss();
            handleSentDeleteCard( result );

        }
    }


    ///////////



    private Integer getInteger(final String number) {
        try {
            return Integer.valueOf(number);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

}

