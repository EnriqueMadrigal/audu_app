package audu.app.utils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import audu.app.models.Capitulo_Class;
import audu.app.models.Categoria_Class;
import audu.app.models.Libro_Class;


public class BooksDB {

    private final BooksDBHelper _dbHelper;

    private SQLiteDatabase _database;

    public BooksDB(Context context)
    {
        _dbHelper = new BooksDBHelper(context);
    }


    public void open()
    {
        _database = _dbHelper.getWritableDatabase();
    }
    public void close()
    {
        _dbHelper.close();
    }


    ///////Categorias
    public void deleteCategorias() {
        _database.delete("categorias", null , null);

    }

    public void insert (Categoria_Class c)
    {
        ContentValues data = new ContentValues();
        data.put("categoriaId", c.get_id());
        data.put("categoria", c.get_name());
        _database.insert("categorias",null,data);
     }

     public ArrayList<Categoria_Class> getCategorias()
     {
         ArrayList<Categoria_Class> categorias = new ArrayList<>();
         Cursor cursor = _database.query("categorias",new String[] {"categoriaId", "categoria"}, null, null, null, null,"categoriaId" );

         cursor.moveToFirst();
         while (!cursor.isAfterLast())
         {
             Categoria_Class curItem = new Categoria_Class();
             curItem.set_id(cursor.getInt(0));
             curItem.set_name(cursor.getString(1));
             categorias.add(curItem);
             cursor.moveToNext();
         }
         cursor.close();
         return  categorias;
     }


    public Categoria_Class getCategoriaById(int curId)
    {
        Categoria_Class categoria = new Categoria_Class();
        String curValue = String.valueOf(curId);
        String[] values = new String[1];
        values[0] = curValue;
        Cursor cursor = _database.query("categorias",new String[] {"categoriaId", "categoria"}, "categoriaId=?", values, null, null,"categoriaId" );

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            Categoria_Class curItem = new Categoria_Class();
            curItem.set_id(cursor.getInt(0));
            curItem.set_name(cursor.getString(1));
            categoria = curItem;
            cursor.moveToNext();
        }
        cursor.close();
        return  categoria;
    }


///////// Libros

    public void deleteLibros()
    {
        _database.delete("libros", null , null);
    }


    public void insert(Libro_Class c)
    {
        ContentValues data = new ContentValues();
        data.put("idLibro", c.get_idLibro());
        data.put("titulo", c.get_titulo());
        data.put("autor", c.get_autor());
        data.put("narrador", c.get_narrador());
        data.put("editorial", c.get_editorial());
        data.put("ano", c.get_ano());
        data.put("sinopsis", c.get_sinopsis());
        data.put("categorias", c.get_categorias());
        data.put("portada", c.get_portada());
        data.put("trailer", c.get_trailer());
        data.put("likes", c.get_likes());

        _database.insert("libros",null,data);
    }


    public ArrayList<Libro_Class> getLibros()
    {
        ArrayList<Libro_Class> libros = new ArrayList<>();
        ArrayList<Categoria_Class> categorias = new ArrayList<>();

        String [] columnas = new String[] {"idLibro", "titulo", "autor", "narrador", "editorial", "ano", "sinopsis", "categorias", "portada", "trailer","likes"};
        Cursor cursor = _database.query("libros",columnas, null, null, null, null,"idLibro" );

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            Libro_Class curItem = new Libro_Class();
            curItem.set_idLibro(cursor.getInt(0));
            curItem.set_titulo(cursor.getString(1));
            curItem.set_autor(cursor.getString(2));
            curItem.set_narrador(cursor.getString(3));
            curItem.set_editorial(cursor.getString(4));
            curItem.set_ano(cursor.getString(5));
            curItem.set_sinopsis(cursor.getString(6));
            curItem.set_categorias(cursor.getString(7));
            curItem.set_portada(cursor.getString(8));
            curItem.set_trailer(cursor.getString(9));
            curItem.set_likes(cursor.getInt(10));

            libros.add(curItem);
            cursor.moveToNext();
        }
        cursor.close();



        return libros;
    }


    public ArrayList<Libro_Class> getLibrosOrderLikes()
    {
        ArrayList<Libro_Class> libros = new ArrayList<>();
        ArrayList<Categoria_Class> categorias = new ArrayList<>();

        String [] columnas = new String[] {"idLIbro", "titulo", "autor", "narrador", "editorial", "ano", "sinopsis", "categorias", "portada", "trailer","likes"};
        Cursor cursor = _database.query("libros",columnas, null, null, null, null,"likes desc" );

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            Libro_Class curItem = new Libro_Class();
            curItem.set_idLibro(cursor.getInt(0));
            curItem.set_titulo(cursor.getString(1));
            curItem.set_autor(cursor.getString(2));
            curItem.set_narrador(cursor.getString(3));
            curItem.set_editorial(cursor.getString(4));
            curItem.set_ano(cursor.getString(5));
            curItem.set_sinopsis(cursor.getString(6));
            curItem.set_categorias(cursor.getString(7));
            curItem.set_portada(cursor.getString(8));
            curItem.set_trailer(cursor.getString(9));
            curItem.set_likes(cursor.getInt(10));

            libros.add(curItem);
            cursor.moveToNext();
        }
        cursor.close();



        return libros;
    }

    /// Libros descargados
    public void deleteLibros_descargados()
    {
        _database.delete("libros_descargados", null , null);
    }


    public void insert_libro_descargado(Libro_Class c)
    {
        ContentValues data = new ContentValues();
        data.put("idLibro", c.get_idLibro());
        data.put("titulo", c.get_titulo());
        data.put("autor", c.get_autor());
        data.put("narrador", c.get_narrador());
        data.put("editorial", c.get_editorial());
        data.put("ano", c.get_ano());
        data.put("sinopsis", c.get_sinopsis());
        data.put("categorias", c.get_categorias());
        data.put("portada", c.get_portada());
        data.put("trailer", c.get_trailer());
        data.put("likes", c.get_likes());

        _database.insert("libros_descargados",null,data);
    }


    public ArrayList<Libro_Class> getLibros_descargados()
    {
        ArrayList<Libro_Class> libros = new ArrayList<>();

        String [] columnas = new String[] {"idLibro", "titulo", "autor", "narrador", "editorial", "ano", "sinopsis", "categorias", "portada", "trailer","likes"};
        Cursor cursor = _database.query("libros_descargados",columnas, null, null, null, null,"idLibro" );

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            Libro_Class curItem = new Libro_Class();
            curItem.set_idLibro(cursor.getInt(0));
            curItem.set_titulo(cursor.getString(1));
            curItem.set_autor(cursor.getString(2));
            curItem.set_narrador(cursor.getString(3));
            curItem.set_editorial(cursor.getString(4));
            curItem.set_ano(cursor.getString(5));
            curItem.set_sinopsis(cursor.getString(6));
            curItem.set_categorias(cursor.getString(7));
            curItem.set_portada(cursor.getString(8));
            curItem.set_trailer(cursor.getString(9));
            curItem.set_likes(cursor.getInt(10));

            libros.add(curItem);
            cursor.moveToNext();
        }
        cursor.close();



        return libros;
    }


    public Libro_Class getLibro_Descargado(int IdLibro)
    {

        Libro_Class curLibro;
        curLibro = new Libro_Class();

        String [] columnas = new String[] {"idLibro", "titulo", "autor", "narrador", "editorial", "ano", "sinopsis", "categorias", "portada", "trailer","likes"};
        String[] values = new String[1];
        values[0] = String.valueOf(IdLibro);


        Cursor cursor = _database.query("libros_descargados",columnas, "idLibro=?", values, null, null,"idLibro" );

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            Libro_Class curItem = new Libro_Class();
            curItem.set_idLibro(cursor.getInt(0));
            curItem.set_titulo(cursor.getString(1));
            curItem.set_autor(cursor.getString(2));
            curItem.set_narrador(cursor.getString(3));
            curItem.set_editorial(cursor.getString(4));
            curItem.set_ano(cursor.getString(5));
            curItem.set_sinopsis(cursor.getString(6));
            curItem.set_categorias(cursor.getString(7));
            curItem.set_portada(cursor.getString(8));
            curItem.set_trailer(cursor.getString(9));
            curItem.set_likes(cursor.getInt(10));

           curLibro = curItem;
            cursor.moveToNext();
        }
        cursor.close();

        if (curLibro.get_idLibro() != 0)
        {
            return curLibro;
        }

        else
        {
            return null;
        }

    }


    //////// Capitulos

    public void deleteCapitulos() {
        _database.delete("capitulos", null , null);

    }

    public void insert (Capitulo_Class c)
    {
        ContentValues data = new ContentValues();
        data.put("idCapitulo", c.get_idCapitulo());
        data.put("idAudioLibro", c.get_idAudioLibro());
        data.put("nombreCapitulo", c.get_nombreCapitulo());
        data.put("subtitulo", c.get_subtitulo());
        data.put("numCapitulo", c.get_numCapitulo());
        data.put("downloaded", c.get_downloaded());
        _database.insert("capitulos",null,data);
    }


    public void updateCapituloDownload(int newValue, int curId)
    {
        ContentValues data = new ContentValues();
        data.put("downloaded", newValue);
        _database.update("capitulos",data,"idCapitulo="+ String.valueOf(curId), null);

    }


    public ArrayList<Capitulo_Class> getCapitulosByIdLibro(int curId)
    {
        ArrayList<Capitulo_Class> capitulos = new ArrayList<>();
        String curValue = String.valueOf(curId);
        String [] columnas = new String[] {"idCapitulo", "idAudioLibro", "nombreCapitulo", "subtitulo", "numCapitulo", "downloaded"};
        String[] values = new String[1];
        values[0] = curValue;
        Cursor cursor = _database.query("capitulos", columnas, "idAudioLibro=?", values, null, null,"numCapitulo" );

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            Capitulo_Class curItem = new Capitulo_Class();
            curItem.set_idCapitulo(cursor.getInt(0));
            curItem.set_idAudioLibro(cursor.getInt(1));
            curItem.set_nombreCapitulo(cursor.getString(2));
            curItem.set_subtitulo(cursor.getString(3));
            curItem.set_numCapitulo(cursor.getInt(4));
            curItem.set_downloaded(cursor.getInt(5));


            capitulos.add(curItem);
            cursor.moveToNext();
        }
        cursor.close();
        return  capitulos;
    }


    //////// Utils

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}
