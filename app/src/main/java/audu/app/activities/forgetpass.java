package audu.app.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import audu.app.R;


public class forgetpass extends AppCompatActivity {



    private Button ingresar;
    private Button solicitar;
    private Button inicio;

    private static String TAG = "ForgetPass";

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.activity_forgetpass);

        ingresar = (Button) findViewById(R.id.btnIngresar);
        solicitar = (Button) findViewById(R.id.btnSolicitarPass);
        inicio = (Button) findViewById(R.id.btnForgetInicioSession);

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Ingresar");


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("El codigo proporcionando no es válido.")
                        .setTitle("! Advertencia ¡")
                        .setCancelable(false)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }

        });


        solicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Ingresar");


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Se enviara un código de vericación a la cuenta de correo.")
                        .setTitle("! Mensaje ¡")
                        .setCancelable(false)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }

        });


        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Ingresar");
                startActivity(new Intent(getApplicationContext(), login.class));
                finish();

            }

        });



    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), login.class));
        finish();
    }


}
