package audu.app.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import audu.app.common;


public class BooksDBHelper extends SQLiteOpenHelper{

    private final static int DB_VERSION = 6;
    public final static String DB_NAME = "audu_books_db.s3db";

    public BooksDBHelper(Context context )
    {
        //super( context, Common.getBaseDirectory() + "/" + DB_NAME, null, DB_VERSION );
        super( context, common.getBaseDirectory() + "/" + DB_NAME, null, DB_VERSION );


    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABLE categorias ( categoriaId INTEGER, categoria TEXT)" );
        db.execSQL( "CREATE TABLE libros ( idLibro INTEGER, titulo TEXT, autor TEXT, narrador TEXT, editorial TEXT, ano TEXT, sinopsis TEXT, categorias TEXT, portada TEXT, trailer TEXT, likes INTEGER)" );
        db.execSQL( "CREATE INDEX Libros_id ON libros( idLibro )" );
        // Nuevas
        db.execSQL( "CREATE TABLE capitulos ( idCapitulo INTEGER, idAudioLibro INTEGER, nombreCapitulo TEXT, subtitulo TEXT,numCapitulo INTEGER , downloaded INTEGER)" );
        db.execSQL( "CREATE INDEX capitulos_id ON capitulos( idCapitulo )" );

        db.execSQL( "CREATE TABLE libros_descargados ( idLibro INTEGER, titulo TEXT, autor TEXT, narrador TEXT, editorial TEXT, ano TEXT, sinopsis TEXT, categorias TEXT, portada TEXT, trailer TEXT, likes INTEGER)" );
        db.execSQL( "CREATE INDEX Librosd_id ON libros_descargados( idLibro )" );

        db.execSQL( "CREATE TABLE favoritos ( idFavorito INTEGER, idAudioLibro INTEGER)" );

        db.execSQL( "CREATE TABLE bookmarks ( idBookMark INTEGER, idAudioLibro INTEGER, idCapitulo INTEGER, descripcion TEXT, fecha DATETIME DEFAULT CURRENT_DATE, tiempo INTEGER)" );
        db.execSQL( "CREATE INDEX Bookmark_id ON bookmarks( idBookMark )" );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS libros");
        db.execSQL("DROP TABLE IF EXISTS capitulos");
        db.execSQL("DROP TABLE IF EXISTS libros_descargados");
        db.execSQL("DROP TABLE IF EXISTS bookmarks");
        db.execSQL("DROP TABLE IF EXISTS favoritos");

        db.execSQL( "CREATE TABLE libros ( idLibro INTEGER, titulo TEXT, autor TEXT, narrador TEXT, editorial TEXT, ano TEXT, sinopsis TEXT, categorias TEXT, portada TEXT, trailer TEXT, likes INTEGER)" );
        db.execSQL( "CREATE INDEX Libros_id ON libros( idLibro )" );

        db.execSQL( "CREATE TABLE capitulos ( idCapitulo INTEGER, idAudioLibro INTEGER, nombreCapitulo TEXT, subtitulo TEXT, numCapitulo INTEGER, downloaded INTEGER)" );
        db.execSQL( "CREATE INDEX capitulos_id ON capitulos( idCapitulo )" );

        db.execSQL( "CREATE TABLE libros_descargados ( idLibro INTEGER, titulo TEXT, autor TEXT, narrador TEXT, editorial TEXT, ano TEXT, sinopsis TEXT, categorias TEXT, portada TEXT, trailer TEXT, likes INTEGER)" );
        db.execSQL( "CREATE INDEX Librosd_id ON libros_descargados( idLibro )" );

        db.execSQL( "CREATE TABLE favoritos ( idFavorito INTEGER, idAudioLibro INTEGER)" );

        db.execSQL( "CREATE TABLE bookmarks ( idBookMark INTEGER, idAudioLibro INTEGER, idCapitulo INTEGER, descripcion TEXT, fecha DATETIME DEFAULT CURRENT_DATE, tiempo INTEGER)" );
        db.execSQL( "CREATE INDEX Bookmark_id ON bookmarks( idBookMark )" );


    }



}
