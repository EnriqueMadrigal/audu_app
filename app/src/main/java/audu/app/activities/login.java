package audu.app.activities;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import audu.app.R;
import audu.app.common;
import audu.app.utils.HttpClient;

public class login extends AppCompatActivity {

    private Button RegistroButton;
    private Button LoginButton;

    private EditText loginUser;
    private EditText passwordUser;
private String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        RegistroButton = findViewById(R.id.btnLoginRegistro);
        LoginButton = (Button) findViewById(R.id.btnLoginIniciar);

        loginUser = (EditText) findViewById(R.id.txtLoginEmail);
        passwordUser = (EditText) findViewById(R.id.txtLoginPassword);



        RegistroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Registro");

                startActivity(new Intent(getApplicationContext(), register.class));
                finish();

            }

        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Registro");

           LoginUser();
            }

        });

    }


    private void LoginUser() {

        if( common.haveInternetPermissions(this, "Login") ) // Revisar permisos de internet
        {

            if (common.isOnline(this)) // Revisar si tenemos conexión
            {

                String curPassword = passwordUser.getText().toString();
                String curUserName = loginUser.getText().toString();


                new UserLogin( curUserName, curPassword).execute();
                //Intent intent = new Intent();
                //intent.setClass(context, presentacion.class);
                //finish();
                //startActivity(intent);

            }

            else
            {
                common.showWarningDialog("! Sin conexión ¡", "Favor de revisar su conexión de Datos..", this);
                //alertDialog.dismiss();
            }

        }


    }


    private void handleSent( HttpClient.HttpResponse response )
    {
        if( response.getCode() == 200 )
        {
            try
            {
                JSONObject json = new JSONObject( response.getResponse() );
                Log.d("LOGin", "resultado");



                if (json.getInt("error") == 1)
                {
                    common.showWarningDialog("! Usuario no válido ¡", "Favor de confirmar su usuario o contraseña", this);
                    return;
                }

                if (json.getInt("activo") == 1)// Suscripcion Activa
                {

                }

                if (json.getInt("error") == 0) // No hay errores continuar
                {
                    JSONObject user = json.getJSONObject( "message" );
                   // Integer UserId = user.getInt("id");
                   // Integer permiso = user.getInt("permiso");

                    /*
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences( this );
                    SharedPreferences.Editor editor = sharedPref.edit();

                    editor.putInt(common.VAR_USER_ID, UserId);
                    editor.putInt(common.VAR_USER_PERMISOS, permiso);
                    editor.putString(common.VAR_USER_NAME, nombre);
                    editor.putString(common.VAR_USER_APELLIDOS, apellidos);
                    editor.commit();

                    */

                    //Log.d("Login",nombre);
                    //Log.d("Login",apellidos);

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                }


                /*
                if( json.getString( "user" ).equals( "not_exists" ) )
                    Common.alert( this, "Vendedor no existente." );
                else if( json.getString( "user" ).equals( "already_registered" ) )
                    Common.alert( this, "Vendedor ya está registrado." );
                else
                {
                    JSONObject user = json.getJSONObject( "user" );
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences( this );
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString( Common.VAR_USER_UID, user.getString( "uid" ) );
                    editor.putString( Common.VAR_USER_NAME, user.getString( "name" ) );
                    editor.putString( Common.VAR_USER_EMPLOYEE_NO, user.getString( "employee_no" ) );
                    editor.putString( Common.VAR_USER_SHOP_ID, user.getString( "shop_id" ) );
                    editor.putString( Common.VAR_USER_SHOP_NAME, user.getString( "shop_name" ) );
                    editor.putString( Common.VAR_USER_SHOP_NUMBER, user.getString( "shop_number" ) );
                    editor.putString( Common.VAR_USER_SHOP_RETAILER_ID, user.getString( "retailer_id" ) );
                    editor.putString( Common.VAR_USER_SHOP_RETAILER_NAME, user.getString( "retailer_name" ) );
                    editor.commit();

                    Intent intent = new Intent( this, MainActivity.class );
                    intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                    intent.addFlags( Intent.FLAG_ACTIVITY_NO_ANIMATION );
                    intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK );
                    startActivity( intent );


                }
                */
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



    private class UserLogin extends AsyncTask<Void, Void, HttpClient.HttpResponse>
    {
        ProgressDialog _progressDialog;
        String _user;
        String _password;

        public UserLogin( String user, String password )
        {
            _user = user;
            _password = password;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            _progressDialog = ProgressDialog.show( login.this, "Espera un momento..", "Registro de usuarios..", true );


        }

        @Override
        protected HttpClient.HttpResponse doInBackground( Void... arg0 )
        {

            ContentValues params = new ContentValues();

            params.put("email_usuario", _user);
            params.put("password", _password);

            HttpClient.HttpResponse response = HttpClient.post( common.API_URL_BASE + "getSuscripcion.php", params );
            android.util.Log.d( "TEST", String.format( "HTTP CODE: %d RESPONSE: %s", response.getCode(), response.getResponse() ) );

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
