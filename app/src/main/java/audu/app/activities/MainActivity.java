package audu.app.activities;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import audu.app.R;
import audu.app.common;
import audu.app.models.Categoria_Class;
import audu.app.models.User_Settings;
import audu.app.util;
import audu.app.utils.HttpClient;

public class MainActivity extends AppCompatActivity {

    private TextView menuName;
    private TextView menuEmail;
    private ImageView menuImage;

    private NavigationView navigationView;

    private ArrayList<Categoria_Class> _categorias;

    private Context context;
    private boolean _isSearchVisible = false;

    Toolbar toolbar;
    public DrawerLayout drawerLayout;

    private String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        navigationView = (NavigationView) findViewById(R.id.navview);


        toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.iconohamburguesa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle( "" );



        //View headerView = navigationView.inflateHeaderView(R.layout.header_navview);

        View headerView =  navigationView.getHeaderView(0);
        menuName = (TextView) headerView.findViewById(R.id.navview_text1);
        menuEmail = (TextView) headerView.findViewById(R.id.navview_text2);
        menuImage = (ImageView) headerView.findViewById(R.id.navview_image);



        util Util = new util(this);

        User_Settings curUser = Util.getUserSettings();

        menuName.setText(curUser.get_name());
        menuEmail.setText(curUser.get_email());

        //NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        //navMenuView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));

        final Menu menu = navigationView.getMenu();
        menu.add ("Categorías");
/*
        for (int i = 1; i <= 3; i++) {
            MenuItem newItem = menu.add(i,100 + i,i,"Item " + i);

           newItem.setIcon(R.drawable.icon_search);

        }
*/

        int curAvatar = Util.getAvatar();
        String Avatar = common.avateres[curAvatar];
        int id = context.getResources().getIdentifier(Avatar, "drawable", context.getPackageName());

        if (id>0)
        {
            menuImage.setImageResource(id);
        }

        getCategorias();

        //////////
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
            Log.d(TAG,"Se seleccion" + String.valueOf(menuItem.getItemId()));

                        drawerLayout.closeDrawers();
                        return true;
                    }

                });
                        /////////
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



        switch(item.getItemId()) {

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
                //handleSearch();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }


    private void AddCategoriastoMenu()
    {
        final Menu menu = navigationView.getMenu();

        for (int i = 0; i < _categorias.size(); i++) {

            Categoria_Class curCat = _categorias.get(i);
            MenuItem newItem = menu.add(i,curCat.get_id(),i,curCat.get_name());
            newItem.setIcon(R.drawable.icon_book);

        }

        MenuItem newItem = menu.add(100,1001,100,"Configuración");

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
            android.util.Log.d( "TEST", "SEARCH: " + search );

         //   products_search _newsearch = products_search.newInstance(search);
         //   Common.SetPage(1);
         //   Common.set_curBusquda(search);
            //Common.setCategoria(curCategoria);
         //   getSupportFragmentManager().beginTransaction().setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right ).replace( R.id.fragment_container, _newsearch, "New Search" ).commit();


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
                    _categorias.add(newCat);

                }

            }
            catch( Exception e )
            {
                android.util.Log.e( "JSONParser", "Cant parse: " + e.getMessage() );
                // Common.alert( this, "No se ha podido registrar, por favor intenta nuevamente más tarde." );
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




}
