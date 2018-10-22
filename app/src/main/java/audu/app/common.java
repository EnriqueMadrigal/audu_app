package audu.app;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

public class common {

    public static final String API_URL_BASE = "https://www.audu.app/dashboard/webservice/app/";


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


    public static boolean haveInternetPermissions(Context context, String TAG) {
        Set<String> required_perms = new HashSet<String>();
        required_perms.add("android.permission.INTERNET");
        required_perms.add("android.permission.ACCESS_WIFI_STATE");
        required_perms.add("android.permission.ACCESS_NETWORK_STATE");

        PackageManager pm = context.getPackageManager();
        String packageName = context.getPackageName();
        int flags = PackageManager.GET_PERMISSIONS;
        PackageInfo packageInfo = null;

        try {
            packageInfo = pm.getPackageInfo(packageName, flags);
            //  versionCode = packageInfo.versionCode;
            //  versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
//			e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
        if( packageInfo.requestedPermissions != null ) {
            for( String p : packageInfo.requestedPermissions ) {
                //Log_v(TAG, "permission: " + p.toString());
                required_perms.remove(p);
            }
            if( required_perms.size() == 0 ) {
                return true;	// permissions are in order
            }
            // something is missing
            for( String p : required_perms ) {
                Log.d(TAG, "required permission missing: " + p);
            }
        }
        Log.d(TAG, "INTERNET/WIFI access required, but no permissions are found in Manifest.xml");
        return false;
    }


    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }



    public static void showWarningDialog(String title ,String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }




}
