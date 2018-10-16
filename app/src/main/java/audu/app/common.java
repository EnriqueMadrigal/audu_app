package audu.app;
import android.content.Context;
import android.util.DisplayMetrics;

public class common {


    public static float getScreenWidthPixels(Context context)
    {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        //float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        //float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        return (float) displayMetrics.widthPixels;
    }



    public static float getScreenHeightPixels(Context context)
    {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        //float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        //float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        return (float) displayMetrics.heightPixels;
    }


    public static float getScreenWidthDP(Context context)
    {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        //float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        //float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        return (float) displayMetrics.widthPixels / displayMetrics.density;
    }



    public static float getScreenHeightDP(Context context)
    {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        //float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        //float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        return (float) displayMetrics.heightPixels / displayMetrics.density;
    }



}
