package audu.app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import audu.app.R;

public class splash extends AppCompatActivity {
    private Long duration = 3000L;
    private ImageView logo_a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo_a = (ImageView) findViewById(R.id.a_image);

        AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(1000);
        animation1.setStartOffset(500);
        animation1.setFillAfter(true);
        logo_a.startAnimation(animation1);


        //scheduleSplashScreen();
    }


    private void scheduleSplashScreen()
    {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!checkUser()) {
                    startActivity(new Intent(getApplicationContext(), login.class));
                    finish();
                }

                else
                {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }





            }
        }, duration); //3000 L = 3 detik


    }

    private boolean checkUser()
    {
      //  SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences( this );

      //  Integer user_id = sharedPref.getInt(common.VAR_USER_ID, 0);

       // if (user_id !=0 ) return true;
      //  else return  false;

        return true;
    }


}
