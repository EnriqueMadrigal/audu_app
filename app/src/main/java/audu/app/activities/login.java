package audu.app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import audu.app.R;

public class login extends AppCompatActivity {

    private Button RegistroButton;
private String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        RegistroButton = findViewById(R.id.btnLoginRegistro);


        RegistroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Registro");

                startActivity(new Intent(getApplicationContext(), register.class));
                finish();

            }

        });



    }
}
