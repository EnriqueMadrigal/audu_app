package audu.app.models;

import java.util.Date;

public class User_Settings {
    private String _name;
    private String _family_name;
    private String _pass;
    private String _phone_number;
    private Integer _sexo;
    private Integer _descargas;
    private Integer _libros;
    private Integer _userid;
    private Date _start_date;
    private String _email;
    //private Date _birthday;


    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }



    public User_Settings()
    {
        _name = "";
        _family_name = "";
        _pass = "";
        _phone_number = "";
        _sexo = 0;
        _descargas = 0;
        _libros = 0;
        _userid = 0;
        _start_date = new Date();
        _email = "";
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_family_name() {
        return _family_name;
    }

    public void set_family_name(String _family_name) {
        this._family_name = _family_name;
    }

    public String get_pass() {
        return _pass;
    }

    public void set_pass(String _pass) {
        this._pass = _pass;
    }

    public String get_phone_number() {
        return _phone_number;
    }

    public void set_phone_number(String _phone_number) {
        this._phone_number = _phone_number;
    }

    public Integer get_sexo() {
        return _sexo;
    }

    public void set_sexo(Integer _sexo) {
        this._sexo = _sexo;
    }

    public Integer get_descargas() {
        return _descargas;
    }

    public void set_descargas(Integer _descargas) {
        this._descargas = _descargas;
    }

    public Integer get_libros() {
        return _libros;
    }

    public void set_libros(Integer _libros) {
        this._libros = _libros;
    }

    public Integer get_userid() {
        return _userid;
    }

    public void set_userid(Integer _userid) {
        this._userid = _userid;
    }

    public Date get_start_date() {
        return _start_date;
    }

    public void set_start_date(Date _start_date) {
        this._start_date = _start_date;
    }





}
