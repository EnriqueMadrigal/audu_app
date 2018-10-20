package audu.app.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import audu.app.R;
import audu.app.adapters.MyPageAdapter;
import audu.app.fragments.banner_image_class_resource;
import audu.app.utils.AutoResizeTextView;
import audu.app.utils.BoxIndicator;

public class intro extends AppCompatActivity {

    private ViewPager mPager;
    private MyPageAdapter myPageAdapter;
    private List<Fragment> fList;

    private Context context;
    private static String TAG = "Intro";

    private Button firstButton;
    private Button lastButton;
    private int currentPage=0;


    private AutoResizeTextView msg1;
    private AutoResizeTextView msg2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        context = this;

        mPager = (ViewPager) findViewById(R.id.ViewPager);
        firstButton = (Button) findViewById(R.id.firstButton);
        lastButton = (Button) findViewById(R.id.lastButton);
        msg1 = (AutoResizeTextView) findViewById(R.id.introMsg1);
        msg2 = (AutoResizeTextView) findViewById(R.id.introMsg2);


        fList = new ArrayList<Fragment>();
        //  fList.add(banner_image_class.newInstance(R.drawable.banner1));
        //  fList.add(banner_image_class.newInstance(R.drawable.banner2));

        myPageAdapter = new MyPageAdapter(getSupportFragmentManager(), fList);


        mPager.setAdapter(myPageAdapter);
        mPager.setCurrentItem(0);
        myPageAdapter.notifyDataSetChanged();
        setPage1Strings();

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                // Check if this is the page you want.
                Log.d(TAG,String.valueOf(position));
                currentPage = position;
                if (currentPage == 0)
                {
                    setPage1Strings();
                }

                else if (currentPage ==1)
                {
                    setPage2Strings();
                }

            }
        });

        BoxIndicator indicator = (BoxIndicator) findViewById( R.id.BoxIndicator );
        indicator.setViewPager( mPager );


        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "first");

                if (currentPage==0)
                {
                    startActivity(new Intent(getApplicationContext(), login.class));
                    finish();
                }

                currentPage = 0;
                setPage1Strings();
                mPager.setCurrentItem(0);


            }

            });

        lastButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Log.d(TAG, "last");
        setPage2Strings();
        if (currentPage==1)
        {
            startActivity(new Intent(getApplicationContext(), login.class));
            finish();
        }

        currentPage = 1;
        mPager.setCurrentItem(1);
    }

});

                LoadImages();
    }

    private void LoadImages()
    {

        fList.add(banner_image_class_resource.newInstance(R.drawable.a));
        fList.add(banner_image_class_resource.newInstance(R.drawable.c));


        myPageAdapter.notifyDataSetChanged();



    }

    public void setPage1Strings()
    {
        firstButton.setText("Saltar");
        lastButton.setText("Siguiente");

        msg1.setText("¡ TE DAMOS LA BIENVENIDA !");
        msg2.setText("Somos la plataforma que lleva la \n magia de las historias a tus oidos.");

    }

    public void setPage2Strings()
    {
        firstButton.setText("Anterior");
        lastButton.setText("Omitir");

        msg1.setText("Encuentra lo que mas te guste");
        msg2.setText("¡ El primer capítulo es gratis!\n Prueba lo que quieras y \n encuentra tu historia favorita");
    }

}
