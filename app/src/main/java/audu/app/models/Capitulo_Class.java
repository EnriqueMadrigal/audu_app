package audu.app.models;
import java.io.Serializable;

public class Capitulo_Class implements  Serializable {
    private int _idCapitulo;
    private int _idAudioLibro;
    private String _nombreCapitulo;
    private String _subtitulo;
    private int _numCapitulo;
    private int _downloaded;


    public Capitulo_Class()
    {
        _idCapitulo = 0;
        _idAudioLibro = 0;
        _nombreCapitulo = "";
        _subtitulo = "";
        _numCapitulo = 0;
        _downloaded = 0;
    }

    public int get_idCapitulo() {
        return _idCapitulo;
    }

    public void set_idCapitulo(int _idCapitulo) {
        this._idCapitulo = _idCapitulo;
    }

    public int get_idAudioLibro() {
        return _idAudioLibro;
    }

    public void set_idAudioLibro(int _idAudioLibro) {
        this._idAudioLibro = _idAudioLibro;
    }

    public String get_nombreCapitulo() {
        return _nombreCapitulo;
    }

    public void set_nombreCapitulo(String _nombreCapitulo) {
        this._nombreCapitulo = _nombreCapitulo;
    }

    public String get_subtitulo() {
        return _subtitulo;
    }

    public void set_subtitulo(String _subtitulo) {
        this._subtitulo = _subtitulo;
    }

    public int get_numCapitulo() {
        return _numCapitulo;
    }

    public void set_numCapitulo(int _numCapitulo) {
        this._numCapitulo = _numCapitulo;
    }

    public int get_downloaded() {
        return _downloaded;
    }

    public void set_downloaded(int _downloaded) {
        this._downloaded = _downloaded;
    }





}
