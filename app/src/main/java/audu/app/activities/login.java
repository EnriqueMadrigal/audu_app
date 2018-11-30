package audu.app.activities;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import audu.app.R;
import audu.app.common;
import audu.app.models.User_Settings;
import audu.app.util;
import audu.app.utils.HttpClient;
import com.facebook.login.widget.LoginButton;

public class login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private Button RegistroButton;
    private Button LoginButton;
    private Button OlvidarButton;
    private ImageButton LoginGoogle;
    private ImageButton LoginFacebook;
    private LoginButton loginButton;

    private EditText loginUser;
    private EditText passwordUser;

    private GoogleApiClient mGoogleApiClient;
    CallbackManager callbackManager;

    private Context context;

private String TAG = "Login";
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.activity_login);

        RegistroButton = (Button) findViewById(R.id.btnLoginRegistro);
        LoginButton = (Button) findViewById(R.id.btnLoginIniciar);
        OlvidarButton = (Button) findViewById(R.id.btnLoginOlvidar);

        loginUser = (EditText) findViewById(R.id.txtLoginEmail);
        passwordUser = (EditText) findViewById(R.id.txtLoginPassword);
        LoginGoogle = (ImageButton) findViewById(R.id.buttonLoginGoogle);
        LoginFacebook = (ImageButton) findViewById(R.id.buttonLoginFacebook);


        //// Google

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        LoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Registro Google");

                signIn();

            }

        });

        RegistroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Registro");

                startActivity(new Intent(getApplicationContext(), register.class));
                finish();

            }

        });


        OlvidarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Olvidar");

                startActivity(new Intent(getApplicationContext(), forgetpass.class));
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

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email","public_profile");
        loginButton.setText("Ingresar con Facebook");
        loginButton.setHeight(40);

        LoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Facebook");

                loginButton.performClick();
            }

        });

        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String accessToken = loginResult.getAccessToken().getToken();


                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject,
                                                    GraphResponse response) {
                                Log.d(TAG, "Facebook graph response: " + response.toString());
                                Log.v("json", "Obtenido");

                                //////
                                try {
                                    String email = jsonObject.getString("email");
                                    String nombre = jsonObject.getString("name");
                                    //String birthday = object.getString("birthday"); // 01/31/1980 format
                                   // saveEmail(email);
                                   // setLoginType("facebook");
                                   // new SyncTask( email,nombre,"facebook" ).execute();

                                    //Intent intent = new Intent();
                                    //intent.setClass(context, presentacion.class);
                                   // finish();
                                   // startActivity(intent);

                                    util Util = new util(context);
                                    Util.setLoginType("facebook");

                                    new UserLoginGoogle(email,nombre).execute();

                                }


                                catch (Exception e)
                                {
                                    Log.d(TAG,"Can not read json facebook");
                                    //return null;

                                }
                                /////

                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
                Log.v("LoginActivity", "cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.v("LoginActivity", exception.getCause().toString());
            }


        });





    }



    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        // Si es de Facebook
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //       mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getEmail()));
            //  updateUI(true);
            String email = acct.getEmail();
            String name = acct.getGivenName() + " " + acct.getFamilyName();
            Log.d(TAG,email);
          //  saveEmail(acct.getEmail());
          //  saveUserName(acct.getGivenName() + " " + acct.getFamilyName());
         //   setLoginType("google");
            Log.d(TAG,name);

            util Util = new util(context);
            Util.setLoginType("google");

            new UserLoginGoogle(email,name).execute();

         //   new SyncTask( acct.getEmail(), acct.getGivenName(),"android" ).execute();
         //   Intent intent = new Intent();
         //   intent.setClass(this, presentacion.class);
         //   finish();
         //   startActivity(intent);



        } else {
            // Signed out, show unauthenticated UI.
            // updateUI(false);
            showInternetErrorDialog();
        }
    }




    protected void showInternetErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Error: \nSin acceso a Internet. \nComprueba la conexión de red y  vuelve a Intentarlo")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    //////////////
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

                    Integer Suscripcion = user.getInt("idSuscripcion");
                    String nombreUsuario = user.getString("nombreUsuario");
                    String emailUsuario = user.getString("emailUsuario");
                    String fechaRegistro = user.getString("fechaRegistro");


                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date convertedDate = new Date();
                    try {
                        convertedDate = simpleDateFormat.parse(fechaRegistro);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        convertedDate = new Date();
                    }


                    User_Settings newUserSettings = new User_Settings();
                    String curPass = passwordUser.getText().toString();

                    newUserSettings.set_name(nombreUsuario);
                    newUserSettings.set_email(emailUsuario);
                    newUserSettings.set_userid(Suscripcion);
                    newUserSettings.set_start_date(convertedDate);
                    newUserSettings.set_pass(curPass);

                    util Util = new util(this);
                    Util.saveUserSetting(newUserSettings);
                    Util.setUserName(nombreUsuario);
                    Util.setUserId(Suscripcion);
                    Util.userHasLogged();
                    Util.setSuscripcion("0");
                    Util.setLoginType("email");

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




    private void handleSentSocial( HttpClient.HttpResponse response )
    {
        if( response.getCode() == 200 )
        {
            try
            {
                JSONObject json = new JSONObject( response.getResponse() );
                Log.d("LOGin", "resultado");
                String Suscription = "0";


                if (json.getInt("error") == 1)
                {
                    common.showWarningDialog("! Usuario no válido ¡", "Favor de confirmar su usuario o contraseña", this);
                    return;
                }

                if (json.getString("activo") == "1")// Suscripcion Activa
                {
                    Suscription = "1";
                }

                if (json.getInt("error") == 0) // No hay errores continuar
                {
                    JSONObject user = json.getJSONObject( "message" );

                    Integer Suscripcion = user.getInt("idSuscripcion");
                    String nombreUsuario = user.getString("nombreUsuario");
                    String emailUsuario = user.getString("emailUsuario");
                    String fechaRegistro = user.getString("fechaRegistro");


                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date convertedDate = new Date();
                    try {
                        convertedDate = simpleDateFormat.parse(fechaRegistro);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        convertedDate = new Date();
                    }


                    User_Settings newUserSettings = new User_Settings();
                    String curPass = passwordUser.getText().toString();

                    newUserSettings.set_name(nombreUsuario);
                    newUserSettings.set_email(emailUsuario);
                    newUserSettings.set_userid(Suscripcion);
                    newUserSettings.set_start_date(convertedDate);
                    newUserSettings.set_pass("");

                    util Util = new util(this);
                    Util.saveUserSetting(newUserSettings);
                    Util.setUserName(nombreUsuario);
                    Util.setUserId(Suscripcion);
                    Util.userHasLogged();
                    Util.setSuscripcion(Suscription);
                   // Util.setLoginType("google");

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



    private class UserLoginGoogle extends AsyncTask<Void, Void, HttpClient.HttpResponse>
    {
        ProgressDialog _progressDialog;
        String _user;
        String _nombre;

        public UserLoginGoogle( String user, String nombre )
        {
            _user = user;
            _nombre = nombre;
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
            params.put("nombre_usuario", _nombre);

            HttpClient.HttpResponse response = HttpClient.post( common.API_URL_BASE + "registerSuscripcionSocial.php", params );
            android.util.Log.d( "TEST", String.format( "HTTP CODE: %d RESPONSE: %s", response.getCode(), response.getResponse() ) );

            return response;
        }

        @Override
        protected void onPostExecute( HttpClient.HttpResponse result )
        {
            super.onPostExecute( result );
            _progressDialog.dismiss();
            handleSentSocial( result );


        }
    }






    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
