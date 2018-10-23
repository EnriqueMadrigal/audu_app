package audu.app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import audu.app.R;

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



    }
}
