package audu.app.models;
import java.io.Serializable;

public class Libro_Class implements  Serializable {

    private int _idLibro;
    private String _titulo;
    private String _autor;
    private String _narrador;
    private String _editorial;
    private String _ano;
    private String _sinopsis;
    private String _categorias;
    private String _portada;
    private String _trailer;
    private int _likes;

    public int get_likes() {
        return _likes;
    }

    public void set_likes(int _likes) {
        this._likes = _likes;
    }


    public int get_idLibro() {
        return _idLibro;
    }

    public void set_idLibro(int _idLibro) {
        this._idLibro = _idLibro;
    }

    public String get_titulo() {
        return _titulo;
    }

    public void set_titulo(String _titulo) {
        this._titulo = _titulo;
    }

    public String get_autor() {
        return _autor;
    }

    public void set_autor(String _autor) {
        this._autor = _autor;
    }

    public String get_narrador() {
        return _narrador;
    }

    public void set_narrador(String _narrador) {
        this._narrador = _narrador;
    }

    public String get_editorial() {
        return _editorial;
    }

    public void set_editorial(String _editorial) {
        this._editorial = _editorial;
    }

    public String get_ano() {
        return _ano;
    }

    public void set_ano(String _ano) {
        this._ano = _ano;
    }

    public String get_sinopsis() {
        return _sinopsis;
    }

    public void set_sinopsis(String _sinopsis) {
        this._sinopsis = _sinopsis;
    }

    public String get_categorias() {
        return _categorias;
    }

    public void set_categorias(String _categorias) {
        this._categorias = _categorias;
    }

    public String get_portada() {
        return _portada;
    }

    public void set_portada(String _portada) {
        this._portada = _portada;
    }

    public String get_trailer() {
        return _trailer;
    }

    public void set_trailer(String _trailer) {
        this._trailer = _trailer;
    }



    public Libro_Class()
    {
        _idLibro = 0;
        _titulo = "";
        _autor = "";
        _narrador = "";
        _editorial = "";
        _ano = "";
        _sinopsis = "";
        _categorias = "";
        _portada = "";
        _trailer = "";

    }

}
