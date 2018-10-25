package audu.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.Date;

import audu.app.models.User_Settings;

public class util {

    private Context context;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public util(Context newContext)
    {
        context = newContext;
        sharedPref = PreferenceManager.getDefaultSharedPreferences( context );
        editor = sharedPref.edit();

    }


    public void setUserId(Integer newUserID)
    {

        editor.putInt (common.VAR_USERID, newUserID );
        editor.commit();
    }

    public int getUserId()
    {
    return sharedPref.getInt(common.VAR_USERID,0);
    }


    public void clearUserId()
    {
        editor.putInt (common.VAR_USERID, 0 );
        editor.commit();
    }


   public void setUserName(String newUserName)
   {
       editor.putString (common.VAR_USERNAME, newUserName );
       editor.commit();
   }


    public  void clearUserName()
    {
        editor.putString (common.VAR_USERNAME, "" );
        editor.commit();
    }


    public  String getUserName()
    {
        return sharedPref.getString(common.VAR_USERNAME,"");
    }



   public boolean getShowMyBooks()
    {
        return sharedPref.getBoolean(common.VAR_SHOW_MYBOOKS, false);
    }

    public void setShowMyBooks(boolean newValue)
    {
        editor.putBoolean(common.VAR_SHOW_MYBOOKS,newValue);
        editor.commit();
   }

    public void userHasLogged()
    {
        editor.putBoolean(common.VAR_USER_HAS_LOGGED, true);
        editor.commit();
    }


    public void userHasLogged_Out()
    {
        editor.putBoolean(common.VAR_USER_HAS_LOGGED, false);
        editor.commit();
    }

    public void setUserPreferences(String userPreferences)
    {
        editor.putString(common.VAR_PREFERENCES_CATEGORIES, userPreferences);
        editor.commit();
    }

    public String getUserPreferences()
    {
        return sharedPref.getString(common.VAR_PREFERENCES_CATEGORIES, "");
    }


    public void updatePassword(String newPassword)
    {
        editor.putString(common.VAR_USER_PASSWORD, "");
        editor.commit();
    }

    public void setSuscripcion(String newSusc)
    {
        editor.putString(common.VAR_USER_SUSCRIPTION, newSusc);
        editor.commit();
    }

    public String getSuscripcion()
    {
        return sharedPref.getString(common.VAR_USER_SUSCRIPTION, "");
    }


    public void setTokenID(String newToken)
    {
        editor.putString(common.VAR_TOKEN_ID, newToken);
        editor.commit();
    }

    public String getTokenID()
    {
        return sharedPref.getString(common.VAR_TOKEN_ID, "");
    }

    public void setSessionDeviceID (String newData)
    {
        editor.putString(common.VAR_DEVICE_SESSION_ID, newData);
        editor.commit();
    }

    public String getSessionDeviceID()
    {
        return sharedPref.getString(common.VAR_DEVICE_SESSION_ID, "");
    }


    public void setFechaPago(Date newFecha)
    {
        editor.putLong(common.VAR_USER_FECHA_PAGO, newFecha.getTime());
        editor.commit();
    }

    public Date getFechaPago()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(sharedPref.getLong(common.VAR_USER_FECHA_PAGO,0));
        //calendar.setTimeZone(TimeZone.getTimeZone(prefs.getString(key + "_zone", TimeZone.getDefault().getID())));
        return calendar.getTime();
    }

    public void setFechaProx( Date newFecha)
    {
        editor.putLong(common.VAR_USER_FECHA_PROX, newFecha.getTime());
        editor.commit();
    }

    public Date getFechaProx()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(sharedPref.getLong(common.VAR_USER_FECHA_PROX,0));
        //calendar.setTimeZone(TimeZone.getTimeZone(prefs.getString(key + "_zone", TimeZone.getDefault().getID())));
        return calendar.getTime();
    }

    public void setAvatar(Integer newAvatar)
    {
        editor.putInt(common.VAR_USER_AVATAR, newAvatar);
        editor.commit();
    }

    public Integer getAvatar()
    {
        return sharedPref.getInt(common.VAR_USER_AVATAR, 0);
    }

    public void saveUserSetting(User_Settings newUserSettings)
    {
       editor.putString(common.VAR_USER_NAME, newUserSettings.get_name());
       editor.putString(common.VAR_USER_EMAIL, newUserSettings.get_email());
       editor.putLong(common.VAR_USER_STARTDATE, newUserSettings.get_start_date().getTime());
       editor.putInt(common.VAR_USER_ID, newUserSettings.get_userid());
       editor.commit();
    }

    public User_Settings getUserSettings()
    {
        User_Settings curUserSettings = new User_Settings();
        curUserSettings.set_name(sharedPref.getString(common.VAR_USER_NAME,""));
        curUserSettings.set_email(sharedPref.getString(common.VAR_USER_EMAIL, ""));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(sharedPref.getLong(common.VAR_USER_STARTDATE,0));
        curUserSettings.set_start_date(calendar.getTime());
        curUserSettings.set_userid(sharedPref.getInt(common.VAR_USER_ID,0));

        return curUserSettings;
    }


    public void clearUserSettings()
    {
        editor.putString(common.VAR_USER_NAME, "");
        editor.putString(common.VAR_USER_EMAIL, "");
        editor.putLong(common.VAR_USER_STARTDATE, 0);
        editor.putInt(common.VAR_USER_ID, 0);
        editor.commit();

    }
























































}











