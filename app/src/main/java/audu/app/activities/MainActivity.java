package audu.app.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import audu.app.R;
import audu.app.adapters.bookGridAdapter;
import audu.app.common;
import audu.app.fragments.busqueda;
import audu.app.fragments.categoria;
import audu.app.fragments.detalle;
import audu.app.fragments.home_fragment;
import audu.app.fragments.mislibros;
import audu.app.fragments.useroptions;
import audu.app.models.Categoria_Class;
import audu.app.models.Libro_Class;
import audu.app.models.User_Settings;
import audu.app.util;
import audu.app.utils.BooksDB;
import audu.app.utils.HttpClient;
import audu.app.utils.IViewHolderClick;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private TextView menuName;
    private TextView menuEmail;
    private ImageView menuImage;
    private NavigationView navigationView;

    private Context context;
    private boolean _isSearchVisible = false;

    Toolbar toolbar;
    public DrawerLayout drawerLayout;

    private String TAG = "MainActivity";

    private home_fragment MainFragment;
    private ArrayList<Categoria_Class> _categorias;
    private ArrayList<String> _favoritos;
    private static final int REQUEST_WRITE_PERMISSION = 100;
    private static final int MENU_CONFIGURACION = 1001;

    private int curFragments = 0;
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        navigationView = (NavigationView) findViewById(R.id.navview);


        toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.iconohamburguesa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("");

        ImageButton btnClear = (ImageButton) toolbar.findViewById(R.id.btnClear);
        EditText txtSearch = (EditText) toolbar.findViewById(R.id.txtSearch);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClearSearchText();
            }
        });
        txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                search();
                return true;
            }
        });


        //View headerView = navigationView.inflateHeaderView(R.layout.header_navview);

        View headerView = navigationView.getHeaderView(0);
        menuName = (TextView) headerView.findViewById(R.id.navview_text1);
        menuEmail = (TextView) headerView.findViewById(R.id.navview_text2);
        menuImage = (ImageView) headerView.findViewById(R.id.navview_image);


        // MainFragment = new home_fragment();
        // getSupportFragmentManager().beginTransaction().setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right ).replace( R.id.fragment_container, MainFragment, "HOME" ).commit();


        //-------------------

        //--------------


        util Util = new util(this);

        User_Settings curUser = Util.getUserSettings();

        menuName.setText(curUser.get_name());
        menuEmail.setText(curUser.get_email());

        //Util.setSuscripcion("0"); // Solo para pruebas

        //NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        //navMenuView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));

        final Menu menu = navigationView.getMenu();
        menu.add("Categorías");
/*
        for (int i = 1; i <= 3; i++) {
            MenuItem newItem = menu.add(i,100 + i,i,"Item " + i);

           newItem.setIcon(R.drawable.icon_search);

        }
*/

        int curAvatar = Util.getAvatar();
        setAvatar(curAvatar);

        //////////
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        Log.d(TAG, "Se seleccion " + String.valueOf(menuItem.getItemId()));

                        int curItemId = menuItem.getItemId();

                        if (curItemId == R.id.menu_home) {

                            FragmentManager fragmentManager = getSupportFragmentManager();
                            if (curFragments > 0) {
                                //fragmentManager.popBackStackImmediate();
                                // fragmentManager.popBackStack("HOME", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                curFragments = 0;
                            }

                        } else if (curItemId == R.id.menu_libros) {
                            show_mislibros();


                        } else if (curItemId == MENU_CONFIGURACION) {
                            //close_session();
                            show_configuration();
                        } else {
                            if (curItemId > 0) {
                                show_categoria(curItemId);
                            }
                        }

                        drawerLayout.closeDrawers();
                        return true;

                    }

                });

        /////////


        ///Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

///// Verificar permisos
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            getCategorias();
        }


    }

//Obtener permisos

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_WRITE_PERMISSION) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        getCategorias();
                    } else {
                        //requestPermissions(new String[]{Manifest.permission.SEND_SMS}, REQUEST_WRITE_PERMISSION);


                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Es necesario permitir el acceso,para una correcta funcionalidad.")
                                .setTitle("! Advertencia ¡")
                                .setCancelable(false)
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finishActivity(0);
                                        finish();

                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();


                    }
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (item.getItemId()) {

            /*
            case R.id.show_menu:
               //drawerLayout.openDrawer(GravityCompat.START);
               //drawerLayout.openDrawer(GravityCompat.END);
                return true;
            */


            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;


            //  case R.id.go_home:
            //      fmFragment fragment4 = new fmFragment();
            //      navigate( fragment4, "FM" );

            //      return true;


            case R.id.go_search:
                handleSearch();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void setAvatar(int curAvatar) {

        String Avatar = common.avateres[curAvatar];
        int id = context.getResources().getIdentifier(Avatar, "drawable", context.getPackageName());

        if (id>0)
        {
            menuImage.setImageResource(id);
        }


    }




private void show_configuration()
{
    curFragments++;
    FragmentManager fragmentManager = getSupportFragmentManager();

    useroptions _useroptions = useroptions.newInstance();

    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
    fragmentTransaction.replace( R.id.fragment_container,_useroptions, "User Options" );
    fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commit();


}

    public void close_session()
    {

        util Util = new util(this);
        Util.clearUserId();
        Util.clearUserName();
        Util.clearUserSettings();
        String loginType = Util.getLoginType();

        if (loginType.equals("google"))
        {
            signOut();
            return;
        }

        else if (loginType.equals("facebook"))
        {
            LoginManager.getInstance().logOut();
        }

        Intent intent = new Intent();
        intent.setClass(context, intro.class);
        finish();
        startActivity(intent);


    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        // updateUI(false);
                        // [END_EXCLUDE]
                        Intent intent = new Intent();
                        intent.setClass(context, intro.class);
                        finish();
                        startActivity(intent);

                    }
                });
    }





    private void show_mislibros()
    {
        curFragments++;

        FragmentManager fragmentManager = getSupportFragmentManager();

        mislibros _mislibros = mislibros.newInstance();


        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
        fragmentTransaction.replace( R.id.fragment_container,_mislibros, "Principal" );
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void  show_categoria(int curCategoria)
    {

        curFragments++;

        //FragmentManager fragmentManager = getFragmentManager();
        FragmentManager fragmentManager = getSupportFragmentManager();


        categoria _categoria = categoria.newInstance(curCategoria);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
        fragmentTransaction.replace( R.id.fragment_container,_categoria, "Categoria" );
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }


    private void AddCategoriastoMenu()
    {
        final Menu menu = navigationView.getMenu();

        for (int i = 0; i < _categorias.size(); i++) {

            Categoria_Class curCat = _categorias.get(i);
            MenuItem newItem = menu.add(i,curCat.get_id(),i,curCat.get_name());
            newItem.setIcon(R.drawable.icon_book);

        }

        MenuItem newItem = menu.add(100,MENU_CONFIGURACION,100,"Configuración");

        getLibros();

    }

    private void AddLibros()
    {
        MainFragment = new home_fragment();
        getSupportFragmentManager().beginTransaction().setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right ).replace( R.id.fragment_container, MainFragment, "HOME" ).commit();


    }



    private void getCategorias() {

        if( common.haveInternetPermissions(this, "Login") ) // Revisar permisos de internet
        {

            if (common.isOnline(this)) // Revisar si tenemos conexión
            {
                _categorias = new ArrayList<>();

                new loadCategorias().execute();
                //Intent intent = new Intent();
                //intent.setClass(context, presentacion.class);
                //finish();
                //startActivity(intent);

            }

            else
            {
                common.showWarningDialog("! Sin conexión ¡", "Favor de revisar su conexión de Datos..", this);
                //alertDialog.dismiss();
            }

        }


    }
    private void getLibros()
    {
        if( common.haveInternetPermissions(this, "Login") ) // Revisar permisos de internet
        {

            if (common.isOnline(this)) // Revisar si tenemos conexión
            {
                _categorias = new ArrayList<>();

                new loadLibros().execute();
                //Intent intent = new Intent();
                //intent.setClass(context, presentacion.class);
                //finish();
                //startActivity(intent);

            }

            else
            {
                common.showWarningDialog("! Sin conexión ¡", "Favor de revisar su conexión de Datos..", this);
                //alertDialog.dismiss();
            }

        }

    }



//////// Search

    private void handleSearch()
    {
        RelativeLayout pnlSearch = (RelativeLayout) toolbar.findViewById(R.id.pnlSearch);
        EditText txtSearch = (EditText) toolbar.findViewById( R.id.txtSearch );
        if( pnlSearch.getVisibility() == View.GONE )
        {
            _isSearchVisible = true;
            getSupportActionBar().setDisplayUseLogoEnabled( false );
            pnlSearch.setVisibility( View.VISIBLE );
            txtSearch.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE );
            imm.showSoftInput( txtSearch, InputMethodManager.SHOW_IMPLICIT );

        }
        else
        {
            search();
        }
    }


    private void search()
    {
        closeSearch();
        EditText txtSearch = (EditText) toolbar.findViewById( R.id.txtSearch );
        String search = txtSearch.getText().toString().trim();
        txtSearch.setText( "" );
        if( search.length() > 0 )
        {
            curFragments++;
            android.util.Log.d( "TEST", "SEARCH: " + search );

            FragmentManager fragmentManager = getSupportFragmentManager();
            busqueda _busqueda = busqueda.newInstance(search);


            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right );
            fragmentTransaction.replace( R.id.fragment_container,_busqueda, "Busqueda" );
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    }
///////

    private void onClearSearchText()
    {
        EditText txtSearch = (EditText) toolbar.findViewById( R.id.txtSearch );
        txtSearch.setText( "" );
    }
///////////

    private void closeSearch()
    {
        _isSearchVisible = false;
        getSupportActionBar().setDisplayUseLogoEnabled( true );
        RelativeLayout pnlSearch = (RelativeLayout) toolbar.findViewById( R.id.pnlSearch );
        EditText txtSearch = (EditText) toolbar.findViewById( R.id.txtSearch );
        InputMethodManager imm = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE );
        imm.hideSoftInputFromWindow( txtSearch.getWindowToken(), 0 );
        pnlSearch.setVisibility( View.GONE );
    }


    /////////////////

    private void handleSent( HttpClient.HttpResponse response )
    {
        if( response.getCode() == 200 )
        {

            BooksDB db = new BooksDB(context);
            db.open();
            db.deleteCategorias();

            //db.deleteCapitulos();
            //db.deleteLibros();
            //db.deleteLibros_descargados();

            try
            {
                JSONObject json = new JSONObject( response.getResponse() );
                JSONArray categorias = json.getJSONArray("categorias");

                for (int i = 0; i < categorias.length(); i++)
                {
                    JSONObject row = categorias.getJSONObject(i);
                    int id = row.getInt("idCategoria");
                    String nombre = row.getString("categoria");

                    Categoria_Class newCat = new Categoria_Class();
                    newCat.set_id(id);
                    newCat.set_name(nombre);
                    //_categorias.add(newCat);
                    db.insert(newCat);

                }

            }
            catch( Exception e )
            {
                android.util.Log.e( "JSONParser", "Cant parse: " + e.getMessage() );
                // Common.alert( this, "No se ha podido registrar, por favor intenta nuevamente más tarde." );
            }

            ArrayList<Categoria_Class> categorias = db.getCategorias();
            db.close();

            for (Categoria_Class categoria: categorias)
            {

                _categorias.add(categoria);
            }

            AddCategoriastoMenu();
        }
        else {
            //Common.alert( this, "No se ha podido registrar, por favor intenta nuevamente más tarde." );
        }
    }
    private class loadCategorias extends AsyncTask<Void, Void, HttpClient.HttpResponse>
    {
        ProgressDialog _progressDialog;

        public loadCategorias()
        {

        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            _progressDialog = ProgressDialog.show( MainActivity.this, "Espera un momento..", "Obteniendo Categorias..", true );


        }

        @Override
        protected HttpClient.HttpResponse doInBackground( Void... arg0 )
        {

            ContentValues params = new ContentValues();

            HttpClient.HttpResponse response = HttpClient.post( common.API_URL_BASE + "getCategorias.php", params );
            android.util.Log.d( "TEST", String.format( "HTTP CODE: %d RESPONSE: %s", response.getCode(), response.getResponse() ) );

            return response;
        }

        @Override
        protected void onPostExecute( HttpClient.HttpResponse result )
        {
            super.onPostExecute( result );
            _progressDialog.dismiss();
            handleSent( result );


        }
    }

///////// Libros

    private void handleSentLibros( HttpClient.HttpResponse response )
    {
        if( response.getCode() == 200 )
        {

            BooksDB db = new BooksDB(context);
            db.open();
            db.deleteLibros();

            try
            {
                JSONObject json = new JSONObject( response.getResponse() );
                JSONArray libros = json.getJSONArray("libros");

                for (int i = 0; i < libros.length(); i++)
                {
                    JSONObject row = libros.getJSONObject(i);
                    int id = row.getInt("idLibro");
                    String titulo = row.getString("titulo");
                    String autor = row.getString("autor");
                    String narrador = row.getString("narrador");
                    String editorial = row.getString("editorial");
                    String sinopsis = row.getString("sinopsis");
                    String categorias = row.getString("categorias");
                    String portada = row.getString("portada");
                    String trailer = row.getString("trailer");

                    Libro_Class newLibro = new Libro_Class();
                    newLibro.set_idLibro(id);
                    newLibro.set_titulo(titulo);
                    newLibro.set_autor(autor);
                    newLibro.set_narrador(narrador);
                    newLibro.set_editorial(editorial);
                    newLibro.set_sinopsis(sinopsis);
                    newLibro.set_categorias(categorias);
                    //newLibro.set_portada(portada);
                    newLibro.set_portada("");
                    newLibro.set_trailer(trailer);

                    db.insert(newLibro);

                }

            }
            catch( Exception e )
            {
                android.util.Log.e( "JSONParser", "Cant parse: " + e.getMessage() );
                // Common.alert( this, "No se ha podido registrar, por favor intenta nuevamente más tarde." );
            }

            ArrayList<Libro_Class> libros = db.getLibros();
            db.close();

             AddLibros();
        }
        else {
            //Common.alert( this, "No se ha podido registrar, por favor intenta nuevamente más tarde." );
            AddLibros();
        }
    }
    private class loadLibros extends AsyncTask<Void, Void, HttpClient.HttpResponse>
    {
        ProgressDialog _progressDialog;

        public loadLibros()
        {

        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            _progressDialog = ProgressDialog.show( MainActivity.this, "Espera un momento..", "Obteniendo Libros..", true );


        }

        @Override
        protected HttpClient.HttpResponse doInBackground( Void... arg0 )
        {

            ContentValues params = new ContentValues();

            HttpClient.HttpResponse response = HttpClient.post( common.API_URL_BASE + "getLibrosData.php", params );
            android.util.Log.d( "TEST", String.format( "HTTP CODE: %d RESPONSE: %s", response.getCode(), response.getResponse() ) );

            return response;
        }

        @Override
        protected void onPostExecute( HttpClient.HttpResponse result )
        {
            super.onPostExecute( result );
            _progressDialog.dismiss();
            handleSentLibros( result );


        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

    }




}
