package audu.app.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import audu.app.common;


public class BooksDBHelper extends SQLiteOpenHelper{

    private final static int DB_VERSION = 3;
    public final static String DB_NAME = "audu_books_db.s3db";

    public BooksDBHelper(Context context )
    {
        //super( context, Common.getBaseDirectory() + "/" + DB_NAME, null, DB_VERSION );
        super( context, common.getBaseDirectory() + "/" + DB_NAME, null, DB_VERSION );


    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABLE categorias ( categoriaId INTEGER, categoria TEXT)" );
        db.execSQL( "CREATE TABLE libros ( idLibro INTEGER, titulo TEXT, autor TEXT, narrador TEXT, editorial TEXT, ano TEXT, sinopsis TEXT, categorias TEXT, portada TEXT, trailer TEXT)" );
        db.execSQL( "CREATE INDEX Libros_id ON libros( idLibro )" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
