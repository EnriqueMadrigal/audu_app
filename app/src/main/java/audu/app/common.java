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

    public static boolean userHasLogged = false;
    public static  String userName;
    //static var IMAGEN_USER: UIImage?

    public  static boolean hasLoadedData = false;
    public static boolean isLoadingData = false;

    public static String VAR_USERNAME = "userName";
    public static String VAR_USERID = "userID";

    public static String VAR_HAS_ACCEPTED_TERMS = "HAS_ACCEPTED_TERMS";
    public static String VAR_USER_HAS_LOGGED = "USER_HAS_LOGGED";
    public static String VAR_USER_IMAGE_PATH = "USER_IMAGE_PATH";
    public static String VAR_USER_NAME = "USER_NAME";
    public static String VAR_USER_LASTNAME = "USER_LASTNAME";
    public static String VAR_USER_EMAIL = "USER_EMAIL";
    public static String VAR_USER_SEX = "USER_SEX";
    public static String VAR_USER_BIRTHDAY = "USER_BIRTHDAY";
    public static String VAR_USER_STARTDATE = "USER_STARTDATE";
    public static String VAR_USER_PASSWORD = "USER_PASSWORD";
    public static String VAR_USER_COUNTRY_CODE = "USER_COUNTRY_CODE";
    public static String VAR_USER_PHONE = "USER_PHONE";
    public static String VAR_USER_ID = "USER_ID";
    public static String VAR_USER_FECHA_PAGO = "USER_FECHA_PAGO";
    public static String VAR_USER_FECHA_PROX = "USER_FECHA_PROX";
    public static String VAR_USER_AVATAR = "USER_AVATAR";
    public static String VAR_PREFERENCES_CATEGORIES = "USER_PREFERENCES_CATEGORIES";
    public static String VAR_SHOW_MYBOOKS = "SHOW_MYBOOKS";
    public static String VAR_USER_SUSCRIPTION = "0";
    public static String VAR_TOKEN_ID = "USER_TOKEN_ID";
    public static String VAR_DEVICE_SESSION_ID = "DEVICE_SESSION_ID";


    public static String[] avateres = {"men_avatar", "woman_avatar"};




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
