package audu.app.activities;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

import audu.app.R;
import audu.app.common;
import audu.app.models.User_Settings;
import audu.app.util;
import audu.app.utils.HttpClient;

public class register extends AppCompatActivity {


    private EditText txtNombre;
    private EditText txtEmail;
    private EditText txtPass;
    private EditText txtConfirPass;

    private Button btnRegistro;
    private Button btnLogin;

    private String TAG = "Register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        txtNombre = (EditText) findViewById(R.id.txtRegistroNombre);
        txtEmail = (EditText) findViewById(R.id.txtRegistroEmail);
        txtPass = (EditText) findViewById(R.id.txtRegistroPassword);
        txtConfirPass = (EditText) findViewById(R.id.txtRegistroConfirmarPassword);

        btnRegistro = (Button) findViewById(R.id.btnRegistro);
        btnLogin = (Button) findViewById(R.id.btnRegistroInicioSession);


        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Registrar ");

             checkValues();

            }

        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Iniciar ");

                startActivity(new Intent(getApplicationContext(), login.class));
                finish();

            }

        });

    }

        private void checkValues()
        {

            String email = txtEmail.getText().toString();
            String pass1 = txtPass.getText().toString();
            String pass2 = txtConfirPass.getText().toString();
            String nombre = txtNombre.getText().toString();


            if (nombre.length()<5)
            {
                common.showWarningDialog("! Advertencia ¡", "El nombre debe de contener al menos 5 caracteres..", this);
                return;
            }

            Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(email);
            if (matcher.matches()) {

                if (pass1.length() > 5 && pass1.length() < 17)
                {

                    if (pass1.equals(pass2))
                    {
                        /// Registrar al usuario
                        new RegisterUser(nombre,pass1,email).execute();

                    }
                    else
                    {
                        common.showWarningDialog("! Advertencia ¡", "Las contraseñas no coinciden...", this);
                    }



                }
                else
                {
                    common.showWarningDialog("! Advertencia ¡", "La contraseña debe de ser al menos de 6 caracteres y menor a 18 caracteres.", this);
                }


            }
            else
            {
                common.showWarningDialog("! Advertencia ¡", "La dirección de correo no es valida..", this);
            }


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
                    common.showWarningDialog("! Usuario no válido ¡", "Favor de confirmar su usuario o contraseña", this);
                    return;
                }

            //    if (json.getInt("activo") == 1)// Suscripcion Activa
           //     {
           //     }

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
                    String curPass = txtPass.getText().toString();

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



    private class RegisterUser extends AsyncTask<Void, Void, HttpClient.HttpResponse>
    {
        ProgressDialog _progressDialog;
        String _nombre;
        String _password;
        String _email;
        String _activo;
        String _fecha_inicio;
        String _fecha_termino;

        public RegisterUser( String nombre, String password, String email )
        {
            _nombre = nombre;
            _password = password;
            _email = email;
            _activo = "T";

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = simpleDateFormat.format(new Date());
            _fecha_inicio = format;
            _fecha_termino = format;

        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            _progressDialog = ProgressDialog.show( register.this, "Espera un momento..", "Registro de usuarios..", true );


        }

        @Override
        protected HttpClient.HttpResponse doInBackground( Void... arg0 )
        {

            ContentValues params = new ContentValues();

            params.put("nombre_usuario", _nombre);
            params.put("email_usuario", _email);
            params.put("password", _password);
            params.put("fecha_inicio", _fecha_inicio);
            params.put("fecha_termino", _fecha_termino);
            params.put("activo", _activo);


            HttpClient.HttpResponse response = HttpClient.post( common.API_URL_BASE + "registerSuscripcion.php", params );
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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), login.class));
        finish();
    }

}
