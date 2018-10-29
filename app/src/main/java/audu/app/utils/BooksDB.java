package audu.app.utils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import java.util.ArrayList;

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
///////// LIbros

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


}
