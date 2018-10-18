package audu.app.activities;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import audu.app.R;
import audu.app.common;
import audu.app.utils.soundWaves;


public class splash extends AppCompatActivity {
    private Long duration = 5500L;

    private RelativeLayout a_relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        a_relativeLayout = (RelativeLayout) findViewById(R.id.a_relativeLayout);



        final ImageView a_logo = new ImageView(this);
        a_logo.setImageResource(R.drawable.logo_a);

        final ImageView head1 = new ImageView(this);
        head1.setImageResource(R.drawable.logo_head);

        final ImageView head2 = new ImageView(this);
        head2.setImageResource(R.drawable.logo_head);

        final ImageView head3 = new ImageView(this);
        head3.setImageResource(R.drawable.logo_head);

        final soundWaves sounds = new soundWaves(this);

        final ImageView udu_logo = new ImageView(this);
        udu_logo.setImageResource(R.drawable.logo_last);



        float curScreenWidht = common.getScreenWidthPixels(this);
        float curScreenHeight = common.getScreenHeightPixels(this);

        final float curWidthA = curScreenWidht * 0.20f;
        float curHead1Width = curScreenWidht * 0.45f;
        float curHead2Width = curScreenWidht * 0.38f;
        float curHead3Width = curScreenWidht * 0.32f;

        float curLogoWidth = curScreenWidht - curHead3Width;
        float curLogoHeight = curWidthA;

        float curSoundWidth = curScreenWidht * 0.10f;

        // Image Logo A
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) curWidthA, (int) curWidthA);

        float posx = (curScreenWidht - curWidthA) /2 ;
        float posy = (curScreenHeight - curWidthA) /2;
        float posLeft = posx + curWidthA;


        params.leftMargin = (int) posx;
        params.topMargin = (int) posy;

        a_relativeLayout.addView(a_logo, params);

        // Image Logo Head 1
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams((int) curHead1Width, (int) curHead1Width);

        float posx1 = (curScreenWidht - curHead1Width) /2 ;
        float posy1 = (curScreenHeight - curHead1Width) /2;

        params1.leftMargin = (int) posx1;
        params1.topMargin = (int) posy1-60;

        a_relativeLayout.addView(head1, params1);


        // Image Logo Head 2
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams((int) curHead2Width, (int) curHead2Width);

        float posx2 = (curScreenWidht - curHead2Width) /2 ;
        float posy2 = (curScreenHeight - curHead2Width) /2;

        params2.leftMargin = (int) posx2;
        params2.topMargin = (int) posy2-52;

        a_relativeLayout.addView(head2, params2);


        // Image Logo Head 3
        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams((int) curHead3Width, (int) curHead3Width);

        float posx3 = (curScreenWidht - curHead3Width) /2 ;
        float posy3 = (curScreenHeight - curHead3Width) /2;

        params3.leftMargin = (int) posx3;
        params3.topMargin = (int) posy3-48;

        a_relativeLayout.addView(head3, params3);


    // Sound Waves
        RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams((int) curSoundWidth, (int) curSoundWidth);

        float posx4 = (curScreenWidht - curSoundWidth) /2 ;
        float posy4 = (curScreenHeight - curSoundWidth) /2;

        params4.leftMargin = (int) posx4;
        params4.topMargin = (int) posy4;

        a_relativeLayout.addView(sounds, params4);

// Image Logo Audu
        RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams((int) curLogoWidth, (int) curLogoHeight);

        float posx5 = (curScreenWidht - curLogoWidth) /2 ;
        float posy5 = (curScreenHeight - curLogoHeight) /2;

        params5.leftMargin = (int) posx5;
        params5.topMargin = (int) posy5-20;

        a_relativeLayout.addView(udu_logo, params5);




        // Animacion de la A
        AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(1000);
        animation1.setStartOffset(500);
        animation1.setFillEnabled(true);
        animation1.setFillAfter(true);
        //a_logo.startAnimation(animation1);


        //Animación del auricualar 1
        AlphaAnimation animation2 = new AlphaAnimation(0.0f, 0.6f);
        animation2.setDuration(500);
        animation2.setStartOffset(1500);
        animation2.setInterpolator(new DecelerateInterpolator());
        animation2.setFillEnabled(true);
        animation2.setFillAfter(true);
        //head1.startAnimation(animation2);

        AlphaAnimation animation3 = new AlphaAnimation(0.6f, 0.0f);
        animation3.setDuration(500);
        animation3.setStartOffset(2000);
        animation3.setInterpolator(new AccelerateInterpolator());
        animation3.setFillEnabled(true);
        animation3.setFillAfter(true);
        //head1.startAnimation(animation3);

        animation3.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
               head1.setVisibility(View.INVISIBLE);
            }
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }
        });


        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(animation2);
        animationSet.addAnimation(animation3);
        head1.startAnimation(animationSet);

        //Animación del auricualar 2

        AlphaAnimation animation4 = new AlphaAnimation(0.0f, 0.6f);
        animation4.setDuration(500);
        animation4.setStartOffset(2000);
        animation4.setInterpolator(new DecelerateInterpolator());
        animation4.setFillEnabled(true);
        animation4.setFillAfter(true);
        //head1.startAnimation(animation2);

        AlphaAnimation animation5 = new AlphaAnimation(0.6f, 0.0f);
        animation5.setDuration(500);
        animation5.setStartOffset(2500);
        animation5.setInterpolator(new AccelerateInterpolator());
        animation5.setFillEnabled(true);
        animation5.setFillAfter(true);
        //head1.startAnimation(animation3);

        animation5.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                head2.setVisibility(View.INVISIBLE);
            }
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }
        });


        AnimationSet animationSet2 = new AnimationSet(false);
        animationSet2.addAnimation(animation4);
        animationSet2.addAnimation(animation5);
        head2.startAnimation(animationSet2);

        //Animación del auricualar 3

        AlphaAnimation animation6 = new AlphaAnimation(0.0f, 1.0f);
        animation6.setDuration(500);
        animation6.setStartOffset(2500);
        animation6.setInterpolator(new DecelerateInterpolator());
        animation6.setFillEnabled(true);
        animation6.setFillAfter(true);
        //head3.startAnimation(animation6);


        //Animación de sound Waves

        AlphaAnimation animation7 = new AlphaAnimation(0.0f, 1.0f);
        animation7.setDuration(500);
        animation7.setStartOffset(3000);
        animation7.setInterpolator(new DecelerateInterpolator());
        animation7.setFillEnabled(true);
        animation7.setFillAfter(true);
        //sounds.startAnimation(animation6);


        //Animación de logoAudu

        AlphaAnimation animation8 = new AlphaAnimation(0.0f, 1.0f);
        animation8.setDuration(500);
        animation8.setStartOffset(4500);
        animation8.setInterpolator(new DecelerateInterpolator());
        animation8.setFillEnabled(true);
        animation8.setFillAfter(true);
        //sounds.startAnimation(animation6);


        final float amountToMoveRight = 20f;

        //move to the Right
        TranslateAnimation anim = new TranslateAnimation(0, amountToMoveRight, 0, 0);
        anim.setDuration(500);
        anim.setStartOffset(4000);
        anim.setFillEnabled(true);
        anim.setFillAfter(true);

        anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation)
            {

                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) a_logo.getLayoutParams();
                RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) head3.getLayoutParams();
                RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) sounds.getLayoutParams();
                //params.topMargin += amountToMoveDown;
                params1.leftMargin += amountToMoveRight;
                params2.leftMargin += amountToMoveRight;
                params3.leftMargin += amountToMoveRight;

                a_logo.setLayoutParams(params1);
                head3.setLayoutParams(params2);
                sounds.setLayoutParams(params3);

            }
        });



// Logo Move Right

        final float amountToMoveRightLogo =  curWidthA*2 - posx5;

        //move to the Right
        TranslateAnimation anim2 = new TranslateAnimation(0, amountToMoveRightLogo, 0, 0);
        anim2.setDuration(1000);
        anim2.setStartOffset(4500);
        //anim2.setFillEnabled(true);
        //anim2.setFillAfter(true);

        anim2.setAnimationListener(new TranslateAnimation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation)
            {

                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) udu_logo.getLayoutParams();
                //params1.leftMargin += amountToMoveRightLogo;
                params1.leftMargin = (int) curWidthA *2;

                udu_logo.setLayoutParams(params1);


            }
        });







        //a_logo.startAnimation(anim);
        //head3.startAnimation(anim);
        //sounds.startAnimation(anim);

        AnimationSet animationSet1 = new AnimationSet(false);
        animationSet1.addAnimation(animation1);
        animationSet1.addAnimation(anim);
        //animationSet1.addAnimation(anim2);
        a_logo.startAnimation(animationSet1);

        AnimationSet animationSet3 = new AnimationSet(false);
        animationSet3.addAnimation(animation6);
        animationSet3.addAnimation(anim);
        //animationSet3.addAnimation(anim2);
        head3.startAnimation(animationSet3);


        AnimationSet animationSet4 = new AnimationSet(false);
        animationSet4.addAnimation(animation7);
        animationSet4.addAnimation(anim);
        //animationSet4.addAnimation(anim2);
        sounds.startAnimation(animationSet4);


        AnimationSet animationSet5 = new AnimationSet(false);
        animationSet5.addAnimation(animation8);
        animationSet5.addAnimation(anim2);
        //animationSet4.addAnimation(anim2);
        udu_logo.startAnimation(animationSet5);



        RelativeLayout.LayoutParams paramsHead = (RelativeLayout.LayoutParams) head3.getLayoutParams();
        int curHeadPosX = paramsHead.leftMargin;

        ValueAnimator animator = ValueAnimator.ofInt(curHeadPosX, (int) curWidthA-20);
        //animator.setValues(propertyRadius);
        animator.setDuration(1000);
        animator.setStartDelay(4500);
        animator.setInterpolator(new AccelerateInterpolator());


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //curPercent = (float) animation.getAnimatedValue(PROPERTY_SCALE);

                int curPos = (int) animation.getAnimatedValue();

                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) a_logo.getLayoutParams();
                RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) head3.getLayoutParams();
                RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) sounds.getLayoutParams();
                //params.topMargin += amountToMoveDown;

                int APosX = params1.leftMargin;
                int headPosX = params2.leftMargin;
                int soundPosx = params3.leftMargin;

                int delta1 = APosX - headPosX;
                int delta2 = soundPosx -headPosX;

                params1.leftMargin = curPos + delta1;
                params2.leftMargin = curPos;
                params3.leftMargin = curPos + delta2;

                a_logo.setLayoutParams(params1);
                head3.setLayoutParams(params2);
                sounds.setLayoutParams(params3);



            }
        });
        animator.start();



        scheduleSplashScreen();
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
                    startActivity(new Intent(getApplicationContext(), intro.class));
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
