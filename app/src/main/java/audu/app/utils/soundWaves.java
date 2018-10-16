package audu.app.utils;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

public class soundWaves extends View {

    private Paint rectPaint;
    private int NumWaves = 6;

    private int barSpaces = NumWaves /2;
    private int totSpaces = NumWaves + barSpaces;

    private float barWidth = 0;
    private float spaceWidth = 0;

    private static  String PROPERTY_SCALE = "property_Scale";
    private static  String PROPERTY_DIRECTION = "property_direction";

    //private float[] maxNumbers = {9,20,12,19,26,7};
    private float[] maxNumbers = {0.30f,0.66f,0.40f,0.63f,0.86f,0.23f};
    private float[] directions = {0.02f,0.02f,0.02f,0.02f,0.02f,0.02f};
     private float curPercent= 0.99f;

public soundWaves(android.content.Context context){
        this(context,null);
    init(context, null);

        }

public soundWaves(Context context, android.util.AttributeSet attrs){
        this(context,attrs,0);
        }

public soundWaves(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);

        }


    @Override
    protected void onDraw (Canvas canvas) {
        drawBars(canvas);
    }

    private void init(Context context, AttributeSet attrs) {
        setSaveEnabled(true);

        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setColor(Color.WHITE);

        //PropertyValuesHolder propertyRadius = PropertyValuesHolder.ofFloat(PROPERTY_SCALE, 0.01f, 0.99f);


        ValueAnimator animator = ValueAnimator.ofInt(0,7000);
        //animator.setValues(propertyRadius);
        animator.setDuration(10000);
        animator.setStartDelay(3000);
        animator.setInterpolator(new AccelerateInterpolator());


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //curPercent = (float) animation.getAnimatedValue(PROPERTY_SCALE);

                for(int i=0;i<NumWaves;i++) {
                maxNumbers[i] = maxNumbers[i] + directions[i];
                if (maxNumbers[i]>0.99f)
                {
                    directions[i] = -directions[i];
                }

                else if (maxNumbers[i]<0.01)
                {
                    directions[i] = -directions[i];
                }


                }
                invalidate();

            }
        });
        animator.start();



    }


    private void drawBars(Canvas canvas)
    {

        //Rect r = new Rect(0,0,getWidth(),getHeight());
        //canvas.drawRect(r , rectPaint);
        getValues();
        float height = (float) getHeight();

        float posx = 0f;
        for(int i=0;i<NumWaves;i++)
        {


            float curHeight = getHeight() * maxNumbers[i];
            float curPosy = (height - curHeight) / 2;


            RectF r = new RectF(posx, curPosy , posx + barWidth , curHeight + curPosy);
            canvas.drawRect(r,rectPaint);

            posx = posx + barWidth + spaceWidth;

        }

    }

    private float getBarCenter() {
        //position the bar slightly below the middle of the drawable area
        float barCenter = (getHeight() - getPaddingTop() - getPaddingBottom()) / 2; //this is the center
     //   barCenter += getPaddingTop() + .1f * getHeight(); //move it down a bit
        return barCenter;
    }

    private void getValues()
    {
        int intWidth = getWidth();
        float curWidth = (float) getWidth();

        barWidth =  curWidth / totSpaces;
        spaceWidth = barWidth /2;


    }
}
